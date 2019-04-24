package cn.offway.hades.controller;

import cn.offway.hades.domain.PhMerchant;
import cn.offway.hades.domain.PhOrderInfo;
import cn.offway.hades.domain.PhSettlementDetail;
import cn.offway.hades.domain.PhSettlementInfo;
import cn.offway.hades.properties.QiniuProperties;
import cn.offway.hades.service.*;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private PhMerchantService merchantService;
    @Autowired
    private PhSettlementInfoService settlementInfoService;
    @Autowired
    private PhSettlementDetailService settlementDetailService;
    @Autowired
    private PhGoodsService goodsService;

    @Scheduled(cron = "0 0 * * * *")
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
                settlementDetail.setCutAmount(orderInfo.getAmount() * merchant.getRatio());
                settlementDetail.setMailFee(orderInfo.getMailFee());
                settlementDetail.setMerchantId(orderInfo.getMerchantId());
                settlementDetail.setMerchantLogo(orderInfo.getMerchantLogo());
                settlementDetail.setMerchantName(orderInfo.getMerchantName());
                settlementDetail.setMVoucherAmount(orderInfo.getMVoucherAmount());
                settlementDetail.setPVoucherAmount(orderInfo.getPVoucherAmount());
                settlementDetail.setOrderNo(orderInfo.getOrderNo());
                settlementDetail.setPayChannel(orderInfo.getPayChannel());
                settlementDetail.setPayFee("");
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

    @ResponseBody
    @RequestMapping("/settle_inner_list")
    public Map<String, Object> getSettleSubList(HttpServletRequest request) {
        int sEcho = Integer.parseInt(request.getParameter("sEcho"));
        int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
        int iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
        String id = request.getParameter("theId");
        Sort sort = new Sort("id");
        Page<PhSettlementDetail> pages = settlementDetailService.findAll(Long.valueOf(id), new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, sort));
        int initEcho = sEcho + 1;
        Map<String, Object> map = new HashMap<>();
        map.put("sEcho", initEcho);
        map.put("iTotalRecords", pages.getTotalElements());//数据总条数
        map.put("iTotalDisplayRecords", pages.getTotalElements());//显示的条数
        map.put("aData", pages.getContent());//数据集合
        return map;
    }
}
