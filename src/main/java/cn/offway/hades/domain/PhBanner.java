package cn.offway.hades.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * Banner管理
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-01 11:26:00 Exp $
 */
@Entity
@Table(name = "ph_banner")
public class PhBanner implements Serializable {

    /** ID **/
    private Long id;

    /** banner **/
    private String banner;

    /** 类别[0-跳转URL,1-品牌,2-商品] **/
    private String type;

    /** 跳转对应ID **/
    private Long redirectId;

    /** 跳转链接 **/
    private String url;

    /** 开始时间 **/
    private Date beginTime;

    /** 截止时间 **/
    private Date endTime;

    /** 状态[0-未上架,1-已上架] **/
    private String status;

    /** 排序 **/
    private Long sort;

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

    @Column(name = "banner", length = 100)
    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
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
    @Column(name = "begin_time")
    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_time")
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Column(name = "status", length = 2)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "sort", length = 11)
    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
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
