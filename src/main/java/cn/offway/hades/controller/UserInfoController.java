package cn.offway.hades.controller;

import cn.offway.hades.domain.PhAdmin;
import cn.offway.hades.domain.PhUserInfo;
import cn.offway.hades.service.PhAdminService;
import cn.offway.hades.service.PhRoleadminService;
import cn.offway.hades.service.PhUserInfoService;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping
public class UserInfoController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private PhUserInfoService userInfoService;
    @Autowired
    private PhRoleadminService roleadminService;
    @Autowired
    private PhAdminService adminService;

    @RequestMapping("/userInfo.html")
    public String list() {
        return "userInfo_index";
    }

    @ResponseBody
    @RequestMapping("/userInfo_list")
    public Map<String, Object> usersData(HttpServletRequest request, String phone, String nickname, String userId, String sex, String year, String channel, String sTimeReg, String eTimeReg) {
        String sortCol = request.getParameter("iSortCol_0");
        String sortName = request.getParameter("mDataProp_" + sortCol);
        String sortDir = request.getParameter("sSortDir_0");
        int sEcho = Integer.parseInt(request.getParameter("sEcho"));
        int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
        int iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
        Date sTime = null, eTime = null, sTimeRegObj = null, eTimeRegObj = null;
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        if (!"".equals(year)) {
            String sTimeStr = year + "-01-01 00:00:00";
            String eTimeStr = year + "-12-31 23:59:59";
            sTime = DateTime.parse(sTimeStr, format).toDate();
            eTime = DateTime.parse(eTimeStr, format).toDate();
        }
        if (sTimeReg != null && !"".equals(sTimeReg)) {
            sTimeRegObj = DateTime.parse(sTimeReg, format).toDate();
        }
        if (eTimeReg != null && !"".equals(eTimeReg)) {
            eTimeRegObj = DateTime.parse(eTimeReg, format).toDate();
        }
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Page<PhUserInfo> pages = userInfoService.list(phone, nickname, userId, sex, sTime, eTime, channel, sTimeRegObj, eTimeRegObj, new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, sort));
        // 为操作次数加1，必须这样做
        int initEcho = sEcho + 1;
        Map<String, Object> map = new HashMap<>();
        map.put("sEcho", initEcho);
        map.put("iTotalRecords", pages.getTotalElements());//数据总条数
        map.put("iTotalDisplayRecords", pages.getTotalElements());//显示的条数
        map.put("aData", pages.getContent());//数据集合
        return map;
    }

    @ResponseBody
    @RequestMapping("/channel_list")
    public Map<String, String> get(@AuthenticationPrincipal PhAdmin admin) {
        Map<String, String> ret = new HashMap<>();
        long channelRoleId = 12;
        long adminRoleId = 1;
        List<Long> roles = roleadminService.findRoleIdByAdminId(admin.getId());
        if (roles.contains(BigInteger.valueOf(adminRoleId))) {
            ret.put("", "全部");
            for (Object uid : roleadminService.findAdminIdByRoleId(channelRoleId)) {
                PhAdmin obj = adminService.findOne(((BigInteger) uid).longValue());
                if (obj != null && !ret.containsKey(obj.getUsername())) {
                    if ("wsd".equals(obj.getUsername().toLowerCase())) {
                        for (int i = 1; i <= 10; i++) {
                            ret.put(obj.getUsername() + i, obj.getNickname() + i);
                        }
                    } else {
                        ret.put(obj.getUsername(), obj.getNickname());
                    }
                }
            }
        } else if (roles.contains(BigInteger.valueOf(channelRoleId))) {
            if ("wsd".equals(admin.getUsername().toLowerCase())) {
                for (int i = 1; i <= 10; i++) {
                    ret.put(admin.getUsername() + i, admin.getNickname() + i);
                }
            } else {
                ret.put(admin.getUsername(), admin.getNickname());
            }
        } else {
            ret.put("", "全部");
        }
        return ret;
    }
}
