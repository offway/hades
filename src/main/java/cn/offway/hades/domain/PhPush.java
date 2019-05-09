package cn.offway.hades.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * 推送记录
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Entity
@Table(name = "ph_push")
public class PhPush implements Serializable {

    /** ID **/
    private Long id;

    /** 推送标题 **/
    private String name;

    /** 推送内容 **/
    private String content;

    /** 类别[0-跳转URL,1-品牌,2-商品,3-明星同款,4-OFFWAY优选] **/
    private String type;

    /** 跳转ID **/
    private Long redirectId;

    /** 跳转链接 **/
    private String url;

    /** 创建时间 **/
    private Date createTime;

    /** 推送时间 **/
    private Date pushTime;

    /** 状态[0-待推送,1-推送成功,2-推送失败] **/
    private String status;

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

    @Column(name = "content", length = 200)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(name = "type", length = 2)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "redirect_id", length = 11)
    public Long getRedirectId() {
        return redirectId;
    }

    public void setRedirectId(Long redirectId) {
        this.redirectId = redirectId;
    }

    @Column(name = "url", length = 50)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "push_time")
    public Date getPushTime() {
        return pushTime;
    }

    public void setPushTime(Date pushTime) {
        this.pushTime = pushTime;
    }

    @Column(name = "status", length = 2)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "remark", length = 200)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
