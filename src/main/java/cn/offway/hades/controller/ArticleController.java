package cn.offway.hades.controller;

import cn.offway.hades.domain.PhAdmin;
import cn.offway.hades.domain.PhArticle;
import cn.offway.hades.domain.PhConfig;
import cn.offway.hades.properties.QiniuProperties;
import cn.offway.hades.service.PhArticleService;
import cn.offway.hades.service.PhConfigService;
import cn.offway.hades.service.PhRoleadminService;
import cn.offway.hades.service.QiniuService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.security.auth.login.Configuration;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping
public class ArticleController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private QiniuProperties qiniuProperties;
    @Autowired
    private QiniuService qiniuService;
    @Autowired
    private PhArticleService articleService;
    @Autowired
    private PhRoleadminService roleadminService;
    @Autowired
    private PhConfigService configService;
    @Value("${ph.url}")
    private String url;

    @RequestMapping("/article.html")
    public String index(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        map.addAttribute("url", url);
        return "article_index";
    }

    @ResponseBody
    @RequestMapping("/article_list")
    public Map<String, Object> getList(HttpServletRequest request, String name, String tag, String status, String title, String type) {
        int sEcho = Integer.parseInt(request.getParameter("sEcho"));
        int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
        int iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "id"), new Sort.Order(Sort.Direction.ASC, "sort"));
        Page<PhArticle> pages = articleService.findAll(name, tag, status, title, type, new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, sort));
        int initEcho = sEcho + 1;
        Map<String, Object> map = new HashMap<>();
        map.put("sEcho", initEcho);
        map.put("iTotalRecords", pages.getTotalElements());//数据总条数
        map.put("iTotalDisplayRecords", pages.getTotalElements());//显示的条数
        map.put("aData", pages.getContent());//数据集合
        return map;
    }

    @ResponseBody
    @RequestMapping("/article_pinTagList")
    public JSONArray getPinTags() {
        String jsonStr = configService.findContentByName("ARTICLE_NAV");
        return JSONArray.parseArray(jsonStr);
    }

    @ResponseBody
    @RequestMapping("/article_savePinTags")
    public boolean saveTags(@RequestParam("names[]") String[] names, @RequestParam("texts[]") String[] texts, @RequestParam("values[]") String[] values) {
        JSONArray array = new JSONArray();
        if (names.length - texts.length + values.length != names.length) {
            return false;
        }
        for (int i = 0; i < names.length; i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", names[i].trim());
            jsonObject.put("value", values[i].trim());
            jsonObject.put("text", texts[i].trim());
            array.add(i, jsonObject);
        }
        savePinTagToDB(array.toJSONString());
        return true;
    }

    @ResponseBody
    @RequestMapping("/article_tagList")
    public JSONObject getTags() {
        String jsonStr = configService.findContentByName("ARTICLE_TAGS");
        return JSONObject.parseObject(jsonStr);
    }

    @ResponseBody
    @RequestMapping("/article_saveTags")
    public boolean saveTags(@RequestParam("values[]") String[] values) {
        JSONObject object = new JSONObject();
        for (String v : values) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("value", v);
            object.put(v, jsonObject);
        }
        saveTagToDB(object.toJSONString());
        return true;
    }

    private void saveTagToDB(String s) {
        PhConfig config = configService.findOne("ARTICLE_TAGS");
        config.setContent(s);
        configService.save(config);
    }

    private void savePinTagToDB(String s) {
        PhConfig config = configService.findOne("ARTICLE_NAV");
        config.setContent(s);
        configService.save(config);
    }

    @ResponseBody
    @RequestMapping("/article_syncRecommend")
    public boolean syncRecommend() {
        String jsonStr = configService.findContentByName("ARTICLE_NAV");
        JSONArray jsonArray = JSONArray.parseArray(jsonStr);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "");
        jsonObject.put("value", "");
        jsonObject.put("text", "推荐");
        jsonArray.add(0, jsonObject);
        savePinTagToDB(jsonArray.toJSONString());
        return true;
    }

    @ResponseBody
    @RequestMapping("/article_syncType")
    public boolean syncType() {
        String jsonStr = configService.findContentByName("ARTICLE_NAV");
        JSONArray jsonArray = JSONArray.parseArray(jsonStr);
        String[] types = new String[]{"资讯", "专题", "视频"};
        int i = 0;
        for (String type : types) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", "type");
            jsonObject.put("value", i);
            jsonObject.put("text", type);
            jsonArray.add(i + 1, jsonObject);
            i++;
        }
        savePinTagToDB(jsonArray.toJSONString());
        return true;
    }

    @ResponseBody
    @RequestMapping("/article_attachTag")
    public boolean attachTag(String value) {
        String jsonStr = configService.findContentByName("ARTICLE_NAV");
        JSONArray jsonArray = JSONArray.parseArray(jsonStr);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "tag");
        jsonObject.put("value", value);
        jsonObject.put("text", value);
        jsonArray.add(jsonObject);
        savePinTagToDB(jsonArray.toJSONString());
        return true;
    }


    @ResponseBody
    @RequestMapping("/article_save")
    public boolean save(PhArticle brand) throws IOException {
        if (brand.getId() == null) {
            brand.setContent(filterWxPicAndReplace(brand.getContent()));
            brand.setCreateTime(new Date());
            brand.setStatus("0");
        } else {
            PhArticle article = articleService.findOne(brand.getId());
            brand.setContent(filterWxPicAndReplace(brand.getContent()));
            brand.setStatus(article.getStatus());
            brand.setCreateTime(article.getCreateTime());
            brand.setApprover(article.getApprover());
            brand.setApproval(article.getApproval());
            brand.setApprovalContent(article.getApprovalContent());
            brand.setRemark(article.getRemark());
        }
        String jsonStr = configService.findContentByName("ARTICLE_TAGS");
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        for (String t : brand.getTag().split(",")) {
            if (!jsonObject.containsKey(t)) {
                JSONObject tmpObj = new JSONObject();
                tmpObj.put("value", t);
                jsonObject.put(t, tmpObj);
            }
        }
        saveTagToDB(jsonObject.toJSONString());
        articleService.save(brand);
        return true;
    }

    @ResponseBody
    @RequestMapping("/article_get")
    public Object get(Long id) {
        return articleService.findOne(id);
    }

    @ResponseBody
    @RequestMapping("/article_del")
    public boolean delete(@RequestParam("ids[]") Long[] ids) {
        for (Long id : ids) {
            articleService.del(id);
        }
        return true;
    }

    @ResponseBody
    @RequestMapping("/article_up")
    public boolean goodsUp(Long id, @AuthenticationPrincipal PhAdmin admin) {
        PhArticle article = articleService.findOne(id);
        List<Long> roles = roleadminService.findRoleIdByAdminId(admin.getId());
        if (roles.contains(BigInteger.valueOf(8L))) {
            return false;
        }
        if (article != null) {
            article.setStatus("1");
            article.setApprover(admin.getNickname());
            article.setApproval(new Date());
            articleService.save(article);
        }
        return true;
    }

    @ResponseBody
    @RequestMapping("/article_down")
    public boolean goodsDown(Long id, @AuthenticationPrincipal PhAdmin admin) {
        PhArticle article = articleService.findOne(id);
        List<Long> roles = roleadminService.findRoleIdByAdminId(admin.getId());
        if (roles.contains(BigInteger.valueOf(8L))) {
            return false;
        }
        if (article != null) {
            article.setStatus("0");
            article.setApprover(admin.getNickname());
            articleService.save(article);
        }
        return true;
    }


    private String filterWxPicAndReplace(String content) throws IOException {
        Pattern p_image;
        Matcher m_image;
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(content);
        StringBuffer m_imageBuffer = new StringBuffer();
        while (m_image.find()) {
            // 得到<img />数据
            String img = m_image.group();
            // 匹配<img>中的src数据
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            StringBuffer imgBuffer = new StringBuffer();
            while (m.find()) {
                // img图片地址
                String oldImgSrc = m.group(1);
                String newImgSrc = "";
                if (null != oldImgSrc && !"".equals(oldImgSrc)) {

                    newImgSrc = getQiniuImg(oldImgSrc);
                    m.appendReplacement(imgBuffer,"src=\"" + newImgSrc + "\"");
                    System.out.println("newImgSrc:::::::::::::::"+newImgSrc);
                }else{
                    m.appendReplacement(imgBuffer,"src=\"" + oldImgSrc + "\"");
                    System.out.println("oldImgSrc:::::::::::::::"+oldImgSrc);
                }
            }


            m.appendTail(imgBuffer);
            String string = java.util.regex.Matcher.quoteReplacement(imgBuffer.toString());
            m_image.appendReplacement(m_imageBuffer,string);
        }
        m_image.appendTail(m_imageBuffer);
        return m_imageBuffer.toString();
    }
    private  String getQiniuImg(String url) throws  IOException{
        InputStream file  = parseFile(new URL(url));
        return uploadImg(file);
    }

    private InputStream parseFile(URL url) throws IOException{
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3*1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        //得到输入流
        return conn.getInputStream();
    }

    private String uploadImg(InputStream file) throws IOException{
        return qiniuService.qiniuUpload(file);
    }
    private  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }
}
