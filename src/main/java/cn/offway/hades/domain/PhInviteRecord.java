package cn.offway.hades.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * 邀请记录表
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
@Entity
@Table(name = "ph_invite_record")
public class PhInviteRecord implements Serializable {

    /** ID **/
    private Long id;

    /** 活动ID **/
    private Long productId;

    /** 微信用户ID **/
    private String unionid;

    /** 被邀请用户ID **/
    private String inviteUnionid;

    /** 被邀请用户头像 **/
    private String inviteHeadUrl;

    /** 被邀请用户昵称 **/
    private String inviteNickName;

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

    @Column(name = "product_id", length = 11)
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Column(name = "unionid", length = 50)
    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    @Column(name = "invite_unionid", length = 50)
    public String getInviteUnionid() {
        return inviteUnionid;
    }

    public void setInviteUnionid(String inviteUnionid) {
        this.inviteUnionid = inviteUnionid;
    }

    @Column(name = "invite_head_url", length = 500)
    public String getInviteHeadUrl() {
        return inviteHeadUrl;
    }

    public void setInviteHeadUrl(String inviteHeadUrl) {
        this.inviteHeadUrl = inviteHeadUrl;
    }

    @Column(name = "invite_nick_name", length = 200)
    public String getInviteNickName() {
        return inviteNickName;
    }

    public void setInviteNickName(String inviteNickName) {
        this.inviteNickName = inviteNickName;
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
