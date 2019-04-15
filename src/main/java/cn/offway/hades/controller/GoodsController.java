package cn.offway.hades.controller;

import cn.offway.hades.domain.*;
import cn.offway.hades.properties.QiniuProperties;
import cn.offway.hades.service.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qiniu.util.Base64;
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
import java.util.*;

@Controller
@RequestMapping
public class GoodsController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private QiniuProperties qiniuProperties;
    @Autowired
    private PhGoodsService goodsService;
    @Autowired
    private PhGoodsTypeService goodsTypeService;
    @Autowired
    private PhGoodsCategoryService goodsCategoryService;
    @Autowired
    private PhGoodsImageService goodsImageService;
    @Autowired
    private PhGoodsPropertyService goodsPropertyService;
    @Autowired
    private PhGoodsStockService goodsStockService;
    @Autowired
    private PhBrandService brandService;
    @Autowired
    private PhMerchantService merchantService;

    @RequestMapping("/goods.html")
    public String index(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        return "goods_index";
    }

    @RequestMapping("/goods_add.html")
    public String add(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        return "goods_add";
    }

    @ResponseBody
    @RequestMapping("/type_and_category_list")
    public List<Object> getTypeAndCategory() {
        List<Object> ret = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        List<PhGoodsType> typeList = goodsTypeService.findAll();
        for (PhGoodsType type : typeList) {
            Map container = objectMapper.convertValue(type, Map.class);
            List<PhGoodsCategory> categoryList = goodsCategoryService.findByPid(type.getId());
            container.put("sub", categoryList);
            ret.add(container);
        }
        return ret;
    }

    @ResponseBody
    @RequestMapping("/brand_list_all")
    public List<PhBrand> getBrand() {
        return brandService.findAll();
    }

    @ResponseBody
    @RequestMapping("/goods_list")
    public Map<String, Object> getList(HttpServletRequest request) {
        int sEcho = Integer.parseInt(request.getParameter("sEcho"));
        int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
        int iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
        String name = request.getParameter("name");
        Sort sort = new Sort("id");
        Page<PhGoods> pages = goodsService.findAll(name, new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, sort));
        int initEcho = sEcho + 1;
        Map<String, Object> map = new HashMap<>();
        map.put("sEcho", initEcho);
        map.put("iTotalRecords", pages.getTotalElements());//数据总条数
        map.put("iTotalDisplayRecords", pages.getTotalElements());//显示的条数
        map.put("aData", pages.getContent());//数据集合
        return map;
    }

    @ResponseBody
    @RequestMapping("/goods_del")
    public boolean delete(@RequestParam("ids[]") Long[] ids) {
        for (Long id : ids) {
            goodsService.del(id);
            goodsImageService.delByPid(id);
            goodsPropertyService.delByPid(id);
            goodsStockService.delByPid(id);
        }
        return true;
    }

    @ResponseBody
    @RequestMapping("/goods_add")
    public boolean add(PhGoods goods, @RequestParam("stocks") String stocks) {
        PhBrand brand = brandService.findOne(goods.getBrandId());
        if (brand != null) {
            goods.setBrandName(brand.getName());
            goods.setBrandLogo(brand.getLogo());
        }
        PhMerchant merchant = merchantService.findOne(goods.getMerchantId());
        if (merchant != null) {
            goods.setMerchantName(merchant.getName());
            goods.setMerchantLogo(merchant.getLogo());
        }
        PhGoodsType goodsType = goodsTypeService.findOne(Long.valueOf(goods.getType()));
        if (goodsType != null) {
            goods.setType(goodsType.getName());
        }
        PhGoodsCategory goodsCategory = goodsCategoryService.findOne(Long.valueOf(goods.getCategory()));
        if (goodsCategory != null) {
            goods.setCategory(goodsCategory.getName());
        }
        goods.setCreateTime(new Date());
        PhGoods goodsSaved = goodsService.save(goods);
        for (Object o : JSON.parseArray(stocks)) {
            JSONObject obj = (JSONObject) o;
            PhGoodsStock goodsStock = new PhGoodsStock();
            goodsStock.setBrandId(goodsSaved.getBrandId());
            goodsStock.setBrandLogo(goodsSaved.getBrandLogo());
            goodsStock.setBrandName(goodsSaved.getBrandName());
            goodsStock.setMerchantId(goodsSaved.getMerchantId());
            goodsStock.setMerchantName(goodsSaved.getMerchantName());
            goodsStock.setMerchantLogo(goodsSaved.getMerchantLogo());
            goodsStock.setGoodsId(goodsSaved.getId());
            goodsStock.setGoodsName(goodsSaved.getName());
            goodsStock.setGoodsImage(goodsSaved.getImage());
            goodsStock.setType(goodsSaved.getType());
            goodsStock.setCategory(goodsSaved.getCategory());
            goodsStock.setSku(obj.getString("sku"));
            goodsStock.setStock(obj.getLong("stock"));
            goodsStock.setPrice(obj.getDouble("price"));
            goodsStock.setRemark(obj.getString("remark"));
            goodsStock.setImage(obj.getString("image"));
            goodsStock.setCreateTime(new Date());
            PhGoodsStock goodsStockSaved = goodsStockService.save(goodsStock);
            byte[] jsonStr = Base64.decode(obj.getString("detail"), Base64.DEFAULT);
            JSONArray jsonArray = JSON.parseArray(new String(jsonStr));
            if (jsonArray != null) {
                for (Object oo : jsonArray) {
                    JSONObject jsonObject = (JSONObject) oo;
                    PhGoodsProperty goodsProperty = new PhGoodsProperty();
                    goodsProperty.setGoodsId(goodsSaved.getId());
                    goodsProperty.setGoodsStockId(goodsStockSaved.getId());
                    goodsProperty.setName(jsonObject.getString("name"));
                    goodsProperty.setValue(jsonObject.getJSONObject("value").getString("value"));
                    goodsProperty.setSort(jsonObject.getJSONObject("value").getLong("sort"));
                    goodsProperty.setRemark(jsonObject.getJSONObject("value").getString("remark"));
                    goodsProperty.setCreateTime(new Date());
                    goodsPropertyService.save(goodsProperty);
                }
            } else {
                logger.error("stocks json 非法");
            }
        }
        return true;
    }
}
