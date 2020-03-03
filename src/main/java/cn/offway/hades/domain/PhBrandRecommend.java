package cn.offway.hades.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * 品牌推荐表
 *
 * @author tbw
 * @version $v: 1.0.0, $time:2020-03-02 14:23:35 Exp $
 */
@Entity
@Table(name = "ph_brand_recommend")
public class PhBrandRecommend implements Serializable {

    /** ID **/
    private Long id;

    /** 品牌ID **/
    private Long brandId;

    /** 品牌名称 **/
    private String name;

    /** 品牌LOGO **/
    private String logo;

    /** 品牌LOGO（大图，推荐展示用） **/
    private String logoBig;

    /** 是否推荐【0-否，1-是】 **/
    private String isRecommend;

    /** 推荐内容 **/
    private String content;

    /** 排序 **/
    private Long sort;

    /** 备注 **/
    private String remake;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "brand_id", length = 11)
    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    @Column(name = "name", length = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "logo", length = 100)
    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Column(name = "logo_big", length = 100)
    public String getLogoBig() {
        return logoBig;
    }

    public void setLogoBig(String logoBig) {
        this.logoBig = logoBig;
    }

    @Column(name = "is_recommend", length = 2)
    public String getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(String isRecommend) {
        this.isRecommend = isRecommend;
    }

    @Column(name = "content", length = 255)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(name = "sort", length = 11)
    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    @Column(name = "remake", length = 255)
    public String getRemake() {
        return remake;
    }

    public void setRemake(String remake) {
        this.remake = remake;
    }

}
