package cn.offway.hades.controller;

import cn.offway.hades.domain.*;
import cn.offway.hades.properties.QiniuProperties;
import cn.offway.hades.service.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
public class PromotionController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private QiniuProperties qiniuProperties;
    @Autowired
    private PhVoucherProjectService voucherProjectService;
    @Autowired
    private PhVoucherInfoService voucherInfoService;
    @Autowired
    private PhPromotionInfoService promotionInfoService;
    @Autowired
    private PhPromotionRuleService promotionRuleService;
    @Autowired
    private PhPromotionGoodsService promotionGoodsService;
    @Autowired
    private PhRoleadminService roleadminService;
    @Autowired
    private PhPickGoodsService pickGoodsService;
    @Autowired
    private PhMerchantService merchantService;
    @Autowired
    private PhMerchantBrandService merchantBrandService;
    @Autowired
    private PhGoodsService goodsService;

    @RequestMapping("/promotion.html")
    public String index(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        return "promotion_index";
    }

    @ResponseBody
    @RequestMapping("/promotion_save")
    public boolean save(PhPromotionInfo promotionInfo, @AuthenticationPrincipal PhAdmin admin, String discountJSONStr, String reduceJSONStr, @RequestParam(name = "goodsId", required = true) String[] goodsId, String gift,String giftLimit) {
        PhPromotionInfo infoSaved;
        if (promotionInfo.getId() == null){
            promotionInfo.setCreateTime(new Date());
            promotionInfo.setStatus("0");
            promotionInfo.setRemark(admin.getNickname());
            infoSaved = promotionInfoService.save(promotionInfo);
        }else {
            PhPromotionInfo getphPromotionInfo = promotionInfoService.findOne(promotionInfo.getId());
            getphPromotionInfo.setName(promotionInfo.getName());
            getphPromotionInfo.setType(promotionInfo.getType());
            getphPromotionInfo.setMerchantId(promotionInfo.getMerchantId());
            getphPromotionInfo.setBeginTime(promotionInfo.getBeginTime());
            getphPromotionInfo.setEndTime(promotionInfo.getEndTime());
            getphPromotionInfo.setMode(promotionInfo.getMode());
            infoSaved = promotionInfoService.save(getphPromotionInfo);
            promotionGoodsService.del(promotionInfo.getId());
            promotionRuleService.del(promotionInfo.getId());
        }
        //保存规则
        switch (promotionInfo.getMode()) {
            case "0":
                JSONArray jsonArray = JSONArray.parseArray(discountJSONStr);
                for (Object object : jsonArray) {
                    JSONObject o = (JSONObject) object;
                    PhPromotionRule rule = new PhPromotionRule();
                    rule.setCreateTime(new Date());
                    rule.setDiscountNum(o.getLong("discount_num"));
                    rule.setDiscountRate(o.getDouble("discount_rate"));
                    rule.setPromotionId(infoSaved.getId());
                    promotionRuleService.save(rule);
                }
                break;
            case "1":
                JSONArray jsonArray2 = JSONArray.parseArray(reduceJSONStr);
                for (Object object : jsonArray2) {
                    JSONObject o = (JSONObject) object;
                    PhPromotionRule rule = new PhPromotionRule();
                    rule.setCreateTime(new Date());
                    rule.setReduceLimit(o.getDouble("reduce_limit"));
                    rule.setReduceAmount(o.getDouble("reduce_amount"));
                    rule.setPromotionId(infoSaved.getId());
                    promotionRuleService.save(rule);
                }
                break;
            case "2":
                PhPromotionRule rule = new PhPromotionRule();
                rule.setCreateTime(new Date());
                rule.setGift(gift);
                rule.setGiftLimit(giftLimit);
                rule.setPromotionId(infoSaved.getId());
                promotionRuleService.save(rule);
            default:
                break;
        }
        //保存商品
        for (String gid : goodsId) {
            if ("".equals(gid.trim())) {
                continue;
            }
            PhPromotionGoods goods = new PhPromotionGoods();
            goods.setCreateTime(new Date());
            goods.setGoodsId(Long.valueOf(gid));
            goods.setPromotionId(infoSaved.getId());
            promotionGoodsService.save(goods);
        }
        return true;
    }

    @ResponseBody
    @RequestMapping("/promotion_del")
    public boolean delete(@RequestParam("ids[]") Long[] ids) {
        for (Long id : ids) {
            promotionInfoService.del(id);
            promotionGoodsService.del(id);
            promotionRuleService.del(id);
        }
        return true;
    }

    @ResponseBody
    @RequestMapping("/promotion_get")
    public PhPromotionInfo get(Long id) {
        return promotionInfoService.findOne(id);
    }

    @ResponseBody
    @RequestMapping("/promotion_find")
    public Map<String, Object> find(Long id) {
        Map<String, Object> map = new HashMap<>();
        PhVoucherProject voucherProject = voucherProjectService.findOne(id);
        if (voucherProject != null) {
            map.put("main", voucherProject);
            map.put("voucherInfoList", voucherInfoService.getByPid(id));
        }
        return map;
    }

    @ResponseBody
    @RequestMapping("/promotion_list")
    public Map<String, Object> getPromotionList(HttpServletRequest request,String type,String status,String mode) {
        int sEcho = Integer.parseInt(request.getParameter("sEcho"));
        int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
        int iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "status"));
        Page<PhPromotionInfo> pages = promotionInfoService.findAll(type,status,mode,new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength,sort));
        //
        int initEcho = sEcho + 1;
        Map<String, Object> map = new HashMap<>();
        map.put("sEcho", initEcho);
        map.put("iTotalRecords", pages.getTotalElements());//数据总条数
        map.put("iTotalDisplayRecords", pages.getTotalElements());//显示的条数
        map.put("aData", pages.getContent());//数据集合
        return map;
    }

    @ResponseBody
    @RequestMapping("/promotion_findAll")
    public Map<String, Object> findPromotionAllList(Long id) {
        PhPromotionInfo promotionInfo = promotionInfoService.findOne(id);
        Map<String, Object> map = new HashMap<>();
        if (promotionInfo != null) {
            List<PhPromotionGoods> promotionGoodsList = promotionGoodsService.findAllByPid(promotionInfo.getId());
            List<PhPromotionRule> promotionRuleList = promotionRuleService.findAllByPid(promotionInfo.getId());
            map.put("main", promotionInfo);
            map.put("promotionGoodsList",promotionGoodsList);
            map.put("promotionRuleList", promotionRuleList);
        }
        return map;
    }

    @ResponseBody
    @RequestMapping("/promotion_up")
    public boolean goodsUp(Long id, @AuthenticationPrincipal PhAdmin admin) {
        PhPromotionInfo promotionInfo = promotionInfoService.findOne(id);
        List<Long> roles = roleadminService.findRoleIdByAdminId(admin.getId());
        if (roles.contains(BigInteger.valueOf(8L))) {
            return false;
        } else {
            if (promotionInfo != null) {
                promotionInfo.setStatus("1");
                promotionInfo.setRemark(admin.getNickname());
                promotionInfoService.save(promotionInfo);
            }
        }
        return true;
    }

    @ResponseBody
    @RequestMapping("/promotion_down")
    public boolean goodsDown(Long id, @AuthenticationPrincipal PhAdmin admin) {
        PhPromotionInfo promotionInfo = promotionInfoService.findOne(id);
        List<Long> roles = roleadminService.findRoleIdByAdminId(admin.getId());
        if (roles.contains(BigInteger.valueOf(8L))) {
            return false;
        } else {
            if (promotionInfo != null) {
                promotionInfo.setStatus("0");
                promotionInfo.setRemark(admin.getNickname());
                promotionInfoService.save(promotionInfo);
            }
        }
        return true;
    }

    @ResponseBody
    @RequestMapping("/promotion_getGoodsListByPickId")
    public List<Integer> getGoodsListByPickId(Long pickId) {
        return pickGoodsService.findAllRestByPid(pickId);
    }

    @ResponseBody
    @RequestMapping("/promotion_getGoodsListAll")
    public List<Integer> getGoodsListAll() {
        return pickGoodsService.findAllRestGoods();
    }

    @ResponseBody
    @RequestMapping("/promotion_getMerchantList")
    public List<PhMerchant> getMerchantList(@RequestParam(name = "id", required = false, defaultValue = "") String id) {
        Long mid = null;
        if (!"".equals(id)) {
            mid = Long.valueOf(id);
        }
        return merchantService.findAll(mid);
    }

    @ResponseBody
    @RequestMapping("/promotion_getBrandList")
    public List<PhMerchantBrand> getBrandList(@RequestParam(name = "id", required = false, defaultValue = "") String id) {
        Long mid = null;
        if (!"".equals(id)) {
            mid = Long.valueOf(id);
        }
        return merchantBrandService.findByPid(mid);
    }

    @ResponseBody
    @RequestMapping("/promotion_getGoodsList")
    public List<PhGoods> getGoodsList(@RequestParam(name = "mid", required = false, defaultValue = "") String mid, String bid) {
        return goodsService.findAllAlt(mid, bid);
    }

    @ResponseBody
    @RequestMapping("/promotion_getGoodsImageByid")
    public PhGoods getGoodsListAll(@RequestParam(name = "id", required = false, defaultValue = "") Long id) {
        return goodsService.findOne(id);
    }
}
