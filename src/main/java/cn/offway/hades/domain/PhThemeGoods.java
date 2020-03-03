package cn.offway.hades.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * 主题商品表
 *
 * @author tbw
 * @version $v: 1.0.0, $time:2020-03-03 14:49:20 Exp $
 */
@Entity
@Table(name = "ph_theme_goods")
public class PhThemeGoods implements Serializable {

    /** ID **/
    private Long id;

    /** 主题ID[ph_theme] **/
    private Long themeId;

    /** 商品ID[见ph_goods] **/
    private Long goodsId;

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

    @Column(name = "theme_id", length = 11)
    public Long getThemeId() {
        return themeId;
    }

    public void setThemeId(Long themeId) {
        this.themeId = themeId;
    }

    @Column(name = "goods_id", length = 11)
    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
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
