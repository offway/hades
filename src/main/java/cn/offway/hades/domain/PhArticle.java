package cn.offway.hades.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * 文章
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Entity
@Table(name = "ph_article")
public class PhArticle implements Serializable {

    /** ID **/
    private Long id;

    /** 名称 **/
    private String name;

    /** 标题 **/
    private String title;

    /** 图片 **/
    private String image;

    /** 视频 **/
    private String video;

    /** 类型[0-资讯，1-专题，2-视频] **/
    private String type;

    /** 标签 **/
    private String tag;

    /** 内容 **/
    private String content;

    /** 推荐商品id,多个以,相隔 **/
    private String goodsIds;

    /** 状态[0-待审核，1-通过，2-不通过] **/
    private String status;

    /** 浏览数 **/
    private Long viewCount;

    /** 点赞数 **/
    private Long praiseCount;

    /** 创建时间 **/
    private Date createTime;

    /** 审批人 **/
    private String approver;

    /** 审批时间 **/
    private Date approval;

    /** 拒绝原因 **/
    private String approvalContent;

    /** 备注 **/
    private String remark;

    /** 排序 **/
    private Long sort;


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

    @Column(name = "title", length = 30)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "image", length = 100)
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Column(name = "video", length = 100)
    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    @Column(name = "type", length = 2)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "tag", length = 100)
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(name = "goods_ids", length = 1000)
    public String getGoodsIds() {
        return goodsIds;
    }

    public void setGoodsIds(String goodsIds) {
        this.goodsIds = goodsIds;
    }

    @Column(name = "status", length = 2)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "view_count", length = 11)
    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    @Column(name = "praise_count", length = 11)
    public Long getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(Long praiseCount) {
        this.praiseCount = praiseCount;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "approver", length = 50)
    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "approval")
    public Date getApproval() {
        return approval;
    }

    public void setApproval(Date approval) {
        this.approval = approval;
    }

    @Column(name = "approval_content", length = 200)
    public String getApprovalContent() {
        return approvalContent;
    }

    public void setApprovalContent(String approvalContent) {
        this.approvalContent = approvalContent;
    }

    @Column(name = "remark", length = 200)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "sort", length = 11)
    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

}
