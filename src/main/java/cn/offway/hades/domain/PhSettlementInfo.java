package cn.offway.hades.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * 商户结算表
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Entity
@Table(name = "ph_settlement_info")
public class PhSettlementInfo implements Serializable {

    /** ID **/
    private Long id;

    /** 商户ID **/
    private Long merchantId;

    /** 商户名称 **/
    private String merchantName;

    /** 商户LOGO **/
    private String merchantLogo;

    /** 商户在线商品数量 **/
    private Long merchantGoodsCount;

    /** 订单成交总金额 **/
    private Double orderAmount;

    /** 订单成交总笔数 **/
    private Long orderCount;

    /** 已结金额 **/
    private Double settledAmount;

    /** 已结订单笔数 **/
    private Long settledCount;

    /** 未结金额 **/
    private Double unsettledAmount;

    /** 未结订单笔数 **/
    private Long unsettledCount;

    /** 统计时间 **/
    private Date statisticalTime;

    /** 备注 **/
    private String remark;

    /** 版本号 **/
    private Long version;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "merchant_id", length = 11)
    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    @Column(name = "merchant_name", length = 100)
    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    @Column(name = "merchant_logo", length = 100)
    public String getMerchantLogo() {
        return merchantLogo;
    }

    public void setMerchantLogo(String merchantLogo) {
        this.merchantLogo = merchantLogo;
    }

    @Column(name = "merchant_goods_count", length = 11)
    public Long getMerchantGoodsCount() {
        return merchantGoodsCount;
    }

    public void setMerchantGoodsCount(Long merchantGoodsCount) {
        this.merchantGoodsCount = merchantGoodsCount;
    }

    @Column(name = "order_amount", precision = 15, scale = 2)
    public Double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
    }

    @Column(name = "order_count", length = 11)
    public Long getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Long orderCount) {
        this.orderCount = orderCount;
    }

    @Column(name = "settled_amount", precision = 15, scale = 2)
    public Double getSettledAmount() {
        return settledAmount;
    }

    public void setSettledAmount(Double settledAmount) {
        this.settledAmount = settledAmount;
    }

    @Column(name = "settled_count", length = 11)
    public Long getSettledCount() {
        return settledCount;
    }

    public void setSettledCount(Long settledCount) {
        this.settledCount = settledCount;
    }

    @Column(name = "unsettled_amount", precision = 15, scale = 2)
    public Double getUnsettledAmount() {
        return unsettledAmount;
    }

    public void setUnsettledAmount(Double unsettledAmount) {
        this.unsettledAmount = unsettledAmount;
    }

    @Column(name = "unsettled_count", length = 11)
    public Long getUnsettledCount() {
        return unsettledCount;
    }

    public void setUnsettledCount(Long unsettledCount) {
        this.unsettledCount = unsettledCount;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "statistical_time")
    public Date getStatisticalTime() {
        return statisticalTime;
    }

    public void setStatisticalTime(Date statisticalTime) {
        this.statisticalTime = statisticalTime;
    }

    @Column(name = "remark", length = 200)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "version", length = 11)
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

}
