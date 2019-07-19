package cn.offway.hades.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

import java.io.Serializable;
import javax.persistence.*;


import java.util.Date;

/**
 * 订单
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Entity
@Table(name = "ph_order_info")
public class PhOrderInfo extends BaseRowModel implements Serializable {

    /** ID **/
    @ExcelProperty(value = "序号",index = 0)
    private Long id;

    /** 预生成订单号 **/
    @ExcelProperty(value = "预生成订单号",index = 10)
    private String preorderNo;

    /** 订单号 **/
    @ExcelProperty(value = "订单号",index = 1)
    private String orderNo;

    /** 订单号副本 for 商品明细 **/
    @ExcelProperty(value = "商品明细",index = 13)
    private String orderNoCopy;

    /** 用户ID **/
    @ExcelProperty(value = "用户ID",index = 2)
    private Long userId;

    /** 地址ID **/
    @ExcelProperty(value = "收货地址",index = 17)
    private Long addrId;

    /** 订单总价 **/
    @ExcelProperty(value = "订单总价",index = 3)
    private Double price;

    /** 实付金额 **/
    @ExcelProperty(value = "实付金额",index = 4)
    private Double amount;

    /** 店铺优惠券ID **/
    @ExcelProperty(value = "店铺优惠卷",index = 16)
    private Long mVoucherId;

    /** 店铺优惠券金额 **/
    private Double mVoucherAmount;

    /** 店铺优惠券金额别名字段 **/
    @ExcelProperty(value = "店铺优惠券金额",index = 7)
    private Double voucherAmountMerchant;

    /** 平台优惠券ID **/
    private Long pVoucherId;

    /** 平台优惠券金额 **/
    private Double pVoucherAmount;

    /** 平台优惠券金额别名字段 **/
    @ExcelProperty(value = "平台优惠券金额",index = 6)
    private Double voucherAmountPlatform;

    /** 钱包金额 **/
    private Double walletAmount;

    /** 运费 **/
    @ExcelProperty(value = "运费",index = 5)
    private Double mailFee;

    /** 状态[0-已下单,1-已付款,2-已发货,3-已收货,4-取消] **/
    @ExcelProperty(value = "状态",index = 8)
    private String status;

    /** 发货人 **/
    private String deliverName;

    /** 发货时间 **/
    private Date deliverTime;

    /** 留言 **/
    private String message;

    /** 创建时间 **/
    @ExcelProperty(value = "创建时间",index = 11)
    private Date createTime;

    /** 备注 **/
    @ExcelProperty(value = "备注",index = 12)
    private String remark;

    /** 版本号 **/
    private Long version;

    /** 商户ID **/
    @ExcelProperty(value = "商户ID",index = 14)
    private Long merchantId;

    /** 商户LOGO **/
    private String merchantLogo;

    /** 商户名称 **/
    @ExcelProperty(value = "商户名称",index = 15)
    private String merchantName;
    
    /** 支付渠道[wxpay,alipay] **/
    @ExcelProperty(value = "支付渠道",index = 9)
    private String payChannel;
    
    /** 是否隐藏[0-否，1-是] **/
    private String isHidden;
    
    /** 确认收货时间  **/
    private Date receiptTime;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "preorder_no", length = 50)
    public String getPreorderNo() {
        return preorderNo;
    }

    public void setPreorderNo(String preorderNo) {
        this.preorderNo = preorderNo;
    }

    @Column(name = "order_no", length = 50)
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
        this.orderNoCopy = orderNo;
    }

    @Column(name = "user_id", length = 11)
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Column(name = "addr_id", length = 11)
    public Long getAddrId() {
        return addrId;
    }

    public void setAddrId(Long addrId) {
        this.addrId = addrId;
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

    @Column(name = "m_voucher_id", length = 11)
    public Long getMVoucherId() {
        return mVoucherId;
    }

    public void setMVoucherId(Long mVoucherId) {
        this.mVoucherId = mVoucherId;
    }

    @Column(name = "m_voucher_amount", precision = 15, scale = 2)
    public Double getMVoucherAmount() {
        return mVoucherAmount;
    }

    public void setMVoucherAmount(Double mVoucherAmount) {
        this.mVoucherAmount = mVoucherAmount;
        this.voucherAmountMerchant = mVoucherAmount;
    }

    @Column(name = "p_voucher_id", length = 11)
    public Long getPVoucherId() {
        return pVoucherId;
    }

    public void setPVoucherId(Long pVoucherId) {
        this.pVoucherId = pVoucherId;
    }

    @Column(name = "p_voucher_amount", precision = 15, scale = 2)
    public Double getPVoucherAmount() {
        return pVoucherAmount;
    }

    public void setPVoucherAmount(Double pVoucherAmount) {
        this.pVoucherAmount = pVoucherAmount;
        this.voucherAmountPlatform = pVoucherAmount;
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

    @Column(name = "status", length = 2)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "deliver_name", length = 200)
    public String getDeliverName() {
        return deliverName;
    }

    public void setDeliverName(String deliverName) {
        this.deliverName = deliverName;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deliver_time")
    public Date getDeliverTime() {
        return deliverTime;
    }

    public void setDeliverTime(Date deliverTime) {
        this.deliverTime = deliverTime;
    }

    @Column(name = "message", length = 200)
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    @Version
    @Column(name = "version", length = 11)
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
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
    
    @Column(name = "pay_channel", length = 2)
    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }
    
    @Column(name = "is_hidden", length = 2)
    public String getIsHidden() {
        return isHidden;
    }

    public void setIsHidden(String isHidden) {
        this.isHidden = isHidden;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "receipt_time")
	public Date getReceiptTime() {
		return receiptTime;
	}

	public void setReceiptTime(Date receiptTime) {
		this.receiptTime = receiptTime;
	}

    @Transient
    public Double getVoucherAmountMerchant() {
        return voucherAmountMerchant;
    }

    public void setVoucherAmountMerchant(Double voucherAmountMerchant) {
        this.voucherAmountMerchant = voucherAmountMerchant;
    }

    @Transient
    public Double getVoucherAmountPlatform() {
        return voucherAmountPlatform;
    }

    public void setVoucherAmountPlatform(Double voucherAmountPlatform) {
        this.voucherAmountPlatform = voucherAmountPlatform;
    }

    @Transient
    public String getOrderNoCopy() {
        return orderNoCopy;
    }

    public void setOrderNoCopy(String orderNoCopy) {
        this.orderNoCopy = orderNoCopy;
    }

}
