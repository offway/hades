package cn.offway.hades.controller;

import cn.offway.hades.domain.*;
import cn.offway.hades.properties.QiniuProperties;
import cn.offway.hades.service.*;
import cn.offway.hades.utils.HttpClientUtil;
import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    @Autowired
    private PhMerchantService merchantService;
    @Autowired
    private JPushService jPushService;
    @Autowired
    private PhPreorderInfoService preorderInfoService;
    @Autowired
    private PhUserInfoService userInfoService;
    @Autowired
    private PhPreorderInfoService phPreorderInfoService;

    @RequestMapping("/order.html")
    public String index(ModelMap map, String theId) {
        if (theId != null) {
            map.addAttribute("theId", theId);
        } else {
            map.addAttribute("theId", null);
        }
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        return "order_index";
    }

    @RequestMapping("/preOrder.html")
    public String indexPre(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        return "order_index_pre";
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
                if (obj.getGoodsId() == null || obj.getGoodsStockId() == null) {
                    continue;
                }
                Map<String, Object> goodsInfo = new HashMap<>();
                PhGoodsStock stock = goodsStockService.findOne(obj.getGoodsStockId());
                PhGoods goods = goodsService.findOne(obj.getGoodsId());
                if (stock == null || goods == null) {
                    continue;
                }
                List<PhGoodsProperty> properties = goodsPropertyService.findByStockId(stock.getId());
                goodsInfo.put("sku", stock.getSku());
                goodsInfo.put("goodsId", stock.getGoodsId());
                goodsInfo.put("goodsName", stock.getGoodsName());
                goodsInfo.put("image", stock.getImage());
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
    public Map<String, Object> getOrderList(HttpServletRequest request, @RequestParam(name = "status[]", required = false, defaultValue = "") String[] status) {
        int sEcho = Integer.parseInt(request.getParameter("sEcho"));
        int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
        int iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
        String theId = request.getParameter("theId");
        Page<PhOrderInfo> pages = null;
        if ("".equals(theId)) {
            String merchantId = request.getParameter("merchantId");
            merchantId = merchantId != null ? merchantId : "0";
            String orderNo = request.getParameter("orderNo");
            String sTimeStr = request.getParameter("sTime");
            String eTimeStr = request.getParameter("eTime");
            Date sTime = null, eTime = null;
            DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            if (!"".equals(sTimeStr) && !"".equals(eTimeStr)) {
                sTime = DateTime.parse(sTimeStr, format).toDate();
                eTime = DateTime.parse(eTimeStr, format).toDate();
            }
            String userId = request.getParameter("userId");
            String payMethod = request.getParameter("payMethod");
            Sort sort = new Sort("id");
            pages = (Page<PhOrderInfo>) orderInfoService.findAll(Long.valueOf(merchantId), orderNo, sTime, eTime, userId, payMethod, status, new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, sort));
        } else {
            Sort sort = new Sort("id");
            pages = orderInfoService.findAll(theId, new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, sort));
        }
        int initEcho = sEcho + 1;
        Map<String, Object> map = new HashMap<>();
        map.put("sEcho", initEcho);
        map.put("iTotalRecords", pages.getTotalElements());//数据总条数
        map.put("iTotalDisplayRecords", pages.getTotalElements());//显示的条数
        map.put("aData", pages.getContent());//数据集合
        return map;
    }

    @ResponseBody
    @RequestMapping("/order_list_all")
    public Map<String, Object> getOrderListAll(HttpServletRequest request, @RequestParam(name = "status[]", required = false, defaultValue = "") String[] status) {
        String theId = request.getParameter("theId");
        List<PhOrderInfo> list = null;
        if ("".equals(theId)) {
            String merchantId = request.getParameter("merchantId");
            merchantId = merchantId != null ? merchantId : "0";
            String orderNo = request.getParameter("orderNo");
            String userId = request.getParameter("userId");
            String payMethod = request.getParameter("payMethod");
            Date sTime = null, eTime = null;
            DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            String sTimeStr = request.getParameter("sTime");
            String eTimeStr = request.getParameter("eTime");
            if (!"".equals(sTimeStr) && !"".equals(eTimeStr)) {
                sTime = DateTime.parse(sTimeStr, format).toDate();
                eTime = DateTime.parse(eTimeStr, format).toDate();
            }
            list = (List<PhOrderInfo>) orderInfoService.findAll(Long.valueOf(merchantId), orderNo, sTime, eTime, userId, payMethod, status, null);
        } else {
            list = orderInfoService.findAll(theId);
        }
        Map<String, Object> map = new HashMap<>();
        double amount = 0, price = 0, mailFee = 0, pVoucherAmount = 0;
        for (PhOrderInfo item : list) {
            amount += item.getAmount();
            price += item.getPrice() - item.getMVoucherAmount();
            mailFee += item.getMailFee();
            pVoucherAmount += item.getPVoucherAmount();
        }
        map.put("amount", amount);
        map.put("price", price);
        map.put("mailFee", mailFee);
        map.put("pVoucherAmount", pVoucherAmount);
        return map;
    }

    @ResponseBody
    @RequestMapping("/order_list_pre")
    public Map<String, Object> getPreOrderList(HttpServletRequest request) {
        int sEcho = Integer.parseInt(request.getParameter("sEcho"));
        int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
        int iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
        String orderNo = request.getParameter("orderNo");
        String sTimeStr = request.getParameter("sTime");
        String eTimeStr = request.getParameter("eTime");
        Date sTime = null, eTime = null;
        if (!"".equals(sTimeStr) && !"".equals(eTimeStr)) {
            DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            sTime = DateTime.parse(sTimeStr, format).toDate();
            eTime = DateTime.parse(eTimeStr, format).toDate();
        }
        String userId = request.getParameter("userId");
        String payMethod = request.getParameter("payMethod");
        String status = request.getParameter("status");
        Sort sort = new Sort("id");
        Page<PhPreorderInfo> pages = preorderInfoService.findAll(orderNo, sTime, eTime, userId, payMethod, status, new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, sort));
        int initEcho = sEcho + 1;
        Map<String, Object> map = new HashMap<>();
        map.put("sEcho", initEcho);
        map.put("iTotalRecords", pages.getTotalElements());//数据总条数
        map.put("iTotalDisplayRecords", pages.getTotalElements());//显示的条数
        map.put("aData", pages.getContent());//数据集合
        return map;
    }

    @ResponseBody
    @RequestMapping("/order_closeOrder")
    public boolean closeOrder(Long id) {
        PhOrderInfo orderInfo = orderInfoService.findOne(id);
        if (orderInfo != null) {
            orderInfo.setStatus("4");//取消
            orderInfoService.save(orderInfo);
        }
        return true;
    }

    @ResponseBody
    @RequestMapping("/order_cancelOrder")
    @Transactional
    public boolean cancelOrder(Long id) {
        PhOrderInfo orderInfo = orderInfoService.findOne(id);
        if (orderInfo != null) {
            PhPreorderInfo preorderInfo = preorderInfoService.findByOrderNoAndStatus(orderInfo.getPreorderNo(), "1");
            if (preorderInfo != null) {
                List<PhOrderInfo> list = orderInfoService.findAll(preorderInfo.getOrderNo());
                for (PhOrderInfo item : list) {
                    doCancelOrder(item);
                }
                preorderInfo.setStatus("4");//取消
                preorderInfo.setVersion(preorderInfo.getVersion() + 1);
                preorderInfoService.save(preorderInfo);
            }
        }
        return true;
    }

    private void doCancelOrder(PhOrderInfo orderInfo) {
        orderInfo.setStatus("4");//取消
        orderInfo.setVersion(orderInfo.getVersion() + 1);
        orderInfoService.save(orderInfo);
        //恢复库存
        List<PhOrderGoods> goodsList = orderGoodsService.findAllByPid(orderInfo.getOrderNo());
        for (PhOrderGoods orderGoods : goodsList) {
            PhGoods goods = goodsService.findOne(orderGoods.getGoodsId());
            PhGoodsStock goodsStock = goodsStockService.findOne(orderGoods.getGoodsStockId());
            if (goods != null && goodsStock != null) {
                goods.setSaleCount(goods.getSaleCount() - orderGoods.getGoodsCount());//撤销销量
                goodsStock.setStock(goodsStock.getStock() + orderGoods.getGoodsCount());//撤销库存数量
                goodsStock.setVersion(goodsStock.getVersion() + 1);
                goodsService.save(goods);
                goodsStockService.save(goodsStock);
            }
        }
        //恢复用户钱包余额
        PhUserInfo userInfo = userInfoService.findOne(orderInfo.getUserId());
        if (userInfo != null) {
            userInfo.setBalance(userInfo.getBalance() + orderInfo.getWalletAmount());
            userInfoService.save(userInfo);
        }
    }

    @ResponseBody
    @RequestMapping("/order_deliverOrder")
    public boolean deliverOrder(Long id, String expressCode, String mailNo) {
        PhOrderInfo orderInfo = orderInfoService.findOne(id);
        if (orderInfo != null) {
            PhOrderExpressInfo orderExpressInfo = new PhOrderExpressInfo();
            orderExpressInfo.setExpressCode(expressCode);
            orderExpressInfo.setMailNo(mailNo);
            orderExpressInfo.setType("0");
            orderExpressInfo.setStatus("0");
            orderExpressInfo.setOrderNo(orderInfo.getOrderNo());
            orderExpressInfo.setCreateTime(new Date());
            //收货人
            PhAddress receiver = addressService.findOne(orderInfo.getAddrId());
            orderExpressInfo.setToProvince(receiver.getProvince());
            orderExpressInfo.setToCity(receiver.getCity());
            orderExpressInfo.setToCounty(receiver.getCounty());
            orderExpressInfo.setToContent(receiver.getContent());
            orderExpressInfo.setToRealName(receiver.getRealName());
            orderExpressInfo.setToPhone(receiver.getPhone());
            //寄件人
            PhMerchant merchant = merchantService.findOne(orderInfo.getMerchantId());
            PhAddress sender = addressService.findOne(merchant.getAddrId());
            orderExpressInfo.setFromProvince(sender.getProvince());
            orderExpressInfo.setFromCity(sender.getCity());
            orderExpressInfo.setFromCounty(sender.getCounty());
            orderExpressInfo.setFromContent(sender.getContent());
            orderExpressInfo.setFromRealName(sender.getRealName());
            orderExpressInfo.setFromPhone(sender.getPhone());
            PhOrderExpressInfo orderExpressInfoSaved = orderExpressInfoService.save(orderExpressInfo);
            //更新订单
            orderInfo.setDeliverName(orderExpressInfoSaved.getFromRealName());
            orderInfo.setDeliverTime(new Date());
            orderInfo.setStatus("2");//已发货
            orderInfoService.save(orderInfo);
            jPushService.sendPushUser("已发货", "已发货提醒：亲，您购买的商品已经发货啦！", null, String.valueOf(orderInfo.getUserId()));
            subscribeExpressInfo(orderExpressInfo, orderInfo);
        }
        return true;
    }

    private void subscribeExpressInfo(PhOrderExpressInfo orderExpressInfo, PhOrderInfo orderInfo) {
        String key = "uyUDaSuE5009";
        Map<String, String> innerInnerParam = new HashMap<>();
        innerInnerParam.put("callbackurl", "https://admin.offway.cn/callback/express?uid=" + orderInfo.getUserId());
        String innerInnerParamStr = JSON.toJSONString(innerInnerParam);
        Map<String, String> innerParam = new HashMap<>();
        innerParam.put("company", orderExpressInfo.getExpressCode());
        innerParam.put("number", orderExpressInfo.getMailNo());
        innerParam.put("key", key);
        innerParam.put("parameters", innerInnerParamStr);
        String innerParamStr = JSON.toJSONString(innerParam);
        Map<String, String> param = new HashMap<>();
        param.put("schema", "json");
        param.put("param", innerParamStr);
        String body = HttpClientUtil.post("https://poll.kuaidi100.com/poll", param);
        logger.info(body);
    }

    @ResponseBody
    @RequestMapping("/order_trackOrder")
    public String trackOrder(Long id) {
        String key = "uyUDaSuE5009";
        String customer = "28B3DE9A2485E14FE0DAD40604A8922C";
        PhOrderInfo orderInfo = orderInfoService.findOne(id);
        if (orderInfo != null) {
            PhOrderExpressInfo orderExpressInfo = orderExpressInfoService.findByPid(orderInfo.getOrderNo(), "0");
            if (orderExpressInfo != null) {
                Map<String, String> innerParam = new HashMap<>();
                innerParam.put("com", orderExpressInfo.getExpressCode());
                innerParam.put("num", orderExpressInfo.getMailNo());
                String innerParamStr = JSON.toJSONString(innerParam);
                String signStr = innerParamStr + key + customer;
                String sign = DigestUtils.md5Hex(signStr.getBytes()).toUpperCase();
                Map<String, String> param = new HashMap<>();
                param.put("customer", customer);
                param.put("param", innerParamStr);
                param.put("sign", sign);
                return HttpClientUtil.post("https://poll.kuaidi100.com/poll/query.do", param);
            }
        }
        return "";
    }

    @Scheduled(cron = "0 0/5 * * * *")
    @ResponseBody
    @RequestMapping("/order_check")
    public void processAllOrder() {
        DateTime now = new DateTime();
        DateTime start = now.minusMinutes(25);
        DateTime stop = now.minusMinutes(20);
        List<PhOrderInfo> list = orderInfoService.findToProcess(start.toDate(), stop.toDate());
        if (list.isEmpty()) {
            logger.info("nothing to do");
        } else {
            ExecutorService pool = Executors.newFixedThreadPool(list.size());
            for (PhOrderInfo orderInfo : list) {
                pool.submit(new Runnable() {
                    @Override
                    public void run() {
                        jPushService.sendPushUser("未付款", "20分钟后提示：亲，您有一笔订单未支付哦！", null, String.valueOf(orderInfo.getUserId()));
                    }
                });
            }
            pool.shutdown();
        }
    }
}
