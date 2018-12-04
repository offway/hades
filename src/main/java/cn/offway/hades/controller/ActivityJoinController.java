package cn.offway.hades.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.offway.hades.domain.PhActivityBlacklist;
import cn.offway.hades.domain.PhActivityJoin;
import cn.offway.hades.properties.QiniuProperties;
import cn.offway.hades.service.PhActivityBlacklistService;
import cn.offway.hades.service.PhActivityJoinService;

/**
 * 每日福利-参与用户管理
 * @author wn
 *
 */
@Controller
public class ActivityJoinController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private PhActivityJoinService phActivityJoinService;
	
	@Autowired
	private QiniuProperties qiniuProperties;
	
	@Autowired
	private PhActivityBlacklistService phActivityBlacklistService;

	/**
	 * 每日福利-参与用户
	 * @param map
	 * @return
	 */
	@RequestMapping("/activityJoins.html")
	public String activityJoins(ModelMap map){
		map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
		return "activityJoins";
	}
	
	/**
	 * 查询数据
	 * @param request
	 * @param code
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/activityJoins-data")
	public Map<String, Object> activityJoinsData(HttpServletRequest request,String activityName,String nickName,String unionid,Long activityId){
		
		String sortCol = request.getParameter("iSortCol_0");
		String sortName = request.getParameter("mDataProp_"+sortCol);
		String sortDir = request.getParameter("sSortDir_0");
		int sEcho = Integer.parseInt(request.getParameter("sEcho"));
		int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
		int iDisplayLength  = Integer.parseInt(request.getParameter("iDisplayLength"));
		Page<PhActivityJoin> pages = phActivityJoinService.findByPage(activityName.trim(),nickName.trim(),unionid.trim(),activityId, new PageRequest(iDisplayStart==0?0:iDisplayStart/iDisplayLength, iDisplayLength<0?9999999:iDisplayLength,Direction.fromString(sortDir),sortName));
		 // 为操作次数加1，必须这样做  
        int initEcho = sEcho + 1;  
        Map<String, Object> map = new HashMap<>();
		map.put("sEcho", initEcho);  
        map.put("iTotalRecords", pages.getTotalElements());//数据总条数  
        map.put("iTotalDisplayRecords", pages.getTotalElements());//显示的条数  
        map.put("aData", pages.getContent());//数据集合 
		return map;
	}
	
	/**
	 * 加入黑名单
	 * @param id
	 * @return
	 */
	@PostMapping("/activityJoins-black")
	@ResponseBody
	public boolean activityJoinsBlack(Long id){
		PhActivityJoin phActivityJoin = phActivityJoinService.findOne(id);
		int count = phActivityBlacklistService.countByUnionid(phActivityJoin.getUnionid());
		if(count==0){
			PhActivityBlacklist blacklist = new PhActivityBlacklist();
			blacklist.setCreateTime(new Date());
			blacklist.setHeadUrl(phActivityJoin.getHeadUrl());
			blacklist.setNickName(phActivityJoin.getNickName());
			blacklist.setUnionid(phActivityJoin.getUnionid());
			phActivityBlacklistService.save(blacklist);
		}
		return true;
	}
	
	
}
