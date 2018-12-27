package cn.offway.hades.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.offway.hades.domain.PhLotteryTicket;
import cn.offway.hades.dto.VTicketCount;
import cn.offway.hades.properties.QiniuProperties;
import cn.offway.hades.service.PhLotteryTicketService;

@Controller
public class LotteryTicketController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private PhLotteryTicketService phLotteryTicketService;
	
	@Autowired
	private QiniuProperties qiniuProperties;
	
	@RequestMapping("/ticket-sort.html")
	public String ticketSort(ModelMap map){
		map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
		return "ticket-sort";
	}
	
	/**
	 * 查询数据
	 * @param request
	 * @param code
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/ticket-sort-data")
	public Map<String, Object> ticketSortData(HttpServletRequest request,Long productId,String nickName,String unionid){
		
		int sEcho = Integer.parseInt(request.getParameter("sEcho"));
		int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
		int iDisplayLength  = Integer.parseInt(request.getParameter("iDisplayLength"));
		Page<VTicketCount> pages =  phLotteryTicketService.findVTicketCount(productId,nickName.trim(),unionid.trim(), iDisplayStart==0?0:iDisplayStart/iDisplayLength, iDisplayLength<0?9999999:iDisplayLength);
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
	@RequestMapping("/ticket-save")
	public boolean ticketSave(Long productId,String unionid,String nickName,String headUrl){
		return phLotteryTicketService.ticketSave(productId, unionid, nickName, headUrl);
	}
	
	@ResponseBody
	@RequestMapping("/ticket-check")
	public boolean checkCodes(Long productId, String codes){
		return phLotteryTicketService.checkCodes(productId, Arrays.asList(codes.split("\n")));
	}
	
}
