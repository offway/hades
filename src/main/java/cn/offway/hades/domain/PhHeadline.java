package cn.offway.hades.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * 
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Entity
@Table(name = "ph_headline")
public class PhHeadline implements Serializable {

    /** 头条id **/
    private String consultationid;

    /** 头条标题 **/
    private String consultationtitle;

    /** 头条图片 **/
    private String consultationimg;

    /** 头条内容 **/
    private String consultationcontent;

    /** 头条点赞数 **/
    private Long praisecount;

    /** 头条评论数量 **/
    private Long commentcount;

    /** 头条来源 **/
    private String consultationform;

    /** 头条状态 1正常 2 禁用 **/
    private Long consultationstatus;

    /** 是否显示在推荐首页 1是 2否 **/
    private Long istop;

    /** 创建人id **/
    private String createid;

    /** 创建时间 **/
    private Date createtime;

    /** 更新人id **/
    private String updateid;

    /** 更新时间 **/
    private Date updatetime;

    /** 头条分类id **/
    private String headlinetypeid;

    /** 头条的浏览量 **/
    private Long viewcount;

    /** 假 阅读量 **/
    private Long viewcountfake;

    /** 收藏数 **/
    private Long collectioncount;

    /** 是否发布：1-已发布；2-未发布 **/
    private Long ispublish;

    /** 发布时间 **/
    private Date ispublishtime;

    /** 是否审核 1 以审核 2 未审核 **/
    private Long isaudit;

    /** 创建方式 1  后台 2 抓取 **/
    private Long establish;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consultationid", unique = true, nullable = false)
    public String getConsultationid() {
        return consultationid;
    }

    public void setConsultationid(String consultationid) {
        this.consultationid = consultationid;
    }

    @Column(name = "consultationtitle", length = 1000)
    public String getConsultationtitle() {
        return consultationtitle;
    }

    public void setConsultationtitle(String consultationtitle) {
        this.consultationtitle = consultationtitle;
    }

    @Column(name = "consultationimg", length = 1000)
    public String getConsultationimg() {
        return consultationimg;
    }

    public void setConsultationimg(String consultationimg) {
        this.consultationimg = consultationimg;
    }

    @Column(name = "consultationcontent")
    public String getConsultationcontent() {
        return consultationcontent;
    }

    public void setConsultationcontent(String consultationcontent) {
        this.consultationcontent = consultationcontent;
    }

    @Column(name = "praisecount", length = 11)
    public Long getPraisecount() {
        return praisecount;
    }

    public void setPraisecount(Long praisecount) {
        this.praisecount = praisecount;
    }

    @Column(name = "commentcount", length = 11)
    public Long getCommentcount() {
        return commentcount;
    }

    public void setCommentcount(Long commentcount) {
        this.commentcount = commentcount;
    }

    @Column(name = "consultationform", length = 500)
    public String getConsultationform() {
        return consultationform;
    }

    public void setConsultationform(String consultationform) {
        this.consultationform = consultationform;
    }

    @Column(name = "consultationstatus", length = 1)
    public Long getConsultationstatus() {
        return consultationstatus;
    }

    public void setConsultationstatus(Long consultationstatus) {
        this.consultationstatus = consultationstatus;
    }

    @Column(name = "istop", length = 1)
    public Long getIstop() {
        return istop;
    }

    public void setIstop(Long istop) {
        this.istop = istop;
    }

    @Column(name = "createid", length = 32)
    public String getCreateid() {
        return createid;
    }

    public void setCreateid(String createid) {
        this.createid = createid;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createtime")
    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    @Column(name = "updateid", length = 32)
    public String getUpdateid() {
        return updateid;
    }

    public void setUpdateid(String updateid) {
        this.updateid = updateid;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updatetime")
    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    @Column(name = "headlinetypeid", length = 50)
    public String getHeadlinetypeid() {
        return headlinetypeid;
    }

    public void setHeadlinetypeid(String headlinetypeid) {
        this.headlinetypeid = headlinetypeid;
    }

    @Column(name = "viewcount", length = 11)
    public Long getViewcount() {
        return viewcount;
    }

    public void setViewcount(Long viewcount) {
        this.viewcount = viewcount;
    }

    @Column(name = "viewcountfake", length = 11)
    public Long getViewcountfake() {
        return viewcountfake;
    }

    public void setViewcountfake(Long viewcountfake) {
        this.viewcountfake = viewcountfake;
    }

    @Column(name = "collectioncount", length = 11)
    public Long getCollectioncount() {
        return collectioncount;
    }

    public void setCollectioncount(Long collectioncount) {
        this.collectioncount = collectioncount;
    }

    @Column(name = "ispublish", length = 11)
    public Long getIspublish() {
        return ispublish;
    }

    public void setIspublish(Long ispublish) {
        this.ispublish = ispublish;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ispublishtime")
    public Date getIspublishtime() {
        return ispublishtime;
    }

    public void setIspublishtime(Date ispublishtime) {
        this.ispublishtime = ispublishtime;
    }

    @Column(name = "isaudit", length = 11)
    public Long getIsaudit() {
        return isaudit;
    }

    public void setIsaudit(Long isaudit) {
        this.isaudit = isaudit;
    }

    @Column(name = "establish", length = 11)
    public Long getEstablish() {
        return establish;
    }

    public void setEstablish(Long establish) {
        this.establish = establish;
    }

}
