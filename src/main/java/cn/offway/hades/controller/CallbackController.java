package cn.offway.hades.controller;

import cn.offway.hades.domain.PhConfig;
import cn.offway.hades.domain.PhRefund;
import cn.offway.hades.domain.PhUserInfo;
import cn.offway.hades.repository.PhArticleRepository;
import cn.offway.hades.runner.InitRunner;
import cn.offway.hades.service.*;
import cn.offway.hades.utils.MeiqiaSigner;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.SignatureException;
import java.text.MessageFormat;
import java.util.Date;
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
    @Autowired
    private PhArticleRepository phArticleRepository;
    @Autowired
    private PhOrderInfoService orderInfoService;
    @Autowired
    private PhConfigService configService;
    @Autowired
    private PhUserInfoService userInfoService;

    @Value("${meqia.key}")
    private String meqiaKey;

    @RequestMapping("/express")
    @ResponseBody
    public Map<String, Object> expressUpdate(String param, String uid, String oid) {
        logger.info(param);
        JSONObject jsonObject = JSONObject.parseObject(param);
        //快递单当前签收状态，包括0在途中、1已揽收、2疑难、3已签收、4退签、5同城派送中、6退回、7转单等7个状态
        int state = jsonObject.getJSONObject("lastResult").getIntValue("state");
        if (state == 5) {
            Map<String, String> args = new HashMap<>();
            args.put("type", "9");
            args.put("id", oid);
            args.put("url", "");
            jPushService.sendPushUser("派送中", "准备收货：亲，您购买的商品已经在路上啦，注意签收哦！", args, uid);
            PhUserInfo userInfo = userInfoService.findOne(Long.valueOf(uid));
            if (userInfo != null) {
                SettlementController.sendSMS("【很潮】app提醒您：您购买的商品正在派送中，请注意签收哦~", userInfo.getPhone());
            }
        } else if (state == 3) {
            String key = "{0}_{1}_{2}_ConfirmPackage";//beginTime + "_" + endTime + "_" + ids + "_" + discount;
            DateTime oneWeekLater = new DateTime().plusDays(7);
            DateTime twoWeeksLater = oneWeekLater.plusDays(7);
            JSONArray jsonArray = new JSONArray();
            JSONObject map = new JSONObject();
            map.put("id", oid);
            map.put("type", "confirmPackage");
            map.put("sTime", oneWeekLater.toString("yyyy-MM-dd HH:mm:ss"));
            map.put("eTime", twoWeeksLater.toString("yyyy-MM-dd HH:mm:ss"));
            jsonArray.add(map);
            key = MessageFormat.format(key, oneWeekLater.toString("yyyy-MM-dd HH:mm:ss"), twoWeeksLater.toString("yyyy-MM-dd HH:mm:ss"), oid);
            //save to DB
            String jsonStr = configService.findContentByName("CRONJOB");
            JSONObject jsonObjectSaved;
            if (jsonStr == null || "".equals(jsonStr.trim())) {
                jsonObjectSaved = new JSONObject();
                jsonObjectSaved.put(key, jsonArray);
            } else {
                jsonObjectSaved = JSON.parseObject(jsonStr);
                if (!jsonObjectSaved.containsKey(key)) {
                    jsonObjectSaved.put(key, jsonArray);
                }
            }
            PhConfig config = configService.findOne("CRONJOB");
            config.setContent(jsonObjectSaved.toJSONString());
            configService.save(config);
            //launch the job
            InitRunner.createJob(jsonArray, key, oneWeekLater.toDate(), twoWeeksLater.toDate(), new Date(), null, null, orderInfoService, configService);
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
     *
     * @param content
     * @return
     * @throws SignatureException
     */
    @ResponseBody
    @PostMapping("/meiqia")
    public String meiqia(@RequestHeader("authorization") String authorization, @RequestBody String content) throws SignatureException {
        logger.info("美洽服务端发送消息,authorization=" + authorization + ";content=" + content);
        //{"contentType": "text", "customizedData": {"avatar": "https://static.runoob.com/images/demo/demo1.jpg", "name": "OFFWAY_739441"}, "messageTime": "2019-06-24T07:46:54.104220", "messageId": 628173038, "clientId": "1N4vhKdSSD5bYIWBVwcY4HWkynE", "content": "1", "customizedId": "14", "fromName": "\u5f88\u6f6e\u5c0f\u52a9\u624b", "deviceOS": "Android", "type": "message", "deviceToken": null}
        //验签
        MeiqiaSigner signer = new MeiqiaSigner(meqiaKey);
        String sign = signer.sign(content);
        if (authorization.equals(sign)) {
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

    @ResponseBody
    @PostMapping("/qiniu/avthumb")
    public String avthumb(@RequestBody String content) {
        logger.info("七牛视频处理结果：" + content);
        JSONObject jsonObject = JSON.parseObject(content);
        JSONArray jsonArray = jsonObject.getJSONArray("items");
        String key = jsonArray.getJSONObject(0).getString("key");
        System.out.println(key);
        String inputKey = jsonObject.getString("inputKey");
        phArticleRepository.updateVideoUrl("http://qiniu.offway.cn/" + inputKey, "http://qiniu.offway.cn/" + key);

        return "success";
    }

}
