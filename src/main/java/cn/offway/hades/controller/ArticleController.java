package cn.offway.hades.controller;

import cn.offway.hades.domain.PhAdmin;
import cn.offway.hades.domain.PhArticle;
import cn.offway.hades.domain.PhBrand;
import cn.offway.hades.properties.QiniuProperties;
import cn.offway.hades.service.PhArticleService;
import cn.offway.hades.service.PhRoleadminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping
public class ArticleController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private QiniuProperties qiniuProperties;
    @Autowired
    private PhArticleService articleService;
    @Autowired
    private PhRoleadminService roleadminService;

    @RequestMapping("/article.html")
    public String index(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        return "article_index";
    }

    @ResponseBody
    @RequestMapping("/article_list")
    public Map<String, Object> getList(HttpServletRequest request,String name, String tag, String status, String title, String type) {
        int sEcho = Integer.parseInt(request.getParameter("sEcho"));
        int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
        int iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "id"), new Sort.Order(Sort.Direction.ASC, "sort"));
        Page<PhArticle> pages = articleService.findAll(name,tag,status,title,type, new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, sort));
        int initEcho = sEcho + 1;
        Map<String, Object> map = new HashMap<>();
        map.put("sEcho", initEcho);
        map.put("iTotalRecords", pages.getTotalElements());//数据总条数
        map.put("iTotalDisplayRecords", pages.getTotalElements());//显示的条数
        map.put("aData", pages.getContent());//数据集合
        return map;
    }

    @ResponseBody
    @RequestMapping("/article_save")
    public boolean save(PhArticle brand) {
        if(brand.getId() == null) {
            brand.setCreateTime(new Date());
            brand.setStatus("0");
        }else {
            PhArticle article = articleService.findOne(brand.getId());
            brand.setStatus(article.getStatus());
            brand.setCreateTime(article.getCreateTime());
            brand.setApprover(article.getApprover());
            brand.setApproval(article.getApproval());
            brand.setApprovalContent(article.getApprovalContent());
            brand.setRemark(article.getRemark());
        }
        articleService.save(brand);
        return true;
    }

    @ResponseBody
    @RequestMapping("/article_get")
    public Object get(Long id) {
        return articleService.findOne(id);
    }

    @ResponseBody
    @RequestMapping("/article_del")
    public boolean delete(@RequestParam("ids[]") Long[] ids) {
        for (Long id : ids) {
            articleService.del(id);
        }
        return true;
    }

    @ResponseBody
    @RequestMapping("/article_up")
    public boolean goodsUp(Long id, @AuthenticationPrincipal PhAdmin admin) {
        PhArticle article = articleService.findOne(id);
        List<Long> roles = roleadminService.findRoleIdByAdminId(admin.getId());
        if (roles.contains(BigInteger.valueOf(8L))) {
            return false;
        }
        if (article != null){
            article.setStatus("1");
            article.setApprover("管理员");
            article.setApproval(new Date());
            articleService.save(article);
        }
        return true;
    }

    @ResponseBody
    @RequestMapping("/article_down")
    public boolean goodsDown(Long id, @AuthenticationPrincipal PhAdmin admin) {
        PhArticle article = articleService.findOne(id);
        List<Long> roles = roleadminService.findRoleIdByAdminId(admin.getId());
        if (roles.contains(BigInteger.valueOf(8L))) {
            return false;
        }
        if (article != null){
            article.setStatus("0");
            article.setApprover("管理员");
            articleService.save(article);
        }
        return true;
    }
}
