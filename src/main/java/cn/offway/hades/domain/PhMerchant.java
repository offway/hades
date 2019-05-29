package cn.offway.hades.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
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

    private String company;

    private Long adminId;

    /** 发货地址ID[见ph_address.id] **/
    private Long addrId;

    /** 退货地址ID[见ph_address.id] **/
    private Long returnAddrId;

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
    private Date createTime;

    /** 备注 **/
    private String remark;
    
    /** 是否包邮[0-否,1-是] **/
    private String isFreeFare;

    private Double ratio;

    /** 商户所在城市[买手店必填] **/
    private String city;

    /** 商户banner[买手店必填] **/
    private String banner;

    /** 背景图[买手店必填] **/
    private String background;

    /** 商户类型[0-品牌商,1-买手店] **/
    private String type;

    @Column(name = "ratio", precision = 15, scale = 2)
    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

    @Column(name = "admin_id", length = 11)
    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    @Column(name = "company", length = 200)
    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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

    @Column(name = "is_free_fare", length = 2)
	public String getIsFreeFare() {
		return isFreeFare;
	}

	public void setIsFreeFare(String isFreeFare) {
		this.isFreeFare = isFreeFare;
	}

    @Column(name = "return_addr_id", length = 11)
    public Long getReturnAddrId() {
        return returnAddrId;
    }

    public void setReturnAddrId(Long returnAddrId) {
        this.returnAddrId = returnAddrId;
    }

    @Column(name = "city", length = 100)
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column(name = "type", length = 2)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "banner", length = 100)
    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    @Column(name = "background", length = 200)
    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }
}
