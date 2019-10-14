package cn.offway.hades.service;

import com.alipay.api.response.AlipayTradeRefundResponse;

public interface AlipayService {
    AlipayTradeRefundResponse refund(double amount, String out_trade_no, String out_request_no, String refund_reason);
}
