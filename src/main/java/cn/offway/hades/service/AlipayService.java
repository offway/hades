package cn.offway.hades.service;

import com.alipay.api.response.AlipayTradeRefundResponse;

public interface AlipayService {
    AlipayTradeRefundResponse refund(double amount, String tradeNo);
}
