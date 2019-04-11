package cn.offway.hades.controller;

import cn.offway.hades.properties.QiniuProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class GoodsCategoryController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private QiniuProperties qiniuProperties;

    @RequestMapping("/goodsCategory.html")
    public String index(ModelMap map, Long id) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        map.addAttribute("theId", id);
        return "goodsCategory_index";
    }
}
