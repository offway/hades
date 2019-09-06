package cn.offway.hades.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * 积分记录
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-09-06 15:21:47 Exp $
 */
@Entity
@Table(name = "ph_accumulate_points")
public class PhAccumulatePoints implements Serializable {

    /** ID **/
    private Long id;

    /** 用户ID **/
    private Long userId;

    /** 来源[0-签到,1-阅读文章,2-分享文章,3-邀请好友完成注册,4-购物消费] **/
    private String type;

    /** 获得积分 **/
    private Long points;

    /** 积分余额 **/
    private Long pointsBalace;

    /** 状态[0-未使用,1-已使用,2-已过期] **/
    private String status;

    /** 创建时间 **/
    private Date createTime;

    /** 版本号 **/
    private Long version;

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

    @Column(name = "user_id", length = 11)
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Column(name = "type", length = 2)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "points", length = 11)
    public Long getPoints() {
        return points;
    }

    public void setPoints(Long points) {
        this.points = points;
    }

    @Column(name = "points_balace", length = 11)
    public Long getPointsBalace() {
        return pointsBalace;
    }

    public void setPointsBalace(Long pointsBalace) {
        this.pointsBalace = pointsBalace;
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

    @Column(name = "version", length = 11)
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Column(name = "remark", length = 200)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
