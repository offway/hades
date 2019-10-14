package cn.offway.hades.service.impl;

import cn.offway.hades.service.AlipayService;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlipayServiceImpl implements AlipayService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AlipayClient alipayClient;

    @Override
    public AlipayTradeRefundResponse refund(double amount, String tradeNo) {
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        model.setOutTradeNo(tradeNo);//支付宝交易号，和商户订单号不能同时为空
        model.setRefundAmount(String.valueOf(amount));//需要退款的金额，该金额不能大于订单金额,单位为元，支持两位小数
        model.setRefundReason("用户申请退款");//退款的原因说明
        request.setBizModel(model);
        try {
            return alipayClient.execute(request);
        } catch (AlipayApiException e) {
            logger.error("alipay refund error", e);
        }
        return null;
    }
}
