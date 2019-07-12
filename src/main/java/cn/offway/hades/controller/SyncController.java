package cn.offway.hades.controller;

import cn.offway.hades.domain.PhArticleDraft;
import cn.offway.hades.service.PhArticleDraftService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.MapUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/sync")
public class SyncController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private PhArticleDraftService articleDraftService;

    @Value("${is-prd}")
    private boolean isPrd;

    @RequestMapping("/bzy")
    @ResponseBody
    public boolean sync() throws IOException {
        doSync();
        return true;
    }

    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void doSync() throws IOException {
//        String profile = System.getProperty("spring.profiles.active", "prd");
        if (!isPrd) {
            //不在开发环境跑
            return;
        }
        BzyClient bzyClient = new BzyClient();
        //创建任务列表
        List<Map<String, String>> taskAll = new ArrayList<Map<String, String>>();
        TaskGroups taskGroups = bzyClient.getBzyTaskGroups();
        if (taskGroups == null || !"success".equals(taskGroups.getError())) {
            return;
        } else {
            for (TaskGroupVo taskGroupVo : taskGroups.getData()) {
                if (taskGroupVo.getTaskGroupName().contains("offway")) {
                    TaskResp tempTasks = bzyClient.getBzyTasksByGroupId(taskGroupVo.getTaskGroupId());
                    if (tempTasks != null && "success".equals(tempTasks.getError())) {
                        taskAll.addAll(tempTasks.getData());
                    }
                }
            }
        }
        //获取任务中的数据
        for (Map<String, String> task : taskAll) {
            BaseResp gettopResp = bzyClient.getDataOfTask(task.get("taskId"));
            if (gettopResp == null) {
                continue;
            }
            List<HashMap<String, Object>> dataList = gettopResp.getData().getDataList();
            if (!"success".equals(gettopResp.getError()) || dataList == null) {
                continue;
            }
            addDynamic(dataList);
            bzyClient.updateDataByTaskId(task.get("taskId"));
        }
    }

    //添加头条信息
    private void addDynamic(List<HashMap<String, Object>> dataList) {
        if (dataList.size() > 0) {
            for (Map<String, Object> dataMap : dataList) {
                String consultationtitle = MapUtils.getString(dataMap, "consultationtitle");
                String consultationimg = MapUtils.getString(dataMap, "consultationimg");
                String consultationcontent = MapUtils.getString(dataMap, "consultationcontent");
                if (consultationtitle != null && !"".equals(consultationimg) && consultationcontent != null && !"".equals(consultationcontent)) {
                    //头条内容
                    if (null != consultationcontent && consultationcontent.indexOf("img{max-width:100%;height:auto;}") < 0) {
                        consultationcontent = "<style>img{max-width:100%;height:auto;}</style>".concat(consultationcontent);
                    }
                    if (!articleDraftService.isExist(consultationtitle)) {
                        PhArticleDraft articleDraft = new PhArticleDraft();
                        articleDraft.setContent(consultationcontent);
                        articleDraft.setTitle(consultationtitle);
                        articleDraft.setImage(consultationimg);
                        articleDraft.setName(consultationtitle);
                        articleDraft.setCreateTime(new Date());
                        articleDraftService.save(articleDraft);
                    }
                }
            }
        }
    }

    private class BzyClient {
        private String url = "http://dataapi.bazhuayu.com/";
        private String username = "13764353666";
        private String password = "tianzai001";
        private String grant_type = "password";
        private TokenResp tokenVo = null;

        private BzyClient() throws IOException {
            getToken();
        }

        /**
         * 获取token
         */
        private void getToken() throws IOException {
            Map<String, String> params = new HashMap<String, String>();
            params.put("username", username);
            params.put("password", password);
            params.put("grant_type", grant_type);
            HttpClientUtils httpClientUtils = new HttpClientUtils();
            HttpUriRequest reqMethod = httpClientUtils.getPostRequest(params, url + "token");
            reqMethod.setHeader("Content-type", "application/x-www-form-urlencoded");
            HttpClient httpclient = HttpClients.createDefault();
            HttpResponse response = httpclient.execute(reqMethod);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity resEntity = response.getEntity();
                String respMsg = EntityUtils.toString(resEntity, "utf-8");
                JSONObject json = JSON.parseObject(respMsg);
                System.out.println(json.toString());
                this.tokenVo = JSONObject.toJavaObject(json, TokenResp.class);
            }
        }

        /**
         * 获取任务组信息
         */
        private TaskGroups getBzyTaskGroups() throws IOException {
            Map<String, String> params = new HashMap<String, String>();
            String respMsg = getBzyRespMsg(params, "api/TaskGroup");
            if ("request fail".equals(respMsg)) {
                return null;
            }
            JSONObject json = JSON.parseObject(respMsg);
            return JSONObject.toJavaObject(json, TaskGroups.class);
        }

        /**
         * 根据任务组id，获取任务组所有任务
         */
        private TaskResp getBzyTasksByGroupId(String groupId) throws IOException {
            Map<String, String> params = new HashMap<String, String>();
            String respMsg = getBzyRespMsg(params, "api/Task?taskGroupId=" + groupId);
            if ("request fail".equals(respMsg)) {
                return null;
            }
            JSONObject json = JSON.parseObject(respMsg);
            return JSONObject.toJavaObject(json, TaskResp.class);
        }

        /**
         * 获取未导出的数据, 记得获取后标记为已导出
         */
        private BaseResp getDataOfTask(String taskId) throws IOException {
            Map<String, String> params = new HashMap<String, String>();
            String respMsg = getBzyRespMsg(params, "api/notexportdata/gettop?taskId=" + taskId + "&size=" + 1000);
            if ("request fail".equals(respMsg)) {
                return null;
            }
            JSONObject json = JSON.parseObject(respMsg);
            return JSONObject.toJavaObject(json, BaseResp.class);
        }

        /**
         * 更新数据状态为已导出
         */
        private void updateDataByTaskId(String taskId) throws IOException {
            Map<String, String> params = new HashMap<String, String>();
            params.put("1", "1");
            String echo = postBzyRespMsg(params, "api/notexportdata/update?taskId=" + taskId);
            System.out.println(echo);
        }

        /**
         * post获取调用八爪鱼接口返回信息
         */
        private String postBzyRespMsg(Map<String, String> params, String apiPath) throws IOException {
            return doRequest(params, apiPath, true);
        }

        /**
         * get获取调用八爪鱼接口返回信息
         */
        private String getBzyRespMsg(Map<String, String> params, String apiPath) throws IOException {
            return doRequest(params, apiPath, false);
        }

        private String doRequest(Map<String, String> params, String apiPath, boolean isPost) throws IOException {
            HttpClientUtils httpClientUtils = new HttpClientUtils();
            HttpUriRequest reqMethod = null;
            if (isPost) {
                reqMethod = httpClientUtils.getPostRequest(params, url + apiPath);
            } else {
                reqMethod = httpClientUtils.getGetRequest(params, url + apiPath);
            }
            reqMethod.setHeader("Content-type", "application/x-www-form-urlencoded");
            reqMethod.setHeader("Authorization", "bearer " + tokenVo.getAccess_token());
            HttpClient httpclient = HttpClients.createDefault();
            String respMsg = null;
            HttpResponse response = httpclient.execute(reqMethod);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                HttpEntity resEntity = response.getEntity();
                respMsg = EntityUtils.toString(resEntity, "utf-8");
            } else {
                respMsg = "request fail";
            }
            return respMsg;
        }

        private String getUrl() {
            return url;
        }

        private void setUrl(String url) {
            this.url = url;
        }

        private String getUsername() {
            return username;
        }

        private void setUsername(String username) {
            this.username = username;
        }

        private String getPassword() {
            return password;
        }

        private void setPassword(String password) {
            this.password = password;
        }
    }

    private class HttpClientUtils {
        private HttpClientBuilder httpBuilder = null;
        private RequestConfig requestConfig = null;

        private HttpClientUtils() {
            //设置http的状态参数
            this.requestConfig = RequestConfig.custom()
                    .setSocketTimeout(5000)
                    .setConnectTimeout(5000)
                    .setConnectionRequestTimeout(5000)
                    .build();
        }

        private CloseableHttpClient getConnection() {
            return httpBuilder.build();
        }

        private HttpUriRequest getRequestMethod(Map<String, String> map, String url, String method) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            Set<Map.Entry<String, String>> entrySet = map.entrySet();
            for (Map.Entry<String, String> e : entrySet) {
                String name = e.getKey();
                String value = e.getValue();
                NameValuePair pair = new BasicNameValuePair(name, value);
                params.add(pair);
            }
            HttpUriRequest reqMethod = null;
            if ("POST".equals(method.toUpperCase())) {
                reqMethod = RequestBuilder.post().setUri(url)
                        .addParameters(params.toArray(new BasicNameValuePair[params.size()]))
                        .setConfig(requestConfig).build();
            } else if ("GET".equals(method.toUpperCase())) {
                reqMethod = RequestBuilder.get().setUri(url)
                        .addParameters(params.toArray(new BasicNameValuePair[params.size()]))
                        .setConfig(requestConfig).build();
            }
            return reqMethod;
        }

        /**
         * 获取GetRequest
         */
        private HttpUriRequest getGetRequest(Map<String, String> paramMap, String url) {
            return getRequestMethod(paramMap, url, "GET");
        }

        /**
         * 获取PostRequest
         */
        private HttpUriRequest getPostRequest(Map<String, String> paramMap, String url) {
            return getRequestMethod(paramMap, url, "POST");
        }
    }

    private static class TaskGroups {
        private List<TaskGroupVo> data = new ArrayList<TaskGroupVo>();
        private String error = "";
        private String error_Description = "";

        public List<TaskGroupVo> getData() {
            return data;
        }

        public void setData(List<TaskGroupVo> data) {
            this.data = data;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getError_Description() {
            return error_Description;
        }

        public void setError_Description(String error_Description) {
            this.error_Description = error_Description;
        }

        @Override
        public String toString() {
            return "TaskGroups [data=" + data + ", error=" + error + ", error_Description=" + error_Description + "]";
        }
    }

    private static class TaskGroupVo {
        private String taskGroupId;
        private String taskGroupName;

        public String getTaskGroupId() {
            return taskGroupId;
        }

        public void setTaskGroupId(String taskGroupId) {
            this.taskGroupId = taskGroupId;
        }

        public String getTaskGroupName() {
            return taskGroupName;
        }

        public void setTaskGroupName(String taskGroupName) {
            this.taskGroupName = taskGroupName;
        }

        @Override
        public String toString() {
            return "TaskGroupVo [taskGroupId=" + taskGroupId + ", taskGroupName=" + taskGroupName + "]";
        }
    }

    private static class TaskResp<T> {
        private List<T> data = null;
        private String error = "";
        private String error_Description = "";

        public List<T> getData() {
            return data;
        }

        public void setData(List<T> data) {
            this.data = data;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getError_Description() {
            return error_Description;
        }

        public void setError_Description(String error_Description) {
            this.error_Description = error_Description;
        }
    }

    private static class BaseResp {
        private NotexportdataVo data;
        private String error = "";
        private String error_Description = "";

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getError_Description() {
            return error_Description;
        }

        public void setError_Description(String error_Description) {
            this.error_Description = error_Description;
        }

        public NotexportdataVo getData() {
            return data;
        }

        public void setData(NotexportdataVo data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "NotexportdataResp [data=" + data + ", error=" + error + ", error_Description=" + error_Description
                    + "]";
        }
    }

    private static class NotexportdataVo {
        private Integer total;
        private Integer offset;
        private Integer currentTotal;
        private List<HashMap<String, Object>> dataList;

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public Integer getCurrentTotal() {
            return currentTotal;
        }

        public void setCurrentTotal(Integer currentTotal) {
            this.currentTotal = currentTotal;
        }

        public List<HashMap<String, Object>> getDataList() {
            return dataList;
        }

        public void setDataList(List<HashMap<String, Object>> dataList) {
            this.dataList = dataList;
        }

        public Integer getOffset() {
            return offset;
        }

        public void setOffset(Integer offset) {
            this.offset = offset;
        }

        @Override
        public String toString() {
            return "NotexportdataVo [total=" + total + ", offset=" + offset + ", currentTotal=" + currentTotal
                    + ", dataList=" + dataList + "]";
        }
    }

    public static class TokenResp {
        private String access_token;
        private String token_type;
        private String expires_in;
        private String refresh_token;

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getToken_type() {
            return token_type;
        }

        public void setToken_type(String token_type) {
            this.token_type = token_type;
        }

        public String getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(String expires_in) {
            this.expires_in = expires_in;
        }

        public String getRefresh_token() {
            return refresh_token;
        }

        public void setRefresh_token(String refresh_token) {
            this.refresh_token = refresh_token;
        }

        @Override
        public String toString() {
            return "TokenResp [access_token=" + access_token + ", token_type=" + token_type + ", expires_in=" + expires_in
                    + ", refresh_token=" + refresh_token + "]";
        }
    }
}
