package cn.offway.hades.controller;

import cn.offway.hades.domain.PhConfig;
import cn.offway.hades.properties.QiniuProperties;
import cn.offway.hades.service.PhConfigService;
import com.alibaba.fastjson.JSONArray;
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
public class MiniBannerController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private PhConfigService configService;
    @Autowired
    private QiniuProperties qiniuProperties;

    @RequestMapping("/miniBanner_a.html")
    public String first(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        map.addAttribute("isApp", "1");
        map.addAttribute("pos", "a");
        map.addAttribute("json", getData("a", true));
        return "miniBanner_common";
    }

    @RequestMapping("/miniBanner_b.html")
    public String second(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        map.addAttribute("isApp", "1");
        map.addAttribute("pos", "b");
        map.addAttribute("json", getData("b", true));
        return "miniBanner_common";
    }

    @RequestMapping("/miniBanner_c.html")
    public String third(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        map.addAttribute("isApp", "1");
        map.addAttribute("pos", "c");
        map.addAttribute("json", getData("c", true));
        return "miniBanner_common";
    }

    @RequestMapping("/miniBanner_d.html")
    public String fourth(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        map.addAttribute("isApp", "0");
        map.addAttribute("pos", "a");
        map.addAttribute("json", getData("a", false));
        return "miniBanner_common";
    }

    @RequestMapping("/miniBanner_e.html")
    public String fifth(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        map.addAttribute("isApp", "0");
        map.addAttribute("pos", "b");
        map.addAttribute("json", getData("b", false));
        return "miniBanner_common";
    }

    @RequestMapping("/miniBanner_f.html")
    public String sixth(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        map.addAttribute("isApp", "0");
        map.addAttribute("pos", "c");
        map.addAttribute("json", getData("c", false));
        return "miniBanner_common";
    }

    private String getData(String pos, boolean isApp) {
        String key = isApp ? "INDEX_SELL_WELL" : "INDEX_SELL_WELL_MINI";
        String jsonStr = configService.findContentByName(key);
        if (jsonStr != null) {
            JSONArray jsonArray = JSONArray.parseArray(jsonStr);
            JSONObject jsonObject = jsonArray.getJSONObject(posToIndex(pos));
            if (jsonObject != null) {
                return jsonObject.toJSONString();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private int posToIndex(String pos) {
        int i = 0;
        switch (pos) {
            case "a":
                i = 0;
                break;
            case "b":
                i = 1;
                break;
            case "c":
                i = 2;
                break;
            default:
                break;
        }
        return i;
    }

    @ResponseBody
    @RequestMapping("/miniBanner_save")
    public boolean save(String pos, String img, String redirectType, String redirectTarget, String remark, String isApp) {
        int i = posToIndex(pos);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("banner", img);
        jsonObject.put("remark", remark);
        jsonObject.put("type", redirectType);
        if ("0".equals(redirectType)) {
            jsonObject.put("url", redirectTarget);
            jsonObject.put("redirectId", "0");
        } else if ("7".equals(redirectType)) {
            jsonObject.put("url", redirectTarget);
            jsonObject.put("redirectId", "0");
        } else {
            jsonObject.put("url", "");
            jsonObject.put("redirectId", "".equals(redirectTarget) ? "0" : redirectTarget);
        }
        String key = "1".equals(isApp) ? "INDEX_SELL_WELL" : "INDEX_SELL_WELL_MINI";
        PhConfig config = configService.findOne(key);
        JSONArray container;
        if (config == null) {
            config = new PhConfig();
            config.setCreateTime(new Date());
            config.setName(key);
            config.setRemark("");
            container = new JSONArray(3);
        } else {
            container = JSONArray.parseArray(config.getContent());
        }
        container.set(i, jsonObject);
        config.setContent(container.toJSONString());
        configService.save(config);
        return true;
    }
}
