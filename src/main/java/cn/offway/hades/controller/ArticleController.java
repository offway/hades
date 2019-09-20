package cn.offway.hades.controller;

import cn.offway.hades.domain.*;
import cn.offway.hades.properties.QiniuProperties;
import cn.offway.hades.service.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
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
    @Autowired
    private PhArticleDraftService articleDraftService;
    @Autowired
    private PhGoodsService goodsService;

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
            brand.setContent(filterWxPicAndReplace(brand.getContent().replaceAll("(?<=(<img.{1,100}))width:\\d+px(?=(.+>))", "").replaceAll("(?<=(<img.{1,100}))height:\\d+px(?=(.+>))", ""), qiniuService));
            brand.setCreateTime(new Date());
            brand.setStatus("0");
        } else {
            PhArticle article = articleService.findOne(brand.getId());
            brand.setContent(filterWxPicAndReplace(brand.getContent().replaceAll("(?<=(<img.{1,100}))width:\\d+px(?=(.+>))", "").replaceAll("(?<=(<img.{1,100}))height:\\d+px(?=(.+>))", ""), qiniuService));
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

    @ResponseBody
    @GetMapping(value = "/article/{type}/{id}.html",produces="text/html;charset=UTF-8")
    public String html(
         @PathVariable String type,
         @PathVariable Long id){
        String html = "<!DOCTYPE html>\n<!--[if lt IE 7 ]> <html class=\"ie ie6\"> <![endif]-->\n<!--[if IE 7 ]>    <html class=\"ie ie7\"> <![endif]-->\n<!--[if IE 8 ]>    <html class=\"ie ie8\"> <![endif]-->\n<!--[if IE 9 ]>    <html class=\"ie ie9\"> <![endif]-->\n<!--[if (gt IE 9)|!(IE)]><!--> <html lang=\"zh-cn\"> <!--<![endif]-->\n<head lang=\"en\">\n    <meta charset=\"UTF-8\">\n    <meta name=\"viewport\" content=\"width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no\" />\n    <title>###NAME###</title>\n    <link rel=\"stylesheet\" href=\"https://api.offway.cn:8443/wxshare/style/style.css\">\n    <script src=\"https://api.offway.cn:8443/js/jquery.min.js\"></script>\n    <script src=\"https://api.offway.cn:8443/wxshare/script/jquery-m.js\"></script>\n    <style>\n		.p-box img {\n		    margin: auto auto !important;\n		}\n		.page-3 {\n		    border-bottom: 10px solid #fff;\n		}\n	</style>\n</head>\n<body>\n<div class=\"page-3\">\n    <div class=\"title\">###TITLE###</div>\n    <div class=\"span\">\n       ###TYPE### \n        <span class=\"eye\">###VIEWCOUNT###</span>\n        <span class=\"time\">###CREATETIME###</span>\n    </div>\n    <div class=\"p-box\">\n###CONTENT### \n ###RECOMMEND_GOODS###\n    </div>\n</div>\n<div class=\"foot-none\"></div>\n<div class=\"foot-btn\"><a href=\"https://android.myapp.com/myapp/detail.htm?apkName=com.puhao.offway\">下载APP</a> </div>\n</body>\n\n<script src=\"https://res.wx.qq.com/open/js/jweixin-1.4.0.js\"></script>\n<script src=\"https://api.offway.cn:8443/js/jquery.min.js\"></script>\n<script type=\"text/javascript\">\nfunction goWxShare(shareOption){	\n	var imgurl =  shareOption.imgurl;\n	\n	var url = shareOption.fromUrl ;\n	var forumid=\"\";\n	\n		\n	if(null===url || ''===url || undefined===url){\n		url = location.href ;\n	}\n	if(url.indexOf(\"forumid=\")>=0){\n		forumid=shareOption.forumid;		\n	}\n\n	\n	$.ajax({  \n	    url:\"https://zeus.offway.cn/access/wx/config\",  \n	    data: {url:url}, \n		dataType: 'json',\n	    type:'post',\n	    error: function(data) {   \n	        // alert(JSON.stringify(data));\n	    },  \n	    success:function(data){\n	    	// alert(JSON.stringify(data));\n	    	//var temp = JSON.parse(data);\n	    	if(null!==data ){\n		    		//通过config接口注入权限验证配置\n					wx.config({\n					  debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。\n					  appId: data.appid, // 必填，公众号的唯一标识\n					  timestamp: data.timestamp, // 必填，生成签名的时间戳\n					  nonceStr: data.nonce, // 必填，生成签名的随机串\n					  signature: data.getSHA1,// 必填，签名，见附录\n					  jsApiList: ['onMenuShareTimeline', 'onMenuShareAppMessage','hideMenuItems'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2\n					});\n			    	\n		    		// 更改七牛分享的尺寸大小\n		    		// var imgurl =  shareOption.imgurl;\n					\n					// ready 接口处理成功\n					wx.ready(function(){\n						//分享给朋友\n						//title:分享的标题；link分享的链接；imgUrl分享的图标\n					 	wx.onMenuShareAppMessage({\n					 	    title: shareOption.title,\n					 	    desc: shareOption.desc,\n					 	    link: url,\n					 	    imgUrl:  imgurl,\n					 	    success: function () {\n					 	    	//getShareConfig(); \n					 	    	// shareTask(1,forumid);\n					 	    }\n					 	});\n\n						//分享给朋友圈\n						//title:分享的标题；link分享的链接；imgUrl分享的图标\n					 	wx.onMenuShareTimeline({\n					 		title: shareOption.title , \n					 		link:  url,\n					 	    imgUrl:  imgurl,\n					 	    success: function () {\n					 	    	//getShareConfig();\n					 	    	// shareTask(2,forumid);\n					 	    }\n					    });\n					 	//============\n					}); 			\n		    	\n		    	}\n	    	//=================\n	    }\n	});\n}\n</script>\n\n<script type=\"text/javascript\">\ngoWxShare({\n	imgurl:'###IMAGE###',\n	title:'###NAME###',\n	desc:'###TITLE###',\n	fromUrl:location.href\n});\n</script>\n</html>";
        PhArticle phArticle = articleService.findOne(id);
        phArticle.setViewCount(phArticle.getViewCount()+1L);
        phArticle = articleService.save(phArticle);
        html = html.replace("###TITLE###", phArticle.getTitle());

        StringBuilder sb = new StringBuilder();
        String [] tags = phArticle.getTag().split(",");
        for (String tag : tags) {
            sb.append("<span class=\"tip\" style=\"margin-right: .1rem;\">"+tag+"</span>");
        }
        html = html.replace("###TYPE###", sb.toString());
        html = html.replace("###VIEWCOUNT###", phArticle.getViewCount()+"");

        Date date = phArticle.getApproval();
        html = html.replace("###CREATETIME###", DateFormatUtils.format(null==date?phArticle.getCreateTime():date, "yyyy-MM-dd"));

        String content = phArticle.getContent();
        html = html.replace("###CONTENT###", content);
        html = html.replace("###IMAGE###", phArticle.getImage());
        html = html.replace("###NAME###", phArticle.getName());

        String re = "";
        if("info".equals(type)){
            html = html.replace("<\\s*?script[^>]*?>[\\s\\S]*?<\\s*?/\\s*?script\\s*?>", "");
            html = html.replace("foot-btn", "undis");
            html = html.replace("foot-none", "undis");
        }else{

            String goodsIds = phArticle.getGoodsIds();
            if(StringUtils.isNotBlank(goodsIds)){
                String[] ids = goodsIds.split(",");
                if(ids.length >0){
                    Long[] idLs = new Long[ids.length];
                    int i = 0;
                    for (String s : ids) {
                        idLs[i++] = Long.valueOf(s);
                    }
                    List<PhGoods> goodss = goodsService.findByIds(idLs);
                    for (PhGoods goods : goodss) {
                        re += "<div style=\"width: 6.75rem;height: 1.9rem;box-shadow: 0 0 0.1rem 0 rgba(127,127,127,0.50);margin: .1rem .0 .1rem 0.04rem;display: flex;align-items: center;\" onclick=\"window.location.href='https://sj.qq.com/myapp/detail.htm?apkName=com.puhao.offway'\">\n" +
                                "  <div style=\"width: 1.4rem;height: 1.4rem;margin: .2rem .2rem auto;\">\n" +
                                "  <img style=\"width: 100%;height: 100%;\" src=\""+goods.getImage()+"\"></div>\n" +
                                "  <div style=\"width: 3.8rem;font-size: .24rem;text-align: left;line-height: .3rem\">"+goods.getName()+"</div>\n" +
                                "  <div style=\"width: .02rem;height: 1rem;background: #F4F4F4;margin-left: .2rem;\"></div>\n" +
                                "  <div style=\"width: .45rem;height: .45rem;font-size: 0;margin-left: .25rem;\">\n" +
                                "  <img style=\"width: 100%;height: 100%;\" src=\"http://qiniu.offway.cn/image/wx/50/48/e04f9904-5d31-01e9-7089-b9dba5897f6e.png\"></div>\n" +
                                "</div>";
                    }
                }
            }
        }
        html = html.replace("###RECOMMEND_GOODS###", re);
        return html;
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
