package cn.offway.hades.controller;

import cn.offway.hades.domain.PhBrand;
import cn.offway.hades.domain.PhGoods;
import cn.offway.hades.domain.PhPick;
import cn.offway.hades.domain.PhPickGoods;
import cn.offway.hades.properties.QiniuProperties;
import cn.offway.hades.service.PhBrandService;
import cn.offway.hades.service.PhGoodsService;
import cn.offway.hades.service.PhPickGoodsService;
import cn.offway.hades.service.PhPickService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping
public class PickController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private QiniuProperties qiniuProperties;
    @Autowired
    private PhPickService pickService;
    @Autowired
    private PhPickGoodsService pickGoodsService;
    @Autowired
    private PhGoodsService goodsService;
    @Autowired
    private PhBrandService brandService;

    @RequestMapping("/pick.html")
    public String index(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        return "pick_index";
    }

    @ResponseBody
    @RequestMapping("/pick_list")
    public Map<String, Object> usersData(HttpServletRequest request) {
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        ObjectMapper objectMapper = new ObjectMapper();
        String sortCol = request.getParameter("iSortCol_0");
        String sortName = request.getParameter("mDataProp_" + sortCol);
        String sortDir = request.getParameter("sSortDir_0");
        int sEcho = Integer.parseInt(request.getParameter("sEcho"));
        int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
        int iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
        //time
        String sTimeStr = request.getParameter("sTime");
        String eTimeStr = request.getParameter("eTime");
        Date sTime = null, eTime = null;
        if (!"".equals(sTimeStr) && !"".equals(eTimeStr)) {
            sTime = DateTime.parse(sTimeStr, format).toDate();
            eTime = DateTime.parse(eTimeStr, format).toDate();
        }
        String id = request.getParameter("id");
        id = "".trim().equals(id) ? "0" : id;
        String name = request.getParameter("name");
        Page<PhPick> pages = pickService.list(Long.valueOf(id), name, sTime, eTime, new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, Sort.Direction.fromString(sortDir), sortName));
        // 为操作次数加1，必须这样做
        int initEcho = sEcho + 1;
        Map<String, Object> map = new HashMap<>();
        map.put("sEcho", initEcho);
        map.put("iTotalRecords", pages.getTotalElements());//数据总条数
        map.put("iTotalDisplayRecords", pages.getTotalElements());//显示的条数
        List<Map> list = new ArrayList<>();
        for (PhPick item : pages.getContent()) {
            Map m = objectMapper.convertValue(item, Map.class);
            List<PhPickGoods> l = pickGoodsService.findByPid(item.getId());
            List<PhGoods> goodsList = new ArrayList<>();
            for (PhPickGoods i : l) {
                goodsList.add(goodsService.findOne(i.getGoodsId()));
            }
            m.put("sub", goodsList);
            list.add(m);
        }
        map.put("aData", list);//数据集合
        return map;
    }

    @RequestMapping("/pick_getBrand")
    @ResponseBody
    public List<PhBrand> getBrandList(String prefix) {
        return brandService.findAll(prefix);
    }

    @RequestMapping("/pick_getGoods")
    @ResponseBody
    public List<PhGoods> getGoodsList(int type, String value) {
        switch (type) {
            case 0:
                return goodsService.findAll("brandId", value);
            case 1:
                return goodsService.findAll("type", value);
            case 2:
                return goodsService.findAll("category", value);
            default:
                return null;
        }
    }

    @RequestMapping("/pick_get")
    @ResponseBody
    public Map<String, Object> get(Long id) {
        ObjectMapper objectMapper = new ObjectMapper();
        PhPick pick = pickService.findOne(id);
        if (pick != null) {
            Map m = objectMapper.convertValue(pick, Map.class);
            List<Map> list = new ArrayList<>();
            for (PhPickGoods pickGoods : pickGoodsService.findByPid(pick.getId())) {
                Map<String, Object> obj = new HashMap<>();
                PhGoods goods = goodsService.findOne(pickGoods.getGoodsId());
                obj.put("id", pickGoods.getGoodsId());
                if (goods != null) {
                    obj.put("name", goods.getName());
                    obj.put("img", goods.getImage());
                } else {
                    obj.put("name", "未知");
                    obj.put("img", "");
                }
                list.add(obj);
            }
            m.put("sub", list);
            return m;
        }
        return null;
    }

    @RequestMapping("/pick_save")
    @ResponseBody
    public boolean save(Long id, String name, String imageUrl, @RequestParam(name = "goodsID") Long[] goodsIDs) {
        PhPick pick;
        if ("".equals(id) || id == null) {
            pick = new PhPick();
            pick.setCreateTime(new Date());
        } else {
            pick = pickService.findOne(id);
            pickGoodsService.delByPid(pick.getId());
        }
        pick.setName(name);
        pick.setImageUrl(imageUrl);
        PhPick pickSaved = pickService.save(pick);
        for (Long gid : goodsIDs) {
            PhPickGoods pickGoods = new PhPickGoods();
            pickGoods.setGoodsId(gid);
            pickGoods.setPickId(pickSaved.getId());
            pickGoods.setCreateTime(new Date());
            pickGoodsService.save(pickGoods);
        }
        return true;
    }

    @RequestMapping("/pick_del")
    @ResponseBody
    public boolean delete(@RequestParam(name = "ids[]") Long[] ids) {
        for (Long id : ids) {
            pickService.del(id);
            pickGoodsService.delByPid(id);
        }
        return true;
    }
}
