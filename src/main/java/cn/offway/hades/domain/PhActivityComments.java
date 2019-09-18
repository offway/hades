package cn.offway.hades.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * 文章评论
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-09-18 16:00:10 Exp $
 */
@Entity
@Table(name = "ph_activity_comments")
public class PhActivityComments implements Serializable {

    /** ID **/
    private Long id;

    /** 用户ID **/
    private Long userId;

    /** 文章ID **/
    private Long articleId;

    /** 评论内容 **/
    private String content;

    /** 创建时间 **/
    private Date createTime;

    /** 是否违规[0-否,1-是] **/
    private String status;


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

    @Column(name = "article_id", length = 11)
    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "status", length = 2)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
