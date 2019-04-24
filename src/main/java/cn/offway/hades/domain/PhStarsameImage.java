package cn.offway.hades.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * 明星同款banner图片
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Entity
@Table(name = "ph_starsame_image")
public class PhStarsameImage implements Serializable {

    /** ID **/
    private Long id;

    /** 明星同款ID **/
    private Long starsameId;

    /** 明星同款标题 **/
    private String starsameTitle;

    /** 图片URL **/
    private String imageUrl;

    /** 排序 **/
    private Long sort;

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

    @Column(name = "starsame_id", length = 11)
    public Long getStarsameId() {
        return starsameId;
    }

    public void setStarsameId(Long starsameId) {
        this.starsameId = starsameId;
    }

    @Column(name = "starsame_title", length = 200)
    public String getStarsameTitle() {
        return starsameTitle;
    }

    public void setStarsameTitle(String starsameTitle) {
        this.starsameTitle = starsameTitle;
    }

    @Column(name = "image_url", length = 100)
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Column(name = "sort", length = 11)
    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
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
