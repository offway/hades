package cn.offway.hades.controller;

import cn.offway.hades.domain.PhAdmin;
import cn.offway.hades.domain.PhUserInfo;
import cn.offway.hades.service.PhOrderInfoService;
import cn.offway.hades.service.PhRoleadminService;
import cn.offway.hades.service.PhUserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping
public class PurchaseRankController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private PhRoleadminService roleadminService;
    @Autowired
    private PhOrderInfoService orderInfoService;
    @Autowired
    private PhUserInfoService userInfoService;

    @RequestMapping("/purchase_rank.html")
    public String index() {
        return "purchase_rank_index";
    }

    @ResponseBody
    @RequestMapping("/purchase_rank_list")
    public Map<String, Object> getList(HttpServletRequest request, @AuthenticationPrincipal PhAdmin admin) {
        List<Long> roles = roleadminService.findRoleIdByAdminId(admin.getId());
        if (roles.contains(BigInteger.valueOf(8L))) {
            return null;
        } else {
            List<Map<String, Object>> data = new ArrayList<>();
            List<Object[]> list = orderInfoService.stat();
            for (Object[] l : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("amount", l[0]);
                map.put("userId", l[1]);
                map.put("orderCount", l[2]);
                PhUserInfo userInfo = userInfoService.findOne(Long.valueOf(String.valueOf(l[1])));
                if (userInfo != null) {
                    map.put("phone", userInfo.getPhone());
                    map.put("nickname", userInfo.getNickname());
                    map.put("createTime", userInfo.getCreateTime());
                } else {
                    map.put("phone", "");
                    map.put("nickname", "");
                    map.put("createTime", "");
                }
                data.add(map);
            }
            Map<String, Object> res = new HashMap<>();
            res.put("data", data);
            return res;
        }
    }
}
