package cn.offway.hades;

import cn.offway.hades.domain.PhMerchant;
import cn.offway.hades.domain.PhOrderInfo;
import cn.offway.hades.domain.PhSettlementDetail;
import cn.offway.hades.domain.PhSettlementInfo;
import cn.offway.hades.service.PhMerchantService;
import cn.offway.hades.service.PhOrderInfoService;
import cn.offway.hades.service.PhSettlementDetailService;
import cn.offway.hades.service.PhSettlementInfoService;
import cn.offway.hades.utils.HttpClientUtil;
import cn.offway.hades.utils.MathUtils;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("prd")
public class HadesApplicationTests {

    @Autowired
    private PhSettlementInfoService phSettlementInfoService;

    @Autowired
    private PhSettlementDetailService phSettlementDetailService;

    @Autowired
    private PhOrderInfoService phOrderInfoService;

    @Autowired
    private PhMerchantService phMerchantService;

    @Test
    public void contextLoads() {
        System.out.println("hello!!!!22222");
    }

    @Test
    public void insertOrder() {
        List<PhOrderInfo> phOrderInfos = phOrderInfoService.findByPreorderNoAndStatus("1020190723000008", "2");
        List<PhSettlementDetail> phSettlementDetails = new ArrayList<>();
        List<PhSettlementInfo> phSettlementInfos = new ArrayList<>();
        for (PhOrderInfo orderInfo : phOrderInfos) {
            PhSettlementDetail settlementDetail = new PhSettlementDetail();
            settlementDetail.setAmount(orderInfo.getAmount());
            settlementDetail.setCreateTime(new Date());
            Long merchantId = orderInfo.getMerchantId();
            PhMerchant phMerchant = phMerchantService.findOne(merchantId);
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
//            double amount = settlementDetail.getAmount() - settlementDetail.getCutAmount() - Double.valueOf(settlementDetail.getPayFee()) - settlementDetail.getMailFee();
            double amount = (settlementDetail.getPrice() - settlementDetail.getMVoucherAmount()) * (1D - phMerchant.getRatio() / 100);
            settlementDetail.setSettledAmount(amount);
            /* 状态[0-待结算,1-结算中,2-已结算] */
            settlementDetail.setStatus("0");
            settlementDetail.setRemark(orderInfo.getStatus());
            phSettlementDetails.add(settlementDetail);
            PhSettlementInfo settlementInfo = phSettlementInfoService.findByPid(merchantId);
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
            phSettlementInfos.add(settlementInfo);
        }
        //入库
        for (PhSettlementDetail detail : phSettlementDetails) {
            phSettlementDetailService.save(detail);
        }
        for (PhSettlementInfo info : phSettlementInfos) {
            phSettlementInfoService.save(info);
        }
    }

    @Test
    public void testKuaiDi100() {
        String key = "uyUDaSuE5009";
        Map<String, String> innerInnerParam = new HashMap<>();
        innerInnerParam.put("callbackurl", "https://admin.offway.cn/callback/express?uid=1111&oid=2222");
        String innerInnerParamStr = JSON.toJSONString(innerInnerParam);
        Map<String, Object> innerParam = new HashMap<>();
        innerParam.put("company", "shunfeng");
        innerParam.put("number", "356570849910");
        innerParam.put("key", key);
        innerParam.put("parameters", innerInnerParam);
        String innerParamStr = JSON.toJSONString(innerParam);
        Map<String, String> param = new HashMap<>();
        param.put("schema", "json");
        param.put("param", innerParamStr);
        String body = HttpClientUtil.post("https://poll.kuaidi100.com/poll", param);
        System.out.println(body);
    }
}
