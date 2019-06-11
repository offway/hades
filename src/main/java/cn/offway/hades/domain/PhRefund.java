package cn.offway.hades.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * 退款/退货
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Entity
@Table(name = "ph_refund")
public class PhRefund extends BaseRowModel implements Serializable {

    /** ID **/
    @ExcelProperty(value = "序号",index = 0)
    private Long id;

    /** 购买订单号 **/
    @ExcelProperty(value = "购买订单号",index = 1)
    private String orderNo;

    /** 用户ID **/
    @ExcelProperty(value = "用户ID",index = 2)
    private Long userId;

    /** 退款金额 **/
    @ExcelProperty(value = "退款金额",index = 3)
    private Double amount;

    /** 快递运单号 **/
    @ExcelProperty(value = "快递运单号",index = 4)
    private String mailNo;

    /** 类型[0-仅退款,1-退货退款,2-换货] **/
    @ExcelProperty(value = "类型",index = 5)
    private String type;

    /** 状态[0-审核中,1-待退货,2-退货中,3-退款中,4-退款成功,5-退款取消,6-审核失败] **/
    @ExcelProperty(value = "状态",index = 6)
    private String status;

    /** 退款原因 **/
    @ExcelProperty(value = "退款原因",index = 7)
    private String reason;

    /** 退款说明 **/
    @ExcelProperty(value = "退款说明",index = 8)
    private String content;

    /** 退款图片[多个,隔开] **/
    private String image;

    /** 审核人 **/
    @ExcelProperty(value = "审核人",index = 9)
    private String checkName;

    /** 审核时间 **/
    @ExcelProperty(value = "审核时间",index = 10)
    private Date checkTime;

    /** 拒绝原因 **/
    @ExcelProperty(value = "拒绝原因",index = 11)
    private String checkReason;

    /** 创建时间 **/
    private Date createTime;

    /** 备注 **/
    private String remark;

    /** 版本号 **/
    private Long version;

    /** 是否整单退款[0-否,1-是] **/
    @ExcelProperty(value = "是否整单退款",index = 12)
    private String isComplete;

    /** 退款商品件数 **/
    @ExcelProperty(value = "退款商品件数",index = 13)
    private Long goodsCount;


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

    @Column(name = "mail_no", length = 50)
    public String getMailNo() {
        return mailNo;
    }

    public void setMailNo(String mailNo) {
        this.mailNo = mailNo;
    }

    @Column(name = "type", length = 2)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "status", length = 2)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "reason", length = 200)
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Column(name = "content", length = 500)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(name = "image")
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    @Column(name = "is_complete", length = 2)
    public String getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(String isComplete) {
        this.isComplete = isComplete;
    }

    @Column(name = "goods_count", length = 11)
    public Long getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(Long goodsCount) {
        this.goodsCount = goodsCount;
    }

}
