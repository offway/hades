package cn.offway.hades.controller;

import cn.offway.hades.domain.PhVoucherProject;
import cn.offway.hades.properties.QiniuProperties;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    @RequestMapping("/coupon.html")
    public String index(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        return "coupon_index";
    }

    @ResponseBody
    @RequestMapping("/coupon_save")
    public boolean save(PhVoucherProject voucherProject) {
        voucherProject.setCreateTime(new Date());
        voucherProject.setLimit(0L);
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
    @RequestMapping("/coupon_list")
    public Map<String, Object> getStockList(HttpServletRequest request) {
        int sEcho = Integer.parseInt(request.getParameter("sEcho"));
        int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
        int iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
        String type = request.getParameter("type");
        String name = request.getParameter("name");
        String merchantId = request.getParameter("merchantId");
        DateFormat formatter = DateFormat.getDateTimeInstance();
        String beginTimeStr = request.getParameter("beginTime");
        String endTimeStr = request.getParameter("endTime");
        Date beginTime = null;
        Date endTime = null;
        try {
            if (!"".equals(beginTimeStr) && !"".equals(endTimeStr)) {
                beginTime = formatter.parse(beginTimeStr);
                endTime = formatter.parse(endTimeStr);
            }
        } catch (ParseException e) {
            logger.error("error", e);
        }
        String remark = request.getParameter("remark");
        Sort sort = new Sort("id");
        Page<PhVoucherProject> pages = voucherProjectService.list(type, name, Long.valueOf(merchantId), beginTime, endTime, remark, new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, sort));
        int initEcho = sEcho + 1;
        Map<String, Object> map = new HashMap<>();
        map.put("sEcho", initEcho);
        map.put("iTotalRecords", pages.getTotalElements());//数据总条数
        map.put("iTotalDisplayRecords", pages.getTotalElements());//显示的条数
        map.put("aData", pages.getContent());//数据集合
        return map;
    }
}
