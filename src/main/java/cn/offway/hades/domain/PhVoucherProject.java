package cn.offway.hades.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * 优惠券方案
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Entity
@Table(name = "ph_voucher_project")
public class PhVoucherProject implements Serializable {

    /** ID **/
    private Long id;

    /** 优惠类型[0-满减，1-折扣] **/
    private String type;

    /** 名称 **/
    private String name;

    /** 券类型[0-无门槛，1-品牌，2-商品] **/
    private String usedType;

    /** 条件[0-存在，1-不存在] **/
    private String condition;

    /** 对应ID[格式如,1,2,3,4,] **/
    private String matchIds;

    /** 满多少金额可用 **/
    private Double usedMinAmount;

    /** 优惠券金额 **/
    private Double amount;

    /** 有效期[单位：天] **/
    private Long validNum;

    /** 发券数量 **/
    private Long limit;

    /** 创建时间 **/
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

    @Column(name = "type", length = 2)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "name", length = 200)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "used_type", length = 2)
    public String getUsedType() {
        return usedType;
    }

    public void setUsedType(String usedType) {
        this.usedType = usedType;
    }

    @Column(name = "condition", length = 2)
    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    @Column(name = "match_ids")
    public String getMatchIds() {
        return matchIds;
    }

    public void setMatchIds(String matchIds) {
        this.matchIds = matchIds;
    }

    @Column(name = "used_min_amount", precision = 15, scale = 2)
    public Double getUsedMinAmount() {
        return usedMinAmount;
    }

    public void setUsedMinAmount(Double usedMinAmount) {
        this.usedMinAmount = usedMinAmount;
    }

    @Column(name = "amount", precision = 15, scale = 2)
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Column(name = "valid_num", length = 11)
    public Long getValidNum() {
        return validNum;
    }

    public void setValidNum(Long validNum) {
        this.validNum = validNum;
    }

    @Column(name = "limit", length = 11)
    public Long getLimit() {
        return limit;
    }

    public void setLimit(Long limit) {
        this.limit = limit;
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
