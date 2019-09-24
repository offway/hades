package cn.offway.hades.controller;

import cn.offway.hades.domain.PhBanner;
import cn.offway.hades.domain.PhBrand;
import cn.offway.hades.properties.QiniuProperties;
import cn.offway.hades.service.PhBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping//("/banner")
public class BannerController {
    @Autowired
    private PhBannerService bannerService;

    @Autowired
    private QiniuProperties qiniuProperties;

    @RequestMapping("/banner.html")
    public String index(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        map.addAttribute("position", 0);
        return "banner_index";
    }

    @RequestMapping("/banner_alt.html")
    public String indexAlt(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        map.addAttribute("position", 1);
        return "banner_index";
    }

    @RequestMapping("/banner_ad.html")
    public String indexAd(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        map.addAttribute("position", 2);
        return "banner_index";
    }

    @RequestMapping("/banner_other.html")
    public String indexOther(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        map.addAttribute("position", 3);
        return "banner_index";
    }

    @RequestMapping("/banner_wx.html")
    public String indexWx(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        map.addAttribute("position", 4);
        return "banner_index";
    }

    @RequestMapping("/banner_list")
    @ResponseBody
    public Map<String, Object> getAll(HttpServletRequest request, String position,String id,String remark) {
        bannerService.updatetime();
        int sEcho = Integer.parseInt(request.getParameter("sEcho"));
        int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
        int iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "status"), new Sort.Order("sort"));
        Page<PhBanner> pages = bannerService.findAll(position,id,remark, new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, sort));
        int initEcho = sEcho + 1;
        Map<String, Object> map = new HashMap<>();
        map.put("sEcho", initEcho);
        map.put("iTotalRecords", pages.getTotalElements());//数据总条数
        map.put("iTotalDisplayRecords", pages.getTotalElements());//显示的条数
        map.put("aData", pages.getContent());//数据集合
        return map;
    }

    @RequestMapping("/banner_save")
    @ResponseBody
    public boolean save(PhBanner banner) {
        if ("".equals(banner.getUrl())) {
            banner.setUrl(null);
        }
        if (banner.getId() == null) {
            banner.setStatus("0");
            banner.setSort(null);
            banner.setCreateTime(new Date());
        }else{
            PhBanner bannernew = bannerService.findOne(banner.getId());
            banner.setStatus(bannernew.getStatus());
            banner.setSort(bannernew.getSort());
            banner.setCreateTime(bannernew.getCreateTime());
        }
        bannerService.save(banner);
        return true;
    }

    @RequestMapping("/banner_get")
    @ResponseBody
    public PhBanner get(@RequestParam("id") Long id) {
        return bannerService.findOne(id);
    }

    @RequestMapping("/banner_del")
    @ResponseBody
    public boolean delete(@RequestParam("ids[]") Long[] ids) {
        for (long id : ids) {
            bannerService.delete(id);
        }
        return true;
    }

    @ResponseBody
    @RequestMapping("/banner_top")
    public boolean top(Long id, Long sort, String position) {
        PhBanner banner = bannerService.findOne(id);
        if (banner != null) {
            bannerService.resort(position, sort);
            banner.setSort(sort);
            bannerService.save(banner);
        }
        return true;
    }

    @RequestMapping("/banner_up")
    @ResponseBody
    public boolean up(Long id, String position) {
        PhBanner banner = bannerService.findOne(id);
        long latest = bannerService.getMaxSort(position);
        banner.setStatus("1");
        banner.setSort(latest + 1);
        bannerService.save(banner);
        return true;
    }

    @RequestMapping("/banner_down")
    @ResponseBody
    public boolean down(Long id) {
        PhBanner banner = bannerService.findOne(id);
        banner.setStatus("0");
        banner.setSort(null);
        bannerService.save(banner);
        return true;
    }

    @ResponseBody
    @RequestMapping("/banner_same")
    public boolean same(){
        List<PhBanner> phBanners = bannerService.findAllByStatus();
        List<PhBanner> savebanner = new ArrayList<>();
        bannerService.deleteByPosition("4");
        for (PhBanner phBanner : phBanners) {
            PhBanner newbanner = new PhBanner();
            newbanner.setCreateTime(new Date());
            newbanner.setSort(phBanner.getSort());
            newbanner.setStatus(phBanner.getStatus());
            newbanner.setUrl(phBanner.getUrl());
            newbanner.setBanner(phBanner.getBanner());
            newbanner.setBeginTime(phBanner.getBeginTime());
            newbanner.setEndTime(phBanner.getEndTime());
            newbanner.setRedirectId(phBanner.getRedirectId());
            newbanner.setRemark(phBanner.getRemark());
            newbanner.setType(phBanner.getType());
            newbanner.setPosition("4");
            savebanner.add(newbanner);
        }
        bannerService.save(savebanner);
//        bannerService.downSame();
//        for (Long id : ids) {
//            PhBanner banner= bannerService.findOne(id);
//            PhBanner newbanner = new PhBanner();
//            newbanner.setCreateTime(new Date());
//            newbanner.setSort(banner.getSort());
//            newbanner.setStatus(banner.getStatus());
//            newbanner.setUrl(banner.getUrl());
//            newbanner.setBanner(banner.getBanner());
//            newbanner.setBeginTime(banner.getBeginTime());
//            newbanner.setEndTime(banner.getEndTime());
//            newbanner.setRedirectId(banner.getRedirectId());
//            newbanner.setRemark(banner.getRemark());
//            newbanner.setType(banner.getType());
//            newbanner.setPosition("4");
//            bannerService.save(newbanner);
//        }
        return true;
    }
}
