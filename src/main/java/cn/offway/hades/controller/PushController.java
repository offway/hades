package cn.offway.hades.controller;

import cn.offway.hades.domain.PhPush;
import cn.offway.hades.properties.QiniuProperties;
import cn.offway.hades.service.JPushService;
import cn.offway.hades.service.PhPushService;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        int sEcho = Integer.parseInt(request.getParameter("sEcho"));
        int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
        int iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
        //time
        String sTimeStr = request.getParameter("sTime");
        String eTimeStr = request.getParameter("eTime");
        Date sTime = null, eTime = null;
        if (!"".equals(sTimeStr) && !"".equals(eTimeStr)) {
            DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            sTime = DateTime.parse(sTimeStr, format).toDate();
            eTime = DateTime.parse(eTimeStr, format).toDate();
        }
        String name = request.getParameter("name");
        String content = request.getParameter("content");
        String type = request.getParameter("type");
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Page<PhPush> pages = pushService.list(name, content, type, sTime, eTime, new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, sort));
        // 为操作次数加1，必须这样做
        int initEcho = sEcho + 1;
        Map<String, Object> map = new HashMap<>();
        map.put("sEcho", initEcho);
        map.put("iTotalRecords", pages.getTotalElements());//数据总条数
        map.put("iTotalDisplayRecords", pages.getTotalElements());//显示的条数
        map.put("aData", pages.getContent());//数据集合
        return map;
    }

    private String purgeString(String str) {
        String regs = "([^\\u4e00-\\u9fa5\\w])+?";
        Pattern pattern = Pattern.compile(regs);
        Matcher matcher = pattern.matcher(str);
        return matcher.replaceAll("");
    }

    @ResponseBody
    @RequestMapping("/push_save")
    public boolean save(PhPush push, String pushAll, String userIdStr, String pushNow, @RequestParam("excelFile") MultipartFile excelFile) throws IOException {
        PhPush pushSaved;
        String[] users = null;
        ArrayList<String> tmpArr = new ArrayList<String>();
        if ("1".equals(pushAll) && !"".equals(userIdStr.trim())) {
            users = userIdStr.split(",");
        } else if ("2".equals(pushAll) && excelFile != null) {
            InputStream buffered = new BufferedInputStream(excelFile.getInputStream());
            ExcelReader reader = new ExcelReader(buffered, "", new AnalysisEventListener<ArrayList>() {

                @Override
                public void invoke(ArrayList data, AnalysisContext analysisContext) {
                    String s = String.valueOf(data.get(0));
                    if (NumberUtils.isNumber(s)) {
                        tmpArr.add(s);
                    }
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                    //
                }
            });
            reader.read();
            String[] tpl = new String[tmpArr.size()];
            users = tmpArr.toArray(tpl);
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
                return jPushService.createSingleSchedule(uuid, "2", purgeString(pushSaved.getName()), pushSaved.getPushTime(), pushSaved.getName(), pushSaved.getContent(), args, users);
            } else {
                if (users != null) {
                    logger.info(StringUtils.join(users, ","));
                    return jPushService.sendPushUser(push.getName(), push.getContent(), args, users);
                } else {
                    return jPushService.sendPush(push.getName(), push.getContent(), args);
                }
            }
        } else {
            pushSaved = pushService.findOne(push.getId());
            //已经推送过的不能编辑
            if ("0".equals(pushNow) && pushSaved.getPushTime().compareTo(new Date()) > 0) {
                pushSaved.setPushTime(push.getPushTime());//定时器只能编辑未来时间
                pushService.save(pushSaved);
                String uuid = pushSaved.getRemark();
                return jPushService.updateScheduleTrigger(uuid, "2", pushSaved.getPushTime());
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
                    return jPushService.sendPushUser(push.getName(), push.getContent(), args, users);
                } else {
                    return jPushService.sendPush(push.getName(), push.getContent(), args);
                }
            } else {
                return false;
            }
        }
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
