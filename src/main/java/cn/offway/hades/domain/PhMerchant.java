package cn.offway.hades.domain;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * 商户表
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Entity
@Table(name = "ph_merchant")
public class PhMerchant implements Serializable {

    /** ID **/
    private Long id;

    /** 商户名称 **/
    private String name;

    /** 商户LOGO **/
    private String logo;

    /** 商户地址 **/
    private String address;

    /** 发货地址ID[见ph_address.id] **/
    private Long addrId;

    /** 联系电话 **/
    private String phone;

    /** 商户邮箱 **/
    private String email;

    /** 合同编号 **/
    private String contractNo;

    /** 签约日期 **/
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date signDate;

    /** 到期日期 **/
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date expireDate;

    /** 状态[0-已申请,1-通过,2-拒绝] **/
    private String status;

    /** 创建时间 **/
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
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

    @Column(name = "name", length = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "logo", length = 100)
    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Column(name = "address", length = 200)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "addr_id", length = 11)
    public Long getAddrId() {
        return addrId;
    }

    public void setAddrId(Long addrId) {
        this.addrId = addrId;
    }

    @Column(name = "phone", length = 20)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(name = "email", length = 200)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "contract_no", length = 50)
    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sign_date")
    public Date getSignDate() {
        return signDate;
    }

    public void setSignDate(Date signDate) {
        this.signDate = signDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expire_date")
    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
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
