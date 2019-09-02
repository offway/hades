package cn.offway.hades.controller;

import cn.offway.hades.domain.PhConfig;
import cn.offway.hades.service.PhConfigService;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping
public class UpdateController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private PhConfigService configService;

    @RequestMapping("/update_android.html")
    public String android(ModelMap map) {
        map.addAttribute("os", "android");
        map.addAttribute("json", getVersion("android"));
        return "update_common";
    }

    @RequestMapping("/update_ios.html")
    public String ios(ModelMap map) {
        map.addAttribute("os", "ios");
        map.addAttribute("json", getVersion("ios"));
        return "update_common";
    }

    private String getVersion(String os) {
        String key = "update_" + os;
        //key 转成大写
        key = key.toUpperCase();
        return configService.findContentByName(key);
    }

    @ResponseBody
    @RequestMapping("/update_save")
    public boolean save(String os, String version, String title, String body, String url, String isForce) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("version", version);
        jsonObject.put("title", title);
        jsonObject.put("body", body);
        jsonObject.put("url", url);
        jsonObject.put("isForce", "1".equals(isForce));
        String key = "update_" + os;
        //key 转成大写
        key = key.toUpperCase();
        PhConfig config = configService.findOne(key);
        if (config == null) {
            config = new PhConfig();
            config.setCreateTime(new Date());
            config.setName(key);
            config.setRemark("");
        }
        config.setContent(jsonObject.toJSONString());
        configService.save(config);
        return true;
    }
}
