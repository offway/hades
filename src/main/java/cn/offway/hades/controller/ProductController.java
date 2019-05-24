package cn.offway.hades.controller;

import java.util.Arrays;
import java.util.Date;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.offway.hades.domain.PhProductInfo;
import cn.offway.hades.properties.QiniuProperties;
import cn.offway.hades.service.JPushService;
import cn.offway.hades.service.PhProductInfoService;
import cn.offway.hades.service.PhWinningRecordService;
import cn.offway.hades.utils.BitUtil;

/**
 * 活动管理
 * @author wn
 *
 */
@Controller
public class ProductController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private PhProductInfoService phProductInfoService;
	
	@Autowired
	private QiniuProperties qiniuProperties;
	
	@Autowired
	private JPushService jPushService;
	
	@Autowired
	private PhWinningRecordService phWinningRecordService;

	/**
	 * 活动
	 * @param map
	 * @return
	 */
	@RequestMapping("/products.html")
	public String products(ModelMap map){
		map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
		return "products";
	}
	
	/**
	 * 活动排序
	 * @param map
	 * @return
	 */
	@RequestMapping("/products-sort.html")
	public String productsSort(ModelMap map){
		return "products-sort";
	}
	
	/**
	 * 查询数据
	 * @param request
	 * @param code
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/products-sort-data")
	public Map<String, Object> productsSortData(HttpServletRequest request,String type,Long channel){
		
		int sEcho = Integer.parseInt(request.getParameter("sEcho"));
		int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
		int iDisplayLength  = Integer.parseInt(request.getParameter("iDisplayLength"));
		Page<PhProductInfo> pages = phProductInfoService.findByType(type,channel,new PageRequest(iDisplayStart==0?0:iDisplayStart/iDisplayLength, iDisplayLength<0?9999999:iDisplayLength));
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
	 * 查询数据
	 * @param request
	 * @param code
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/products-data")
	public Map<String, Object> productsData(HttpServletRequest request,String name,String type,String status,Long channel){
		
		String sortCol = request.getParameter("iSortCol_0");
		String sortName = request.getParameter("mDataProp_"+sortCol);
		String sortDir = request.getParameter("sSortDir_0");
		int sEcho = Integer.parseInt(request.getParameter("sEcho"));
		int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
		int iDisplayLength  = Integer.parseInt(request.getParameter("iDisplayLength"));
		Page<PhProductInfo> pages = phProductInfoService.findByPage(name, type, status,channel,new PageRequest(iDisplayStart==0?0:iDisplayStart/iDisplayLength, iDisplayLength<0?9999999:iDisplayLength,Direction.fromString(sortDir),sortName));
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
	@PostMapping("/products-save")
	public boolean save(PhProductInfo phProductInfo){
		try {
			phProductInfoService.saveProduct(phProductInfo);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存活动异常,{}",JSON.toJSONString(phProductInfo),e);
			return false;
		}
		
	}
	
	@ResponseBody
	@PostMapping("/products-one")
	public PhProductInfo findOne(Long id){
		return phProductInfoService.findOne(id);
	}
	
	/**
	 * 保存中奖记录
	 * @param productId
	 * @param codes
	 * @return
	 */
	@ResponseBody
	@PostMapping("/products-win")
	public boolean win(Long productId,String codes){
		int count = phWinningRecordService.saveWin(productId, Arrays.asList(codes.split(",")));
		return count>0;
	}
	
	/**
	 * 手动开奖通知
	 * @param productId
	 * @throws Exception
	 */
	@RequestMapping("/products-notice/{productId}")
	@ResponseBody
	public boolean notice(@PathVariable Long productId,String video,String codes) throws Exception {
		return phProductInfoService.notice(productId, video, codes);
	}
	
	@PostMapping("/products-rule")
	@ResponseBody
	public boolean productsRule(Long id,String ruleContent){
		PhProductInfo phProductInfo = phProductInfoService.findOne(id);
		phProductInfo.setRuleContent(ruleContent);
		phProductInfo.setAppRuleContent(ruleContent);
		phProductInfoService.save(phProductInfo);
		return true;
	}
	
	@PostMapping("/products-sort")
	@ResponseBody
	public boolean productsSort(Long id){
		phProductInfoService.updateSort(id);
		return true;
	}
	
	@PostMapping("/products-update")
	@ResponseBody
	public boolean productsUpdate(@RequestParam("ids[]") Long[] ids,String status){
		List<PhProductInfo> phProductInfos = phProductInfoService.findAll(Arrays.asList(ids));
		for (PhProductInfo phProductInfo : phProductInfos) {
			if(StringUtils.isNotBlank(status)){
				if(BitUtil.has(phProductInfo.getChannel().intValue(), BitUtil.APP)){
					if(!phProductInfo.getStatus().equals(status)&& phProductInfo.getBeginTime().after(new Date())){
						if(status.equals("1")){
							Map<String, String> extras = new HashMap<>();
							extras.put("type", "6");//0-H5,1-精选文章,2-活动
							extras.put("id", null);
							extras.put("url", null);
							jPushService.createSingleSchedule(String.valueOf(phProductInfo.getId()), "0", "抽奖通知", phProductInfo.getBeginTime(), "抽奖通知", "【免费送】"+phProductInfo.getShareDesc(), extras);
						}else if(status.equals("0")){
							jPushService.deleteSchedule(String.valueOf(phProductInfo.getId()), "0");
						}
					}
				}
				phProductInfo.setStatus(status);
			}
		}
		
		phProductInfoService.save(phProductInfos);
		return true;
	}
	
}
