package cn.offway.hades.controller;

import cn.offway.hades.domain.PhCelebrityList;
import cn.offway.hades.properties.QiniuProperties;
import cn.offway.hades.service.PhCelebrityListService;
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
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping
public class StarManagerController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private PhCelebrityListService celebrityListService;
    @Autowired
    private QiniuProperties qiniuProperties;

    @RequestMapping("/starList.html")
    public String list(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        return "starList_index";
    }

    @ResponseBody
    @RequestMapping("/starList_list")
    public Map<String, Object> usersData(HttpServletRequest request, int sEcho, int iDisplayStart, int iDisplayLength, String name) {
        String sortCol = request.getParameter("iSortCol_0");
        String sortName = request.getParameter("mDataProp_" + sortCol);
        String sortDir = request.getParameter("sSortDir_0");
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        PageRequest pr = new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, sort);
        Page<PhCelebrityList> pages = celebrityListService.list(name, pr);
        // 为操作次数加1，必须这样做
        int initEcho = sEcho + 1;
        Map<String, Object> map = new HashMap<>();
        map.put("sEcho", initEcho);
        map.put("iTotalRecords", pages.getTotalElements());//数据总条数
        map.put("iTotalDisplayRecords", pages.getTotalElements());//显示的条数
        map.put("aData", pages.getContent());//数据集合
        return map;
    }

    @RequestMapping("/starList_del")
    @ResponseBody
    @Transactional
    public boolean delete(@RequestParam("ids[]") Long[] ids) {
        for (long id : ids) {
            celebrityListService.delete(id);
        }
        return true;
    }

    @RequestMapping("/starList_save")
    @ResponseBody
    public boolean save(PhCelebrityList star) {
        celebrityListService.save(star);
        return true;
    }

    @RequestMapping("/starList_get")
    @ResponseBody
    public PhCelebrityList findOne(long id) {
        return celebrityListService.findOne(id);
    }
}