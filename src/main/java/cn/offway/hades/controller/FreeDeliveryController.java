package cn.offway.hades.controller;

import cn.offway.hades.domain.*;
import cn.offway.hades.properties.QiniuProperties;
import cn.offway.hades.service.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.*;

@Controller
@RequestMapping
public class FreeDeliveryController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private QiniuProperties qiniuProperties;
    @Autowired
    private PhFreeDeliveryService freeDeliveryService;
    @Autowired
    private PhFreeProductService freeProductService;
    @Autowired
    private PhFreeDeliveryUserService freeDeliveryUserService;
    @Autowired
    private PhFreeDeliveryBoostService freeDeliveryBoostService;
    @Autowired
    private PhGoodsService goodsService;
    @Autowired
    private PhBrandService phBrandService;

    @RequestMapping("/freeDelivery.html")
    public String index(ModelMap map) {
        return "freeDelivery_index";
    }

    @RequestMapping("/freeDelivery_add.html")
    public String add(ModelMap map, Long id) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        map.addAttribute("theId", id);
        return "freeDelivery_add";
    }

    @ResponseBody
    @RequestMapping("/freeDelivery_editFind")
    public Map<String, Object> find(Long id) {
        Map<String, Object> map = new HashMap<>();
        PhFreeProduct freeProduct = freeProductService.findOne(id);
        List<PhFreeDelivery> freeDelivery = freeDeliveryService.findOneByProductId(id);
        map.put("freeProduct", freeProduct);
        map.put("freeDelivery", freeDelivery);
        return map;
    }


    @ResponseBody
    @RequestMapping("/freeDelivery_list")
    public Map<String, Object> getList(HttpServletRequest request, int sEcho, int iDisplayStart, int iDisplayLength) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "id"));
        Page<PhFreeProduct> pages = freeProductService.findAll(new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, sort));
        int initEcho = sEcho + 1;
        return echoBody(initEcho, pages.getTotalElements(), pages.getTotalElements(), pages.getContent());
    }

    private Map<String, Object> echoBody(int initEcho, long iTotalRecords, long iTotalDisplayRecords, List<?> data) {
        Map<String, Object> map = new HashMap<>();
        map.put("sEcho", initEcho);
        map.put("iTotalRecords", iTotalRecords);//数据总条数
        map.put("iTotalDisplayRecords", iTotalDisplayRecords);//显示的条数
        map.put("aData", data);//数据集合
        return map;
    }

    @ResponseBody
    @RequestMapping("/freeDelivery_goods_list")
    public List<Map<String, Object>> getGoodsList(Long id) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> data = new ArrayList<>();
        List<PhFreeDelivery> list = freeDeliveryService.findOneByProductId(id);
        for (PhFreeDelivery delivery : list) {
            Map<String, Object> map = objectMapper.convertValue(delivery, Map.class);
            map.put("realJoinCount", freeDeliveryUserService.getCountByPid(delivery.getId()));
            map.put("realBoostCount", freeDeliveryBoostService.getCountByPid(delivery.getId()));
            data.add(map);
        }
        return data;
    }

    @ResponseBody
    @RequestMapping("/freeDelivery_join_list")
    public List<Map<String, Object>> getJoinList(Long id) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> data = new ArrayList<>();
        List<PhFreeDeliveryUser> list = freeDeliveryUserService.getListByPid(id);
        for (PhFreeDeliveryUser user : list) {
            Map<String, Object> map = objectMapper.convertValue(user, Map.class);
            map.put("subList", freeDeliveryBoostService.getListByPid(user.getId()));
            data.add(map);
        }
        return data;
    }

    @ResponseBody
    @RequestMapping("/freeDelivery_save")
    public boolean save(String json, @AuthenticationPrincipal PhAdmin admin) {
        int sumGooodsCount = 0;
        int sumBoostCount = 0;
        boolean isNew = true;
        JSONObject jsonObject = JSONObject.parseObject(json);
        List<PhFreeDelivery> phFreeDeliveryList = new ArrayList<>();
        PhFreeProduct freeProduct = JSONObject.parseObject(json, PhFreeProduct.class);
        JSONArray gidsList = jsonObject.getJSONArray("gidsList");
        JSONArray priceList = jsonObject.getJSONArray("priceList");
        JSONArray goodsSizeList = jsonObject.getJSONArray("goodsSizeList");
        JSONArray goodsCountList = jsonObject.getJSONArray("goodsCountList");
        JSONArray goodsIdList = jsonObject.getJSONArray("goodsIdList");
        JSONArray brandIdList = jsonObject.getJSONArray("brandIdList");
        JSONArray boostCountList = jsonObject.getJSONArray("boostCountList");
        JSONArray goodsNameList = jsonObject.getJSONArray("goodsNameList");
        JSONArray goodsImgeList = jsonObject.getJSONArray("goodsImgeList");
        JSONArray userTypesList = jsonObject.getJSONArray("userTypesList");
        JSONArray remarkList = jsonObject.getJSONArray("remarkList");
        freeProduct.setCreator(admin.getNickname());
        freeProduct.setCreateTime(new Date());
        freeProduct = freeProductService.save(freeProduct);
        if (gidsList.size() > 0) {
            List<Long> did = gidsList.toJavaList(Long.class);
            if (did.size() > 0) {
                isNew = false;
//                freeDeliveryService.deleteByproductId(freeProduct.getId());
            }
        }
        PhFreeDelivery freeDeliveries;
        for (int i = 0; i < goodsIdList.size(); i++) {
            if (isNew) {
                freeDeliveries = new PhFreeDelivery();
            } else {
                List<PhFreeDelivery> freeDeliveriesList = freeDeliveryService.findOneByProductId(freeProduct.getId());
                freeDeliveries = freeDeliveriesList.get(i);
            }
            if (!"0".equals(goodsIdList.get(i).toString())) {
                PhGoods goods = goodsService.findOne(Long.valueOf(goodsIdList.get(i).toString()));
                freeDeliveries.setBrandLogo(goods.getBrandLogo());
                freeDeliveries.setBrandName(goods.getBrandName());
            } else {
                PhBrand brand = phBrandService.findOne(Long.valueOf(brandIdList.get(i).toString()));
                freeDeliveries.setBrandLogo(brand.getLogo());
                freeDeliveries.setBrandName(brand.getName());
            }
            freeDeliveries.setUserType(userTypesList.get(i).toString());
            freeDeliveries.setImage(goodsImgeList.get(i).toString());
            freeDeliveries.setName(goodsNameList.get(i).toString());
            freeDeliveries.setBoostCount(Long.valueOf(boostCountList.get(i).toString()));
            freeDeliveries.setBrandId(Long.valueOf(brandIdList.get(i).toString()));
            freeDeliveries.setGoodsId(Long.valueOf(goodsIdList.get(i).toString()));
            freeDeliveries.setGoodsCount(Long.valueOf(goodsCountList.get(i).toString()));
            freeDeliveries.setGoodsSize(goodsSizeList.get(i).toString());
            freeDeliveries.setPrice(Double.valueOf(priceList.get(i).toString()));
            freeDeliveries.setCreateTime(new Date());
            if (isNew){
                freeDeliveries.setStatus("0");
            }
            freeDeliveries.setVersion(0L);
            freeDeliveries.setSort((long) i);
            freeDeliveries.setProductId(freeProduct.getId());
            freeDeliveries.setRemark("限量" + goodsCountList.get(i) + remarkList.get(i));
            phFreeDeliveryList.add(freeDeliveries);
            sumGooodsCount += Integer.valueOf(goodsCountList.get(i).toString());
            sumBoostCount += Integer.valueOf(boostCountList.get(i).toString());
        }
        freeProduct.setSumBoostCount((long) sumBoostCount);
        freeProduct.setSumGooodsCount((long) sumGooodsCount);
        freeProductService.save(freeProduct);
        freeDeliveryService.save(phFreeDeliveryList);
        return true;
    }

    @ResponseBody
    @RequestMapping("/freeDelivery_del")
    public boolean delete(@RequestParam("ids[]") Long[] ids) {
        List<Long> id = Arrays.asList(ids);
        freeProductService.deleteList(id);
        freeDeliveryService.deleteByproductIdInList(id);
        return true;
    }
}
