package cn.offway.hades.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 品牌推荐表
 *
 * @author tbw
 * @version $v: 1.0.0, $time:2020-03-02 14:23:35 Exp $
 */
@Entity
@Table(name = "ph_brand")
public class PhBrandRecommend implements Serializable {

    /** ID **/
    private Long id;

    /** 品牌名称 **/
    private String name;

    /** 品牌LOGO **/
    private String logo;

    /** 品牌LOGO(大) **/
    private String logoBig;

    /** 类型[0-国内品牌，1-国际品牌] **/
    private String type;

    /** 是否小程序推荐[0-否，1-是] **/
    private String isRecommendMini;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "name", length = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "logo", length = 200)
    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Column(name = "logo_big", length = 200)
    public String getLogoBig() {
        return logoBig;
    }

    public void setLogoBig(String logoBig) {
        this.logoBig = logoBig;
    }

    @Column(name = "type", length = 2)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "is_recommend_mini", length = 2)
    public String getIsRecommendMini() {
        return isRecommendMini;
    }

    public void setIsRecommendMini(String isRecommendMini) {
        this.isRecommendMini = isRecommendMini;
    }
}
