package cn.offway.hades.controller;

import cn.offway.hades.domain.*;
import cn.offway.hades.properties.QiniuProperties;
import cn.offway.hades.service.PhPromotionInfoService;
import cn.offway.hades.service.PhRoleadminService;
import cn.offway.hades.service.PhVoucherInfoService;
import cn.offway.hades.service.PhVoucherProjectService;
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
import java.util.*;

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
    private PhRoleadminService roleadminService;

    @RequestMapping("/promotion.html")
    public String index(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        return "promotion_index";
    }

    @ResponseBody
    @RequestMapping("/promotion_save")
    public boolean save(PhPromotionInfo PromotionInfo,@AuthenticationPrincipal PhAdmin admin,String discountJSONStr,String reduceJSONStr) {
        PromotionInfo.setCreateTime(new Date());
        PromotionInfo.setStatus("0");
        PromotionInfo.setRemark(admin.getNickname());
        promotionInfoService.save(PromotionInfo);
        return true;
    }

    @ResponseBody
    @RequestMapping("/promotion_del")
    public boolean delete(@RequestParam("ids[]") Long[] ids) {
        for (Long id : ids) {
            promotionInfoService.del(id);
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
    public Map<String, Object> getPromotionList(HttpServletRequest request) {
        int sEcho = Integer.parseInt(request.getParameter("sEcho"));
        int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
        int iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "id"));
        Page<PhPromotionInfo> pages = promotionInfoService.findAll( new PageRequest(iDisplayStart==0?0:iDisplayStart/iDisplayLength, iDisplayLength<0?9999999:iDisplayLength));
        int initEcho = sEcho + 1;
        Map<String, Object> map = new HashMap<>();
        map.put("sEcho", initEcho);
        map.put("iTotalRecords", pages.getTotalElements());//数据总条数
        map.put("iTotalDisplayRecords", pages.getTotalElements());//显示的条数
        map.put("aData", pages.getContent());//数据集合
        return map;
    }

    @ResponseBody
    @RequestMapping("/promotion_up")
    public boolean goodsUp(Long id, @AuthenticationPrincipal PhAdmin admin) {
        PhPromotionInfo promotionInfo = promotionInfoService.findOne(id);
        List<Long> roles = roleadminService.findRoleIdByAdminId(admin.getId());
        if (roles.contains(BigInteger.valueOf(8L))){
            return false;
        }else {
            if (promotionInfo != null){
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
        if (roles.contains(BigInteger.valueOf(8L))){
            return false;
        }else {
            if (promotionInfo != null){
                promotionInfo.setStatus("0");
                promotionInfo.setRemark(admin.getNickname());
                promotionInfoService.save(promotionInfo);
            }
        }
        return true;
    }
}
