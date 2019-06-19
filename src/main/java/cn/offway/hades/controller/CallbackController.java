package cn.offway.hades.controller;

import cn.offway.hades.domain.PhRefund;
import cn.offway.hades.service.JPushService;
import cn.offway.hades.service.PhRefundService;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/callback")
public class CallbackController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private JPushService jPushService;
    @Autowired
    private PhRefundService refundService;

    @RequestMapping("/express")
    @ResponseBody
    public Map<String, Object> expressUpdate(String param, String uid, String oid) {
        logger.info(param);
        JSONObject jsonObject = JSONObject.parseObject(param);
        //快递单当前签收状态，包括0在途中、1已揽收、2疑难、3已签收、4退签、5同城派送中、6退回、7转单等7个状态
        int state = jsonObject.getJSONObject("destResult").getIntValue("state");
        if (state == 5) {
            Map<String, String> args = new HashMap<>();
            args.put("type", "9");
            args.put("id", oid);
            args.put("url", "");
            jPushService.sendPushUser("派送中", "准备收货：亲，您购买的商品已经在路上啦，注意签收哦！", args, uid);
        }
        Map<String, Object> ret = new HashMap<>();
        ret.put("result", true);
        ret.put("returnCode", 200);
        ret.put("message", "接收成功");
        return ret;
    }

    @RequestMapping("/expressRefund")
    @ResponseBody
    public Map<String, Object> expressUpdateRefund(String param, long id) {
        logger.info(param);
        JSONObject jsonObject = JSONObject.parseObject(param);
        //快递单当前签收状态，包括0在途中、1已揽收、2疑难、3已签收、4退签、5同城派送中、6退回、7转单等7个状态
        int state = jsonObject.getJSONObject("destResult").getIntValue("state");
        if (state == 3) {
            PhRefund refund = refundService.findOne(id);
            if (refund != null && "2".equals(refund.getStatus())) {
                /* 状态[0-审核中,1-待退货,2-退货中,3-退款中,4-退款成功,5-退款取消,6-审核失败] **/
                refund.setStatus("3");
                refundService.save(refund);
            }
        }
        Map<String, Object> ret = new HashMap<>();
        ret.put("result", true);
        ret.put("returnCode", 200);
        ret.put("message", "接收成功");
        return ret;
    }
}
