package cn.offway.hades.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 明星同款
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Entity
@Table(name = "ph_starsame")
public class PhStarsame implements Serializable {

    /**
     * ID
     **/
    private Long id;

    /**
     * 标题
     **/
    private String title;

    /**
     * 图片URL
     **/
    private String imageUrl;

    /**
     * 明星姓名
     **/
    private String starName;

    /**
     * 浏览数
     **/
    private Long viewCount;

    /**
     * 点赞数
     **/
    private Long praiseCount;

    /**
     * 打call数
     **/
    private Long callCount;

    /**
     * 创建时间
     **/
    private Date createTime;

    /**
     * 备注
     **/
    private String remark;

    /**
     * 排序
     **/
    private Long sort;

    /**
     * 小程序排序
     **/
    private Long sortMini;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "title", length = 200)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "image_url", length = 100)
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Column(name = "star_name", length = 100)
    public String getStarName() {
        return starName;
    }

    public void setStarName(String starName) {
        this.starName = starName;
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

    @Column(name = "call_count", length = 11)
    public Long getCallCount() {
        return callCount;
    }

    public void setCallCount(Long callCount) {
        this.callCount = callCount;
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

    @Column(name = "sort", length = 11)
    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    @Column(name = "sort_mini", length = 11)
    public Long getSortMini() {
        return sortMini;
    }

    public void setSortMini(Long sortMini) {
        this.sortMini = sortMini;
    }

}
