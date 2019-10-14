package cn.offway.hades.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "alipay-sdk")
public class AlipayProperties {

    /**
     * 支付宝网关
     **/
    private String url;

    /**
     * APPID即创建应用后生成
     **/
    private String appid;

    /**
     * 开发者应用私钥，由开发者自己生成
     **/
    private String privatekey;

    /**
     * 支付宝公钥，由支付宝生成
     **/
    private String publickey;

    /**
     * 设置HTTP接入域名
     **/
    private String accountEndpoint;

    /**
     * MQ实例 ID
     **/
    private String instanceId;

    /**
     * 阿里云身份验证，在阿里云服务器管理控制台创建
     **/
    private String accessId;

    /**
     * 阿里云身份验证，在阿里云服务器管理控制台创建
     **/
    private String accessKey;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPrivatekey() {
        return privatekey;
    }

    public void setPrivatekey(String privatekey) {
        this.privatekey = privatekey;
    }

    public String getPublickey() {
        return publickey;
    }

    public void setPublickey(String publickey) {
        this.publickey = publickey;
    }

    public String getAccountEndpoint() {
        return accountEndpoint;
    }

    public void setAccountEndpoint(String accountEndpoint) {
        this.accountEndpoint = accountEndpoint;
    }

    public String getAccessId() {
        return accessId;
    }

    public void setAccessId(String accessId) {
        this.accessId = accessId;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}