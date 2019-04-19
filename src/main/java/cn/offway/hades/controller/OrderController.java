package cn.offway.hades.controller;

import cn.offway.hades.domain.*;
import cn.offway.hades.properties.QiniuProperties;
import cn.offway.hades.service.*;
import com.alibaba.fastjson.JSON;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    @Autowired
    private PhAddressService addressService;
    @Autowired
    private PhGoodsStockService goodsStockService;
    @Autowired
    private PhGoodsService goodsService;
    @Autowired
    private PhGoodsPropertyService goodsPropertyService;

    @RequestMapping("/order.html")
    public String index(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        return "order_index";
    }

    @RequestMapping("/order_detail.html")
    public String orderDetail(ModelMap map, Long id) {
        Map<String, Object> dataList = new HashMap<>();
        PhOrderInfo orderInfo = orderInfoService.findOne(id);
        if (orderInfo != null) {
            //基本信息
            List<Map> basicInfoList = new ArrayList<>();
            Map<String, Object> basicInfo = new HashMap<>();
            basicInfo.put("orderNo", orderInfo.getOrderNo());
            basicInfo.put("createTime", orderInfo.getCreateTime());
            basicInfo.put("userId", orderInfo.getUserId());
            basicInfo.put("payChannel", orderInfo.getPayChannel());
            basicInfo.put("status", orderInfo.getStatus());
            PhOrderExpressInfo orderExpressInfo = orderExpressInfoService.findByPid(orderInfo.getOrderNo(), "0");//类型[0-寄,1-退]
            if (orderExpressInfo != null) {
                basicInfo.put("expressCode", orderExpressInfo.getExpressCode());
                basicInfo.put("mailNo", orderExpressInfo.getMailNo());
            }
            basicInfoList.add(basicInfo);
            dataList.put("basicInfo", basicInfoList);
            //备注
            List<Map> remarkInfoList = new ArrayList<>();
            Map<String, Object> remarkInfo = new HashMap<>();
            remarkInfo.put("remark", orderInfo.getRemark());
            remarkInfoList.add(remarkInfo);
            dataList.put("remarkInfo", remarkInfoList);
            //收货人信息
            List<Map> receiverInfoList = new ArrayList<>();
            Map<String, Object> receiverInfo = new HashMap<>();
            PhAddress address = addressService.findOne(orderInfo.getAddrId());
            if (address != null) {
                receiverInfo.put("realName", address.getRealName());
                receiverInfo.put("phone", address.getPhone());
                receiverInfo.put("location", address.getProvince() + address.getCity() + address.getCounty() + address.getContent());
            }
            receiverInfoList.add(receiverInfo);
            dataList.put("receiverInfo", receiverInfoList);
            //商品信息
            List<Map> goodsInfoList = new ArrayList<>();
            List<PhOrderGoods> orderGoodsList = orderGoodsService.findAllByPid(orderInfo.getOrderNo());
            for (PhOrderGoods obj : orderGoodsList) {
                Map<String, Object> goodsInfo = new HashMap<>();
                PhGoodsStock stock = goodsStockService.findOne(obj.getGoodsStockId());
                PhGoods goods = goodsService.findOne(obj.getGoodsId());
                List<PhGoodsProperty> properties = goodsPropertyService.findByStockId(stock.getId());
                goodsInfo.put("sku", stock.getSku());
                goodsInfo.put("goodsId", stock.getGoodsId());
                goodsInfo.put("goodsName", stock.getGoodsName());
                goodsInfo.put("code", goods.getCode());
                goodsInfo.put("price", stock.getPrice());
                goodsInfo.put("brandName", goods.getBrandName());
                StringBuilder sb = new StringBuilder();
                for (PhGoodsProperty p : properties) {
                    sb.append(p.getName());
                    sb.append(":");
                    sb.append(p.getValue());
                    sb.append(" ");
                }
                goodsInfo.put("skuStr", sb.toString());
                goodsInfo.put("goodsCount", obj.getGoodsCount());
                goodsInfoList.add(goodsInfo);
            }
            dataList.put("goodsInfo", goodsInfoList);
            //费用信息
            List<Map> feeInfoList = new ArrayList<>();
            Map<String, Object> feeInfo = new HashMap<>();
            feeInfo.put("price", orderInfo.getPrice());
            feeInfo.put("pVoucherAmount", orderInfo.getPVoucherAmount());
            feeInfo.put("mVoucherAmount", orderInfo.getMVoucherAmount());
            feeInfo.put("walletAmount", orderInfo.getWalletAmount());
            feeInfo.put("mailFee", orderInfo.getMailFee());
            feeInfo.put("amount", orderInfo.getAmount());
            feeInfoList.add(feeInfo);
            dataList.put("feeInfo", feeInfoList);
        }
        map.addAttribute("jsonStr", JSON.toJSONString(dataList));
        return "order_detail";
    }

    @ResponseBody
    @RequestMapping("/order_list")
    public Map<String, Object> getOrderList(HttpServletRequest request) {
        int sEcho = Integer.parseInt(request.getParameter("sEcho"));
        int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
        int iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
        String merchantId = request.getParameter("merchantId");
        merchantId = merchantId != null ? merchantId : "0";
        String orderNo = request.getParameter("orderNo");
        String sTime = request.getParameter("sTime");
        String eTime = request.getParameter("eTime");
        String userId = request.getParameter("userId");
        String payMethod = request.getParameter("payMethod");
        String status = request.getParameter("status");
        Sort sort = new Sort("id");
        Page<PhOrderInfo> pages = orderInfoService.findAll(Long.valueOf(merchantId), orderNo, sTime, eTime, userId, payMethod, status, new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, sort));
        int initEcho = sEcho + 1;
        Map<String, Object> map = new HashMap<>();
        map.put("sEcho", initEcho);
        map.put("iTotalRecords", pages.getTotalElements());//数据总条数
        map.put("iTotalDisplayRecords", pages.getTotalElements());//显示的条数
        map.put("aData", pages.getContent());//数据集合
        return map;
    }
}
