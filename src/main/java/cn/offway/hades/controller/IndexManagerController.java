package cn.offway.hades.controller;

import cn.offway.hades.domain.PhConfig;
import cn.offway.hades.properties.QiniuProperties;
import cn.offway.hades.service.PhConfigService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@RequestMapping
@Controller
public class IndexManagerController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private QiniuProperties qiniuProperties;
    private PhConfigService configService;
    private PhConfig phConfig;
    private JSONArray jsonArray;

    @Autowired
    public IndexManagerController(QiniuProperties qiniuProperties, PhConfigService configService) {
        this.qiniuProperties = qiniuProperties;
        this.configService = configService;
        String jsonStr = configService.findContentByName("INDEX_IMAGES");
        if (jsonStr != null && !"".equals(jsonStr)) {
            jsonArray = JSON.parseArray(jsonStr);
        }
        phConfig = configService.findOne("INDEX_IMAGES");
    }

    private String getImgFromJSON(String num) {
        if (jsonArray != null) {
            for (Object o : jsonArray) {
                JSONObject obj = (JSONObject) o;
                if (num.equals(obj.getString("type"))) {
                    return obj.getString("image");
                }
            }
            return "";
        } else {
            return "";
        }
    }

    @ResponseBody
    @RequestMapping("/index_save")
    public boolean save(String id, String logo, String value) {
        if (jsonArray == null) {
            List<Object> list = new ArrayList<>();
            Map<String, String> map = new HashMap<>();
            map.put("type", id);
            map.put("value", value);
            map.put("image", logo);
            list.add(map);
            jsonArray = new JSONArray(list);
        } else {
            Object it = null;
            for (Object o : jsonArray) {
                JSONObject obj = (JSONObject) o;
                if (id.equals(obj.getString("type"))) {
                    it = obj;
                }
            }
            if (it != null) {
                jsonArray.remove(it);
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", id);
            jsonObject.put("value", value);
            jsonObject.put("image", logo);
            jsonArray.add(jsonObject);
        }
        if (phConfig == null) {
            phConfig = new PhConfig();
            phConfig.setName("INDEX_IMAGES");
            phConfig.setCreateTime(new Date());
        }
        phConfig.setContent(jsonArray.toJSONString());
        configService.save(phConfig);
        return true;
    }

    @RequestMapping("/index_zgll.html")
    public String zgll(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        map.addAttribute("image", getImgFromJSON("0"));
        map.addAttribute("type", "0");
        map.addAttribute("value", "中国力量");
        return "index_common";
    }

    @RequestMapping("/index_gjcl.html")
    public String gjcl(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        map.addAttribute("image", getImgFromJSON("1"));
        map.addAttribute("type", "1");
        map.addAttribute("value", "高街潮流");
        return "index_common";
    }

    @RequestMapping("/index_msd.html")
    public String msd(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        map.addAttribute("image", getImgFromJSON("2"));
        map.addAttribute("type", "2");
        map.addAttribute("value", "买手店");
        return "index_common";
    }

    @RequestMapping("/index_xlzq.html")
    public String xlzq(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        map.addAttribute("image", getImgFromJSON("3"));
        map.addAttribute("type", "3");
        map.addAttribute("value", "限量专区");
        return "index_common";
    }

    @RequestMapping("/index_mfs.html")
    public String mfs(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        map.addAttribute("image", getImgFromJSON("4"));
        map.addAttribute("type", "4");
        map.addAttribute("value", "免费送");
        return "index_common";
    }

}
