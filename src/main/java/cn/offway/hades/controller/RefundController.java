package cn.offway.hades.controller;

import cn.offway.hades.domain.*;
import cn.offway.hades.properties.QiniuProperties;
import cn.offway.hades.repository.PhRefundOrderGoodsRepository;
import cn.offway.hades.service.*;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.event.WriteHandler;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping
public class RefundController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private QiniuProperties qiniuProperties;
    @Autowired
    private PhRoleadminService roleadminService;
    @Autowired
    private PhRefundService refundService;
    @Autowired
    private PhRefundGoodsService refundGoodsService;
    @Autowired
    private PhOrderInfoService orderInfoService;
    @Autowired
    private PhOrderGoodsService orderGoodsService;
    @Autowired
    private PhGoodsService goodsService;
    @Autowired
    private PhGoodsPropertyService goodsPropertyService;
    @Autowired
    private PhGoodsStockService goodsStockService;
    @Autowired
    private PhAddressService addressService;
    @Autowired
    private PhMerchantService merchantService;
    @Autowired
    private PhUserInfoService userInfoService;
    @Autowired
    private PhCapitalFlowService capitalFlowService;
    @Autowired
    private PhVoucherInfoService voucherInfoService;
    @Autowired
    private PhRefundOrderGoodsService refundOrderGoodsService;
    @Autowired
    private PhRefundOrderGoodsRepository refundOrderGoodsRepository;
    @Autowired
    private PhSettlementDetailService settlementDetailService;
    @Autowired
    private AlipayService alipayService;

    @RequestMapping("/refund.html")
    public String index(ModelMap map, @AuthenticationPrincipal PhAdmin admin) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        List<Long> roles = roleadminService.findRoleIdByAdminId(admin.getId());
        if (roles.contains(BigInteger.valueOf(8L))) {
            map.addAttribute("isAdmin", "0");
        } else {
            map.addAttribute("isAdmin", "1");
        }
        return "refund_index";
    }

    private Date strToDate(String s) {
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        if (!"".equals(s)) {
            return DateTime.parse(s, format).toDate();
        } else {
            return null;
        }
    }

    private Map<String, Object> buildResponse(Object sEcho, Object iTotalRecords, Object iTotalDisplayRecords, Object aData) {
        Map<String, Object> map = new HashMap<>();
        map.put("sEcho", sEcho);
        map.put("iTotalRecords", iTotalRecords);//数据总条数
        map.put("iTotalDisplayRecords", iTotalDisplayRecords);//显示的条数
        map.put("aData", aData);//数据集合
        return map;
    }

    @ResponseBody
    @RequestMapping("/refund_list")
    public Map<String, Object> getList(HttpServletRequest request, String orderNo, String sTime, String eTime, String userId, String sTimeCheck, String eTimeCheck, @RequestParam(name = "type[]", required = false, defaultValue = "") String[] type, String status, String merchantId, @AuthenticationPrincipal PhAdmin admin) {
        Object mid = null;
        int sEcho = Integer.parseInt(request.getParameter("sEcho"));
        int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
        int iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        PageRequest pr = new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, sort);
        List<Long> roles = roleadminService.findRoleIdByAdminId(admin.getId());
        if (roles.contains(BigInteger.valueOf(8L))) {
            PhMerchant merchant = merchantService.findByAdminId(admin.getId());
            if (merchant != null) {
                mid = merchant.getId();
            }
        } else {
            mid = StringUtils.isNotBlank(merchantId) ? merchantId : null;
        }
        Page<PhRefund> pages = refundService.list(mid, orderNo, strToDate(sTime), strToDate(eTime), userId, strToDate(sTimeCheck), strToDate(eTimeCheck), type, status, pr);
        ObjectMapper objectMapper = new ObjectMapper();
        List<Object> ret = new ArrayList<>();
        for (PhRefund type0 : pages.getContent()) {
            Map container = objectMapper.convertValue(type0, Map.class);
            PhOrderInfo categoryList = orderInfoService.findOne(type0.getOrderNo());
            container.put("preorderNo", categoryList.getPreorderNo());
            container.put("payChannel", categoryList.getPayChannel());
            ret.add(container);
        }
        int initEcho = sEcho + 1;
        return buildResponse(initEcho, pages.getTotalElements(), pages.getTotalElements(), ret);
    }

    @RequestMapping("/refund_list_export.html")
    public void exportList(HttpServletResponse response, String orderNo, String sTime, String eTime, String userId, String sTimeCheck, String eTimeCheck, @RequestParam(name = "type[]", required = false, defaultValue = "") String[] type, String status) {
        List<PhRefund> all = refundService.all(orderNo, strToDate(sTime), strToDate(eTime), userId, strToDate(sTimeCheck), strToDate(eTimeCheck), type, status);
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("multipart/form-data");
            response.setCharacterEncoding("utf-8");
            String fileName = new String(("RefundList_" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                    .getBytes(), StandardCharsets.UTF_8);
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            ExcelWriter writer = new ExcelWriter(null, outputStream, ExcelTypeEnum.XLSX, true, new WriteHandler() {
                @Override
                public void sheet(int i, org.apache.poi.ss.usermodel.Sheet sheet) {
                    //nothing
                }

                @Override
                public void row(int i, Row row) {
                    //nothing
                }

                @Override
                public void cell(int i, Cell cell) {
                    if (cell.getRowIndex() == 0) {
                        return;
                    }
                    switch (i) {
                        case 5:
                            /* 类型[0-仅退款,1-退货退款,2-换货] **/
                            String[] arr = new String[]{"仅退款", "退货退款", "换货"};
                            cell.setCellValue(arr[Integer.valueOf(cell.getStringCellValue())]);
                            break;
                        case 6:
                            /* 状态[0-审核中,1-待退货,2-退货中,3-退款中,4-退款成功,5-退款取消,6-审核失败] **/
                            String[] arr2 = new String[]{"审核中", "待退货", "退货中", "退款中", "退款成功", "退款取消", "审核失败"};
                            cell.setCellValue(arr2[Integer.valueOf(cell.getStringCellValue())]);
                            break;
                        case 12:
                            if ("0".equals(cell.getStringCellValue())) {
                                cell.setCellValue("否");
                            } else if ("1".equals(cell.getStringCellValue())) {
                                cell.setCellValue("是");
                            } else {
                                cell.setCellValue("未知");
                            }
                            break;
                        default:
                            break;
                    }
                }
            });
            Sheet sheet = new Sheet(1, 0, PhRefund.class);
            sheet.setSheetName("Refund");
            sheet.setAutoWidth(true);
            writer.write(all, sheet);
            writer.finish();
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            logger.error("IO ERROR", e);
        }
    }

    @RequestMapping("/refund_detail.html")
    public String orderDetail(ModelMap map, Long id, String readOnly, String needCheck) {
        Map<String, Object> dataList = new HashMap<>();
        PhRefund refund = refundService.findOne(id);
        String action = "";
        if (refund != null) {
            if ("0".equals(refund.getStatus())) {
                action = "goods";
            } else if ("3".equals(refund.getStatus())) {
                action = "money";
            }
            List<PhRefundGoods> refundGoodsList = refundGoodsService.listByPid(refund.getId());
            PhOrderInfo orderInfo = orderInfoService.findOne(refund.getOrderNo());
            List<PhOrderGoods> orderGoodsList = orderGoodsService.findAllByPid(orderInfo.getOrderNo());
            boolean needClone = false;
            if (refundGoodsList.isEmpty() && "1".equals(refund.getIsComplete())) {
                needClone = true;
            }
            WeakHashMap<Long, PhOrderGoods> holder = new WeakHashMap<>();
            for (PhOrderGoods orderGoods : orderGoodsList) {
                holder.put(orderGoods.getGoodsId(), orderGoods);
                if (needClone) {
                    PhRefundGoods obj = new PhRefundGoods();
                    obj.setOrderGoodsId(orderGoods.getGoodsId());
                    obj.setGoodsCount(orderGoods.getGoodsCount());
                    refundGoodsList.add(obj);
                }
            }
            //商品信息
            List<Map> goodsInfoList = new ArrayList<>();
            for (PhRefundGoods refundGoods : refundGoodsList) {
                Map<String, Object> goodsInfo = new HashMap<>();
                if (holder.containsKey(refundGoods.getOrderGoodsId())) {
                    PhOrderGoods orderGoods = holder.get(refundGoods.getOrderGoodsId());
                    PhGoods goods = goodsService.findOne(orderGoods.getGoodsId());
                    List<PhGoodsProperty> propertyList = goodsPropertyService.findByStockId(orderGoods.getGoodsStockId());
                    PhGoodsStock goodsStock = goodsStockService.findOne(orderGoods.getGoodsStockId());
                    goodsInfo.put("SKU", orderGoods.getGoodsStockId());
                    goodsInfo.put("goodsId", orderGoods.getGoodsId());
                    goodsInfo.put("goodsName", orderGoods.getGoodsName());
                    goodsInfo.put("code", goods.getCode());
                    goodsInfo.put("price", goods.getPrice());
                    goodsInfo.put("brandName", goods.getBrandName());
                    goodsInfo.put("type", goods.getType() + goods.getCategory());
                    goodsInfo.put("merchantName", goods.getMerchantName());
                    goodsInfo.put("stockImg", goodsStock == null ? goods.getImage() : goodsStock.getImage());
                    goodsInfo.put("toStockImage", refundGoods.getToStockImage());
                    goodsInfo.put("toStockDesc", refundGoods.getToStockDesc());
                    goodsInfo.put("toStockId", refundGoods.getToStockId());
                    goodsInfo.put("reason", refundGoods.getReason());
                    StringBuilder sb = new StringBuilder();
                    for (PhGoodsProperty p : propertyList) {
                        sb.append(p.getName());
                        sb.append(":");
                        sb.append(p.getValue());
                        sb.append(";");
                    }
                    goodsInfo.put("goodsProperty", sb.toString());
                    goodsInfo.put("goodsCount", refundGoods.getGoodsCount());
                } else if (refundGoods != null) {
                    PhGoods goods;
                    if (refundGoods.getGoodsId() == null) {
                        goods = goodsService.findOne(refundGoods.getOrderGoodsId());
                    } else {
                        goods = goodsService.findOne(refundGoods.getGoodsId());
                    }
                    List<PhGoodsStock> goodsStock = goodsStockService.findByPid(refundGoods.getGoodsId());
                    String sku = "";
                    for (PhGoodsStock stock : goodsStock) {
                        if (stock.getGoodsId().longValue() == refundGoods.getGoodsId().longValue()) {
                            sku = stock.getSku();
                        }
                    }
                    goodsInfo.put("SKU", sku);
                    goodsInfo.put("goodsId", refundGoods.getGoodsId());
                    goodsInfo.put("goodsName", refundGoods.getGoodsName());
                    goodsInfo.put("code", ((goods.getCode() != null) ? goods.getCode() : ""));
                    goodsInfo.put("price", refundGoods.getPrice());
                    goodsInfo.put("brandName", ((goods.getBrandName() != null) ? goods.getBrandName() : ""));
                    goodsInfo.put("type", ((goods.getType() != null) ? goods.getType() : "" + ((goods.getCategory() != null) ? goods.getCategory() : "")));
                    goodsInfo.put("merchantName", refund.getRemark());
                    goodsInfo.put("stockImg", refundGoods.getFromStockImage());
                    goodsInfo.put("toStockImage", refundGoods.getToStockImage());
                    goodsInfo.put("toStockDesc", refundGoods.getToStockDesc());
                    goodsInfo.put("toStockId", refundGoods.getToStockId());
                    goodsInfo.put("reason", refundGoods.getReason());
                    goodsInfo.put("goodsProperty", refundGoods.getRemark());
                    goodsInfo.put("goodsCount", refundGoods.getGoodsCount());
                }
                goodsInfoList.add(goodsInfo);
            }
            dataList.put("goodsInfo", goodsInfoList);
            //费用信息
            List<Map> feeInfoList = new ArrayList<>();
            Map<String, Object> feeInfo = new HashMap<>();
            feeInfo.put("price", orderInfo.getPrice());
            feeInfo.put("voucherAmount", orderInfo.getMVoucherAmount() + orderInfo.getPVoucherAmount());
            feeInfo.put("walletAmount", orderInfo.getWalletAmount());
            feeInfo.put("mailFee", orderInfo.getMailFee());
            feeInfo.put("tax", 0);
            feeInfo.put("amount", orderInfo.getAmount());
            feeInfo.put("refundAmount", refund.getAmount());
            feeInfoList.add(feeInfo);
            dataList.put("feeInfo", feeInfoList);
            //订单收货人信息
            List<Map> receiverInfoList = new ArrayList<>();
            Map<String, Object> receiverInfo = new HashMap<>();
            PhAddress address = addressService.findOne(orderInfo.getAddrId());
            if (address != null) {
                receiverInfo.put("userId", address.getUserId());
                receiverInfo.put("realName", address.getRealName());
                receiverInfo.put("phone", address.getPhone());
                receiverInfo.put("zipCode", "");
                receiverInfo.put("location", address.getProvince() + address.getCity() + address.getCounty() + address.getContent());
            }
            receiverInfoList.add(receiverInfo);
            dataList.put("receiverInfo", receiverInfoList);
            //售后单信息
            List<Map> refundInfoList = new ArrayList<>();
            Map<String, Object> refundInfo = new HashMap<>();
            refundInfo.put("id", refund.getOrderNo());
            refundInfo.put("createTime", refund.getCreateTime());
            refundInfo.put("userId", refund.getUserId());
            refundInfo.put("type", refund.getType());
            refundInfo.put("amount", refund.getAmount());
            refundInfo.put("payChannel", orderInfo.getPayChannel());
            refundInfo.put("status", orderInfo.getStatus());
            refundInfo.put("orderNo", orderInfo.getOrderNo());
            refundInfo.put("reason", refund.getReason());
            refundInfoList.add(refundInfo);
            dataList.put("refundInfo", refundInfoList);
            //售后单处理信息
            if (refund.getMailNo() != null && "".equals(refund.getMailNo())) {
                List<Map> refundResultInfoList = new ArrayList<>();
                Map<String, Object> refundResultInfo = new HashMap<>();
                PhMerchant merchant = merchantService.findOne(orderInfo.getMerchantId());
                PhAddress backAddress = addressService.findOne(merchant.getReturnAddrId());
                refundResultInfo.put("reason", refund.getReason());
                if (backAddress != null) {
                    refundResultInfo.put("realName", backAddress.getRealName());
                    refundResultInfo.put("location", backAddress.getProvince() + address.getCity() + address.getCounty() + address.getContent());
                    refundResultInfo.put("phone", backAddress.getPhone());
                }
                refundResultInfo.put("expressCode", "");
                refundResultInfo.put("mailNo", refund.getMailNo());
                refundResultInfo.put("status", refund.getStatus());
                refundResultInfoList.add(refundResultInfo);
                dataList.put("refundResultInfo", refundResultInfoList);
            }
            //问题描述信息
            List<Map> contentInfoList = new ArrayList<>();
            Map<String, Object> contentInfo = new HashMap<>();
            contentInfo.put("content", refund.getContent() == null ? "无" : refund.getContent());
            contentInfoList.add(contentInfo);
            dataList.put("contentInfo", contentInfoList);
            //凭证图片信息
            List<Map> imgInfoList = new ArrayList<>();
            Map<String, Object> imgInfo = new HashMap<>();
            imgInfo.put("image", refund.getImage());
            imgInfoList.add(imgInfo);
            dataList.put("imgInfo", imgInfoList);
            //操作信息
            List<Map> operateInfoList = new ArrayList<>();
            Map<String, Object> operateInfo = new HashMap<>();
            operateInfo.put("checkName", refund.getCheckName() == null ? "无" : refund.getCheckName());
            operateInfo.put("checkTime", refund.getCheckTime() == null ? "" : refund.getCheckTime());
            operateInfoList.add(operateInfo);
            dataList.put("operateInfo", operateInfoList);
            //处理备注信息
            List<Map> remarkInfoList = new ArrayList<>();
            Map<String, Object> remarkInfo = new HashMap<>();
            remarkInfo.put("remark", refund.getCheckReason() == null ? "无" : refund.getCheckReason());
            remarkInfoList.add(remarkInfo);
            dataList.put("remarkInfo", remarkInfoList);
            //支付宝信息
            PhUserInfo userInfo = userInfoService.findOne(refund.getUserId());
            if (userInfo != null) {
                map.addAttribute("alipayNickName", userInfo.getAlipayNickName());
                map.addAttribute("alipayUserId", userInfo.getAlipayUserId());
            }
            map.addAttribute("amount", refund.getAmount());
        }
        map.addAttribute("jsonStr", JSON.toJSONString(dataList));
        map.addAttribute("readOnly", readOnly);
        map.addAttribute("needCheck", needCheck);
        map.addAttribute("action", action);
        map.addAttribute("theId", id);
        return "refund_detail";
    }

    @ResponseBody
    @RequestMapping("/refund_confirmDelivery")
    public boolean confirmDelivery(Long id, @AuthenticationPrincipal PhAdmin admin) {
        PhRefund refund = refundService.findOne(id);
        /* 状态[0-审核中,1-待退货,2-退货中,3-退款中,4-退款成功,5-退款取消,6-审核失败] **/
        refund.setStatus("3");
        //保存状态
        refundService.save(refund);
        return true;
    }

    @ResponseBody
    @RequestMapping("/refund_find")
    public boolean find(String orderNo, @AuthenticationPrincipal PhAdmin admin) {
        PhRefund refund = refundService.findOne(orderNo, "0", "1", "2", "3", "4");
        return refund != null;
    }

    @ResponseBody
    @RequestMapping("/refund_query")
    public PhRefund find(String orderNo) {
        return refundService.findOne(orderNo, "0", "1", "2", "3", "4", "5");
    }

    @ResponseBody
    @RequestMapping("/refund_deny")
    public boolean deny(Long id, String reason, @AuthenticationPrincipal PhAdmin admin) {
        PhRefund refund = refundService.findOne(id);
        /* 状态[0-审核中,1-待退货,2-退货中,3-退款中,4-退款成功,5-退款取消,6-审核失败] **/
        switch (refund.getStatus()) {
            case "0":
                refund.setStatus("6");
                break;
            case "3":
                refund.setStatus("5");
                break;
            default:
                break;
        }
        //保存状态
        refund.setCheckName(admin.getNickname());
        refund.setCheckTime(new Date());
        refund.setCheckReason(reason);
        refundService.save(refund);
        return true;
    }

    @ResponseBody
    @Transactional
    @RequestMapping("/refund_allow")
    public boolean allow(Long id, @AuthenticationPrincipal PhAdmin admin) {
        PhRefund refund = refundService.findOne(id);
        /* 状态[0-审核中,1-待退货,2-退货中,3-退款中,4-退款成功,5-退款取消,6-审核失败] **/
        switch (refund.getStatus()) {
            case "0":
                /* 类型[0-仅退款,1-退货退款,2-换货] **/
                switch (refund.getType()) {
                    case "0":
                        SettlementController.sendSMS("【很潮】您好，有一笔退款审核已通过，请通过后台确认打款~", "+8613918021859", "+8613663478885", "+8615300959951");
                        refund.setStatus("3");
                        break;
                    case "1":
                    case "2":
                        refund.setStatus("1");
                        break;
                }
                break;
            case "3":
                List<Long> roles = roleadminService.findRoleIdByAdminId(admin.getId());
                if (roles.contains(BigInteger.valueOf(8L))) {
                    return false;
                }
                refund.setStatus("4");
                /* 是否整单退款[0-否,1-是] **/
                if ("1".equals(refund.getIsComplete())) {
                    PhOrderInfo orderInfo = orderInfoService.findOne(refund.getOrderNo());
                    if (orderInfo != null) {
                        /* 状态[0-已下单,1-已付款,2-已发货,3-已收货,4-取消] **/
                        orderInfo.setStatus("4");
                        orderInfoService.save(orderInfo);
                        //恢复钱包余额
                        PhUserInfo userInfo = userInfoService.findOne(orderInfo.getUserId());
                        userInfo.setBalance((userInfo.getBalance() + orderInfo.getWalletAmount()));
                        userInfoService.save(userInfo);
                        if (Double.compare(orderInfo.getWalletAmount(), 0.0) > 0) {
                            PhCapitalFlow capitalFlow = new PhCapitalFlow();
                            capitalFlow.setAmount(orderInfo.getWalletAmount());
                            capitalFlow.setBusinessType("2");
                            capitalFlow.setUserId(orderInfo.getUserId());
                            capitalFlow.setType("1");
                            capitalFlow.setCreateTime(new Date());
                            capitalFlow.setRemark("退款返回");
                            capitalFlow.setOrderNo(orderInfo.getOrderNo());
                            capitalFlowService.save(capitalFlow);
                        }
                        //恢复优惠卷
                        if (orderInfo.getMVoucherId() != null) {
                            PhVoucherInfo voucherInfo = voucherInfoService.findOne(orderInfo.getMVoucherId());
                            voucherInfo.setStatus("0");
                            voucherInfoService.save(voucherInfo);
                        }
                        if (orderInfo.getPVoucherId() != null) {
                            PhVoucherInfo voucherInfo = voucherInfoService.findOne(orderInfo.getPVoucherId());
                            voucherInfo.setStatus("0");
                            voucherInfoService.save(voucherInfo);
                        }
                    }
                }
//                //调用支付宝SDK退款至付款账户
//                doRefund(refund);
//                //更新财务系统订单状态
//                PhSettlementDetail detail = settlementDetailService.findOne(refund.getOrderNo());
//                if (detail != null) {
//                    detail.setRemark("5");
//                    settlementDetailService.save(detail);
//                }
                break;
            default:
                break;
        }
        //保存状态
        refund.setCheckName(admin.getNickname());
        refund.setCheckTime(new Date());
        refund.setCheckReason("");
        refundService.save(refund);
        refundOrderGoodsService.updateByRefund(refund);
        return true;
    }

    private void doRefund(PhRefund refund) {
        String requestNo = MessageFormat.format("{0}_{1}", refund.getOrderNo(), UUID.randomUUID().toString());
        alipayService.refund(refund.getAmount(), refund.getOrderNo(), requestNo, refund.getReason());
    }

    @ResponseBody
    @RequestMapping("/refund_delivery")
    public boolean delivery(PhRefund refund, @AuthenticationPrincipal PhAdmin admin) {
        PhRefund refund0 = refundService.findOne(refund.getId());
        refund0.setShipMailNo(refund.getMailNo());
        refund0.setShipExpressCode(refund.getExpressCode());
        refund0.setStatus("7");
        refundService.save(refund0);
        return true;
    }
}
