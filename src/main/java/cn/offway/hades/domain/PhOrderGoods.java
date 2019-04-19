package cn.offway.hades.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * 订单商品
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Entity
@Table(name = "ph_order_goods")
public class PhOrderGoods implements Serializable {

    /** ID **/
    private Long id;

    /** 订单号 **/
    private String orderNo;

    /** 价格 **/
    private Double price;

    /** 商品ID **/
    private Long goodsId;

    /** 商品名称 **/
    private String goodsName;

    /** 商品图片 **/
    private String goodsImage;

    /** 品牌ID **/
    private Long brandId;

    /** 品牌名称 **/
    private String brandName;

    /** 品牌LOGO **/
    private String brandLogo;

    /** 商品数量 **/
    private Long goodsCount;

    /** 创建时间 **/
    private Date createTime;

    /** 备注 **/
    private String remark;

    /** 商户ID **/
    private Long merchantId;

    /** 商户LOGO **/
    private String merchantLogo;

    /** 商户名称 **/
    private String merchantName;

    /** 商品库存ID **/
    private Long goodsStockId;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "order_no", length = 50)
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @Column(name = "price", precision = 15, scale = 2)
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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

    @Column(name = "goods_image", length = 100)
    public String getGoodsImage() {
        return goodsImage;
    }

    public void setGoodsImage(String goodsImage) {
        this.goodsImage = goodsImage;
    }

    @Column(name = "brand_id", length = 11)
    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    @Column(name = "brand_name", length = 50)
    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    @Column(name = "brand_logo", length = 200)
    public String getBrandLogo() {
        return brandLogo;
    }

    public void setBrandLogo(String brandLogo) {
        this.brandLogo = brandLogo;
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

    @Column(name = "merchant_id", length = 11)
    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    @Column(name = "merchant_logo", length = 100)
    public String getMerchantLogo() {
        return merchantLogo;
    }

    public void setMerchantLogo(String merchantLogo) {
        this.merchantLogo = merchantLogo;
    }

    @Column(name = "merchant_name", length = 100)
    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    @Column(name = "goods_stock_id", length = 11)
    public Long getGoodsStockId() {
        return goodsStockId;
    }

    public void setGoodsStockId(Long goodsStockId) {
        this.goodsStockId = goodsStockId;
    }

}
