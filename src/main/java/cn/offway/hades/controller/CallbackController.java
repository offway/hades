package cn.offway.hades.controller;

import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.offway.hades.domain.PhRefund;
import cn.offway.hades.service.JPushService;
import cn.offway.hades.service.PhRefundService;
import cn.offway.hades.utils.MeiqiaSigner;

@Controller
@RequestMapping("/callback")
public class CallbackController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private JPushService jPushService;
    @Autowired
    private PhRefundService refundService;
    
    @Value("${meqia.key}")
    private String meqiaKey;

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
    
    /**
     * 美洽服务端发送消息
     * @param content
     * @return
     * @throws SignatureException 
     */
    @ResponseBody
    @PostMapping("/meiqia")
	public String meiqia(@RequestHeader("authorization") String authorization,  @RequestBody String content) throws SignatureException{
    	logger.info("美洽服务端发送消息,authorization="+authorization+";content="+content);
    	//{"contentType": "text", "customizedData": {"avatar": "https://static.runoob.com/images/demo/demo1.jpg", "name": "OFFWAY_739441"}, "messageTime": "2019-06-24T07:46:54.104220", "messageId": 628173038, "clientId": "1N4vhKdSSD5bYIWBVwcY4HWkynE", "content": "1", "customizedId": "14", "fromName": "\u5f88\u6f6e\u5c0f\u52a9\u624b", "deviceOS": "Android", "type": "message", "deviceToken": null}
    	//验签
    	MeiqiaSigner signer =  new MeiqiaSigner(meqiaKey);
        String sign = signer.sign(content);
        if(authorization.equals(sign)){
        	JSONObject jsonObject = JSON.parseObject(content);
        	String msg = jsonObject.getString("content");
        	String customizedId = jsonObject.getString("customizedId");
        	Map<String, String> extras = new HashMap<>();
			extras.put("type", "99");//0-H5,1-精选文章,2-活动
			extras.put("id", null);
			extras.put("url", null);
			jPushService.sendPushUser("客服消息", msg, extras, customizedId);
        }
    	return "success";
	}
}
