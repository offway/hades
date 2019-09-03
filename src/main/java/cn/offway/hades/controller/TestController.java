package cn.offway.hades.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test")
public class TestController {
    @RequestMapping("/1")
    @ResponseBody
    public void testSms() {
        SettlementController.sendSMS("【很潮】您好，有一笔退款审核已通过，请通过后台确认打款~", "+8613761839483", "+8613663478885");
    }

    @RequestMapping("/2")
    @ResponseBody
    public void testSms2() {
        SettlementController.sendSMS("很潮app提醒您：您有一笔订单未支付哦~", "+8613761839483", "+8613663478885");
    }

    @RequestMapping("/3")
    @ResponseBody
    public void testSms3() {
        SettlementController.sendSMS("很潮app提醒您：您购买的商品正在派送中，请注意签收哦~", "+8613761839483", "+8613663478885");
    }

    @RequestMapping("/4")
    @ResponseBody
    public void testSms4() {
        SettlementController.sendSMS("很潮app提醒您：您购买的商品xxxxx已经发货啦，请及时查看物流信息哦~", "+8613761839483", "+8613663478885");
    }
}
