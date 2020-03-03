package cn.offway.hades.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * 主题列表
 *
 * @author tbw
 * @version $v: 1.0.0, $time:2020-03-03 14:49:20 Exp $
 */
@Entity
@Table(name = "ph_theme")
public class PhTheme implements Serializable {

    /** ID **/
    private Long id;

    /** 主题名称 **/
    private String name;

    /** 图片URL **/
    private String imageUrl;

    /** 主题内容 **/
    private String content;

    /** 创建时间 **/
    private Date createTime;

    /** 备注 **/
    private String remark;

    /** 是否推荐【0-否，1-是】 **/
    private String isRecommend;


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

    @Column(name = "image_url", length = 100)
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Column(name = "content", length = 500)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    @Column(name = "is_recommend", length = 2)
    public String getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(String isRecommend) {
        this.isRecommend = isRecommend;
    }

}
