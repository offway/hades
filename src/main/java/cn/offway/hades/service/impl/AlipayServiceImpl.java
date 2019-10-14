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
    public AlipayTradeRefundResponse refund(double amount, String out_trade_no, String out_request_no, String refund_reason) {
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        model.setOutTradeNo(out_trade_no);//支付宝交易号，和商户订单号不能同时为空
        model.setOutRequestNo(out_request_no);//标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传。
        model.setRefundAmount(String.valueOf(amount));//需要退款的金额，该金额不能大于订单金额,单位为元，支持两位小数
        model.setRefundReason("用户申请退款:" + refund_reason);//退款的原因说明
        request.setBizModel(model);
        try {
            return alipayClient.execute(request);
        } catch (AlipayApiException e) {
            logger.error("alipay refund error", e);
        }
        return null;
    }
}
