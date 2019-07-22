package cn.offway.hades.controller;

import cn.offway.hades.domain.PhMerchant;
import cn.offway.hades.domain.PhVoucherProject;
import cn.offway.hades.properties.QiniuProperties;
import cn.offway.hades.service.PhMerchantService;
import cn.offway.hades.service.PhVoucherInfoService;
import cn.offway.hades.service.PhVoucherProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
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
public class CouponController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private QiniuProperties qiniuProperties;
    @Autowired
    private PhVoucherProjectService voucherProjectService;
    @Autowired
    private PhVoucherInfoService voucherInfoService;
    @Autowired
    private PhMerchantService merchantService;

    @RequestMapping("/coupon.html")
    public String index(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        return "coupon_index";
    }

    @ResponseBody
    @RequestMapping("/coupon_save")
    public boolean save(PhVoucherProject voucherProject) {
        if (voucherProject.getMerchantId() != null) {
            PhMerchant merchant = merchantService.findOne(voucherProject.getMerchantId());
            if (merchant != null) {
                voucherProject.setMerchantName(merchant.getName());
            }
        }
        if (voucherProject.getId() == null) {
            voucherProject.setCreateTime(new Date());
            voucherProject.setLimit(0L);
            voucherProject.setIsPrivate("0");
        }
        voucherProjectService.save(voucherProject);
        return true;
    }

    @ResponseBody
    @RequestMapping("/coupon_del")
    public boolean delete(@RequestParam("ids[]") Long[] ids) {
        for (Long id : ids) {
            voucherProjectService.del(id);
            voucherInfoService.delByPid(id);
        }
        return true;
    }

    @ResponseBody
    @Transactional
    @RequestMapping("/coupon_markAsPrivate")
    public boolean markAsPrivate(@RequestParam("ids[]") Long[] ids) {
        for (Long id : ids) {
            PhVoucherProject voucherProject = voucherProjectService.findOne(id);
            if (voucherProject != null) {
                voucherProject.setIsPrivate("1");
                voucherProjectService.save(voucherProject);
            }
        }
        return true;
    }

    @ResponseBody
    @Transactional
    @RequestMapping("/coupon_markAsPublic")
    public boolean markAsPublic(@RequestParam("ids[]") Long[] ids) {
        for (Long id : ids) {
            PhVoucherProject voucherProject = voucherProjectService.findOne(id);
            if (voucherProject != null) {
                voucherProject.setIsPrivate("0");
                voucherProjectService.save(voucherProject);
            }
        }
        return true;
    }

    @ResponseBody
    @RequestMapping("/coupon_get")
    public PhVoucherProject get(Long id) {
        return voucherProjectService.findOne(id);
    }

    @ResponseBody
    @RequestMapping("/coupon_find")
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
    @RequestMapping("/coupon_list")
    public Map<String, Object> getStockList(HttpServletRequest request) {
        ObjectMapper objectMapper = new ObjectMapper();
        int sEcho = Integer.parseInt(request.getParameter("sEcho"));
        int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
        int iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
        String type = request.getParameter("type");
        String name = request.getParameter("name");
        String merchantId = request.getParameter("merchantId");
        String remark = request.getParameter("remark");
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        String beginTimeStr = request.getParameter("beginTime");
        String endTimeStr = request.getParameter("endTime");
        Date beginTime = null;
        if (!"".equals(beginTimeStr)) {
            beginTime = DateTime.parse(beginTimeStr, format).toDate();
        }
        Date endTime = null;
        if (!"".equals(endTimeStr)) {
            endTime = DateTime.parse(endTimeStr, format).toDate();
        }
        Sort sort = new Sort("id");
        PageRequest pr = new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, sort);
        Page<PhVoucherProject> pages = voucherProjectService.list(type, name, Long.valueOf(merchantId), beginTime, endTime, remark, pr);
        int initEcho = sEcho + 1;
        Map<String, Object> map = new HashMap<>();
        map.put("sEcho", initEcho);
        map.put("iTotalRecords", pages.getTotalElements());//数据总条数
        map.put("iTotalDisplayRecords", pages.getTotalElements());//显示的条数
        List<Object> list = new ArrayList<>();
        for (PhVoucherProject item : pages.getContent()) {
            Map m = objectMapper.convertValue(item, Map.class);
            if (item.getMerchantId() != null) {
                PhMerchant merchant = merchantService.findOne(item.getMerchantId());
                if (merchant != null) {
                    m.put("merchantName", merchant.getName());
                } else {
                    m.put("merchantName", "未知");
                }
            } else {
                m.put("merchantName", "");
            }
            list.add(m);
        }
        map.put("aData", list);//数据集合
        return map;
    }
}
