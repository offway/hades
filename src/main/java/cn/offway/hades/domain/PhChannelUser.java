package cn.offway.hades.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 * 用户推广渠道表
 *
 * @author tbw
 * @version $v: 1.0.0, $time:2020-01-10 11:33:13 Exp $
 */
@Entity
@Table(name = "ph_channel_user")
public class PhChannelUser implements Serializable {

    /** ID **/
    private Long id;

    /** 渠道名称 **/
    private String channelName;

    /** 渠道代码 **/
    private String channel;

    /** 渠道绑定ID **/
    private Long userId;

    /** 渠道绑定ID名称 **/
    private String userName;

    /** 渠道绑定ID电话 **/
    private String userPhone;

    /** 渠道绑定ID头像 **/
    private String userHeadimgurl;

    /** 渠道抽成比例 **/
    private Double proportion;

    /** 备注 **/
    private String remark;

    /** 创建时间 **/
    private Date createTime;

    /**  **/
    private Long number;

    /**  **/
    private Long adminId;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "channel_name", length = 200)
    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    @Column(name = "channel", length = 50)
    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    @Column(name = "user_id", length = 11)
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Column(name = "user_name", length = 255)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "user_phone", length = 255)
    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    @Column(name = "user_headimgurl", length = 255)
    public String getUserHeadimgurl() {
        return userHeadimgurl;
    }

    public void setUserHeadimgurl(String userHeadimgurl) {
        this.userHeadimgurl = userHeadimgurl;
    }

    @Column(name = "proportion", precision = 15, scale = 2)
    public Double getProportion() {
        return proportion;
    }

    public void setProportion(Double proportion) {
        this.proportion = proportion;
    }

    @Column(name = "remark", length = 255)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "number", length = 11)
    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    @Column(name = "admin_id", length = 11)
    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

}
