package cn.offway.hades.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * 订单物流
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Entity
@Table(name = "ph_order_express_info")
public class PhOrderExpressInfo implements Serializable {

    /** ID **/
    private Long id;

    /** 订单号 **/
    private String orderNo;

    /** 类型[0-寄,1-退] **/
    private String type;

    /** 快递运单号 **/
    private String mailNo;

    /** 快递公司编码 **/
    private String expressCode;

    /** 寄件方手机 **/
    private String fromPhone;

    /** 寄件方姓名 **/
    private String fromRealName;

    /** 寄件方省份 **/
    private String fromProvince;

    /** 寄件方城市 **/
    private String fromCity;

    /** 寄件方区/县 **/
    private String fromCounty;

    /** 寄件方详细地址 **/
    private String fromContent;

    /** 收件方手机 **/
    private String toPhone;

    /** 收件方姓名 **/
    private String toRealName;

    /** 收件方省份 **/
    private String toProvince;

    /** 收件方城市 **/
    private String toCity;

    /** 收件方区/县 **/
    private String toCounty;

    /** 收件方详细地址 **/
    private String toContent;

    /** 状态[0-新建,1-已下单,2-已接单,3-运送中,4-已签收] **/
    private String status;

    /** 创建时间 **/
    private Date createTime;

    /** 备注 **/
    private String remark;

    @Column(name = "express_code", length = 50)
    public String getExpressCode() {
        return expressCode;
    }

    public void setExpressCode(String expressCode) {
        this.expressCode = expressCode;
    }

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

    @Column(name = "type", length = 2)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "mail_no", length = 50)
    public String getMailNo() {
        return mailNo;
    }

    public void setMailNo(String mailNo) {
        this.mailNo = mailNo;
    }

    @Column(name = "from_phone", length = 50)
    public String getFromPhone() {
        return fromPhone;
    }

    public void setFromPhone(String fromPhone) {
        this.fromPhone = fromPhone;
    }

    @Column(name = "from_real_name", length = 50)
    public String getFromRealName() {
        return fromRealName;
    }

    public void setFromRealName(String fromRealName) {
        this.fromRealName = fromRealName;
    }

    @Column(name = "from_province", length = 20)
    public String getFromProvince() {
        return fromProvince;
    }

    public void setFromProvince(String fromProvince) {
        this.fromProvince = fromProvince;
    }

    @Column(name = "from_city", length = 20)
    public String getFromCity() {
        return fromCity;
    }

    public void setFromCity(String fromCity) {
        this.fromCity = fromCity;
    }

    @Column(name = "from_county", length = 20)
    public String getFromCounty() {
        return fromCounty;
    }

    public void setFromCounty(String fromCounty) {
        this.fromCounty = fromCounty;
    }

    @Column(name = "from_content", length = 200)
    public String getFromContent() {
        return fromContent;
    }

    public void setFromContent(String fromContent) {
        this.fromContent = fromContent;
    }

    @Column(name = "to_phone", length = 50)
    public String getToPhone() {
        return toPhone;
    }

    public void setToPhone(String toPhone) {
        this.toPhone = toPhone;
    }

    @Column(name = "to_real_name", length = 50)
    public String getToRealName() {
        return toRealName;
    }

    public void setToRealName(String toRealName) {
        this.toRealName = toRealName;
    }

    @Column(name = "to_province", length = 20)
    public String getToProvince() {
        return toProvince;
    }

    public void setToProvince(String toProvince) {
        this.toProvince = toProvince;
    }

    @Column(name = "to_city", length = 20)
    public String getToCity() {
        return toCity;
    }

    public void setToCity(String toCity) {
        this.toCity = toCity;
    }

    @Column(name = "to_county", length = 20)
    public String getToCounty() {
        return toCounty;
    }

    public void setToCounty(String toCounty) {
        this.toCounty = toCounty;
    }

    @Column(name = "to_content", length = 200)
    public String getToContent() {
        return toContent;
    }

    public void setToContent(String toContent) {
        this.toContent = toContent;
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

}
