package cn.offway.hades.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * 免费送助力
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-08-14 10:53:56 Exp $
 */
@Entity
@Table(name = "ph_free_delivery_boost")
public class PhFreeDeliveryBoost implements Serializable {

    /** ID **/
    private Long id;

    /** 免费送参与ID **/
    private Long freeDeliveryUserId;

    /** 助力用户ID **/
    private Long boostUserId;

    /** 助力用户昵称 **/
    private String boostNickname;

    /** 助力用户头像 **/
    private String boostHeadimgurl;

    /** 创建时间 **/
    private Date createTime;

    /** 备注 **/
    private String remark;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "free_delivery_user_id", length = 11)
    public Long getFreeDeliveryUserId() {
        return freeDeliveryUserId;
    }

    public void setFreeDeliveryUserId(Long freeDeliveryUserId) {
        this.freeDeliveryUserId = freeDeliveryUserId;
    }

    @Column(name = "boost_user_id", length = 11)
    public Long getBoostUserId() {
        return boostUserId;
    }

    public void setBoostUserId(Long boostUserId) {
        this.boostUserId = boostUserId;
    }

    @Column(name = "boost_nickname", length = 200)
    public String getBoostNickname() {
        return boostNickname;
    }

    public void setBoostNickname(String boostNickname) {
        this.boostNickname = boostNickname;
    }

    @Column(name = "boost_headimgurl", length = 500)
    public String getBoostHeadimgurl() {
        return boostHeadimgurl;
    }

    public void setBoostHeadimgurl(String boostHeadimgurl) {
        this.boostHeadimgurl = boostHeadimgurl;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "remark", length = 200)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
