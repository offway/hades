package cn.offway.hades.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * 提现订单
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-07-25 13:22:40 Exp $
 */
@Entity
@Table(name = "ph_withdraw_info")
public class PhWithdrawInfo implements Serializable {

    /** ID **/
    private Long id;

    /** 订单号 **/
    private String orderNo;

    /** 用户ID **/
    private Long userId;

    /** 金额 **/
    private Double amount;

    /** 状态[0-申请,1-审核成功,2-审核失败,3-提现成功,4-提现失败] **/
    private String status;

    /** 审核人 **/
    private String checkName;

    /** 审核时间 **/
    private Date checkTime;

    /** 拒绝原因 **/
    private String checkReason;

    /** 创建时间 **/
    private Date createTime;

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

    @Column(name = "amount", precision = 15, scale = 2)
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Column(name = "status", length = 2)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "check_name", length = 200)
    public String getCheckName() {
        return checkName;
    }

    public void setCheckName(String checkName) {
        this.checkName = checkName;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "check_time")
    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    @Column(name = "check_reason", length = 500)
    public String getCheckReason() {
        return checkReason;
    }

    public void setCheckReason(String checkReason) {
        this.checkReason = checkReason;
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

}
