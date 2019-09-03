package cn.offway.hades.domain;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * 消息通知
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-01 11:26:00 Exp $
 */
@Entity
@Table(name = "ph_notice")
public class PhNotice implements Serializable {

    /** ID **/
    private Long id;

    /** 用户ID **/
    private Long userId;

    /** 类型[0-系统消息，1-订单通知，2-活动通知] **/
    private String type;

    /** 消息内容 **/
    private String content;

    /** 是否已读[0-否,1-是] **/
    private String isRead;

    /** 创建时间 **/
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 备注 **/
    private String remark;
    
    /** 图片 **/
    private String image;


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

    @Column(name = "content", length = 200)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(name = "is_read", length = 2)
    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
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

    @Column(name = "image", length = 200)
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
    
    

}
