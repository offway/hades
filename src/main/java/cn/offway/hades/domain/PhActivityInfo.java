package cn.offway.hades.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * 活动表-每日福利
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
@Entity
@Table(name = "ph_activity_info")
public class PhActivityInfo implements Serializable {

    /** 活动ID **/
    private Long id;

    /** 活动名称 **/
    private String name;

    /** 活动描述 **/
    private String productDesc;

    /** 规格 **/
    private String specification;

    /** 中奖人数 **/
    private Long winNum;

    /** 参与人数 **/
    private Long joinNum;

    /** 封面图片 **/
    private String image;

    /** 分享标题 **/
    private String shareTitle;

    /** 分享描述 **/
    private String shareDesc;

    /** 活动开始时间 **/
    private Date beginTime;

    /** 活动截止时间 **/
    private Date endTime;

    /** 状态[0-未上架,1-已上架] **/
    private String status;

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

    @Column(name = "name", length = 200)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "product_desc", length = 200)
    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    @Column(name = "specification", length = 200)
    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    @Column(name = "win_num", length = 11)
    public Long getWinNum() {
        return winNum;
    }

    public void setWinNum(Long winNum) {
        this.winNum = winNum;
    }

    @Column(name = "join_num", length = 11)
    public Long getJoinNum() {
        return joinNum;
    }

    public void setJoinNum(Long joinNum) {
        this.joinNum = joinNum;
    }

    @Column(name = "image", length = 100)
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Column(name = "share_title", length = 100)
    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    @Column(name = "share_desc", length = 200)
    public String getShareDesc() {
        return shareDesc;
    }

    public void setShareDesc(String shareDesc) {
        this.shareDesc = shareDesc;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "begin_time")
    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_time")
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Column(name = "status", length = 2)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
