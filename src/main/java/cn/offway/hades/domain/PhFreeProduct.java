package cn.offway.hades.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * 免费送产品表
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-08-14 13:29:18 Exp $
 */
@Entity
@Table(name = "ph_free_product")
public class PhFreeProduct implements Serializable {

    /** ID **/
    private Long id;

    /** 活动名称 **/
    private String name;

    /** 开始时间 **/
    private Date beginTime;

    /** 截止时间 **/
    private Date endTime;

    /** 顶部主题图片 **/
    private String image;

    /** 创建时间 **/
    private Date createTime;

    /** 备注[不要展示在外面让他们瞎填] **/
    private String remark;

    /** 商品种类 **/
    private String goodsType;

    /** 总商品数量 **/
    private Long sumGooodsCount;

    /** 总助力次数 **/
    private Long sumBoostCount;

    /** 创建人 **/
    private String creator;


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

    @Column(name = "image", length = 100)
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    @Column(name = "goods_type", length = 100)
    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    @Column(name = "sum_gooods_count", length = 11)
    public Long getSumGooodsCount() {
        return sumGooodsCount;
    }

    public void setSumGooodsCount(Long sumGooodsCount) {
        this.sumGooodsCount = sumGooodsCount;
    }

    @Column(name = "sum_boost_count", length = 11)
    public Long getSumBoostCount() {
        return sumBoostCount;
    }

    public void setSumBoostCount(Long sumBoostCount) {
        this.sumBoostCount = sumBoostCount;
    }

    @Column(name = "creator", length = 100)
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

}
