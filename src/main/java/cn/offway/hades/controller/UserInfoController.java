package cn.offway.hades.controller;

import cn.offway.hades.domain.PhUserInfo;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping
public class UserInfoController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private PhUserInfoService userInfoService;

    @RequestMapping("/userInfo.html")
    public String list() {
        return "userInfo_index";
    }

    @ResponseBody
    @RequestMapping("/userInfo_list")
    public Map<String, Object> usersData(HttpServletRequest request, String phone, String nickname, String sex, String year, String channel) {
        String sortCol = request.getParameter("iSortCol_0");
        String sortName = request.getParameter("mDataProp_" + sortCol);
        String sortDir = request.getParameter("sSortDir_0");
        int sEcho = Integer.parseInt(request.getParameter("sEcho"));
        int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
        int iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
        Date sTime = null, eTime = null;
        if (!"".equals(year)) {
            String sTimeStr = year + "-01-01 00:00:00";
            String eTimeStr = year + "-12-31 23:59:59";
            DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            sTime = DateTime.parse(sTimeStr, format).toDate();
            eTime = DateTime.parse(eTimeStr, format).toDate();
        }
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Page<PhUserInfo> pages = userInfoService.list(phone, nickname, sex, sTime, eTime, channel, new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, sort));
        // 为操作次数加1，必须这样做
        int initEcho = sEcho + 1;
        Map<String, Object> map = new HashMap<>();
        map.put("sEcho", initEcho);
        map.put("iTotalRecords", pages.getTotalElements());//数据总条数
        map.put("iTotalDisplayRecords", pages.getTotalElements());//显示的条数
        map.put("aData", pages.getContent());//数据集合
        return map;
    }
}
