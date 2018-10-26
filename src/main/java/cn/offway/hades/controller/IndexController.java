package cn.offway.hades.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.offway.hades.domain.PhLotteryTicket;
import cn.offway.hades.service.PhLotteryTicketService;

@Controller
@RequestMapping
public class IndexController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhLotteryTicketService phLotteryTicketService;

	/**
	 * 登录页面
	 * @return
	 */
	@RequestMapping("/login")
	public String login(){
		return "login";
	}
	
	/**
	 * 登录验证
	 * @param userName
	 * @param password
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/login/submit")
	public boolean loginSubmit(String userName,String password,HttpSession session){
		if("admin".equals(userName) && password.equals("123456")){
			session.setAttribute("user", userName);
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 登出
	 * @return
	 */
	@RequestMapping("/logout")
	public String logout(HttpSession session){
		session.removeAttribute("user");
		return "redirect:/login";
	}
	
	
	
	/**
	 * 首页
	 * @return
	 */
	@RequestMapping("/")
	public String index(HttpSession session){
		if(null==session.getAttribute("user")){
			return "redirect:/login";
		}
		return "home";
	}
	
	/**
	 * 活动-抽奖
	 * @return
	 */
	@RequestMapping("/activity-lottery")
	public String activityLottery(HttpSession session){
		if(null==session.getAttribute("user")){
			return "redirect:/login";
		}
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
	public Map<String, Object> activityLotteryData(HttpServletRequest request,String code){
		
		String sortCol = request.getParameter("iSortCol_0");
		String sortName = request.getParameter("mDataProp_"+sortCol);
		String sortDir = request.getParameter("sSortDir_0");
		int sEcho = Integer.parseInt(request.getParameter("sEcho"));
		int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
		int iDisplayLength  = Integer.parseInt(request.getParameter("iDisplayLength"));
		Page<PhLotteryTicket> pages = phLotteryTicketService.findByPage(code, new PageRequest(iDisplayStart==0?0:iDisplayStart/iDisplayLength, iDisplayLength<0?9999999:iDisplayLength,Direction.fromString(sortDir),sortName));
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
