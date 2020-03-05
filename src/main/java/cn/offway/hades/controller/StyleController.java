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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping
public class StyleController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private QiniuProperties qiniuProperties;
    @Autowired
    private PhConfigService configService;

    @RequestMapping("/style.html")
    public String index(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        return "style_index";
    }

    @RequestMapping("/style_list")
    @ResponseBody
    public Map<String, Object> list() {
        Map<String, Object> list = new HashMap<>();
        String jsonStr = configService.findContentByName("INDEX_STYLE_FULL");
        JSONArray jsonArray = JSONArray.parseArray(jsonStr);
        int i = 0;
        List<Map> mapList = new ArrayList<>();
        for (Map m : jsonArray.toJavaList(Map.class)) {
            m.put("id", i);
            i++;
            mapList.add(m);
        }
        list.put("data", mapList);
        return list;
    }

    @RequestMapping("/style_find")
    @ResponseBody
    public JSONObject fetch(Integer id) {
        String jsonStr = configService.findContentByName("INDEX_STYLE_FULL");
        JSONArray jsonArray = JSONArray.parseArray(jsonStr);
        return jsonArray.getJSONObject(id);
    }

    @RequestMapping("/style_del")
    @ResponseBody
    public boolean delete(@RequestParam("ids[]") Long[] ids) {
        String jsonStr = configService.findContentByName("INDEX_STYLE_FULL");
        JSONArray jsonArray = JSONArray.parseArray(jsonStr);
        List<Object> tmpList = new ArrayList<>();
        for (Long id : ids) {
            tmpList.add(jsonArray.get(id.intValue()));
        }
        for (Object o : tmpList) {
            jsonArray.remove(o);
        }
        PhConfig config = configService.findOne("INDEX_STYLE_FULL");
        config.setContent(jsonArray.toJSONString());
        configService.save(config);
        return true;
    }

    @ResponseBody
    @RequestMapping("/style_save_sort")
    public boolean updateStockListMix(@RequestParam("value") String[] values, @RequestParam("image") String[] images, @RequestParam("banner") String[] banners,
                                      @RequestParam("reason") String[] reasons, @RequestParam("info") String[] infos) {
        if (values.length != images.length) {
            return false;
        }
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArray2 = new JSONArray();
        for (int i = 0; i < values.length; i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("value", values[i]);
            jsonObject.put("image", images[i]);
            jsonObject.put("banner", banners[i]);
            jsonObject.put("reason", reasons[i]);
            jsonObject.put("info", infos[i]);
            jsonArray.add(jsonObject);
            if (i <= 7) {
                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.put("value", values[i]);
                jsonObject2.put("image", images[i]);
                jsonArray2.add(jsonObject2);
            }
        }
        PhConfig config = configService.findOne("INDEX_STYLE_FULL");
        PhConfig config2 = configService.findOne("INDEX_STYLE");
        config2.setContent(jsonArray2.toJSONString());
        config.setContent(jsonArray.toJSONString());
        configService.save(config);
        configService.save(config2);
        return true;
    }


    @RequestMapping("/style_save")
    @ResponseBody
    public boolean save(Integer id, String name, String image, String banner, String reason, String info) {
        String jsonStr = configService.findContentByName("INDEX_STYLE_FULL");
        JSONArray jsonArray = JSONArray.parseArray(jsonStr);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("value", name);
        jsonObject.put("image", image);
        jsonObject.put("banner", banner);
        jsonObject.put("reason", reason);
        jsonObject.put("info", info);
        if (id == null) {
            jsonArray.add(jsonObject);
        } else {
            jsonArray.set(id, jsonObject);
        }
        PhConfig config = configService.findOne("INDEX_STYLE_FULL");
        config.setContent(jsonArray.toJSONString());
        configService.save(config);
        return true;
    }

    @ResponseBody
    @RequestMapping("/style_pin_mini")
    public boolean pinMini(@RequestParam("ids[]") Long[] ids) {
        String key = "INDEX_STYLE_FULL_MINI";
        String jsonStr = configService.findContentByName(key);
        JSONArray jsonArray;
        if (jsonStr != null && !"".equals(jsonStr)) {
            jsonArray = JSON.parseArray(jsonStr);
        } else {
            jsonArray = new JSONArray();
        }
        for (Long id : ids) {
            JSONObject jsonObject = fetch(id.intValue());
            jsonArray.add(jsonObject);
        }
        PhConfig config = configService.findOne(key);
        if (config == null) {
            config = new PhConfig();
            config.setName(key);
            config.setCreateTime(new Date());
        }
        config.setContent(jsonArray.toJSONString());
        configService.save(config);
        return true;
    }

    @ResponseBody
    @RequestMapping("/style_pin_mini_list")
    public JSONArray pinListMini() {
        String key = "INDEX_STYLE_FULL_MINI";
        String jsonStr = configService.findContentByName(key);
        JSONArray jsonArray = new JSONArray();
        if (jsonStr != null && !"".equals(jsonStr)) {
            jsonArray = JSON.parseArray(jsonStr);
        }
        return jsonArray;
    }

    @ResponseBody
    @RequestMapping("/style_pin_mini_save")
    public boolean pinSaveMini(@RequestParam(name = "values[]", required = false) String[] values, @RequestParam(name = "images[]", required = false) String[] images,
                               @RequestParam(name = "banners[]", required = false) String[] banners, @RequestParam(name = "reasons[]", required = false) String[] reasons,
                               @RequestParam(name = "infos[]", required = false) String[] infos) {
        String key = "INDEX_STYLE_FULL_MINI";
        PhConfig config = configService.findOne(key);
        JSONArray jsonArray = new JSONArray();
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("value", values[i]);
                jsonObject.put("image", images[i]);
                jsonObject.put("banner", banners[i]);
                jsonObject.put("reason", reasons[i]);
                jsonObject.put("info", infos[i]);
                jsonArray.add(jsonObject);
            }
        }
        if (config == null) {
            config = new PhConfig();
            config.setName(key);
            config.setCreateTime(new Date());
        }
        config.setContent(jsonArray.toJSONString());
        configService.save(config);
        return true;
    }
}
