package cn.offway.hades.controller;

import cn.offway.hades.domain.PhAdmin;
import cn.offway.hades.properties.QiniuProperties;
import cn.offway.hades.service.PhMerchantService;
import cn.offway.hades.service.PhRoleadminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MerchantOrderController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private QiniuProperties qiniuProperties;
    @Autowired
    private PhMerchantService merchantService;
    @Autowired
    private PhRoleadminService roleadminService;

    @RequestMapping("/merchantOrder.html")
    public String index(ModelMap map, @AuthenticationPrincipal PhAdmin admin) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        List<Long> roles = roleadminService.findRoleIdByAdminId(admin.getId());
        if (roles.contains(BigInteger.valueOf(8L))) {
            map.addAttribute("isAdmin", 0);
        } else {
            map.addAttribute("isAdmin", 1);
        }
        return "merchantOrder_index";
    }

    @ResponseBody
    @RequestMapping("/merchantOrder_list")
    public Map<String, Object> getList(HttpServletRequest request, @AuthenticationPrincipal PhAdmin admin) {
        List<Long> roles = roleadminService.findRoleIdByAdminId(admin.getId());
        if (roles.contains(BigInteger.valueOf(8L))) {
            return null;
        } else {
            List<Map<String, Object>> data = new ArrayList<>();
            List<Object[]> list = merchantService.stat();
            for (Object[] l : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("merchantId", l[0]);
                map.put("merchantName", l[1]);
                map.put("merchantLogo", l[2]);
                map.put("merchantBrandCount", l[3]);
                List<Object[]> listOrder = merchantService.statOrder(Long.valueOf(String.valueOf(l[0])));
                map.put("merchantOrderCount", listOrder.size());
                int goodsCount = 0;
                double price = 0;
                for (Object[] ll : listOrder) {
                    goodsCount += Integer.valueOf(String.valueOf(ll[2]));
                    price += Double.valueOf(String.valueOf(ll[4]));
                }
                map.put("merchantGoodsCount", goodsCount);
                map.put("merchantPrice", price);
                data.add(map);
            }
            Map<String, Object> res = new HashMap<>();
            res.put("data", data);
            return res;
        }
    }
}
