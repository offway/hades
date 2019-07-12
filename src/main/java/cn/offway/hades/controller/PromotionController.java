package cn.offway.hades.controller;

import cn.offway.hades.domain.PhMerchant;
import cn.offway.hades.domain.PhPromotionInfo;
import cn.offway.hades.domain.PhVoucherProject;
import cn.offway.hades.properties.QiniuProperties;
import cn.offway.hades.service.PhMerchantService;
import cn.offway.hades.service.PhPromotionInfoService;
import cn.offway.hades.service.PhVoucherInfoService;
import cn.offway.hades.service.PhVoucherProjectService;
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

    @RequestMapping("/promotion.html")
    public String index(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        return "promotion_index";
    }

    @ResponseBody
    @RequestMapping("/promotion_save")
    public boolean save(PhPromotionInfo PromotionInfo) {
        PromotionInfo.setCreateTime(new Date());
        PromotionInfo.setStatus("0");
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
}
