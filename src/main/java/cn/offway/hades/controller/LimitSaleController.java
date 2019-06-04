package cn.offway.hades.controller;

import cn.offway.hades.properties.QiniuProperties;
import cn.offway.hades.service.PhLimitedSaleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping
@Controller
public class LimitSaleController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private QiniuProperties qiniuProperties;
    @Autowired
    private PhLimitedSaleService limitedSaleService;

    @RequestMapping("/limit_sale.html")
    public String index(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        return "limit_sale";
    }

    @RequestMapping("/limit_sale_add.html")
    public String add(ModelMap map, String args) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        map.addAttribute("args", args);
        return "limit_sale_add";
    }
}
