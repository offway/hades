package cn.offway.hades.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * 关注列表
 *
 * @author tbw
 * @version $v: 1.0.0, $time:2020-03-02 14:23:35 Exp $
 */
@Entity
@Table(name = "ph_follow")
public class PhFollow implements Serializable {

    /** ID **/
    private Long id;

    /** 用户ID **/
    private Long userId;

    /** 微信unionid **/
    private String unionid;

    /** 用户名称 **/
    private String nickname;

    /** 用户头像 **/
    private String headimgurl;

    /** 明星ID **/
    private Long celebrityId;

    /** 明星头像 **/
    private String celebrityHeadimgurl;

    /** 明星名称 **/
    private String celebrityName;

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

    @Column(name = "unionid", length = 200)
    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    @Column(name = "nickname", length = 200)
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Column(name = "headimgurl", length = 500)
    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    @Column(name = "celebrity_id", length = 11)
    public Long getCelebrityId() {
        return celebrityId;
    }

    public void setCelebrityId(Long celebrityId) {
        this.celebrityId = celebrityId;
    }

    @Column(name = "celebrity_headimgurl", length = 500)
    public String getCelebrityHeadimgurl() {
        return celebrityHeadimgurl;
    }

    public void setCelebrityHeadimgurl(String celebrityHeadimgurl) {
        this.celebrityHeadimgurl = celebrityHeadimgurl;
    }

    @Column(name = "celebrity_name", length = 200)
    public String getCelebrityName() {
        return celebrityName;
    }

    public void setCelebrityName(String celebrityName) {
        this.celebrityName = celebrityName;
    }

    @Column(name = "remark", length = 200)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
