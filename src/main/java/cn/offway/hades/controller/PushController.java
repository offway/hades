package cn.offway.hades.controller;

import cn.offway.hades.domain.PhPush;
import cn.offway.hades.properties.QiniuProperties;
import cn.offway.hades.service.JPushService;
import cn.offway.hades.service.PhPushService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping
public class PushController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private QiniuProperties qiniuProperties;
    @Autowired
    private PhPushService pushService;
    @Autowired
    private JPushService jPushService;

    @RequestMapping("/push.html")
    public String index(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        return "push_index";
    }

    @ResponseBody
    @RequestMapping("/push_list")
    public Map<String, Object> usersData(HttpServletRequest request) {
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        ObjectMapper objectMapper = new ObjectMapper();
        String sortCol = request.getParameter("iSortCol_0");
        String sortName = request.getParameter("mDataProp_" + sortCol);
        String sortDir = request.getParameter("sSortDir_0");
        int sEcho = Integer.parseInt(request.getParameter("sEcho"));
        int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
        int iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
        //time
        String sTimeStr = request.getParameter("sTime");
        String eTimeStr = request.getParameter("eTime");
        Date sTime = null, eTime = null;
        if (!"".equals(sTimeStr) && !"".equals(eTimeStr)) {
            sTime = DateTime.parse(sTimeStr, format).toDate();
            eTime = DateTime.parse(eTimeStr, format).toDate();
        }
        String name = request.getParameter("name");
        String content = request.getParameter("content");
        String type = request.getParameter("type");
        Page<PhPush> pages = pushService.list(name, content, type, sTime, eTime, new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, Sort.Direction.fromString(sortDir), sortName));
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
    @RequestMapping("/push_save")
    public boolean save(PhPush push, String pushAll, String userIdStr, String pushNow) {
        PhPush pushSaved;
        String[] users = null;
        if ("1".equals(pushAll) && !"".equals(userIdStr.trim())) {
            users = userIdStr.split(",");
        }
        if (push.getId() == null) {
            String uuid = UUID.randomUUID().toString();
            push.setRemark(uuid);
            push.setCreateTime(new Date());
            if ("1".equals(pushNow)) {
                push.setPushTime(new Date());
            }
            pushSaved = pushService.save(push);
            Map<String, String> args = new HashMap<>();
            args.put("type", pushSaved.getType());
            args.put("id", String.valueOf(pushSaved.getRedirectId()));
            args.put("url", pushSaved.getUrl());
            if ("0".equals(pushNow) && push.getPushTime().compareTo(new Date()) > 0) {
                jPushService.createSingleSchedule(uuid, "2", pushSaved.getName(), pushSaved.getPushTime(), pushSaved.getName(), pushSaved.getContent(), args, users);
            } else {
                if (users != null) {
                    jPushService.sendPushUser(push.getName(), push.getContent(), args, users);
                } else {
                    jPushService.sendPush(push.getName(), push.getContent(), args);
                }
            }
        } else {
            pushSaved = pushService.findOne(push.getId());
            //已经推送过的不能编辑
            if ("0".equals(pushNow) && pushSaved.getPushTime().compareTo(new Date()) > 0) {
                pushSaved.setPushTime(push.getPushTime());//定时器只能编辑未来时间
                pushService.save(pushSaved);
                String uuid = pushSaved.getRemark();
                jPushService.updateScheduleTrigger(uuid, "2", pushSaved.getPushTime());
            } else if ("1".equals(pushNow)) {
                push.setId(pushSaved.getId());
                push.setPushTime(new Date());
                push.setCreateTime(pushSaved.getCreateTime());
                pushService.save(push);
                Map<String, String> args = new HashMap<>();
                args.put("type", push.getType());
                args.put("id", String.valueOf(push.getRedirectId()));
                args.put("url", push.getUrl());
                if (users != null) {
                    jPushService.sendPushUser(push.getName(), push.getContent(), args, users);
                } else {
                    jPushService.sendPush(push.getName(), push.getContent(), args);
                }
            } else {
                return false;
            }
        }
        return true;
    }

    @ResponseBody
    @RequestMapping("/push_get")
    public PhPush get(Long id) {
        return pushService.findOne(id);
    }

    @RequestMapping("/push_del")
    @ResponseBody
    public boolean delete(@RequestParam(name = "ids[]") Long[] ids) {
        for (Long id : ids) {
            PhPush push = pushService.findOne(id);
            jPushService.deleteSchedule(push.getRemark(), "2");
            pushService.del(push.getId());
        }
        return true;
    }
}
