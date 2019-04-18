package cn.offway.hades.controller;

import cn.offway.hades.domain.PhOrderInfo;
import cn.offway.hades.properties.QiniuProperties;
import cn.offway.hades.service.PhOrderExpressInfoService;
import cn.offway.hades.service.PhOrderGoodsService;
import cn.offway.hades.service.PhOrderInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping
public class OrderController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private QiniuProperties qiniuProperties;
    @Autowired
    private PhOrderInfoService orderInfoService;
    @Autowired
    private PhOrderGoodsService orderGoodsService;
    @Autowired
    private PhOrderExpressInfoService orderExpressInfoService;

    @RequestMapping("/order.html")
    public String index(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        return "order_index";
    }

    @ResponseBody
    @RequestMapping("/order_list")
    public Map<String, Object> getStockList(HttpServletRequest request) {
        int sEcho = Integer.parseInt(request.getParameter("sEcho"));
        int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
        int iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
        String mid = request.getParameter("theId");
        String orderNo = request.getParameter("orderNo");
        String sTime = request.getParameter("sTime");
        String eTime = request.getParameter("eTime");
        String userId = request.getParameter("userId");
        String payMethod = request.getParameter("payMethod");
        String status = request.getParameter("status");
        Sort sort = new Sort("id");
        Page<PhOrderInfo> pages = orderInfoService.findAll(Long.valueOf(mid), orderNo, sTime, eTime, userId, payMethod, status, new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, sort));
        int initEcho = sEcho + 1;
        Map<String, Object> map = new HashMap<>();
        map.put("sEcho", initEcho);
        map.put("iTotalRecords", pages.getTotalElements());//数据总条数
        map.put("iTotalDisplayRecords", pages.getTotalElements());//显示的条数
        map.put("aData", pages.getContent());//数据集合
        return map;
    }
}
