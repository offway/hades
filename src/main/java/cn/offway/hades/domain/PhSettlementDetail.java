package cn.offway.hades.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * 商户结算明细表
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Entity
@Table(name = "ph_settlement_detail")
public class PhSettlementDetail extends BaseRowModel implements Serializable {

    /** ID **/
    @ExcelProperty(value = "序号",index = 0)
    private Long id;

    /** 商户ID **/
    private Long merchantId;

    /** 商户名称 **/
    private String merchantName;

    /** 商户LOGO **/
    private String merchantLogo;

    /** 订单号 **/
    @ExcelProperty(value = "订单号",index = 1)
    private String orderNo;

    /** 订单号副本 for 商品名称 **/
    @ExcelProperty(value = "商品名称",index = 2)
    private String orderNoCopy;

    /** 订单总价 **/
    @ExcelProperty(value = "订单总价",index = 3)
    private Double price;

    /** 实付金额 **/
    @ExcelProperty(value = "实付金额",index = 4)
    private Double amount;

    /** 店铺优惠券金额 **/
    @ExcelProperty(value = "店铺优惠券金额",index = 5)
    private Double mVoucherAmount;

    /** 平台优惠券金额 **/
    private Double pVoucherAmount;

    /** 商户促销优惠金额  **/
    private Double promotionAmount;

    /** 平台促销优惠金额  **/
    private Double platformPromotionAmount;

    /** 钱包金额 **/
    private Double walletAmount;

    /** 运费 **/
    @ExcelProperty(value = "运费",index = 6)
    private Double mailFee;

    /** 支付渠道[wxpay-微信支付,alipay-支付宝] **/
    @ExcelProperty(value = "支付渠道",index = 7)
    private String payChannel;

    /** 支付渠道手续费 **/
    private String payFee;

    /** 抽成金额 **/
    @ExcelProperty(value = "抽成金额",index = 9)
    private Double cutAmount;

    /** 抽成比率 **/
    @ExcelProperty(value = "抽成比率",index = 8)
    private Double cutRate;

    /** 结算金额 **/
    private Double settledAmount;

    /** 状态[0-待结算,1-结算中,2-已结算] **/
    @ExcelProperty(value = "结算状态",index = 11)
    private String status;

    /** 结算人 **/
    private String settledName;

    /** 结算时间 **/
    @ExcelProperty(value = "结算时间",index = 13)
    private Date settledTime;

    /** 创建时间 **/
    @ExcelProperty(value = "创建时间",index = 12)
    private Date createTime;

    /** 备注 **/
    @ExcelProperty(value = "订单状态",index = 10)
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

    @Column(name = "order_no", length = 50)
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
        this.orderNoCopy = orderNo;
    }

    @Column(name = "price", precision = 15, scale = 2)
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Column(name = "amount", precision = 15, scale = 2)
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Column(name = "m_voucher_amount", precision = 15, scale = 2)
    public Double getMVoucherAmount() {
        return mVoucherAmount;
    }

    public void setMVoucherAmount(Double mVoucherAmount) {
        this.mVoucherAmount = mVoucherAmount;
    }

    @Column(name = "p_voucher_amount", precision = 15, scale = 2)
    public Double getPVoucherAmount() {
        return pVoucherAmount;
    }

    public void setPVoucherAmount(Double pVoucherAmount) {
        this.pVoucherAmount = pVoucherAmount;
    }

    @Column(name = "wallet_amount", precision = 15, scale = 2)
    public Double getWalletAmount() {
        return walletAmount;
    }

    public void setWalletAmount(Double walletAmount) {
        this.walletAmount = walletAmount;
    }

    @Column(name = "mail_fee", precision = 15, scale = 2)
    public Double getMailFee() {
        return mailFee;
    }

    public void setMailFee(Double mailFee) {
        this.mailFee = mailFee;
    }

    @Column(name = "pay_channel", length = 50)
    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }

    @Column(name = "pay_fee", length = 50)
    public String getPayFee() {
        return payFee;
    }

    public void setPayFee(String payFee) {
        this.payFee = payFee;
    }

    @Column(name = "cut_amount", precision = 15, scale = 2)
    public Double getCutAmount() {
        return cutAmount;
    }

    public void setCutAmount(Double cutAmount) {
        this.cutAmount = cutAmount;
    }

    @Column(name = "cut_rate", precision = 15, scale = 2)
    public Double getCutRate() {
        return cutRate;
    }

    public void setCutRate(Double cutRate) {
        this.cutRate = cutRate;
    }

    @Column(name = "settled_amount", precision = 15, scale = 2)
    public Double getSettledAmount() {
        return settledAmount;
    }

    public void setSettledAmount(Double settledAmount) {
        this.settledAmount = settledAmount;
    }

    @Column(name = "status", length = 2)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "settled_name", length = 200)
    public String getSettledName() {
        return settledName;
    }

    public void setSettledName(String settledName) {
        this.settledName = settledName;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "settled_time")
    public Date getSettledTime() {
        return settledTime;
    }

    public void setSettledTime(Date settledTime) {
        this.settledTime = settledTime;
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

    @Transient
    public String getOrderNoCopy() {
        return orderNoCopy;
    }

    public void setOrderNoCopy(String orderNoCopy) {
        this.orderNoCopy = orderNoCopy;
    }

    @Transient
    public Double getPromotionAmount() {
        return promotionAmount;
    }

    public void setPromotionAmount(Double promotionAmount) {
        this.promotionAmount = promotionAmount;
    }

    @Transient
    public Double getPlatformPromotionAmount() {
        return platformPromotionAmount;
    }

    public void setPlatformPromotionAmount(Double platformPromotionAmount) {
        this.platformPromotionAmount = platformPromotionAmount;
    }
}
