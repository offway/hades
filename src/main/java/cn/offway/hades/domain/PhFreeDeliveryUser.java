package cn.offway.hades.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * 免费送参与用户
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-08-14 10:53:56 Exp $
 */
@Entity
@Table(name = "ph_free_delivery_user")
public class PhFreeDeliveryUser implements Serializable {

    /** ID **/
    private Long id;

    /** 免费送ID **/
    private Long freeDeliveryId;

    /** 用户ID **/
    private Long userId;

    /** 用户昵称 **/
    private String nickname;

    /** 用户头像 **/
    private String headimgurl;

    /** 当前助力次数 **/
    private Long currentCount;

    /** 需要助力次数 **/
    private Long boostCount;

    /** 创建时间 **/
    private Date createTime;

    /** 最后助力时间 **/
    private Date lastTime;

    /** 备注 **/
    private String remark;

    /** 版本号 **/
    private Long version;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "free_delivery_id", length = 11)
    public Long getFreeDeliveryId() {
        return freeDeliveryId;
    }

    public void setFreeDeliveryId(Long freeDeliveryId) {
        this.freeDeliveryId = freeDeliveryId;
    }

    @Column(name = "user_id", length = 11)
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Column(name = "nickname", length = 200)
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Column(name = "headimgurl", length = 500)
    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    @Column(name = "current_count", length = 11)
    public Long getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(Long currentCount) {
        this.currentCount = currentCount;
    }

    @Column(name = "boost_count", length = 11)
    public Long getBoostCount() {
        return boostCount;
    }

    public void setBoostCount(Long boostCount) {
        this.boostCount = boostCount;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_time")
    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    @Column(name = "remark", length = 200)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "version", length = 11)
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

}
