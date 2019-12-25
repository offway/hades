package cn.offway.hades.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * 定时任务记录表
 *
 * @author tbw
 * @version $v: 1.0.0, $time:2019-12-25 14:28:54 Exp $
 */
@Entity
@Table(name = "ph_discount_log")
public class PhDiscountLog implements Serializable {

    /** Id **/
    private Long id;

    /** 商品ID **/
    private String goodsid;

    /** 创建时间 **/
    private Date date;

    /** 开始时间 **/
    private Date stime;

    /** 结束时间 **/
    private Date etime;

    /** 折扣 **/
    private String discount;

    /** 创建用户 **/
    private String user;

    /** 类型 **/
    private String type;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "goodsid", length = 255)
    public String getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(String goodsid) {
        this.goodsid = goodsid;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "stime")
    public Date getStime() {
        return stime;
    }

    public void setStime(Date stime) {
        this.stime = stime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "etime")
    public Date getEtime() {
        return etime;
    }

    public void setEtime(Date etime) {
        this.etime = etime;
    }

    @Column(name = "discount", length = 20)
    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    @Column(name = "user", length = 100)
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Column(name = "type", length = 255)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
