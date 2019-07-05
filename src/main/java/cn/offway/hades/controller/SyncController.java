package cn.offway.hades.controller;

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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/sync")
public class SyncController {
    @RequestMapping("/bzy")
    public boolean sync() throws IOException {
        BzyClient bzyClient = new BzyClient();
        //创建任务列表
        List<Map<String, String>> taskAll = new ArrayList<Map<String, String>>();
        TaskGroups taskGroups = bzyClient.getBzyTaskGroups();
        if (taskGroups == null || !"success".equals(taskGroups.getError())) {
            return false;
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
        return true;
    }

    //添加头条信息
    private void addDynamic(List<HashMap<String, Object>> dataList) {
        if (dataList.size() > 0) {
            for (Map<String, Object> dataMap : dataList) {
                String consultationtitle = MapUtils.getString(dataMap, "consultationtitle");
                String consultationimg = MapUtils.getString(dataMap, "consultationimg");
                String consultationcontent = MapUtils.getString(dataMap, "consultationcontent");
                if (consultationtitle != null && !"".equals(consultationimg) && consultationcontent != null && !"".equals(consultationcontent)) {
                    //头条ID
                    dataMap.put("consultationid", UUID.randomUUID().toString().replace("-", ""));
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    //头条发布时间
                    //dataMap.put("ispublishtime", sdf.format(new Date()));
                    //创建时间
                    dataMap.put("createid", 1);
                    dataMap.put("updateid", 1);
                    dataMap.put("createtime", sdf.format(new Date()));
                    dataMap.put("updatetime", sdf.format(new Date()));
                    dataMap.put("isaudit", 1);
                    dataMap.put("establish", 2);
                    //头条内容
                    if (null != consultationcontent && consultationcontent.indexOf("img{max-width:100%;height:auto;}") < 0) {
                        consultationcontent = "<style>img{max-width:100%;height:auto;}</style>".concat(consultationcontent);
                    }
                    dataMap.put("consultationcontent", consultationcontent);
//                    ApiDtConsultationService.addDynamic(dataMap);
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

    private class TaskGroups {
        private List<TaskGroupVo> data = new ArrayList<TaskGroupVo>();
        private String error = "";
        private String error_Description = "";

        private List<TaskGroupVo> getData() {
            return data;
        }

        private void setData(List<TaskGroupVo> data) {
            this.data = data;
        }

        private String getError() {
            return error;
        }

        private void setError(String error) {
            this.error = error;
        }

        private String getError_Description() {
            return error_Description;
        }

        private void setError_Description(String error_Description) {
            this.error_Description = error_Description;
        }

        @Override
        public String toString() {
            return "TaskGroups [data=" + data + ", error=" + error + ", error_Description=" + error_Description + "]";
        }
    }

    private class TaskGroupVo {
        private String taskGroupId;
        private String taskGroupName;

        private String getTaskGroupId() {
            return taskGroupId;
        }

        private void setTaskGroupId(String taskGroupId) {
            this.taskGroupId = taskGroupId;
        }

        private String getTaskGroupName() {
            return taskGroupName;
        }

        private void setTaskGroupName(String taskGroupName) {
            this.taskGroupName = taskGroupName;
        }

        @Override
        public String toString() {
            return "TaskGroupVo [taskGroupId=" + taskGroupId + ", taskGroupName=" + taskGroupName + "]";
        }
    }

    private class TaskResp<T> {
        private List<T> data = null;
        private String error = "";
        private String error_Description = "";

        private List<T> getData() {
            return data;
        }

        private void setData(List<T> data) {
            this.data = data;
        }

        private String getError() {
            return error;
        }

        private void setError(String error) {
            this.error = error;
        }

        private String getError_Description() {
            return error_Description;
        }

        private void setError_Description(String error_Description) {
            this.error_Description = error_Description;
        }
    }

    private class BaseResp {
        private NotexportdataVo data;
        private String error = "";
        private String error_Description = "";

        private String getError() {
            return error;
        }

        private void setError(String error) {
            this.error = error;
        }

        private String getError_Description() {
            return error_Description;
        }

        private void setError_Description(String error_Description) {
            this.error_Description = error_Description;
        }

        private NotexportdataVo getData() {
            return data;
        }

        private void setData(NotexportdataVo data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "NotexportdataResp [data=" + data + ", error=" + error + ", error_Description=" + error_Description
                    + "]";
        }
    }

    private class NotexportdataVo {
        private Integer total;
        private Integer offset;
        private Integer currentTotal;
        private List<HashMap<String, Object>> dataList;

        private Integer getTotal() {
            return total;
        }

        private void setTotal(Integer total) {
            this.total = total;
        }

        private Integer getCurrentTotal() {
            return currentTotal;
        }

        private void setCurrentTotal(Integer currentTotal) {
            this.currentTotal = currentTotal;
        }

        private List<HashMap<String, Object>> getDataList() {
            return dataList;
        }

        private void setDataList(List<HashMap<String, Object>> dataList) {
            this.dataList = dataList;
        }

        private Integer getOffset() {
            return offset;
        }

        private void setOffset(Integer offset) {
            this.offset = offset;
        }

        @Override
        public String toString() {
            return "NotexportdataVo [total=" + total + ", offset=" + offset + ", currentTotal=" + currentTotal
                    + ", dataList=" + dataList + "]";
        }
    }

    private class TokenResp {
        private String access_token;
        private String token_type;
        private String expires_in;
        private String refresh_token;

        private String getAccess_token() {
            return access_token;
        }

        private void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        private String getToken_type() {
            return token_type;
        }

        private void setToken_type(String token_type) {
            this.token_type = token_type;
        }

        private String getExpires_in() {
            return expires_in;
        }

        private void setExpires_in(String expires_in) {
            this.expires_in = expires_in;
        }

        private String getRefresh_token() {
            return refresh_token;
        }

        private void setRefresh_token(String refresh_token) {
            this.refresh_token = refresh_token;
        }

        @Override
        public String toString() {
            return "TokenResp [access_token=" + access_token + ", token_type=" + token_type + ", expires_in=" + expires_in
                    + ", refresh_token=" + refresh_token + "]";
        }
    }
}
