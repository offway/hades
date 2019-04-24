package cn.offway.hades.controller;

import cn.offway.hades.domain.PhAdmin;
import cn.offway.hades.domain.PhLotteryTicket;
import cn.offway.hades.domain.PhMerchant;
import cn.offway.hades.domain.PhResource;
import cn.offway.hades.service.PhLotteryTicketService;
import cn.offway.hades.service.PhMerchantService;
import cn.offway.hades.service.PhRoleadminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping
public class IndexController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhLotteryTicketService phLotteryTicketService;
	@Autowired
	private PhRoleadminService roleadminService;
	@Autowired
	private PhMerchantService merchantService;

	/**
	 * 登录页面
	 * @return
	 */
	@RequestMapping("/login.html")
	public String login(){
		return "login";
	}
	
	
	
	
	@RequestMapping("/resoures")
	@ResponseBody
	public List<PhResource> resoures(@AuthenticationPrincipal PhAdmin admin){
		return admin.getResources();
	}


	/**
	 * 首页
	 */
	@RequestMapping("/")
	public String index(@AuthenticationPrincipal PhAdmin admin, ModelMap map) {
		List<Long> roles = roleadminService.findRoleIdByAdminId(admin.getId());
		if (roles.contains(BigInteger.valueOf(8L))) {
			PhMerchant merchant = merchantService.findByAdminId(admin.getId());
			map.addAttribute("theId", merchant.getId());
			return "new_home";
		} else {
			return "home";
		}
	}
	
	/**
	 * 活动-抽奖
	 * @return
	 */
	@RequestMapping("/activity-lottery.html")
	public String activityLottery(){
		return "activity-lottery";
	}
	
	
	/**
	 * 查询数据
	 * @param request
	 * @param code
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/activity-lottery-data")
	public Map<String, Object> activityLotteryData(HttpServletRequest request,String code,Long productId,String nickName,String unionid){
		
		String sortCol = request.getParameter("iSortCol_0");
		String sortName = request.getParameter("mDataProp_"+sortCol);
		String sortDir = request.getParameter("sSortDir_0");
		int sEcho = Integer.parseInt(request.getParameter("sEcho"));
		int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
		int iDisplayLength  = Integer.parseInt(request.getParameter("iDisplayLength"));
		Page<PhLotteryTicket> pages = phLotteryTicketService.findByPage(code,productId,nickName.trim(),unionid.trim(), new PageRequest(iDisplayStart==0?0:iDisplayStart/iDisplayLength, iDisplayLength<0?9999999:iDisplayLength,Direction.fromString(sortDir),sortName));
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
