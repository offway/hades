package cn.offway.hades.controller;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import cn.offway.hades.domain.PhRole;
import cn.offway.hades.service.PhRoleService;
import cn.offway.hades.service.PhRoleresourceService;

/**
 * 角色管理
 * @author wn
 *
 */
@Controller
public class RoleController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private PhRoleService phRoleService;
	
	@Autowired
	private PhRoleresourceService phRoleresourceService;

	@RequestMapping("/roles.html")
	public String roles(){
		return "roles";
	}
	
	/**
	 * 查询数据
	 * @param request
	 * @param code
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/roles-data")
	public Map<String, Object> rolesData(HttpServletRequest request,String name){
		
		String sortCol = request.getParameter("iSortCol_0");
		String sortName = request.getParameter("mDataProp_"+sortCol);
		String sortDir = request.getParameter("sSortDir_0");
		int sEcho = Integer.parseInt(request.getParameter("sEcho"));
		int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
		int iDisplayLength  = Integer.parseInt(request.getParameter("iDisplayLength"));
		Page<PhRole> pages = phRoleService.findByPage(name, new PageRequest(iDisplayStart==0?0:iDisplayStart/iDisplayLength, iDisplayLength<0?9999999:iDisplayLength,Direction.fromString(sortDir),sortName));
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
	 * 删除用户
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@PostMapping("/roles-delete")
	public boolean delete(String ids){
		try {
			phRoleService.deleteRole(ids);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除用户异常,用户id:{}",ids,e);
			return false;
		}
	}
	
	@ResponseBody
	@PostMapping("/roles-save")
	public boolean save(PhRole phRole,@RequestParam(required = false, value="resources[]") Long[] resources){
		try {
			phRoleService.save(phRole, resources);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除用户异常,phRole:{},resources:{}",JSON.toJSONString(phRole,SerializerFeature.WriteMapNullValue),resources,e);
			return false;
		}
	}
	
	@ResponseBody
	@GetMapping("/roles-one")
	public Map<String, Object> rolesOne(Long id){
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("role", phRoleService.findOne(id));
		resultMap.put("resourceIds", phRoleresourceService.findSourceIdByRoleId(id));
		return resultMap;
	}
}
