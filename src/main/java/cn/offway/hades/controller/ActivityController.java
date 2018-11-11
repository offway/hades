package cn.offway.hades.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import cn.offway.hades.service.PhLotteryTicketService;

@RestController
public class ActivityController {

	@Autowired
	private PhLotteryTicketService phLotteryTicketService;
	
	@GetMapping("/notice/{productId}")
	public void notice(@PathVariable Long productId) {
		phLotteryTicketService.notice(productId);
		
	}

}
