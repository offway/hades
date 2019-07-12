package cn.offway.hades.controller;

import cn.offway.hades.domain.*;
import cn.offway.hades.properties.QiniuProperties;
import cn.offway.hades.service.*;
import cn.offway.hades.utils.MathUtils;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.event.WriteHandler;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping
public class SettlementController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private QiniuProperties qiniuProperties;
    @Autowired
    private PhOrderInfoService orderInfoService;
    @Autowired
    private PhOrderExpressInfoService orderExpressInfoService;
    @Autowired
    private PhOrderGoodsService orderGoodsService;
    @Autowired
    private PhMerchantService merchantService;
    @Autowired
    private PhSettlementInfoService settlementInfoService;
    @Autowired
    private PhSettlementDetailService settlementDetailService;
    @Autowired
    private PhGoodsService goodsService;

    //    @Scheduled(cron = "0 0 * * * *")
    @Deprecated
    @RequestMapping("/stat")
    @ResponseBody
    public void dailyStat() {
        DateTime oneWeekAgo = new DateTime().minusDays(7);
        DateTime oneMonthAgo = new DateTime().minusMonths(1);
        List<PhOrderInfo> list = orderInfoService.findToCheck(oneMonthAgo.toDate(), oneWeekAgo.toDate());
        PhMerchant merchant = null;
        PhSettlementInfo settlementInfo = null;
        double orderAmount = 0;
        long orderCount = 0;
        double unsettledAmount = 0;
        long unsettleCount = 0;
        for (PhOrderInfo orderInfo : list) {
            if (merchant == null || (Long.compare(merchant.getId(), orderInfo.getMerchantId()) != 0)) {
                if (merchant != null) {
                    //reset counter
                    orderAmount = 0;
                    orderCount = 0;
                    unsettledAmount = 0;
                    unsettleCount = 0;
                }
                merchant = merchantService.findOne(orderInfo.getMerchantId());
                if (settlementInfoService.isExist(merchant.getId())) {
                    settlementInfo = settlementInfoService.findByPid(merchant.getId());
                } else {
                    settlementInfo = new PhSettlementInfo();
                    settlementInfo.setMerchantId(merchant.getId());
                    settlementInfo.setMerchantLogo(merchant.getLogo());
                    settlementInfo.setMerchantName(merchant.getName());
                    settlementInfo.setRemark(merchant.getRemark());
                }
                settlementInfo.setMerchantGoodsCount(goodsService.getCountByPid(merchant.getId()));
                settlementInfo.setOrderAmount(0d);
                settlementInfo.setOrderCount(0L);
                settlementInfo.setSettledAmount(0d);
                settlementInfo.setSettledCount(0L);
                settlementInfo.setUnsettledAmount(0d);
                settlementInfo.setUnsettledCount(0L);
                settlementInfo.setStatisticalTime(new Date());
            }
            if (settlementDetailService.isExist(orderInfo.getOrderNo())) {
                //skip
            } else {
                //累加
                orderCount++;
                unsettleCount++;
                orderAmount += orderInfo.getAmount();
                unsettledAmount += orderInfo.getAmount();
                //入库
                PhSettlementDetail settlementDetail = new PhSettlementDetail();
                settlementDetail.setAmount(orderInfo.getAmount());
                settlementDetail.setCreateTime(new Date());
                settlementDetail.setCutRate(merchant.getRatio());
                settlementDetail.setCutAmount(orderInfo.getAmount() * merchant.getRatio() / 100);
                settlementDetail.setMailFee(orderInfo.getMailFee());
                settlementDetail.setMerchantId(orderInfo.getMerchantId());
                settlementDetail.setMerchantLogo(orderInfo.getMerchantLogo());
                settlementDetail.setMerchantName(orderInfo.getMerchantName());
                settlementDetail.setMVoucherAmount(orderInfo.getMVoucherAmount());
                settlementDetail.setPVoucherAmount(orderInfo.getPVoucherAmount());
                settlementDetail.setOrderNo(orderInfo.getOrderNo());
                settlementDetail.setPayChannel(orderInfo.getPayChannel());
                settlementDetail.setPayFee(String.format("%.2f", orderInfo.getAmount() * 0.003));//千分之三的手续费
                settlementDetail.setPrice(orderInfo.getPrice());
                settlementDetail.setRemark(orderInfo.getRemark());
                settlementDetail.setWalletAmount(orderInfo.getWalletAmount());
                /* 状态[0-待结算,1-结算中,2-已结算] */
                settlementDetail.setStatus("0");
                settlementDetailService.save(settlementDetail);
            }
            //save previous one before switch
            settlementInfo.setOrderAmount(orderAmount);
            settlementInfo.setOrderCount(orderCount);
            settlementInfo.setUnsettledAmount(unsettledAmount);
            settlementInfo.setUnsettledCount(unsettleCount);
            settlementInfoService.save(settlementInfo);
        }
    }

    @Transactional
    @RequestMapping("/reSync")
    @ResponseBody
    public void reSync() {
        List<PhOrderInfo> phOrderInfos = orderInfoService.findByPreorderNoAndStatus("", "1", "2", "3");
        List<PhSettlementDetail> phSettlementDetails = new ArrayList<>();
        for (PhOrderInfo orderInfo : phOrderInfos) {
            PhSettlementDetail settlementDetail = new PhSettlementDetail();
            settlementDetail.setAmount(orderInfo.getAmount());
            settlementDetail.setCreateTime(orderInfo.getCreateTime());
            Long merchantId = orderInfo.getMerchantId();
            PhMerchant phMerchant = merchantService.findOne(merchantId);
            settlementDetail.setCutRate(phMerchant.getRatio());
            settlementDetail.setCutAmount(orderInfo.getAmount() * phMerchant.getRatio() / 100);
            settlementDetail.setMailFee(orderInfo.getMailFee());
            settlementDetail.setMerchantId(orderInfo.getMerchantId());
            settlementDetail.setMerchantLogo(orderInfo.getMerchantLogo());
            settlementDetail.setMerchantName(orderInfo.getMerchantName());
            settlementDetail.setMVoucherAmount(orderInfo.getMVoucherAmount());
            settlementDetail.setPVoucherAmount(orderInfo.getPVoucherAmount());
            settlementDetail.setOrderNo(orderInfo.getOrderNo());
            settlementDetail.setPayChannel(orderInfo.getPayChannel());
            settlementDetail.setPayFee(String.format("%.2f", orderInfo.getAmount() * 0.003));//千分之三的手续费
            settlementDetail.setPrice(orderInfo.getPrice());
            settlementDetail.setWalletAmount(orderInfo.getWalletAmount());
            //计算结算金额
            double amount = settlementDetail.getAmount() - settlementDetail.getCutAmount() - Double.valueOf(settlementDetail.getPayFee()) - settlementDetail.getMailFee();
            settlementDetail.setSettledAmount(amount);
            /* 状态[0-待结算,1-结算中,2-已结算] */
            settlementDetail.setStatus("0");
            settlementDetail.setRemark(orderInfo.getStatus());
            phSettlementDetails.add(settlementDetail);
            PhSettlementInfo settlementInfo = settlementInfoService.findByPid(merchantId);
            if (null == settlementInfo) {
                settlementInfo = new PhSettlementInfo();
                settlementInfo.setMerchantId(phMerchant.getId());
                settlementInfo.setMerchantLogo(phMerchant.getLogo());
                settlementInfo.setMerchantName(phMerchant.getName());
                settlementInfo.setMerchantGoodsCount(0L);
                settlementInfo.setStatisticalTime(new Date());
                settlementInfo.setOrderAmount(0d);
                settlementInfo.setOrderCount(0L);
                settlementInfo.setSettledAmount(0d);
                settlementInfo.setSettledCount(0L);
                settlementInfo.setUnsettledAmount(0d);
                settlementInfo.setUnsettledCount(0L);
            }
            settlementInfo.setOrderAmount(MathUtils.add(settlementDetail.getAmount(), settlementInfo.getOrderAmount()));
            settlementInfo.setOrderCount(settlementInfo.getOrderCount() + 1L);
            settlementInfo.setUnsettledAmount(MathUtils.add(settlementInfo.getUnsettledAmount(), settlementDetail.getSettledAmount()));
            settlementInfo.setUnsettledCount(settlementInfo.getUnsettledCount() + 1L);
            settlementInfo.setStatisticalTime(new Date());
            settlementInfoService.save(settlementInfo);
        }
        //入库
        for (PhSettlementDetail detail : phSettlementDetails) {
            settlementDetailService.save(detail);
        }
    }

    @RequestMapping("/settle.html")
    public String index(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        return "settle_index";
    }

    @RequestMapping("/settle_inner.html")
    public String inner(ModelMap map, Long id) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        map.addAttribute("theId", id);
        return "settle_sub_index";
    }

    @ResponseBody
    @RequestMapping("/settle_list")
    public Map<String, Object> getSettleList(HttpServletRequest request) {
        int sEcho = Integer.parseInt(request.getParameter("sEcho"));
        int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
        int iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
        Sort sort = new Sort("id");
        Page<PhSettlementInfo> pages = settlementInfoService.findAll(new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, sort));
        int initEcho = sEcho + 1;
        Map<String, Object> map = new HashMap<>();
        map.put("sEcho", initEcho);
        map.put("iTotalRecords", pages.getTotalElements());//数据总条数
        map.put("iTotalDisplayRecords", pages.getTotalElements());//显示的条数
        map.put("aData", pages.getContent());//数据集合
        return map;
    }

    private Date strToDate(String s) {
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        if (!"".equals(s)) {
            return DateTime.parse(s, format).toDate();
        } else {
            return null;
        }
    }

    @ResponseBody
    @RequestMapping("/settle_inner_list")
    public Map<String, Object> getSettleSubList(HttpServletRequest request, String sTime, String eTime, String orderStatus, String status, String payChannel) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Object> list = new ArrayList<>();
        int sEcho = Integer.parseInt(request.getParameter("sEcho"));
        int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
        int iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
        String id = request.getParameter("theId");
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "id"));
        PageRequest pageRequest = new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, sort);
        Page<PhSettlementDetail> pages = (Page<PhSettlementDetail>) settlementDetailService.findAll(Long.valueOf(id), strToDate(sTime), strToDate(eTime), orderStatus, status, payChannel, pageRequest);
        for (PhSettlementDetail detail : pages.getContent()) {
            Map m = objectMapper.convertValue(detail, Map.class);
            if (detail.getOrderNo() != null) {
                m.put("goods", orderGoodsService.findAllByPid(detail.getOrderNo()));
                m.put("orderId", orderInfoService.findOne(detail.getOrderNo()).getId());
            } else {
                m.put("goods", null);
                m.put("orderId", null);
            }
            list.add(m);
        }
        int initEcho = sEcho + 1;
        Map<String, Object> map = new HashMap<>();
        map.put("sEcho", initEcho);
        map.put("iTotalRecords", pages.getTotalElements());//数据总条数
        map.put("iTotalDisplayRecords", pages.getTotalElements());//显示的条数
        map.put("aData", list);//数据集合
        return map;
    }

    @ResponseBody
    @RequestMapping("/settle_inner_list_all")
    public Map<String, Object> getSettleSubListAll(@RequestParam("theId") String id, String sTime, String eTime, String orderStatus, String status, String payChannel) {
        List<PhSettlementDetail> pages = (List<PhSettlementDetail>) settlementDetailService.findAll(Long.valueOf(id), strToDate(sTime), strToDate(eTime), orderStatus, status, payChannel, null);
        Map<String, Object> map = new HashMap<>();
        double price = 0, mVoucherAmount = 0, settledAmount = 0, settledAmount_a = 0, settledAmount_b = 0, cutAmount = 0, mailFee = 0, mailFee_a = 0, mailFee_b = 0;
        for (PhSettlementDetail item : pages) {
            price += item.getPrice();
            mVoucherAmount += item.getMVoucherAmount();
            settledAmount += item.getSettledAmount();
            cutAmount += item.getCutAmount();
            mailFee += item.getMailFee();
            if ("0".equals(item.getStatus())) {
                settledAmount_a += item.getSettledAmount();
                mailFee_a += item.getMailFee();
            } else {
                settledAmount_b += item.getSettledAmount();
                mailFee_b += item.getMailFee();
            }
        }
        map.put("price", price);
        map.put("mVoucherAmount", mVoucherAmount);
        map.put("settledAmount", settledAmount);
        map.put("settledAmount_a", settledAmount_a);
        map.put("settledAmount_b", settledAmount_b);
        map.put("cutAmount", cutAmount);
        map.put("mailFee", mailFee);
        map.put("mailFee_a", mailFee_a);
        map.put("mailFee_b", mailFee_b);
        return map;
    }

    @Deprecated
    @ResponseBody
    @Transactional
    @RequestMapping("/settle_inner_calc")
    public boolean markAsToSettle(@RequestParam("ids[]") Long[] ids) {
        for (Long id : ids) {
            PhSettlementDetail detail = settlementDetailService.findOne(id);
            if (detail != null) {
                detail.setSettledAmount(detail.getAmount() - detail.getCutAmount() - Double.valueOf(detail.getPayFee()) - detail.getMailFee());
                detail.setStatus("1");//结算中
                settlementDetailService.save(detail);
            }
        }
        return true;
    }

    @Deprecated
    @ResponseBody
    @Transactional
    @RequestMapping("/settle_inner_settle")
    public boolean markAsSettled(@RequestParam("ids[]") Long[] ids, @AuthenticationPrincipal PhAdmin admin) {
        for (Long id : ids) {
            PhSettlementDetail detail = settlementDetailService.findOne(id);
            if (detail != null) {
                detail.setStatus("2");//已结算
                detail.setSettledName(admin.getNickname());
                detail.setSettledTime(new Date());
                settlementDetailService.save(detail);
                PhSettlementInfo info = settlementInfoService.findByPid(detail.getMerchantId());
                info.setUnsettledCount(info.getUnsettledCount() - 1);
                info.setUnsettledAmount(info.getUnsettledAmount() - detail.getSettledAmount());
                info.setSettledCount(info.getSettledCount() + 1);
                info.setSettledAmount(info.getSettledAmount() + detail.getSettledAmount());
                info.setStatisticalTime(new Date());
                settlementInfoService.save(info);
            }
        }
        return true;
    }

    @ResponseBody
    @Transactional
    @RequestMapping("/settle_inner_batchSettle")
    public double batchSettle(@RequestParam("ids[]") Long[] ids, @AuthenticationPrincipal PhAdmin admin) {
        double totalAmount = 0;
        PhSettlementInfo info = null;//settlementInfoService.findByPid(detail.getMerchantId());
        for (Long id : ids) {
            PhSettlementDetail detail = settlementDetailService.findOne(id);
            if (detail != null && "0".equals(detail.getStatus())) {
                double amount = detail.getSettledAmount();//detail.getAmount() - detail.getCutAmount() - Double.valueOf(detail.getPayFee()) - detail.getMailFee();
                totalAmount += amount;
//                detail.setSettledAmount(amount);
                detail.setStatus("2");//已结算
                detail.setSettledName(admin.getNickname());
                detail.setSettledTime(new Date());
                settlementDetailService.save(detail);
                if (info == null) {
                    info = settlementInfoService.findByPid(detail.getMerchantId());
                }
                info.setUnsettledCount(info.getUnsettledCount() - 1);
                info.setUnsettledAmount(info.getUnsettledAmount() - detail.getSettledAmount());
                info.setSettledCount(info.getSettledCount() + 1);
                info.setSettledAmount(info.getSettledAmount() + detail.getSettledAmount());
            }
        }
        if (info != null) {
            info.setStatisticalTime(new Date());
            settlementInfoService.save(info);
        }
        return totalAmount;
    }

    @RequestMapping("/settle_inner_export.html")
    public void exportSettle(@RequestParam("ids[]") Long[] ids, @AuthenticationPrincipal PhAdmin admin, HttpServletResponse response) {
        List<PhSettlementDetail> list = settlementDetailService.findList(ids);
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("multipart/form-data");
            response.setCharacterEncoding("utf-8");
            String fileName = new String(("SettleList_" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
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
                        case 10:
                            /* 状态[0-已下单,1-已付款,2-已发货,3-已收货,4-取消] **/
                            String[] arr = new String[]{"已下单", "已付款", "已发货", "已收货", "取消"};
                            if (cell.getStringCellValue() == null || "".equals(cell.getStringCellValue())) {
                                cell.setCellValue("未知");
                            } else {
                                cell.setCellValue(arr[Integer.valueOf(cell.getStringCellValue())]);
                            }
                            break;
                        case 11:
                            /* 状态[0-待结算,1-结算中,2-已结算] **/
                            String[] arr2 = new String[]{"待结算", "结算中", "已结算"};
                            cell.setCellValue(arr2[Integer.valueOf(cell.getStringCellValue())]);
                            break;
                        case 7:
                            if ("alipay".equals(cell.getStringCellValue())) {
                                cell.setCellValue("支付宝");
                            } else if ("wxpay".equals(cell.getStringCellValue())) {
                                cell.setCellValue("微信支付");
                            } else {
                                cell.setCellValue("未知");
                            }
                            break;
                        case 2:
                            String orderNo = cell.getStringCellValue();
                            StringBuilder sb = new StringBuilder();
                            for (PhOrderGoods goods : orderGoodsService.findAllByPid(orderNo)) {
                                sb.append("名称:");
                                sb.append(goods.getGoodsName());
                                sb.append("数量:");
                                sb.append(goods.getGoodsCount());
                                sb.append("\r\n");
                            }
                            cell.setCellValue(sb.toString());
                            break;
                        default:
                            break;
                    }
                }
            });
            Sheet sheet = new Sheet(1, 0, PhSettlementDetail.class);
            sheet.setSheetName("Settle");
            sheet.setAutoWidth(true);
            writer.write(list, sheet);
            writer.finish();
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            logger.error("IO ERROR", e);
        }
    }
}
