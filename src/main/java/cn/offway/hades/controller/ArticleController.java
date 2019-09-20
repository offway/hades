package cn.offway.hades.controller;

import cn.offway.hades.domain.PhAdmin;
import cn.offway.hades.domain.PhArticle;
import cn.offway.hades.domain.PhArticleDraft;
import cn.offway.hades.domain.PhConfig;
import cn.offway.hades.properties.QiniuProperties;
import cn.offway.hades.service.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    @Autowired
    private PhArticleDraftService articleDraftService;
    @Value("${ph.url}")
    private String url;

    @RequestMapping("/article.html")
    public String index(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        map.addAttribute("url", url);
        return "article_index";
    }

    @RequestMapping("/article_add.html")
    public String add(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        map.addAttribute("url", url);
        map.addAttribute("theId", "ABCD");
        return "article_compose";
    }

    @RequestMapping("/article_edit.html")
    public String edit(ModelMap map, String id) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        map.addAttribute("url", url);
        map.addAttribute("theId", id);
        return "article_compose";
    }

    @RequestMapping("/article_draft.html")
    public String indexDraft() {
        return "articleDraft_index";
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
        return echoBody(initEcho, pages.getTotalElements(), pages.getTotalElements(), pages.getContent());
    }

    @ResponseBody
    @RequestMapping("/article_draft_list")
    public Map<String, Object> getDraftList(HttpServletRequest request, String name, String title) {
        int sEcho = Integer.parseInt(request.getParameter("sEcho"));
        int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
        int iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "id"));
        Page<PhArticleDraft> pages = articleDraftService.findAll(name, title, new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, sort));
        int initEcho = sEcho + 1;
        return echoBody(initEcho, pages.getTotalElements(), pages.getTotalElements(), pages.getContent());
    }

    private Map<String, Object> echoBody(int initEcho, long iTotalRecords, long iTotalDisplayRecords, List<?> data) {
        Map<String, Object> map = new HashMap<>();
        map.put("sEcho", initEcho);
        map.put("iTotalRecords", iTotalRecords);//数据总条数
        map.put("iTotalDisplayRecords", iTotalDisplayRecords);//显示的条数
        map.put("aData", data);//数据集合
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
            brand.setContent(filterWxPicAndReplace(brand.getContent()/*.replaceAll("(?<=(<img.{1,100}))width:\\d+px(?=(.+>))", "").replaceAll("(?<=(<img.{1,100}))height:\\d+px(?=(.+>))", "")*/, qiniuService));
            brand.setCreateTime(new Date());
            brand.setStatus("0");
        } else {
            PhArticle article = articleService.findOne(brand.getId());
            brand.setContent(filterWxPicAndReplace(brand.getContent()/*.replaceAll("(?<=(<img.{1,100}))width:\\d+px(?=(.+>))", "").replaceAll("(?<=(<img.{1,100}))height:\\d+px(?=(.+>))", "")*/, qiniuService));
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
    @RequestMapping("/article_draft_del")
    public boolean deleteDraft(@RequestParam("ids[]") Long[] ids) {
        for (Long id : ids) {
            articleDraftService.del(id);
        }
        return true;
    }

    @ResponseBody
    @Transactional
    @RequestMapping("/article_draft_sync")
    public boolean syncDraft(@RequestParam("ids[]") Long[] ids) {
        for (Long id : ids) {
            PhArticleDraft articleDraft = articleDraftService.findOne(id);
            if (articleDraft != null) {
                PhArticle article = new PhArticle();
                article.setTitle(articleDraft.getTitle());
                article.setContent(articleDraft.getContent());
                article.setName(articleDraft.getName());
                article.setImage(articleDraft.getImage());
                article.setStatus("0");
                article.setPraiseCount(0L);
                article.setViewCount(0L);
                article.setType("0");
                article.setCreateTime(new Date());
                article.setTag("资讯");
                articleService.save(article);
                //mark as synced
                articleDraft.setStatus("1");
                articleDraftService.save(articleDraft);
            }
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
            article.setRemark("");
            articleService.save(article);
        }
        return true;
    }

    @ResponseBody
    @RequestMapping("/article_startup")
    public boolean starttimeup(@RequestParam("ids[]") Long[] ids, String starttime, @AuthenticationPrincipal PhAdmin admin) {
        List<Long> roles = roleadminService.findRoleIdByAdminId(admin.getId());
        if (roles.contains(BigInteger.valueOf(8L))) {
            return false;
        }
        for (Long id : ids) {
            PhArticle article = articleService.findOne(id);
            DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            Date beginTime = null;
            beginTime = DateTime.parse(starttime, format).toDate();
            article.setStatus("1");
            article.setRemark("定时上架");
            article.setApproval(beginTime);
            article.setApprover(admin.getNickname());
            articleService.save(article);
        }
        return true;
    }

    public static String filterWxPicAndReplace(String content, QiniuService qiniuService) throws IOException {
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
                if (null != oldImgSrc && !"".equals(oldImgSrc) && !oldImgSrc.contains("http://qiniu.offway.cn") && (oldImgSrc.startsWith("http://") || oldImgSrc.startsWith("https://"))) {
                    newImgSrc = getQiniuImg(oldImgSrc, qiniuService);
                    m.appendReplacement(imgBuffer, "src=\"" + newImgSrc + "\"");
                    System.out.println("newImgSrc:::::::::::::::" + newImgSrc);
                } else {
                    m.appendReplacement(imgBuffer, "src=\"" + oldImgSrc + "\"");
                    System.out.println("oldImgSrc:::::::::::::::" + oldImgSrc);
                }
            }
            m.appendTail(imgBuffer);
            String string = java.util.regex.Matcher.quoteReplacement(imgBuffer.toString());
            m_image.appendReplacement(m_imageBuffer, string);
        }
        m_image.appendTail(m_imageBuffer);
        return m_imageBuffer.toString();
    }

    private static String getQiniuImg(String url, QiniuService qiniuService) throws IOException {
        InputStream file = parseFile(new URL(url));
        return uploadImg(file, qiniuService);
    }

    private static InputStream parseFile(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3 * 1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        //得到输入流
        return conn.getInputStream();
    }

    private static String uploadImg(InputStream file, QiniuService qiniuService) {
        return qiniuService.qiniuUpload(file);
    }
}
