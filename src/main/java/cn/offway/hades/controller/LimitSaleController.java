package cn.offway.hades.controller;

import cn.offway.hades.domain.PhAdmin;
import cn.offway.hades.domain.PhGoods;
import cn.offway.hades.domain.PhLimitedSale;
import cn.offway.hades.properties.QiniuProperties;
import cn.offway.hades.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequestMapping
@Controller
public class LimitSaleController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private QiniuProperties qiniuProperties;
    @Autowired
    private PhLimitedSaleService limitedSaleService;
    @Autowired
    private PhGoodsService goodsService;
    @Autowired
    private PhGoodsStockService goodsStockService;
    @Autowired
    private PhGoodsImageService goodsImageService;
    @Autowired
    private PhGoodsPropertyService goodsPropertyService;
    @Autowired
    private QiniuService qiniuService;

    @RequestMapping("/limit_sale.html")
    public String index(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        return "limit_sale";
    }

    @RequestMapping("/limit_sale_insert.html")
    public String insert(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        map.addAttribute("theId", "XYZ");
        return "limit_sale_compose";
    }

    @RequestMapping("/limit_sale_modify.html")
    public String modify(ModelMap map, String id) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        map.addAttribute("theId", id);
        return "limit_sale_compose";
    }

    @RequestMapping("/limit_sale_add.html")
    public String add(ModelMap map, String args) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        args = args.replaceAll("_", "+");
        map.addAttribute("args", args);
        return "limit_sale_add";
    }

    @RequestMapping("/limit_sale_edit.html")
    public String edit(ModelMap map, String args) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        args = args.replaceAll("_", "+");
        map.addAttribute("args", args);
        return "limit_sale_edit";
    }

    @ResponseBody
    @RequestMapping("/limit_sale_list")
    public Map<String, Object> getList(HttpServletRequest request) {
        int sEcho = Integer.parseInt(request.getParameter("sEcho"));
        int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
        int iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
        Sort sort = new Sort(Sort.Direction.DESC, "status");
        Page<PhLimitedSale> pages = limitedSaleService.list(new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, sort));
        int initEcho = sEcho + 1;
        Map<String, Object> map = new HashMap<>();
        map.put("sEcho", initEcho);
        map.put("iTotalRecords", pages.getTotalElements());//数据总条数
        map.put("iTotalDisplayRecords", pages.getTotalElements());//显示的条数
        map.put("aData", pages.getContent());//数据集合
        return map;
    }

    @ResponseBody
    @RequestMapping("/limit_sale_del")
    @Transactional
    public boolean delete(@RequestParam("ids[]") Long[] ids) {
        for (Long id : ids) {
            PhLimitedSale limitedSale = limitedSaleService.findOne(id);
            if (limitedSale != null) {
                goodsService.del(limitedSale.getGoodsId());
                goodsImageService.delByPid(limitedSale.getGoodsId());
                goodsPropertyService.delByPid(limitedSale.getGoodsId());
                goodsStockService.delByPid(limitedSale.getGoodsId());
                limitedSaleService.del(id);
            }
        }
        return true;
    }

    @ResponseBody
    @RequestMapping("/limit_sale_get")
    public PhLimitedSale get(Long id) {
        return limitedSaleService.findOne(id);
    }

    @ResponseBody
    @RequestMapping("/limit_sale_add")
    public boolean add(PhLimitedSale limitedSale) throws IOException {
        if (limitedSale.getId() == null) {
            limitedSale.setCreateTime(new Date());
        } else {
            limitedSale.setCreateTime(limitedSaleService.findOne(limitedSale.getId()).getCreateTime());
        }
        String newInfoStr = limitedSale.getInfo().replaceAll("(?<=(<img.{1,500}))style=\".+\"(?=(.+>))", "style=\"width:100% !important\"");
//        newInfoStr = newInfoStr.replaceAll("height:auto", "height:100%").replaceAll("width:auto", "width:100%");
        limitedSale.setInfo(ArticleController.filterWxPicAndReplace(newInfoStr, qiniuService));
        limitedSale.setIsShow("0");
        limitedSaleService.save(limitedSale);
        return true;
    }

    @ResponseBody
    @RequestMapping("/limit_downIt")
    public boolean downIt(Long id, @AuthenticationPrincipal PhAdmin admin) {
        PhLimitedSale limitedSale = limitedSaleService.findOne(id);
        limitedSale.setStatus("0");
        limitedSaleService.save(limitedSale);
        PhGoods goods = goodsService.findOne(limitedSale.getGoodsId());
        goods.setStatus("0");
        goodsService.save(goods);
        return true;
    }

    @ResponseBody
    @RequestMapping("/limit_upIt")
    public boolean upIt(Long id, @AuthenticationPrincipal PhAdmin admin) {
        PhLimitedSale limitedSale = limitedSaleService.findOne(id);
        limitedSale.setStatus("1");
        limitedSaleService.save(limitedSale);
        PhGoods goods = goodsService.findOne(limitedSale.getGoodsId());
        goods.setStatus("1");
        goodsService.save(goods);
        return true;
    }

    @ResponseBody
    @RequestMapping("/limit_isshow")
    public boolean isShow(Long id){
        limitedSaleService.issetshow(id);
        return true;
    }

    @ResponseBody
    @RequestMapping("/limit_isshowDo")
    public boolean isShowDo(){
        limitedSaleService.isshow();
        return true;
    }
}
