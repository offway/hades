package cn.offway.hades.controller;

import cn.offway.hades.domain.PhBrand;
import cn.offway.hades.domain.PhConfig;
import cn.offway.hades.properties.QiniuProperties;
import cn.offway.hades.service.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping
public class BrandController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private QiniuProperties qiniuProperties;
    @Autowired
    private PhBrandService brandService;
    @Autowired
    private PhGoodsService goodsService;
    @Autowired
    private PhGoodsStockService goodsStockService;
    @Autowired
    private PhMerchantBrandService merchantBrandService;
    @Autowired
    private PhConfigService configService;

    @RequestMapping("/brand.html")
    public String index(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        return "brand_index";
    }

    @ResponseBody
    @RequestMapping("/brand_list")
    public Map<String, Object> getList(HttpServletRequest request, String status) {
        int sEcho = Integer.parseInt(request.getParameter("sEcho"));
        int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
        int iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
        String name = request.getParameter("name");
        String type = request.getParameter("type");
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "isRecommend"), new Sort.Order(Sort.Direction.ASC, "sort"), new Sort.Order(Sort.Direction.ASC, "id"));
        PageRequest pr = new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, sort);
        Page<PhBrand> pages = brandService.findAll(name, type, status, pr);
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
    @Transactional
    @RequestMapping("/brand_up")
    public boolean up(@RequestParam("ids[]") Long[] ids) {
        for (Long id : ids) {
            PhBrand brand = brandService.findOne(id);
            if (brand != null) {
                brand.setStatus("1");
                brandService.save(brand);
            }
        }
        return true;
    }

    @ResponseBody
    @Transactional
    @RequestMapping("/brand_down")
    public boolean down(@RequestParam("ids[]") Long[] ids) {
        for (Long id : ids) {
            PhBrand brand = brandService.findOne(id);
            if (brand != null) {
                brand.setStatus("0");
                brandService.save(brand);
            }
        }
        return true;
    }

    @ResponseBody
    @RequestMapping("/brand_save")
    public boolean save(PhBrand brand) {
        if ("".equals(brand.getBanner().trim())) {
            brand.setBanner(null);
        }
        if ("".equals(brand.getBannerBig().trim())) {
            brand.setBannerBig(null);
        }
        brand.setCreateTime(new Date());
        brandService.save(brand);
        if (brand.getId() != null) {
            goodsService.updateBrandInfo(brand);
            goodsStockService.updateBrandInfo(brand);
            merchantBrandService.updateBrandInfo(brand);
        }
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
    public boolean unTop(Long id) {
        PhBrand brand = brandService.findOne(id);
        if (brand != null) {
            brand.setIsRecommend("0");
            brandService.save(brand);
            return true;
        }
        return false;
    }

    @ResponseBody
    @RequestMapping("/brand_pin")
    public boolean pin(@RequestParam("ids[]") Long[] ids, String to) {
        String key = "logo".equals(to) ? "INDEX_BRAND_LOGO" : "INDEX_BRAND_GOODS";
        String jsonStr = configService.findContentByName(key);
        JSONArray jsonArray;
        if (jsonStr != null && !"".equals(jsonStr)) {
            jsonArray = JSON.parseArray(jsonStr);
        } else {
            jsonArray = new JSONArray();
        }
        for (Long id : ids) {
            PhBrand brand = brandService.findOne(id);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", brand.getId());
            if ("logo".equals(to)) {
                jsonObject.put("image", brand.getLogoBig() == null ? "NONE" : brand.getLogoBig());
            } else {
                jsonObject.put("image", brand.getBanner() == null ? "NONE" : brand.getBanner());
            }
            jsonArray.add(jsonObject);
        }
        PhConfig config = configService.findOne(key);
        if (config == null) {
            config = new PhConfig();
            config.setName(key);
            config.setCreateTime(new Date());
        }
        config.setContent(jsonArray.toJSONString());
        configService.save(config);
        return true;
    }

    @ResponseBody
    @RequestMapping("/brand_pin_list")
    public List<PhBrand> pinList(String to) {
        String key = "logo".equals(to) ? "INDEX_BRAND_LOGO" : "INDEX_BRAND_GOODS";
        String jsonStr = configService.findContentByName(key);
        JSONArray jsonArray = new JSONArray();
        if (jsonStr != null && !"".equals(jsonStr)) {
            jsonArray = JSON.parseArray(jsonStr);
        }
        List<PhBrand> list = new ArrayList<>();
        for (Object o : jsonArray) {
            JSONObject object = (JSONObject) o;
            list.add(brandService.findOne(object.getLongValue("id")));
        }
        return list;
    }

    @ResponseBody
    @RequestMapping("/brand_pin_save")
    public boolean pinSave(@RequestParam(name = "ids[]", required = false) String[] ids, @RequestParam(name = "images[]", required = false) String[] images, boolean isLogo) {
        String key = isLogo ? "INDEX_BRAND_LOGO" : "INDEX_BRAND_GOODS";
        PhConfig config = configService.findOne(key);
        JSONArray jsonArray = new JSONArray();
        if (ids != null) {
            for (int i = 0; i < ids.length; i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", ids[i]);
                jsonObject.put("image", images[i]);
                jsonArray.add(jsonObject);
            }
        }
        if (config == null) {
            config = new PhConfig();
            config.setName(key);
            config.setCreateTime(new Date());
        }
        config.setContent(jsonArray.toJSONString());
        configService.save(config);
        return true;
    }
}
