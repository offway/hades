package cn.offway.hades.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * 预生成订单
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Entity
@Table(name = "ph_preorder_info")
public class PhPreorderInfo implements Serializable {

    /** ID **/
    private Long id;

    /** 预生成订单号 **/
    private String orderNo;

    /** 用户ID **/
    private Long userId;

    /** 地址ID **/
    private Long addrId;

    /** 订单总价 **/
    private Double price;

    /** 实付金额 **/
    private Double amount;

    /** 优惠券总金额 **/
    private Double voucherAmount;

    /** 平台优惠券ID **/
    private Long pVoucherId;

    /** 钱包金额 **/
    private Double walletAmount;

    /** 总运费 **/
    private Double mailFee;

    /** 状态[0-待付款,1-已付款,2-交易关闭,,3-付款中] **/
    private String status;

    /** 创建时间 **/
    private Date createTime;

    /** 备注 **/
    private String remark;

    /** 版本号 **/
    private Long version;
    
    /** 支付渠道[wxpay,alipay] **/
    private String payChannel;


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

    @Column(name = "voucher_amount", precision = 15, scale = 2)
    public Double getVoucherAmount() {
        return voucherAmount;
    }

    public void setVoucherAmount(Double voucherAmount) {
        this.voucherAmount = voucherAmount;
    }

    @Column(name = "p_voucher_id", length = 11)
    public Long getPVoucherId() {
        return pVoucherId;
    }

    public void setPVoucherId(Long pVoucherId) {
        this.pVoucherId = pVoucherId;
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

    @Column(name = "pay_channel", length = 2)
    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }
}
