package cn.offway.hades.controller;

import cn.offway.hades.domain.PhBrand;
import cn.offway.hades.domain.PhBrandRecommend;
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
    private PhBrandRecommendService brandRecommendService;
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

    @RequestMapping("/brandMini.html")
    public String indexMini(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        return "brand_mini_index";
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
    @RequestMapping("/brand_mini_list")
    public Map<String, Object> getMiniList(HttpServletRequest request, String name, String type) {
        int sEcho = Integer.parseInt(request.getParameter("sEcho"));
        int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
        int iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "isRecommendMini"), new Sort.Order(Sort.Direction.ASC, "id"));
        PageRequest pr = new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, sort);
        Page<PhBrandRecommend> pages = brandRecommendService.list(name, type, pr);
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
        if (brand.getId() == null) {
            /* 状态[0-未上架,1-已上架] **/
            brand.setStatus("0");
        }
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
    public boolean pin(@RequestParam("ids[]") Long[] ids, String to, @RequestParam(required = false, defaultValue = "0") int mini) {
        String key = "logo".equals(to) ? "INDEX_BRAND_LOGO" : (mini == 1 ? "INDEX_BRAND_GOODS_MINI" : "INDEX_BRAND_GOODS");
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
    @RequestMapping("/brand_pin_mini")
    @Transactional
    public boolean pinMini(@RequestParam("ids[]") Long[] ids, String reason) {
        String key = "NEW_INDEX_BRAND_MINI";
        String jsonStr = configService.findContentByName(key);
        JSONArray jsonArray;
        if (jsonStr != null && !"".equals(jsonStr)) {
            jsonArray = JSON.parseArray(jsonStr);
        } else {
            jsonArray = new JSONArray();
        }
        for (Long id : ids) {
            PhBrandRecommend brand = brandRecommendService.findOne(id);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", brand.getId());
            jsonObject.put("name", brand.getName());
            jsonObject.put("image", brand.getLogoBig() == null ? "NONE" : brand.getLogoBig());
            jsonObject.put("reason", reason);
            jsonArray.add(jsonObject);
            //update DB
            brand.setIsRecommendMini("1");
            brandRecommendService.save(brand);
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
    public List<PhBrand> pinList(String to, @RequestParam(required = false, defaultValue = "0") int mini) {
        String key = "logo".equals(to) ? "INDEX_BRAND_LOGO" : (mini == 1 ? "INDEX_BRAND_GOODS_MINI" : "INDEX_BRAND_GOODS");
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
    @RequestMapping("/brand_pin_mini_list")
    public JSONArray pinListMini() {
        String key = "NEW_INDEX_BRAND_MINI";
        String jsonStr = configService.findContentByName(key);
        JSONArray jsonArray = new JSONArray();
        if (jsonStr != null && !"".equals(jsonStr)) {
            jsonArray = JSON.parseArray(jsonStr);
        }
        return jsonArray;
    }

    @ResponseBody
    @RequestMapping("/brand_pin_save")
    public boolean pinSave(@RequestParam(name = "ids[]", required = false) String[] ids, @RequestParam(name = "images[]", required = false) String[] images, boolean isLogo, boolean isMini) {
        String key = isLogo ? "INDEX_BRAND_LOGO" : (isMini ? "INDEX_BRAND_GOODS_MINI" : "INDEX_BRAND_GOODS");
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

    @ResponseBody
    @RequestMapping("/brand_pin_mini_save")
    @Transactional
    public boolean pinSaveMini(@RequestParam(name = "ids[]", required = false) String[] ids, @RequestParam(name = "images[]", required = false) String[] images,
                               @RequestParam(name = "names[]", required = false) String[] names, @RequestParam(name = "reasons[]", required = false) String[] reasons,
                               @RequestParam(name = "idsDel[]", required = false) Long[] idsDel) {
        String key = "NEW_INDEX_BRAND_MINI";
        PhConfig config = configService.findOne(key);
        JSONArray jsonArray = new JSONArray();
        if (ids != null) {
            for (int i = 0; i < ids.length; i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", ids[i]);
                jsonObject.put("image", images[i]);
                jsonObject.put("name", names[i]);
                jsonObject.put("reason", reasons[i]);
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
        //update DB
        if (idsDel != null) {
            for (Long id : idsDel) {
                PhBrandRecommend tmp = brandRecommendService.findOne(id);
                tmp.setIsRecommendMini("0");
                brandRecommendService.save(tmp);
            }
        }
        return true;
    }

    @ResponseBody
    @RequestMapping("/brand_pin_sum")
    public Long pinsum() {
        String jsonStr = configService.findContentByName("INDEX_BRAND_LOGO");
        JSONArray jsonArray;
        if (jsonStr != null && !"".equals(jsonStr)) {
            jsonArray = JSON.parseArray(jsonStr);
        } else {
            jsonArray = new JSONArray();
        }
        return Long.valueOf(jsonArray.size());
    }
}
