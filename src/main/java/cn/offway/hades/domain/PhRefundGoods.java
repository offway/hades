package cn.offway.hades.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * 退款/退货商品明细
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-08-06 15:46:21 Exp $
 */
@Entity
@Table(name = "ph_refund_goods")
public class PhRefundGoods implements Serializable {

    /** ID **/
    private Long id;

    /** 退款ID **/
    private Long refundId;

    /** 订单商品ID **/
    private Long orderGoodsId;

    /** 退款商品数量 **/
    private Long goodsCount;

    /** 创建时间 **/
    private Date createTime;

    /** 备注 **/
    private String remark;

    /** 版本号 **/
    private Long version;

    /** 商品ID **/
    private Long goodsId;

    /** 商品名称 **/
    private String goodsName;

    /** 价格 **/
    private Double price;

    /** 商品库存ID **/
    private Long fromStockId;

    /** 商品图片 **/
    private String fromStockImage;

    /** 换后商品库存ID **/
    private Long toStockId;

    /** 换后商品库存描述 **/
    private String toStockDesc;

    /** 换后商品图片 **/
    private String toStockImage;

    /** 换货原因 **/
    private String reason;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "refund_id", length = 11)
    public Long getRefundId() {
        return refundId;
    }

    public void setRefundId(Long refundId) {
        this.refundId = refundId;
    }

    @Column(name = "order_goods_id", length = 11)
    public Long getOrderGoodsId() {
        return orderGoodsId;
    }

    public void setOrderGoodsId(Long orderGoodsId) {
        this.orderGoodsId = orderGoodsId;
    }

    @Column(name = "goods_count", length = 11)
    public Long getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(Long goodsCount) {
        this.goodsCount = goodsCount;
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

    @Column(name = "version", length = 11)
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Column(name = "goods_id", length = 11)
    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    @Column(name = "goods_name", length = 100)
    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    @Column(name = "price", precision = 15, scale = 2)
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Column(name = "from_stock_id", length = 11)
    public Long getFromStockId() {
        return fromStockId;
    }

    public void setFromStockId(Long fromStockId) {
        this.fromStockId = fromStockId;
    }

    @Column(name = "from_stock_image", length = 100)
    public String getFromStockImage() {
        return fromStockImage;
    }

    public void setFromStockImage(String fromStockImage) {
        this.fromStockImage = fromStockImage;
    }

    @Column(name = "to_stock_id", length = 11)
    public Long getToStockId() {
        return toStockId;
    }

    public void setToStockId(Long toStockId) {
        this.toStockId = toStockId;
    }

    @Column(name = "to_stock_desc", length = 100)
    public String getToStockDesc() {
        return toStockDesc;
    }

    public void setToStockDesc(String toStockDesc) {
        this.toStockDesc = toStockDesc;
    }

    @Column(name = "to_stock_image", length = 100)
    public String getToStockImage() {
        return toStockImage;
    }

    public void setToStockImage(String toStockImage) {
        this.toStockImage = toStockImage;
    }

    @Column(name = "reason", length = 200)
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
