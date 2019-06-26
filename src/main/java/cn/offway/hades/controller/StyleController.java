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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String jsonStr = configService.findContentByName("INDEX_STYLE");
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
        String jsonStr = configService.findContentByName("INDEX_STYLE");
        JSONArray jsonArray = JSONArray.parseArray(jsonStr);
        return jsonArray.getJSONObject(id);
    }

    @RequestMapping("/style_del")
    @ResponseBody
    public boolean delete(@RequestParam("ids[]") Long[] ids) {
        String jsonStr = configService.findContentByName("INDEX_STYLE");
        JSONArray jsonArray = JSONArray.parseArray(jsonStr);
        List<Object> tmpList = new ArrayList<>();
        for (Long id : ids) {
            tmpList.add(jsonArray.get(id.intValue()));
        }
        for (Object o : tmpList) {
            jsonArray.remove(o);
        }
        PhConfig config = configService.findOne("INDEX_STYLE");
        config.setContent(jsonArray.toJSONString());
        configService.save(config);
        return true;
    }

    @ResponseBody
    @RequestMapping("/style_save_sort")
    public boolean updateStockListMix(@RequestParam("value") String[] values, @RequestParam("image") String[] images) {
        if (values.length != images.length) {
            return false;
        }
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < values.length; i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("value", values[i]);
            jsonObject.put("image", images[i]);
            jsonArray.add(jsonObject);
        }
        PhConfig config = configService.findOne("INDEX_STYLE");
        config.setContent(jsonArray.toJSONString());
        configService.save(config);
        return true;
    }


    @RequestMapping("/style_save")
    @ResponseBody
    public boolean save(Integer id, String name, String image) {
        String jsonStr = configService.findContentByName("INDEX_STYLE");
        JSONArray jsonArray = JSONArray.parseArray(jsonStr);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("value", name);
        jsonObject.put("image", image);
        if (id == null) {
            jsonArray.add(jsonObject);
        } else {
            jsonArray.set(id, jsonObject);
        }
        PhConfig config = configService.findOne("INDEX_STYLE");
        config.setContent(jsonArray.toJSONString());
        configService.save(config);
        return true;
    }
}
