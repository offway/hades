package cn.offway.hades.controller;

import cn.offway.hades.domain.PhArticle;
import cn.offway.hades.domain.PhBrand;
import cn.offway.hades.properties.QiniuProperties;
import cn.offway.hades.service.PhArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping
public class ArticleController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private QiniuProperties qiniuProperties;
    @Autowired
    private PhArticleService articleService;

    @RequestMapping("/article.html")
    public String index(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        return "article_index";
    }

    @ResponseBody
    @RequestMapping("/article_list")
    public Map<String, Object> getList(HttpServletRequest request) {
        int sEcho = Integer.parseInt(request.getParameter("sEcho"));
        int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
        int iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "id"), new Sort.Order(Sort.Direction.ASC, "sort"));
        Page<PhArticle> pages = articleService.findAll( new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, sort));
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
        if(brand.getId() == null){
            brand.setCreateTime(new Date());
            brand.setStatus("0");
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
}
