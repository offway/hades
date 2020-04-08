package cn.offway.hades;

import cn.offway.hades.domain.*;
import cn.offway.hades.repository.PhMerchantBrandRepository;
import cn.offway.hades.service.*;
import cn.offway.hades.utils.HttpClientUtil;
import cn.offway.hades.utils.MathUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunpian.sdk.YunpianClient;
import com.yunpian.sdk.model.Result;
import com.yunpian.sdk.model.SmsSingleSend;
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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class HadesApplicationTests {

    @Autowired
    private PhSettlementInfoService phSettlementInfoService;

    @Autowired
    private PhSettlementDetailService phSettlementDetailService;

    @Autowired
    private PhOrderInfoService phOrderInfoService;

    @Autowired
    private PhMerchantService phMerchantService;

    @Autowired
    private PhMerchantBrandRepository merchantBrandRepository;

    @Autowired
    private PhMerchantBrandService merchantBrandService;

    @Autowired
    private PhBrandService brandService;

    @Autowired
    private PhArticleDraftService articleDraftService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void contextLoads() {
        System.out.println("hello!!!!33333");
    }

    @Test
    @Transactional
    @Rollback(false)
    public void insertOrder() {
        String[] orderList = new String[]{"34775565626634202", "34775565626634206", "34775565632475305", "34775565636336405", "34775565646040612", "34775565655543004", "34775565655543013", "34775565655543015", "34775565661404104", "34775565665245204", "34775565674747407", "34775565674747421"};
        for (String orderId : orderList) {
            List<PhOrderInfo> phOrderInfos = phOrderInfoService.findByPreorderNoAndStatus(orderId, "1", "2", "3");
            if (phOrderInfos.size() == 0) {
                System.out.println("empty set Id:" + orderId);
            }
            List<PhSettlementDetail> phSettlementDetails = new ArrayList<>();
            List<PhSettlementInfo> phSettlementInfos = new ArrayList<>();
            for (PhOrderInfo orderInfo : phOrderInfos) {
                PhSettlementDetail settlementDetail = new PhSettlementDetail();
                settlementDetail.setAmount(orderInfo.getAmount());
                settlementDetail.setCreateTime(new Date());
                Long merchantId = orderInfo.getMerchantId();
                PhMerchant phMerchant = phMerchantService.findOne(merchantId);
                settlementDetail.setCutRate(phMerchant.getRatio());
                settlementDetail.setCutAmount(orderInfo.getAmount() * phMerchant.getRatio() / 100);
                settlementDetail.setMailFee(orderInfo.getMailFee());
                settlementDetail.setMerchantId(orderInfo.getMerchantId());
                settlementDetail.setMerchantLogo(orderInfo.getMerchantLogo());
                settlementDetail.setMerchantName(orderInfo.getMerchantName());
                settlementDetail.setMVoucherAmount(orderInfo.getMVoucherAmount());
                settlementDetail.setPVoucherAmount(orderInfo.getPVoucherAmount());
                settlementDetail.setPromotionAmount(orderInfo.getPromotionAmount());
                settlementDetail.setPlatformPromotionAmount(orderInfo.getPlatformPromotionAmount());
                settlementDetail.setOrderNo(orderInfo.getOrderNo());
                settlementDetail.setPayChannel(orderInfo.getPayChannel());
                settlementDetail.setPayFee(String.format("%.2f", orderInfo.getAmount() * 0.003));//千分之三的手续费
                settlementDetail.setPrice(orderInfo.getPrice());
                settlementDetail.setWalletAmount(orderInfo.getWalletAmount());
                //计算结算金额
                double amount = (settlementDetail.getPrice() - settlementDetail.getMVoucherAmount() - settlementDetail.getPromotionAmount()) * (1D - phMerchant.getRatio() / 100);
                settlementDetail.setSettledAmount(amount);
                /* 状态[0-待结算,1-结算中,2-已结算] */
                settlementDetail.setStatus("0");
                settlementDetail.setRemark(orderInfo.getStatus());
                phSettlementDetails.add(settlementDetail);
                PhSettlementInfo settlementInfo = phSettlementInfoService.findByPid(merchantId);
                if (null == settlementInfo) {
                    settlementInfo = new PhSettlementInfo();
                    settlementInfo.setMerchantId(phMerchant.getId());
                    settlementInfo.setMerchantLogo(phMerchant.getLogo());
                    settlementInfo.setMerchantName(phMerchant.getName());
                    settlementInfo.setMerchantGoodsCount(0L);
                    settlementInfo.setStatisticalTime(new Date());
                    settlementInfo.setOrderAmount(0d);
                    settlementInfo.setOrderCount(0L);
                    settlementInfo.setSettledAmount(0d);
                    settlementInfo.setSettledCount(0L);
                    settlementInfo.setUnsettledAmount(0d);
                    settlementInfo.setUnsettledCount(0L);
                }
                settlementInfo.setOrderAmount(MathUtils.add(settlementDetail.getAmount(), settlementInfo.getOrderAmount()));
                settlementInfo.setOrderCount(settlementInfo.getOrderCount() + 1L);
                settlementInfo.setUnsettledAmount(MathUtils.add(settlementInfo.getUnsettledAmount(), settlementDetail.getSettledAmount()));
                settlementInfo.setUnsettledCount(settlementInfo.getUnsettledCount() + 1L);
                settlementInfo.setStatisticalTime(new Date());
                phSettlementInfos.add(settlementInfo);
            }
            //入库
            for (PhSettlementDetail detail : phSettlementDetails) {
                PhSettlementDetail obj = phSettlementDetailService.save(detail);
                System.out.println("saved Id:" + obj.getId());
            }
            for (PhSettlementInfo info : phSettlementInfos) {
                phSettlementInfoService.save(info);
            }
        }
    }

    @Test
    public void testKuaiDi100() {
        String key = "uyUDaSuE5009";
        Map<String, String> innerInnerParam = new HashMap<>();
        innerInnerParam.put("callbackurl", "https://admin.offway.cn/callback/express?uid=1111&oid=2222");
        String innerInnerParamStr = JSON.toJSONString(innerInnerParam);
        Map<String, Object> innerParam = new HashMap<>();
        innerParam.put("company", "shunfeng");
        innerParam.put("number", "356570849910");
        innerParam.put("key", key);
        innerParam.put("parameters", innerInnerParam);
        String innerParamStr = JSON.toJSONString(innerParam);
        Map<String, String> param = new HashMap<>();
        param.put("schema", "json");
        param.put("param", innerParamStr);
        String body = HttpClientUtil.post("https://poll.kuaidi100.com/poll", param);
        System.out.println(body);
    }

    @Test
    public void sendSMS() {
        //初始化clnt,使用单例方式
        YunpianClient client = new YunpianClient("d7c58b5d229428d28434533b17ff084a").init();
        //发送短信API
        Map<String, String> param = client.newParam(2);
        param.put(YunpianClient.MOBILE, "+8613761839483");
        param.put(YunpianClient.TEXT, "【很潮】您好，有一笔退款审核已通过，请通过后台确认打款~");
        Result<SmsSingleSend> r = client.sms().single_send(param);
        System.out.println(r.getMsg());
        //change the phone number
        param.put(YunpianClient.MOBILE, "+8613663478885");
        Result<SmsSingleSend> r2 = client.sms().single_send(param);
        System.out.println(r2.getMsg());
        //获取返回结果，返回码:r.getCode(),返回码描述:r.getMsg(),API结果:r.getData(),其他说明:r.getDetail(),调用异常:r.getThrowable()
        //账户:clnt.user().* 签名:clnt.sign().* 模版:clnt.tpl().* 短信:clnt.sms().* 语音:clnt.voice().* 流量:clnt.flow().* 隐私通话:clnt.call().*
        //释放clnt
        client.close();
    }

    @Test
    public void testSQL() {
        List<Object[]> o = merchantBrandRepository.checkEmptyBrand();
        for (Object[] a : o) {
            for (Object b : a) {
                System.out.println(b);
            }
        }
    }

    @Test
    public void testEmptyBrand() {
        //SELECT count(b.id) as ct,a.id as pk,a.name FROM phweb.ph_brand a left join phweb.ph_goods b on a.id = b.brand_id group by a.id
        List<Object[]> list = merchantBrandRepository.checkAllEmptyBrand();
        int ct = 0;
        for (Object[] item : list) {
            long count = Long.valueOf(String.valueOf(item[0]));
            if (count == 0) {
                long pk = Long.valueOf(String.valueOf(item[1]));
                String name = String.valueOf(item[2]);
                PhBrand brand = brandService.findOne(pk);
                if (brand != null) {
                    /* 状态[0-未上架,1-已上架] **/
                    brand.setStatus("0");
                    brandService.save(brand);
                    System.out.println(MessageFormat.format("{0}品牌被下架,ID 为{1}", name, brand.getId()));
                    logger.info(MessageFormat.format("{0}品牌被下架,ID 为{1}", name, brand.getId()));
                    ct++;
                }
            }
        }
        System.out.println(MessageFormat.format("品牌被下架总数为{0}", ct));
        logger.info(MessageFormat.format("品牌被下架总数为{0}", ct));
    }

    @Test
    public void doSync() throws IOException {
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
        private String username = "puhao2019";
        private String password = "puhao2018";
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
