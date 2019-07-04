package cn.offway.hades.controller;

import cn.offway.hades.properties.QiniuProperties;
import cn.offway.hades.service.QiniuService;
import cn.offway.hades.utils.HttpClientUtil;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 七牛
 *
 * @author wn
 */
@RestController
@RequestMapping("/qiniu")
public class QiniuController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private QiniuProperties qiniuProperties;

    @Autowired
    private QiniuService qiniuService;

    @GetMapping("/token")
    public String token(@RequestParam(name = "isVideo", required = false) String isVideo) {
        try {
            Auth auth = Auth.create(qiniuProperties.getAccessKey(), qiniuProperties.getSecretKey());
            StringMap putPolicy = new StringMap();
            if (isVideo != null) {
                putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fsize\":$(fsize),\"fname\":$(fname),\"param\":\"$(x:param)\"}");
            } else {
                putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fsize\":$(fsize),\"fname\":$(fname),\"param\":\"$(x:param)\"}");
            }
            return auth.uploadToken(qiniuProperties.getBucket(), null, qiniuProperties.getExpireSeconds(), putPolicy);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取七牛上传文件token异常", e);
            return "";
        }

    }

    @RequestMapping("/authorization")
    public Map<String, Object> authorization(String url) {
        Auth auth = Auth.create(qiniuProperties.getAccessKey(), qiniuProperties.getSecretKey());
        return auth.authorization(url).map();
    }

    @RequestMapping("/pfop")
    public String pfop(String urlSign, String url, String key, String fops, String pipeline) {
        Object auth = authorization(urlSign).get("Authorization");
        //parameters
        Map<String, String> param = new HashMap<>();
        param.put("bucket", qiniuProperties.getBucket());
        param.put("key", key);
        param.put("fops", fops);
        param.put("pipeline", pipeline);
        //headers
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", String.valueOf(auth));
        header.put("Content-Type", "application/x-www-form-urlencoded");
        header.put("Host", "api.qiniu.com");
        return HttpClientUtil.postWithHeader(url, param, header);
    }

    @PostMapping("/delete")
    public boolean delete(String url) {
        return qiniuService.qiniuDelete(url.replace(qiniuProperties.getUrl() + "/", ""));
    }
}
