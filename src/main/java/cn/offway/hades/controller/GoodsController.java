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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
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
    @Autowired
    private PhMerchantBrandService merchantBrandService;
    @Autowired
    private PhRoleadminService roleadminService;
    @Autowired
    private PhMerchantFareService merchantFareService;
    @Autowired
    private PhMerchantFareSpecialService merchantFareSpecialService;

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

    @RequestMapping("/goods_edit.html")
    public String edit(ModelMap map, Long id) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        map.addAttribute("theId", id);
        return "goods_edit";
    }

    @RequestMapping("/goods_stock_index.html")
    public String stockIndex(ModelMap map, Long id) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        map.addAttribute("theId", id);
        return "goods_stock_index";
    }

    @RequestMapping("/fare_index.html")
    public String fareIndex(ModelMap map, Long id) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        map.addAttribute("theId", id);
        return "fare_index";
    }

    @RequestMapping("/fare_spec_index.html")
    public String fareSpecIndex(ModelMap map, Long id) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        map.addAttribute("theId", id);
        return "fare_spec_index";
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
    @RequestMapping("/merchant_list_all")
    public List<PhMerchant> getMerchant(@AuthenticationPrincipal PhAdmin admin) {
        List<Long> roles = roleadminService.findRoleIdByAdminId(admin.getId());
        if (roles.contains(BigInteger.valueOf(8L))) {
            PhMerchant merchant = merchantService.findByAdminId(admin.getId());
            return merchantService.findAll(merchant.getId());
        } else {
            List<PhMerchant> list = merchantService.findAll();
            PhMerchant defaultAll = new PhMerchant();
            defaultAll.setId(0L);
            defaultAll.setName("全部");
            list.add(defaultAll);
            return list;
        }
    }

    @ResponseBody
    @RequestMapping("/brand_list_all")
    public List<?> getBrand(@AuthenticationPrincipal PhAdmin admin) {
        List<Long> roles = roleadminService.findRoleIdByAdminId(admin.getId());
        if (roles.contains(BigInteger.valueOf(8L))) {
            PhMerchant merchant = merchantService.findByAdminId(admin.getId());
            return merchantBrandService.findByPid(merchant.getId());
        } else {
            return brandService.findAll();
        }
    }

    @ResponseBody
    @RequestMapping("/fare_list")
    public List<PhMerchantFare> getFareList(Long pid) {
        return merchantFareService.getByPid(pid);
    }

    @ResponseBody
    @RequestMapping("/fare_add")
    public boolean addFare(PhMerchantFare merchantFare) {
        merchantFare.setCreateTime(new Date());
        Long cnt = merchantFareService.getCountByPid(merchantFare.getMerchantId());
        if (cnt > 0) {
            merchantFare.setIsDefault("0");
        } else {
            merchantFare.setIsDefault("1");
            updateFreeFare(merchantFare);
        }
        merchantFareService.save(merchantFare);
        return true;
    }

    private void updateFreeFare(PhMerchantFare merchantFare) {
        PhMerchant merchant = merchantService.findOne(merchantFare.getMerchantId());
        if (merchantFare.getFareFirstPrice() == 0d && merchantFare.getFareNextPrice() == 0d) {
            /* 是否包邮[0-否,1-是] */
            merchant.setIsFreeFare("1");
        } else {
            merchant.setIsFreeFare("0");
        }
        merchantService.save(merchant);
    }

    @ResponseBody
    @RequestMapping("/fare_spec_add_batch")
    public boolean addFareBatch(PhMerchantFareSpecial merchantFareSpecial, @RequestParam("provinceStr[]") String[] provinceStr, @RequestParam("cityStr[]") String[] cityStr, @RequestParam("countyStr[]") String[] countyStr) {
        int i = 0;
        for (String p : provinceStr) {
            merchantFareSpecial.setProvince(provinceStr[i]);
            merchantFareSpecial.setCity(cityStr[i]);
            merchantFareSpecial.setCounty(countyStr[i]);
            merchantFareSpecial.setCreateTime(new Date());
            merchantFareSpecialService.save(merchantFareSpecial);
            i++;
        }
        return true;
    }

    @ResponseBody
    @RequestMapping("/fare_spec_add")
    public boolean addFare(PhMerchantFareSpecial merchantFareSpecial, @RequestParam("provinceStr") String provinceStr, @RequestParam("cityStr") String cityStr, @RequestParam("countyStr") String countyStr) {
        String[] pList = provinceStr.split(",");
        String[] cList = cityStr.split(",");
        String[] ccList = countyStr.split(",");
        int i = 0;
        for (String p : pList) {
            PhMerchantFareSpecial tmp = new PhMerchantFareSpecial();
            tmp.setFareFirstNum(merchantFareSpecial.getFareFirstNum());
            tmp.setFareFirstPrice(merchantFareSpecial.getFareFirstPrice());
            tmp.setFareNextNum(merchantFareSpecial.getFareNextNum());
            tmp.setFareNextPrice(merchantFareSpecial.getFareNextPrice());
            tmp.setMerchantFareId(merchantFareSpecial.getMerchantFareId());
            tmp.setRemark(merchantFareSpecial.getRemark());
            tmp.setProvince(p);
            if ("".equals(cList[i].trim())) {
                tmp.setCity(null);
            } else {
                tmp.setCity(cList[i]);
            }
            if ("".equals(ccList[i].trim())) {
                tmp.setCounty(null);
            } else {
                tmp.setCounty(ccList[i]);
            }
            tmp.setCreateTime(new Date());
            merchantFareSpecialService.save(tmp);
            i++;
        }
        return true;
    }

    @ResponseBody
    @RequestMapping("/merchant_fare_list")
    public Map<String, Object> getMerchantFareList(HttpServletRequest request) {
        int sEcho = Integer.parseInt(request.getParameter("sEcho"));
        int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
        int iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
        String gid = request.getParameter("theId");
        String remark = request.getParameter("remark");
        Sort sort = new Sort("id");
        Page<PhMerchantFare> pages = merchantFareService.getByPidPage(Long.valueOf(gid), remark, new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, sort));
        int initEcho = sEcho + 1;
        Map<String, Object> map = new HashMap<>();
        map.put("sEcho", initEcho);
        map.put("iTotalRecords", pages.getTotalElements());//数据总条数
        map.put("iTotalDisplayRecords", pages.getTotalElements());//显示的条数
        map.put("aData", pages.getContent());//数据集合
        return map;
    }

    @ResponseBody
    @RequestMapping("/merchant_fare_spec_list")
    public Map<String, Object> getMerchantFareSpecList(HttpServletRequest request) {
        int sEcho = Integer.parseInt(request.getParameter("sEcho"));
        int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
        int iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
        String gid = request.getParameter("theId");
        String remark = request.getParameter("remark");
        Sort sort = new Sort("id");
        Page<PhMerchantFareSpecial> pages = merchantFareSpecialService.getByPidPage(Long.valueOf(gid), remark, new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, sort));
        int initEcho = sEcho + 1;
        Map<String, Object> map = new HashMap<>();
        map.put("sEcho", initEcho);
        map.put("iTotalRecords", pages.getTotalElements());//数据总条数
        map.put("iTotalDisplayRecords", pages.getTotalElements());//显示的条数
        map.put("aData", pages.getContent());//数据集合
        return map;
    }

    @ResponseBody
    @RequestMapping("/goods_stock_list")
    public Map<String, Object> getStockList(HttpServletRequest request) {
        int sEcho = Integer.parseInt(request.getParameter("sEcho"));
        int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
        int iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
        String gid = request.getParameter("theId");
        String remark = request.getParameter("remark");
        Sort sort = new Sort("id");
        Page<PhGoodsStock> pages = goodsStockService.findAll(gid, remark, new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, sort));
        int initEcho = sEcho + 1;
        Map<String, Object> map = new HashMap<>();
        map.put("sEcho", initEcho);
        map.put("iTotalRecords", pages.getTotalElements());//数据总条数
        map.put("iTotalDisplayRecords", pages.getTotalElements());//显示的条数
        map.put("aData", pages.getContent());//数据集合
        return map;
    }

    @ResponseBody
    @RequestMapping("/goods_list")
    public Map<String, Object> getList(HttpServletRequest request) {
        int sEcho = Integer.parseInt(request.getParameter("sEcho"));
        int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
        int iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
        String id = request.getParameter("id");
        if ("".equals(id.trim())) {
            id = "0";
        }
        String name = request.getParameter("name");
        String code = request.getParameter("code");
        String status = request.getParameter("status");
        String merchantId = request.getParameter("merchantId");
        String type = request.getParameter("type");
        String category = request.getParameter("category");
        Sort sort = new Sort("id");
        Page<PhGoods> pages = goodsService.findAll(name, Long.valueOf(id), code, status, Long.valueOf(merchantId), type, category, new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, sort));
        int initEcho = sEcho + 1;
        Map<String, Object> map = new HashMap<>();
        map.put("sEcho", initEcho);
        map.put("iTotalRecords", pages.getTotalElements());//数据总条数
        map.put("iTotalDisplayRecords", pages.getTotalElements());//显示的条数
        map.put("aData", pages.getContent());//数据集合
        return map;
    }

    @ResponseBody
    @RequestMapping("/goods_find")
    public Map<String, Object> find(Long id) {
        Map<String, Object> map = new HashMap<>();
        PhGoods goods = goodsService.findOne(id);
        if (goods != null) {
            map.put("main", goods);
            map.put("propertyList", goodsPropertyService.findByPid(goods.getId()));
            map.put("imageList", goodsImageService.findByPid(goods.getId()));
            List<PhGoodsStock> stocks = goodsStockService.findByPid(goods.getId());
            map.put("stockList", stocks);
            Map<String, Object> stockMap = new HashMap<>();
            for (PhGoodsStock stock : stocks) {
                StringBuilder sb = new StringBuilder();
                List<PhGoodsProperty> l = goodsPropertyService.findByStockId(stock.getId());
                for (PhGoodsProperty property : l) {
                    sb.append(property.getName());
                    sb.append(property.getValue());
                }
                stockMap.put(sb.toString(), stock);
            }
            map.put("stockMap", stockMap);
        }
        return map;
    }

    @ResponseBody
    @RequestMapping("/goods_stock_find")
    public Map<String, Object> findStock(Long id) {
        Map<String, Object> map = new HashMap<>();
        PhGoodsStock goodsStock = goodsStockService.findOne(id);
        if (goodsStock != null) {
            map.put("main", goodsStock);
            map.put("propertyList", goodsPropertyService.findByStockId(goodsStock.getId()));
        }
        return map;
    }

    @ResponseBody
    @RequestMapping("/goods_findOne")
    public PhGoods findOne(Long id) {
        return goodsService.findOne(id);
    }

    @ResponseBody
    @RequestMapping("/goods_del")
    public boolean delete(@RequestParam("ids[]") Long[] ids, @AuthenticationPrincipal PhAdmin admin) {
        List<Long> roles = roleadminService.findRoleIdByAdminId(admin.getId());
        if (roles.contains(BigInteger.valueOf(8L))) {
            return false;
        } else {
            for (Long id : ids) {
                goodsService.del(id);
                goodsImageService.delByPid(id);
                goodsPropertyService.delByPid(id);
                goodsStockService.delByPid(id);
            }
            return true;
        }
    }

    @ResponseBody
    @RequestMapping("/goods_stock_del")
    public boolean deleteStock(@RequestParam("ids[]") Long[] ids) {
        for (Long id : ids) {
            goodsStockService.del(id);
            goodsPropertyService.delByStockId(id);
        }
        return true;
    }

    @ResponseBody
    @RequestMapping("/goods_stock_update_stock")
    public boolean updateStockStock(@RequestParam("ids[]") Long[] ids, Long stock) {
        for (Long id : ids) {
            PhGoodsStock goodsStock = goodsStockService.findOne(id);
            goodsStock.setStock(goodsStock.getStock() + stock);
            goodsStock.setVersion(goodsStock.getVersion() + 1);
            goodsStockService.save(goodsStock);
        }
        return true;
    }

    @ResponseBody
    @RequestMapping("/goods_stock_update_price")
    public boolean updateStockPrice(@RequestParam("ids[]") Long[] ids, Double price) {
        for (Long id : ids) {
            PhGoodsStock goodsStock = goodsStockService.findOne(id);
            goodsStock.setPrice(price);
            goodsStockService.save(goodsStock);
        }
        return true;
    }

    @ResponseBody
    @RequestMapping("/fare_del")
    public boolean deleteFare(@RequestParam("ids[]") Long[] ids) {
        for (Long id : ids) {
            merchantFareService.del(id);
            merchantFareSpecialService.delByPid(id);
        }
        return true;
    }

    @ResponseBody
    @RequestMapping("/fare_spec_del")
    public boolean deleteFareSpec(@RequestParam("ids[]") Long[] ids) {
        for (Long id : ids) {
            merchantFareSpecialService.del(id);
        }
        return true;
    }

    @ResponseBody
    @RequestMapping("/fare_get")
    public PhMerchantFare getFare(Long id) {
        return merchantFareService.findOne(id);
    }

    @ResponseBody
    @RequestMapping("/fare_spec_get")
    public PhMerchantFareSpecial getFareSpec(Long id) {
        return merchantFareSpecialService.findOne(id);
    }

    @ResponseBody
    @RequestMapping("/fare_set_default")
    public boolean setDefaultFare(Long id) {
        PhMerchantFare merchantFare = merchantFareService.findOne(id);
        merchantFareService.makeAllToUnDefault(merchantFare.getMerchantId());
        merchantFare.setIsDefault("1");
        updateFreeFare(merchantFare);
        merchantFareService.save(merchantFare);
        return true;
    }

    @ResponseBody
    @RequestMapping("/goods_up")
    public boolean goodsUp(Long id) {
        PhGoods goods = goodsService.findOne(id);
        if (goods != null) {
            goods.setStatus("1");
            goods.setUpTime(new Date());
            goodsService.save(goods);
        }
        return true;
    }

    @ResponseBody
    @RequestMapping("/goods_down")
    public boolean goodsDown(Long id) {
        PhGoods goods = goodsService.findOne(id);
        if (goods != null) {
            goods.setStatus("0");
            goodsService.save(goods);
        }
        return true;
    }

    @ResponseBody
    @RequestMapping("/goods_add")
    public boolean add(PhGoods goods, @RequestParam("stocks") String stocks, @RequestParam("banners") String[] banners, @RequestParam("intros") String[] intros) throws UnsupportedEncodingException {
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
        if (goods.getId() == null) {//add
            goods.setCreateTime(new Date());
            goods.setStatus("0");//默认未上架
            goods.setViewCount(0L);
            goods.setSaleCount(0L);
            goods.setUpTime(null);
        } else {
            PhGoods tmpObj = goodsService.findOne(goods.getId());
            goods.setCreateTime(tmpObj.getCreateTime());
            goods.setStatus(tmpObj.getStatus());
            goods.setViewCount(tmpObj.getViewCount());
            goods.setSaleCount(tmpObj.getSaleCount());
            goods.setUpTime(tmpObj.getUpTime());
        }
        PhGoods goodsSaved = goodsService.save(goods);
        if (goods.getId() != null) {
            //try purge first 轮播图 & 商品长图
            goodsImageService.delByPid(goods.getId());
        }
        //banner 轮播图
        long i = 0;
        for (String banner : banners) {
            PhGoodsImage goodsImage = new PhGoodsImage();
            goodsImage.setGoodsId(goodsSaved.getId());
            goodsImage.setGoodsName(goodsSaved.getName());
            goodsImage.setImageUrl(banner);
            goodsImage.setType("0");
            goodsImage.setSort(i);
            goodsImage.setCreateTime(new Date());
            goodsImageService.save(goodsImage);
            i++;
        }
        //商品长图
        i = 0;
        for (String intro : intros) {
            PhGoodsImage goodsImage = new PhGoodsImage();
            goodsImage.setGoodsId(goodsSaved.getId());
            goodsImage.setGoodsName(goodsSaved.getName());
            goodsImage.setImageUrl(intro);
            goodsImage.setType("1");
            goodsImage.setSort(i);
            goodsImage.setCreateTime(new Date());
            goodsImageService.save(goodsImage);
            i++;
        }
        //库存 & 规格
        List<Double> priceList = new ArrayList<>();
        for (Object o : JSON.parseArray(stocks)) {
            JSONObject obj = (JSONObject) o;
            PhGoodsStock goodsStock;
            if (obj.containsKey("id") && !"".equals(obj.getString("id"))) {//edit
                goodsStock = goodsStockService.findOne(Long.valueOf(obj.getString("id")));
            } else {
                goodsStock = new PhGoodsStock();
            }
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
            priceList.add(goodsStock.getPrice());
            goodsStock.setRemark(obj.getString("remark"));
            goodsStock.setImage(obj.getString("image"));
            goodsStock.setCreateTime(new Date());
            PhGoodsStock goodsStockSaved = goodsStockService.save(goodsStock);
            if (goodsStock.getId() != null) {
                //try purge first 规格
                goodsPropertyService.delByStockId(goodsStock.getId());
            }
            byte[] jsonStr = Base64.decode(obj.getString("detail"), Base64.DEFAULT);
            JSONArray jsonArray = JSON.parseArray(new String(jsonStr, StandardCharsets.UTF_8));
            if (jsonArray != null) {
                long index = 0L;
                for (Object oo : jsonArray) {
                    JSONObject jsonObject = (JSONObject) oo;
                    PhGoodsProperty goodsProperty = new PhGoodsProperty();
                    goodsProperty.setGoodsId(goodsSaved.getId());
                    goodsProperty.setGoodsStockId(goodsStockSaved.getId());
                    goodsProperty.setName(jsonObject.getString("name"));
                    goodsProperty.setValue(jsonObject.getJSONObject("value").getString("value"));
                    goodsProperty.setSort(index);
//                    goodsProperty.setSort(jsonObject.getJSONObject("value").getLong("sort"));
                    goodsProperty.setRemark(jsonObject.getJSONObject("value").getString("remark"));
                    goodsProperty.setCreateTime(new Date());
                    goodsPropertyService.save(goodsProperty);
                    index++;
                }
            } else {
                logger.error("stocks json 非法");
            }
        }
        Collections.sort(priceList);
        Double lowest = priceList.get(0);
        Double highest = priceList.get(priceList.size() - 1);
        String priceRange;
        if (Double.compare(lowest, highest) == 0) {
            priceRange = String.format("%.2f", lowest);
        } else {
            priceRange = String.format("%.2f-%.2f", lowest, highest);
        }
        goodsSaved.setPriceRange(priceRange);
        goodsService.save(goodsSaved);
        return true;
    }
}
