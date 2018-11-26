package cn.offway.hades.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * 活动参与表-每日福利
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
@Entity
@Table(name = "ph_activity_join")
public class PhActivityJoin implements Serializable {

    /** ID **/
    private Long id;

    /** 活动ID **/
    private Long activityId;

    /** 活动名称 **/
    private String activityName;

    /** 封面图片 **/
    private String activityImage;

    /** 微信用户ID **/
    private String unionid;

    /** 微信用户头像 **/
    private String headUrl;

    /** 微信用户昵称 **/
    private String nickName;

    /** 是否中奖[0-否,1-是] **/
    private String isLucky;

    /** 创建时间 **/
    private Date createTime;

    /** form_id小程序推送使用 **/
    private String formId;

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

    @Column(name = "activity_id", length = 11)
    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    @Column(name = "activity_name", length = 200)
    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    @Column(name = "activity_image", length = 100)
    public String getActivityImage() {
        return activityImage;
    }

    public void setActivityImage(String activityImage) {
        this.activityImage = activityImage;
    }

    @Column(name = "unionid", length = 200)
    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    @Column(name = "head_url", length = 500)
    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    @Column(name = "nick_name", length = 200)
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Column(name = "is_lucky", length = 2)
    public String getIsLucky() {
        return isLucky;
    }

    public void setIsLucky(String isLucky) {
        this.isLucky = isLucky;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "form_id", length = 100)
    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    @Column(name = "remark", length = 200)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
