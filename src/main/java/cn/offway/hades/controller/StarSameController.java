package cn.offway.hades.controller;

import cn.offway.hades.domain.PhStarsame;
import cn.offway.hades.properties.QiniuProperties;
import cn.offway.hades.repository.PhStarsameGoodsRepository;
import cn.offway.hades.repository.PhStarsameImageRepository;
import cn.offway.hades.repository.PhStarsameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping
public class StarSameController {
    @Autowired
    private PhStarsameRepository starsameRepository;
    @Autowired
    private PhStarsameGoodsRepository starsameGoodsRepository;
    @Autowired
    private PhStarsameImageRepository starsameImageRepository;
    @Autowired
    private QiniuProperties qiniuProperties;

    @RequestMapping("/starSame.html")
    public String index(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        return "starSame_index";
    }

    @ResponseBody
    @RequestMapping("/starSame_list")
    public Map<String, Object> getList(HttpServletRequest request) {
        int sEcho = Integer.parseInt(request.getParameter("sEcho"));
        int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
        int iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
        Page<PhStarsame> pages = starsameRepository.findAll(new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength));
        int initEcho = sEcho + 1;
        Map<String, Object> map = new HashMap<>();
        map.put("sEcho", initEcho);
        map.put("iTotalRecords", pages.getTotalElements());//数据总条数
        map.put("iTotalDisplayRecords", pages.getTotalElements());//显示的条数
        map.put("aData", pages.getContent());//数据集合
        return map;
    }

    @ResponseBody
    @RequestMapping("/starSame_del")
    public boolean delete(@RequestParam("ids[]") Long[] ids) {
        for (Long id : ids) {
            starsameRepository.delete(id);
            starsameGoodsRepository.deleteByPid(id);
            starsameImageRepository.deleteByPid(id);
        }
        return true;
    }
}
