package cn.offway.hades.runner;

import cn.offway.hades.domain.PhMerchant;
import cn.offway.hades.domain.PhOrderInfo;
import cn.offway.hades.domain.PhSettlementDetail;
import cn.offway.hades.domain.PhSettlementInfo;
import cn.offway.hades.service.PhMerchantService;
import cn.offway.hades.service.PhOrderInfoService;
import cn.offway.hades.service.PhSettlementDetailService;
import cn.offway.hades.service.PhSettlementInfoService;
import cn.offway.hades.utils.MathUtils;
import com.aliyun.mq.http.MQClient;
import com.aliyun.mq.http.MQConsumer;
import com.aliyun.mq.http.common.AckMessageException;
import com.aliyun.mq.http.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class MQRunner implements ApplicationRunner {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    // 所属的 Topic
    private final static String TOPIC = "ORDER_PAY_SUCCESS";
    // 您在控制台创建的 Consumer ID(Group ID)
    private final static String GROUP_ID = "GID_HADES";
    // Topic所属实例ID，默认实例为空
    private final static String INSTANCE_ID = "MQ_INST_1021766862384088_BayRNaow";
    // 设置HTTP接入域名（此处以公共云生产环境为例）
    private final static String HTTP_ENDPOINT = "http://1021766862384088.mqrest.cn-qingdao-public.aliyuncs.com";
    // AccessKey 阿里云身份验证，在阿里云服务器管理控制台创建
    private final static String ACCESS_KEY = "LTAIp3JnL0OkoWAc";
    // SecretKey 阿里云身份验证，在阿里云服务器管理控制台创建
    private final static String SECRET_KEY = "MGOZFUbEZh8DLwWMQZhTu9tMBggl0F";

    @Autowired
    private PhSettlementInfoService phSettlementInfoService;

    @Autowired
    private PhSettlementDetailService phSettlementDetailService;

    @Autowired
    private PhOrderInfoService phOrderInfoService;

    @Autowired
    private PhMerchantService phMerchantService;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        MQClient mqClient = new MQClient(HTTP_ENDPOINT, ACCESS_KEY, SECRET_KEY);
        MQConsumer consumer = mqClient.getConsumer(INSTANCE_ID, TOPIC, GROUP_ID, null);
        // 在当前线程循环消费消息，建议是多开个几个线程并发消费消息
        do {
            List<Message> messages = null;
            try {
                // 长轮询消费消息
                // 长轮询表示如果topic没有消息则请求会在服务端挂住3s，3s内如果有消息可以消费则立即返回
                messages = consumer.consumeMessage(
                        3,// 一次最多消费3条(最多可设置为16条)
                        3// 长轮询时间3秒（最多可设置为30秒）
                );
            } catch (Throwable e) {
                logger.error("MQ_ERROR", e);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e1) {
                    logger.error("MQ_ERROR", e);
                }
            }
            // 没有消息
            if (messages == null || messages.isEmpty()) {
                System.out.println(Thread.currentThread().getName() + ": no new message, continue!");
                continue;
            }
            // 处理业务逻辑
            for (Message message : messages) {
                System.out.println("Receive message: " + message);
                logger.info(message.getMessageBodyString());
                save(message.getMessageBodyString());
            }
            // Message.nextConsumeTime前若不确认消息消费成功，则消息会重复消费
            // 消息句柄有时间戳，同一条消息每次消费拿到的都不一样
            {
                List<String> handles = new ArrayList<>();
                for (Message message : messages) {
                    handles.add(message.getReceiptHandle());
                }
                try {
                    consumer.ackMessage(handles);
                } catch (Throwable e) {
                    // 某些消息的句柄可能超时了会导致确认不成功
                    if (e instanceof AckMessageException) {
                        AckMessageException errors = (AckMessageException) e;
                        System.out.println("Ack message fail, requestId is:" + errors.getRequestId() + ", fail handles:");
                        if (errors.getErrorMessages() != null) {
                            for (String errorHandle : errors.getErrorMessages().keySet()) {
                                System.out.println("Handle:" + errorHandle + ", ErrorCode:" + errors.getErrorMessages().get(errorHandle).getErrorCode()
                                        + ", ErrorMsg:" + errors.getErrorMessages().get(errorHandle).getErrorMessage());
                            }
                        }
                        continue;
                    }
                    logger.error("QM_ERROR", e);
                }
            }
        } while (true);
    }

    public void save(String preorderNo) {
        List<PhOrderInfo> phOrderInfos = phOrderInfoService.findByPreorderNoAndStatus(preorderNo, "1");
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
            settlementInfo.setUnsettledAmount(MathUtils.add(settlementInfo.getUnsettledAmount(), settlementDetail.getAmount()));
            settlementInfo.setUnsettledCount(settlementInfo.getUnsettledCount() + 1L);
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
}
