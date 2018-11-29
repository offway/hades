package cn.offway.hades.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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
import cn.offway.hades.domain.PhActivityPrize;
import cn.offway.hades.properties.QiniuProperties;
import cn.offway.hades.service.PhActivityBlacklistService;
import cn.offway.hades.service.PhActivityPrizeService;

/**
 * 中奖用户管理
 * @author wn
 *
 */
@Controller
public class ActivityPrizeController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private PhActivityPrizeService phActivityPrizeService;
	
	@Autowired
	private QiniuProperties qiniuProperties;
	
	@Autowired
	private PhActivityBlacklistService phActivityBlacklistService;

	/**
	 * 中奖用户
	 * @param map
	 * @return
	 */
	@RequestMapping("/activityPrizes.html")
	public String activityPrizes(ModelMap map){
		map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
		return "activityPrizes";
	}
	
	/**
	 * 查询数据
	 * @param request
	 * @param code
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/activityPrizes-data")
	public Map<String, Object> activityPrizesData(HttpServletRequest request,String activityName, String nickName, String unionid){
		
		String sortCol = request.getParameter("iSortCol_0");
		String sortName = request.getParameter("mDataProp_"+sortCol);
		String sortDir = request.getParameter("sSortDir_0");
		int sEcho = Integer.parseInt(request.getParameter("sEcho"));
		int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
		int iDisplayLength  = Integer.parseInt(request.getParameter("iDisplayLength"));
		Page<PhActivityPrize> pages = phActivityPrizeService.findByPage(activityName,nickName,unionid, new PageRequest(iDisplayStart==0?0:iDisplayStart/iDisplayLength, iDisplayLength<0?9999999:iDisplayLength,Direction.fromString(sortDir),sortName));
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
	@PostMapping("/activityPrizes-save")
	public boolean save(PhActivityPrize phActivityPrize){
		try {
//			phActivityPrizeService.saveProduct(phActivityPrize);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存中奖用户异常,{}",JSON.toJSONString(phActivityPrize),e);
			return false;
		}
		
	}
	
	@ResponseBody
	@PostMapping("/activityPrizes-one")
	public PhActivityPrize findOne(Long id){
		return phActivityPrizeService.findOne(id);
	}
	
	@PostMapping("/activityPrizes-update")
	@ResponseBody
	public boolean activityPrizesRule(Long id,String expressNumber){
		PhActivityPrize phActivityPrize = phActivityPrizeService.findOne(id);
		if(StringUtils.isNotBlank(expressNumber)){
			phActivityPrize.setExpressNumber(expressNumber);
		}
		phActivityPrizeService.save(phActivityPrize);
		return true;
	}
	
	/**
	 * 加入黑名单
	 * @param id
	 * @return
	 */
	@PostMapping("/activityPrizes-black")
	@ResponseBody
	public boolean activityPrizesBlack(Long id){
		PhActivityPrize phActivityPrize = phActivityPrizeService.findOne(id);
		int count = phActivityBlacklistService.countByUnionid(phActivityPrize.getUnionid());
		if(count==0){
			PhActivityBlacklist blacklist = new PhActivityBlacklist();
			blacklist.setCreateTime(new Date());
			blacklist.setHeadUrl(phActivityPrize.getHeadUrl());
			blacklist.setNickName(phActivityPrize.getNickName());
			blacklist.setUnionid(phActivityPrize.getUnionid());
			phActivityBlacklistService.save(blacklist);
		}
		return true;
	}
	
}
