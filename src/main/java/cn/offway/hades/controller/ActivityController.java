package cn.offway.hades.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.offway.hades.domain.PhActivityImage;
import cn.offway.hades.domain.PhActivityInfo;
import cn.offway.hades.properties.QiniuProperties;
import cn.offway.hades.service.PhActivityImageService;
import cn.offway.hades.service.PhActivityInfoService;
import cn.offway.hades.service.PhActivityPrizeService;

/**
 * 每日福利管理
 * @author wn
 *
 */
@Controller
public class ActivityController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private PhActivityInfoService phActivityInfoService;
	
	@Autowired
	private QiniuProperties qiniuProperties;
	
	@Autowired
	private PhActivityImageService phActivityImageService;
	
	@Autowired
	private PhActivityPrizeService phActivityPrizeService; 

	/**
	 * 每日福利
	 * @param map
	 * @return
	 */
	@RequestMapping("/activitys.html")
	public String activitys(ModelMap map){
		map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
		return "activitys";
	}
	
	/**
	 * 查询数据
	 * @param request
	 * @param code
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/activitys-data")
	public Map<String, Object> activitysData(HttpServletRequest request,String name){
		
		String sortCol = request.getParameter("iSortCol_0");
		String sortName = request.getParameter("mDataProp_"+sortCol);
		String sortDir = request.getParameter("sSortDir_0");
		int sEcho = Integer.parseInt(request.getParameter("sEcho"));
		int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
		int iDisplayLength  = Integer.parseInt(request.getParameter("iDisplayLength"));
		Page<PhActivityInfo> pages = phActivityInfoService.findByPage(name.trim(), new PageRequest(iDisplayStart==0?0:iDisplayStart/iDisplayLength, iDisplayLength<0?9999999:iDisplayLength,Direction.fromString(sortDir),sortName));
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
	@PostMapping("/activitys-save")
	public boolean save(PhActivityInfo phActivityInfo,String banner,String detail){
		try {
			phActivityInfoService.save(phActivityInfo, banner, detail);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存每日福利异常,{}",JSON.toJSONString(phActivityInfo),e);
			return false;
		}
		
	}
	
	@ResponseBody
	@PostMapping("/activitys-one")
	public PhActivityInfo findOne(Long id){
		return phActivityInfoService.findOne(id);
	}
	
	@PostMapping("/activitys-update")
	@ResponseBody
	public boolean activitysUpdate(@RequestParam("ids[]") Long[] ids,Long joinNum,String status){
		List<PhActivityInfo> phActivityInfos = phActivityInfoService.findAll(Arrays.asList(ids));
		for (PhActivityInfo phActivityInfo : phActivityInfos) {
			if(null!=joinNum){
				phActivityInfo.setJoinNum(joinNum);
			}
			if(StringUtils.isNotBlank(status)){
				phActivityInfo.setStatus(status);
			}
		}
		
		phActivityInfoService.save(phActivityInfos);
		return true;
	}
	
	@GetMapping("/activitys-images")
	@ResponseBody
	public List<PhActivityImage> images(Long id){
		return phActivityImageService.findByActivityId(id);
	}
	
	@PostMapping("/activitys-images-delete")
	@ResponseBody
	public boolean imagesDelete(Long activityImageId){
		return phActivityInfoService.imagesDelete(activityImageId);
		
	}
	
	@GetMapping("/activitys-notice/{activityId}")
	@ResponseBody
	public boolean activitysNotice(@PathVariable Long activityId){
		try {
			PhActivityInfo phActivityInfo = phActivityInfoService.findOne(activityId);
			if(null != phActivityInfo){
				String token = phActivityPrizeService.getToken();
				phActivityPrizeService.activityOpen(token, phActivityInfo);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("每日福利手动开奖异常,活动ID:{}",activityId,e);
			return false;
		}
		
	}
	
}
