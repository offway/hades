package cn.offway.hades.controller;

import cn.offway.hades.domain.PhGoods;
import cn.offway.hades.domain.PhStarsame;
import cn.offway.hades.domain.PhStarsameGoods;
import cn.offway.hades.domain.PhStarsameImage;
import cn.offway.hades.properties.QiniuProperties;
import cn.offway.hades.service.PhGoodsService;
import cn.offway.hades.service.PhStarsameGoodsService;
import cn.offway.hades.service.PhStarsameImageService;
import cn.offway.hades.service.PhStarsameService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qiniu.util.Base64;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping
public class StarSameController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private PhStarsameService starsameService;
    @Autowired
    private PhStarsameGoodsService starsameGoodsService;
    @Autowired
    private PhStarsameImageService starsameImageService;
    @Autowired
    private PhGoodsService goodsService;
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
        Sort sort = new Sort("sort");
        Page<PhStarsame> pages = starsameService.findAll(new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, sort));
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
            starsameService.delete(id);
            starsameGoodsService.deleteByPid(id);
            starsameImageService.deleteByPid(id);
        }
        return true;
    }

    @ResponseBody
    @RequestMapping("/starSame_find")
    public Map<String, Object> find(Long id) {
        PhStarsame starsame = starsameService.findOne(id);
        Map<String, Object> map = new HashMap<>();
        if (starsame != null) {
            List<PhStarsameGoods> goodsList = starsameGoodsService.findAllByPid(starsame.getId());
            List<PhStarsameImage> imageList = starsameImageService.findAllByPid(starsame.getId());
            map.put("main", starsame);
            map.put("goodsList", goodsList);
            map.put("imageList", imageList);
        }
        return map;
    }

    @ResponseBody
    @RequestMapping("/starSame_top")
    public boolean top(Long id) {
        PhStarsame starsame = starsameService.findOne(id);
        if (starsame != null) {
            starsameService.resort(0L);
            starsame.setSort(0L);
            starsameService.save(starsame);
        }
        return true;
    }

    @ResponseBody
    @PostMapping("/starSame_save")
    public boolean save(PhStarsame starsame, String goodsIDStr, String imagesJSONStr) {
        starsame.setCreateTime(new Date());
        PhStarsame starsameObj = starsameService.save(starsame);
        //purge first
        starsameGoodsService.deleteByPid(starsameObj.getId());
        String[] goodsList = goodsIDStr.split(",");
        for (String gid : goodsList) {
            if (NumberUtils.isNumber(gid)) {
                PhGoods goods = goodsService.findOne(Long.valueOf(gid));
                if (goods != null) {
                    PhStarsameGoods starsameGoods = new PhStarsameGoods();
                    starsameGoods.setStarsameId(starsameObj.getId());
                    starsameGoods.setStarsameTitle(starsameObj.getTitle());
                    starsameGoods.setGoodsId(goods.getId());
                    starsameGoods.setGoodsName(goods.getName());
                    starsameGoods.setGoodsImage(goods.getImage());
                    starsameGoods.setBrandId(goods.getBrandId());
                    starsameGoods.setBrandName(goods.getBrandName());
                    starsameGoods.setBrandLogo(goods.getBrandLogo());
                    starsameGoods.setRemark(goods.getRemark());
                    starsameGoods.setCreateTime(new Date());
                    starsameGoodsService.save(starsameGoods);
                } else {
                    logger.error("goods Id 非法");
                }
            } else {
                logger.error("goods Id 非法");
            }
        }
        //purge first
        starsameImageService.deleteByPid(starsameObj.getId());
        String text = new String(Base64.decode(imagesJSONStr.getBytes(), Base64.DEFAULT));
        JSONArray jsonArray = JSON.parseArray(text);
        if (jsonArray != null) {
            for (Object o : jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                PhStarsameImage starsameImage = new PhStarsameImage();
                starsameImage.setStarsameId(starsameObj.getId());
                starsameImage.setStarsameTitle(starsameObj.getTitle());
                starsameImage.setImageUrl(jsonObject.getString("url"));
                starsameImage.setRemark(jsonObject.getString("remark"));
                starsameImage.setSort(jsonObject.getLong("sort"));
                starsameImage.setCreateTime(new Date());
                starsameImageService.save(starsameImage);
            }
        } else {
            logger.error("images json 非法");
        }
        return true;
    }
}
