package cn.offway.hades.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * 明星信息表
 *
 * @author tbw
 * @version $v: 1.0.0, $time:2020-03-02 14:23:35 Exp $
 */
@Entity
@Table(name = "ph_celebrity_list")
public class PhCelebrityList implements Serializable {

    /** ID **/
    private Long id;

    /** 明星名称 **/
    private String name;

    /** 明星头像 **/
    private String headimgurl;

    /** 职业 **/
    private String profession;

    /** 真实粉丝数量 **/
    private Long fansSum;

    /** 虚拟粉丝数量 **/
    private Long fans;

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

    @Column(name = "headimgurl", length = 500)
    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    @Column(name = "profession", length = 200)
    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    @Column(name = "fans_sum", length = 11)
    public Long getFansSum() {
        return fansSum;
    }

    public void setFansSum(Long fansSum) {
        this.fansSum = fansSum;
    }

    @Column(name = "fans", length = 11)
    public Long getFans() {
        return fans;
    }

    public void setFans(Long fans) {
        this.fans = fans;
    }

    @Column(name = "remark", length = 200)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
