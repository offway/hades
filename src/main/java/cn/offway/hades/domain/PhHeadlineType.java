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
@Table(name = "ph_headline_type")
public class PhHeadlineType implements Serializable {

    /** 热门搜索id **/
    private String headlinetypeid;

    /** 热门搜索关键字 **/
    private String headlinetypename;

    /** 发放状态 1正常 2 禁用 **/
    private Long hostsearchstatus;

    /** 创建人id **/
    private String createid;

    /** 创建时间 **/
    private Date createtime;

    /**  **/
    private String updateid;

    /** 更新时间 **/
    private Date updatetime;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "headlinetypeid", unique = true, nullable = false)
    public String getHeadlinetypeid() {
        return headlinetypeid;
    }

    public void setHeadlinetypeid(String headlinetypeid) {
        this.headlinetypeid = headlinetypeid;
    }

    @Column(name = "headlinetypename", length = 200)
    public String getHeadlinetypename() {
        return headlinetypename;
    }

    public void setHeadlinetypename(String headlinetypename) {
        this.headlinetypename = headlinetypename;
    }

    @Column(name = "hostsearchstatus", length = 1)
    public Long getHostsearchstatus() {
        return hostsearchstatus;
    }

    public void setHostsearchstatus(Long hostsearchstatus) {
        this.hostsearchstatus = hostsearchstatus;
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

}
