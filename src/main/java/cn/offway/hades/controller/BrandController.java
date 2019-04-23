package cn.offway.hades.controller;

import cn.offway.hades.domain.PhBrand;
import cn.offway.hades.properties.QiniuProperties;
import cn.offway.hades.service.PhBrandService;
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
public class BrandController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private QiniuProperties qiniuProperties;
    @Autowired
    private PhBrandService brandService;

    @RequestMapping("/brand.html")
    public String index(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        return "brand_index";
    }

    @ResponseBody
    @RequestMapping("/brand_list")
    public Map<String, Object> getList(HttpServletRequest request) {
        int sEcho = Integer.parseInt(request.getParameter("sEcho"));
        int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
        int iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
        String name = request.getParameter("name");
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "isRecommend"), new Sort.Order(Sort.Direction.ASC, "sort"));
        Page<PhBrand> pages = brandService.findAll(name, new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, sort));
        int initEcho = sEcho + 1;
        Map<String, Object> map = new HashMap<>();
        map.put("sEcho", initEcho);
        map.put("iTotalRecords", pages.getTotalElements());//数据总条数
        map.put("iTotalDisplayRecords", pages.getTotalElements());//显示的条数
        map.put("aData", pages.getContent());//数据集合
        return map;
    }

    @ResponseBody
    @RequestMapping("/brand_del")
    public boolean delete(@RequestParam("ids[]") Long[] ids) {
        for (Long id : ids) {
            brandService.del(id);
        }
        return true;
    }

    @ResponseBody
    @RequestMapping("/brand_save")
    public boolean save(PhBrand brand) {
        brand.setCreateTime(new Date());
        brandService.save(brand);
        return true;
    }

    @ResponseBody
    @RequestMapping("/brand_get")
    public Object get(Long id) {
        return brandService.findOne(id);
    }

    @ResponseBody
    @RequestMapping("/brand_toggleTop")
    public boolean toggleTop(Long id) {
        PhBrand brand = brandService.findOne(id);
        if (brand != null) {
            if ("1".equals(brand.getIsRecommend())) {
                brand.setIsRecommend("0");
                brand.setSort(null);
            } else {
                brand.setIsRecommend("1");
                long lastSort = brandService.getMaxSort();
                brand.setSort(lastSort + 1);
            }
            brandService.save(brand);
            return true;
        }
        return false;
    }

    @ResponseBody
    @RequestMapping("/brand_top")
    public boolean top(Long id, Long sort) {
        PhBrand brand = brandService.findOne(id);
        if (brand != null) {
            brandService.resort(sort);
            brand.setSort(sort);
            brandService.save(brand);
            return true;
        }
        return false;
    }

    @ResponseBody
    @RequestMapping("/brand_untop")
    public boolean untop(Long id) {
        PhBrand brand = brandService.findOne(id);
        if (brand != null) {
            brand.setIsRecommend("0");
            brandService.save(brand);
            return true;
        }
        return false;
    }
}
