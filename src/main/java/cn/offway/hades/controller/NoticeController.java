package cn.offway.hades.controller;

import cn.offway.hades.domain.PhNotice;
import cn.offway.hades.properties.QiniuProperties;
import cn.offway.hades.service.PhNoticeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping
public class NoticeController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private PhNoticeService noticeService;
    @Autowired
    private QiniuProperties qiniuProperties;

    @RequestMapping("/notice.html")
    public String index(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        return "notice_index";
    }

    @ResponseBody
    @RequestMapping("/notice_list")
    public Map<String, Object> getList(HttpServletRequest request, int sEcho, int iDisplayStart, int iDisplayLength) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "id"));
        Page<PhNotice> pages = noticeService.findAll(new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, sort));
        int initEcho = sEcho + 1;
        return echoBody(initEcho, pages.getTotalElements(), pages.getTotalElements(), pages.getContent());
    }

    private Map<String, Object> echoBody(int initEcho, long iTotalRecords, long iTotalDisplayRecords, List<?> data) {
        Map<String, Object> map = new HashMap<>();
        map.put("sEcho", initEcho);
        map.put("iTotalRecords", iTotalRecords);//数据总条数
        map.put("iTotalDisplayRecords", iTotalDisplayRecords);//显示的条数
        map.put("aData", data);//数据集合
        return map;
    }

    @ResponseBody
    @RequestMapping("/notice_find")
    public PhNotice find(Long id) {
        return noticeService.findOne(id);
    }

    @ResponseBody
    @Transactional
    @RequestMapping("/notice_del")
    public boolean delete(@RequestParam("ids[]") Long[] ids) {
        for (Long id : ids) {
            noticeService.del(id);
        }
        return true;
    }

    @ResponseBody
    @RequestMapping("/notice_save")
    public boolean save(PhNotice notice) {
        if (notice.getId() == null) {
            notice.setCreateTime(new Date());
            notice.setIsRead("0");
        }
        noticeService.save(notice);
        return true;
    }
}
