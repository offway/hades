package cn.offway.hades;

import cn.offway.hades.domain.*;
import cn.offway.hades.repository.PhMerchantBrandRepository;
import cn.offway.hades.service.*;
import cn.offway.hades.utils.HttpClientUtil;
import cn.offway.hades.utils.MathUtils;
import com.alibaba.fastjson.JSON;
import com.yunpian.sdk.YunpianClient;
import com.yunpian.sdk.model.Result;
import com.yunpian.sdk.model.SmsSingleSend;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
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

    @Autowired
    private PhMerchantBrandRepository merchantBrandRepository;

    @Autowired
    private PhMerchantBrandService merchantBrandService;

    @Autowired
    private PhBrandService brandService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void contextLoads() {
        System.out.println("hello!!!!33333");
    }

    @Test
    @Transactional
    @Rollback(false)
    public void insertOrder() {
        String[] orderList = new String[]{"34775565626634202", "34775565626634206", "34775565632475305", "34775565636336405", "34775565646040612", "34775565655543004", "34775565655543013", "34775565655543015", "34775565661404104", "34775565665245204", "34775565674747407", "34775565674747421"};
        for (String orderId : orderList) {
            List<PhOrderInfo> phOrderInfos = phOrderInfoService.findByPreorderNoAndStatus(orderId, "1", "2", "3");
            if (phOrderInfos.size() == 0) {
                System.out.println("empty set Id:" + orderId);
            }
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
                settlementDetail.setPromotionAmount(orderInfo.getPromotionAmount());
                settlementDetail.setPlatformPromotionAmount(orderInfo.getPlatformPromotionAmount());
                settlementDetail.setOrderNo(orderInfo.getOrderNo());
                settlementDetail.setPayChannel(orderInfo.getPayChannel());
                settlementDetail.setPayFee(String.format("%.2f", orderInfo.getAmount() * 0.003));//千分之三的手续费
                settlementDetail.setPrice(orderInfo.getPrice());
                settlementDetail.setWalletAmount(orderInfo.getWalletAmount());
                //计算结算金额
                double amount = (settlementDetail.getPrice() - settlementDetail.getMVoucherAmount() - settlementDetail.getPromotionAmount()) * (1D - phMerchant.getRatio() / 100);
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
                PhSettlementDetail obj = phSettlementDetailService.save(detail);
                System.out.println("saved Id:" + obj.getId());
            }
            for (PhSettlementInfo info : phSettlementInfos) {
                phSettlementInfoService.save(info);
            }
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

    @Test
    public void sendSMS() {
        //初始化clnt,使用单例方式
        YunpianClient client = new YunpianClient("d7c58b5d229428d28434533b17ff084a").init();
        //发送短信API
        Map<String, String> param = client.newParam(2);
        param.put(YunpianClient.MOBILE, "+8613761839483");
        param.put(YunpianClient.TEXT, "【很潮】您好，有一笔退款审核已通过，请通过后台确认打款~");
        Result<SmsSingleSend> r = client.sms().single_send(param);
        System.out.println(r.getMsg());
        //change the phone number
        param.put(YunpianClient.MOBILE, "+8613663478885");
        Result<SmsSingleSend> r2 = client.sms().single_send(param);
        System.out.println(r2.getMsg());
        //获取返回结果，返回码:r.getCode(),返回码描述:r.getMsg(),API结果:r.getData(),其他说明:r.getDetail(),调用异常:r.getThrowable()
        //账户:clnt.user().* 签名:clnt.sign().* 模版:clnt.tpl().* 短信:clnt.sms().* 语音:clnt.voice().* 流量:clnt.flow().* 隐私通话:clnt.call().*
        //释放clnt
        client.close();
    }

    @Test
    public void testSQL() {
        List<Object[]> o = merchantBrandRepository.checkEmptyBrand();
        for (Object[] a : o) {
            for (Object b : a) {
                System.out.println(b);
            }
        }
    }

    @Test
    public void testEmptyBrand() {
        //SELECT count(b.id) as ct,a.id as pk,a.name FROM phweb.ph_brand a left join phweb.ph_goods b on a.id = b.brand_id group by a.id
        List<Object[]> list = merchantBrandRepository.checkAllEmptyBrand();
        int ct = 0;
        for (Object[] item : list) {
            long count = Long.valueOf(String.valueOf(item[0]));
            if (count == 0) {
                long pk = Long.valueOf(String.valueOf(item[1]));
                String name = String.valueOf(item[2]);
                PhBrand brand = brandService.findOne(pk);
                if (brand != null) {
                    /* 状态[0-未上架,1-已上架] **/
                    brand.setStatus("0");
                    brandService.save(brand);
                    System.out.println(MessageFormat.format("{0}品牌被下架,ID 为{1}", name, brand.getId()));
                    logger.info(MessageFormat.format("{0}品牌被下架,ID 为{1}", name, brand.getId()));
                    ct++;
                }
            }
        }
        System.out.println(MessageFormat.format("品牌被下架总数为{0}", ct));
        logger.info(MessageFormat.format("品牌被下架总数为{0}", ct));
    }
}
