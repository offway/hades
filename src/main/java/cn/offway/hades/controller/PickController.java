package cn.offway.hades.controller;

import cn.offway.hades.domain.*;
import cn.offway.hades.properties.QiniuProperties;
import cn.offway.hades.service.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.*;

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
    private PhThemeService themeService;
    @Autowired
    private PhThemeGoodsService themeGoodsService;
    @Autowired
    private PhGoodsService goodsService;
    @Autowired
    private PhBrandService brandService;
    @Autowired
    private PhConfigService configService;

    @RequestMapping("/pick.html")
    public String index(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        return "pick_index";
    }

    @RequestMapping("/theme.html")
    public String indexTheme(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        return "theme_index";
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
//            List<PhGoods> goodsList = new ArrayList<>();
//            for (PhPickGoods i : l) {
//                goodsList.add(goodsService.findOne(i.getGoodsId()));
//            }
            m.put("sub", l);
            list.add(m);
        }
        map.put("aData", list);//数据集合
        return map;
    }

    @ResponseBody
    @RequestMapping("/theme_list")
    public Map<String, Object> themeList(HttpServletRequest request, int sEcho, int iDisplayStart, int iDisplayLength) {
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        ObjectMapper objectMapper = new ObjectMapper();
        String sortCol = request.getParameter("iSortCol_0");
        String sortName = request.getParameter("mDataProp_" + sortCol);
        String sortDir = request.getParameter("sSortDir_0");
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
        PageRequest pr = new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, Sort.Direction.fromString(sortDir), sortName);
        Page<PhTheme> pages = themeService.list(Long.valueOf(id), name, sTime, eTime, pr);
        // 为操作次数加1，必须这样做
        int initEcho = sEcho + 1;
        Map<String, Object> map = new HashMap<>();
        map.put("sEcho", initEcho);
        map.put("iTotalRecords", pages.getTotalElements());//数据总条数
        map.put("iTotalDisplayRecords", pages.getTotalElements());//显示的条数
        List<Map> list = new ArrayList<>();
        for (PhTheme item : pages.getContent()) {
            Map m = objectMapper.convertValue(item, Map.class);
            List<PhThemeGoods> l = themeGoodsService.findByPid(item.getId());
            m.put("sub", l);
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
            case 3:
                return goodsService.findAll("keyword", value);
            default:
                return null;
        }
    }

    @RequestMapping("/pick_get")
    @ResponseBody
    public Map<String, Object> get(Long id) throws InterruptedException, ExecutionException {
        ObjectMapper objectMapper = new ObjectMapper();
        PhPick pick = pickService.findOne(id);
        if (pick != null) {
            Map m = objectMapper.convertValue(pick, Map.class);
            LinkedList<Map> list = new LinkedList<>();
            List<PhPickGoods> pickGoodsList = pickGoodsService.findByPid(pick.getId());
            ExecutorService pool = Executors.newFixedThreadPool(pickGoodsList.size());
            List<Future<Map>> returnList = new ArrayList<>();
            for (PhPickGoods pickGoods : pickGoodsList) {
                Future<Map> future = pool.submit(new Callable<Map>() {
                    @Override
                    public Map call() throws Exception {
                        Map<String, Object> obj = new HashMap<>();
                        PhGoods goods = goodsService.findOne(pickGoods.getGoodsId());
                        obj.put("id", pickGoods.getGoodsId());
                        if (goods != null) {
                            obj.put("name", goods.getName());
                            obj.put("img", goods.getImage());
                            obj.put("price", goods.getPrice());
                        } else {
                            obj.put("name", "未知");
                            obj.put("img", "");
                            obj.put("price", "未知");
                        }
                        return obj;
                    }
                });
                returnList.add(future);
            }
            int timeout = returnList.size() / 100;
            timeout = timeout == 0 ? 1 : timeout;
            pool.awaitTermination(timeout, TimeUnit.SECONDS);
            for (Future<Map> f : returnList) {
                if (f.isDone()) {
                    list.add(f.get());
                }
            }
            pool.shutdown();
            m.put("sub", list);
            return m;
        }
        return null;
    }

    @RequestMapping("/theme_get")
    @ResponseBody
    public Map<String, Object> getTheme(Long id) throws InterruptedException, ExecutionException {
        ObjectMapper objectMapper = new ObjectMapper();
        PhTheme theme = themeService.findOne(id);
        if (theme != null) {
            Map m = objectMapper.convertValue(theme, Map.class);
            LinkedList<Map> list = new LinkedList<>();
            List<PhThemeGoods> themeGoodsList = themeGoodsService.findByPid(theme.getId());
            ExecutorService pool = Executors.newFixedThreadPool(themeGoodsList.size());
            List<Future<Map>> returnList = new ArrayList<>();
            for (PhThemeGoods themeGoods : themeGoodsList) {
                Future<Map> future = pool.submit(new Callable<Map>() {
                    @Override
                    public Map call() throws Exception {
                        Map<String, Object> obj = new HashMap<>();
                        PhGoods goods = goodsService.findOne(themeGoods.getGoodsId());
                        obj.put("id", themeGoods.getGoodsId());
                        if (goods != null) {
                            obj.put("name", goods.getName());
                            obj.put("img", goods.getImage());
                            obj.put("price", goods.getPrice());
                        } else {
                            obj.put("name", "未知");
                            obj.put("img", "");
                            obj.put("price", "未知");
                        }
                        return obj;
                    }
                });
                returnList.add(future);
            }
            int timeout = returnList.size() / 100;
            timeout = timeout == 0 ? 1 : timeout;
            pool.awaitTermination(timeout, TimeUnit.SECONDS);
            for (Future<Map> f : returnList) {
                if (f.isDone()) {
                    list.add(f.get());
                }
            }
            pool.shutdown();
            m.put("sub", list);
            return m;
        }
        return null;
    }

    @RequestMapping("/pick_save")
    @ResponseBody
    public boolean save(Long id, String name, String imageUrl, String shareImg, @RequestParam(name = "goodsID") Long[] goodsIDs) {
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
        pick.setShareImg(shareImg);
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

    @RequestMapping("/theme_save")
    @ResponseBody
    @Transactional
    public boolean saveTheme(Long id, String name, String imageUrl, String content, String remark, @RequestParam(name = "goodsID") Long[] goodsIDs) {
        PhTheme theme;
        if ("".equals(id) || id == null) {
            theme = new PhTheme();
            theme.setCreateTime(new Date());
            theme.setIsRecommend("0");
            ;
        } else {
            theme = themeService.findOne(id);
            themeGoodsService.delByPid(theme.getId());
        }
        theme.setName(name);
        theme.setImageUrl(imageUrl);
        theme.setContent(content);
        theme.setRemark(remark);
        PhTheme themeSaved = themeService.save(theme);
        for (Long gid : goodsIDs) {
            PhThemeGoods themeGoods = new PhThemeGoods();
            themeGoods.setGoodsId(gid);
            themeGoods.setThemeId(themeSaved.getId());
            themeGoods.setCreateTime(new Date());
            themeGoodsService.save(themeGoods);
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

    @RequestMapping("/theme_del")
    @ResponseBody
    @Transactional
    public boolean deleteTheme(@RequestParam(name = "ids[]") Long[] ids) {
        for (Long id : ids) {
            themeService.delete(id);
            themeGoodsService.delByPid(id);
        }
        return true;
    }

    @ResponseBody
    @RequestMapping("/theme_pin_mini_list")
    public JSONArray pinListMini() {
        String key = "NEW_INDEX_THEME_MINI";
        String jsonStr = configService.findContentByName(key);
        JSONArray jsonArray = new JSONArray();
        if (jsonStr != null && !"".equals(jsonStr)) {
            jsonArray = JSON.parseArray(jsonStr);
        }
        return jsonArray;
    }

    @ResponseBody
    @RequestMapping("/theme_pin_mini")
    @Transactional
    public boolean pinMini(@RequestParam("ids[]") Long[] ids) {
        String key = "NEW_INDEX_THEME_MINI";
        String jsonStr = configService.findContentByName(key);
        JSONArray jsonArray;
        if (jsonStr != null && !"".equals(jsonStr)) {
            jsonArray = JSON.parseArray(jsonStr);
        } else {
            jsonArray = new JSONArray();
        }
        for (Long id : ids) {
            PhTheme theme = themeService.findOne(id);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", theme.getId());
            jsonObject.put("name", theme.getName());
            jsonObject.put("content", theme.getContent());
            jsonObject.put("image", theme.getRemark() == null ? "NONE" : theme.getRemark());
            jsonArray.add(jsonObject);
            //update DB
            theme.setIsRecommend("1");
            themeService.save(theme);
        }
        PhConfig config = configService.findOne(key);
        if (config == null) {
            config = new PhConfig();
            config.setName(key);
            config.setCreateTime(new Date());
        }
        config.setContent(jsonArray.toJSONString());
        configService.save(config);
        return true;
    }

    @ResponseBody
    @RequestMapping("/theme_pin_mini_save")
    @Transactional
    public boolean pinSaveMini(@RequestParam(name = "ids[]", required = false) String[] ids, @RequestParam(name = "images[]", required = false) String[] images,
                               @RequestParam(name = "names[]", required = false) String[] names, @RequestParam(name = "contents[]", required = false) String[] contents,
                               @RequestParam(name = "idsDel[]", required = false) Long[] idsDel) {
        String key = "NEW_INDEX_THEME_MINI";
        PhConfig config = configService.findOne(key);
        JSONArray jsonArray = new JSONArray();
        if (ids != null) {
            for (int i = 0; i < ids.length; i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", ids[i]);
                jsonObject.put("name", names[i]);
                jsonObject.put("content", contents[i]);
                jsonObject.put("image", images[i]);
                jsonArray.add(jsonObject);
            }
        }
        if (config == null) {
            config = new PhConfig();
            config.setName(key);
            config.setCreateTime(new Date());
        }
        config.setContent(jsonArray.toJSONString());
        configService.save(config);
        //update DB
        if (idsDel != null) {
            for (Long id : idsDel) {
                PhTheme tmp = themeService.findOne(id);
                tmp.setIsRecommend("0");
                themeService.save(tmp);
            }
        }
        return true;
    }
}
