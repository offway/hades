package cn.offway.hades.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * 促销活动规则
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Entity
@Table(name = "ph_promotion_rule")
public class PhPromotionRule implements Serializable {

    /** ID **/
    private Long id;

    /** 促销活动ID **/
    private Long promotionId;

    /** 折扣件数 **/
    private Long discountNum;

    /** 折扣率 **/
    private Double discountRate;

    /** 满减条件金额 **/
    private Double reduceLimit;

    /** 满减优惠金额 **/
    private Double reduceAmount;

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

    @Column(name = "promotion_id", length = 11)
    public Long getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Long promotionId) {
        this.promotionId = promotionId;
    }

    @Column(name = "discount_num", length = 11)
    public Long getDiscountNum() {
        return discountNum;
    }

    public void setDiscountNum(Long discountNum) {
        this.discountNum = discountNum;
    }

    @Column(name = "discount_rate", precision = 15, scale = 2)
    public Double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(Double discountRate) {
        this.discountRate = discountRate;
    }

    @Column(name = "reduce_limit", precision = 15, scale = 2)
    public Double getReduceLimit() {
        return reduceLimit;
    }

    public void setReduceLimit(Double reduceLimit) {
        this.reduceLimit = reduceLimit;
    }

    @Column(name = "reduce_amount", precision = 15, scale = 2)
    public Double getReduceAmount() {
        return reduceAmount;
    }

    public void setReduceAmount(Double reduceAmount) {
        this.reduceAmount = reduceAmount;
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
