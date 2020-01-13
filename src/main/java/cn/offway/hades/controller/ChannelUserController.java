package cn.offway.hades.controller;

import cn.offway.hades.domain.PhChannelUser;
import cn.offway.hades.domain.PhGoodsType;
import cn.offway.hades.domain.PhUserInfo;
import cn.offway.hades.service.PhChannelUserService;
import cn.offway.hades.service.PhUserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping
public class ChannelUserController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private PhChannelUserService phChannelUserService;
    @Autowired
    private PhUserInfoService phUserInfoService;


    @RequestMapping("/channelhtml.html")
    public String list() {
        return "channel_index";
    }

    @ResponseBody
    @RequestMapping("/channel_User_list")
    public Map<String, Object> usersData(HttpServletRequest request, String channelName, String channel, String userId, String userPhone) {
        String sortCol = request.getParameter("iSortCol_0");
        String sortName = request.getParameter("mDataProp_" + sortCol);
        String sortDir = request.getParameter("sSortDir_0");
        int sEcho = Integer.parseInt(request.getParameter("sEcho"));
        int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
        int iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
        Sort sort = new Sort("id");
        Page<PhChannelUser> pages = phChannelUserService.list(channelName, channel, userId, userPhone, new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, sort));

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
    @RequestMapping("/channel_user_save")
    public boolean save(PhChannelUser phChannelUser) {
        if (null != phChannelUser){
            PhUserInfo userInfo = phUserInfoService.findOne(phChannelUser.getUserId());
            if (null != userInfo){
                PhChannelUser channelUser = phChannelUserService.findByUserId(phChannelUser.getUserId());
                if (channelUser != null && phChannelUser.getUserId()== null){
                    return false;
                }
                if(userInfo.getHeadimgurl() != null){
                    phChannelUser.setUserHeadimgurl(userInfo.getHeadimgurl());
                }
                phChannelUser.setUserPhone(userInfo.getPhone());
                phChannelUser.setUserName(userInfo.getNickname());
                phChannelUser.setCreateTime(new Date());
                phChannelUser.setNumber(0L);
                phChannelUserService.save(phChannelUser);
            }
            return true;
        }
        return false;
    }


    @ResponseBody
    @RequestMapping("/channel_user_get")
    public PhChannelUser get(Long id) {
        return phChannelUserService.findOne(id);
    }

    @ResponseBody
    @RequestMapping("/channel_user_del")
    public boolean del(@RequestParam("ids[]")Long[] ids) {
        for (Long aLong : ids) {
            phChannelUserService.delete(aLong);
        }
        return true;
    }

    @ResponseBody
    @RequestMapping("/channel_user_info")
    public Map<String, Object> channelUsersData(HttpServletRequest request, String channel) {
        String sortCol = request.getParameter("iSortCol_0");
        String sortName = request.getParameter("mDataProp_" + sortCol);
        String sortDir = request.getParameter("sSortDir_0");
        int sEcho = Integer.parseInt(request.getParameter("sEcho"));
        int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
        int iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
        Sort sort = new Sort("id");
        Page<PhUserInfo> pages = phUserInfoService.findAllByPage(channel, new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, sort));
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
