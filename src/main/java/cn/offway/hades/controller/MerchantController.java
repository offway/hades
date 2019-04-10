package cn.offway.hades.controller;

import cn.offway.hades.domain.PhMerchant;
import cn.offway.hades.domain.PhMerchantBrand;
import cn.offway.hades.properties.QiniuProperties;
import cn.offway.hades.service.PhMerchantBrandService;
import cn.offway.hades.service.PhMerchantFileService;
import cn.offway.hades.service.PhMerchantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping
public class MerchantController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private QiniuProperties qiniuProperties;
    @Autowired
    private PhMerchantService merchantService;
    @Autowired
    private PhMerchantBrandService merchantBrandService;
    @Autowired
    private PhMerchantFileService merchantFileService;

    @RequestMapping("/merchant.html")
    public String index(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        return "merchant_index";
    }

    @RequestMapping("/merchant_add.html")
    public String merchant_add(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        return "merchant_add";
    }

    @ResponseBody
    @RequestMapping("/merchant_list")
    public Map<String, Object> getList(HttpServletRequest request) {
        int sEcho = Integer.parseInt(request.getParameter("sEcho"));
        int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
        int iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
        Sort sort = new Sort("id");
        Page<PhMerchant> pages = merchantService.findAll(new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, sort));
        List<PhMerchant> list = pages.getContent();
        List<Object> data = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        for (PhMerchant item : list) {
            Map<String, Object> row = mapper.convertValue(item, Map.class);
            long mid = item.getId();
            List<PhMerchantBrand> brandList = merchantBrandService.findByPid(mid);
            row.put("brandList", brandList);
            data.add(row);
        }
        int initEcho = sEcho + 1;
        Map<String, Object> map = new HashMap<>();
        map.put("sEcho", initEcho);
        map.put("iTotalRecords", pages.getTotalElements());//数据总条数
        map.put("iTotalDisplayRecords", pages.getTotalElements());//显示的条数
        map.put("aData", data);//数据集合
        return map;
    }

    @ResponseBody
    @RequestMapping("/merchant_del")
    public boolean delete(@RequestParam("ids[]") Long[] ids) {
        for (Long id : ids) {
            merchantService.del(id);
            merchantBrandService.delByPid(id);
            merchantFileService.delByPid(id);
        }
        return true;
    }
}
