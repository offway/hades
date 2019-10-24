package cn.offway.hades.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 活动产品表
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
@Entity
@Table(name = "ph_product_info")
public class PhProductInfo implements Serializable {

    /** 活动ID **/
    private Long id;

    /** 活动名称 **/
    private String name;

    /** 活动描述 **/
    private String productDesc;

    /** 活动列表图 **/
    private String image;

    /** 活动banner **/
    private String banner;

    /** 奖品价值[单位RMB] **/
    private String price;

    /** 缩略图 **/
    private String thumbnail;

    /** 分享图片 **/
    private String shareImage;

    /** 分享标题 **/
    private String shareTitle;

    /** 分享描述 **/
    private String shareDesc;
    
    /** 保存图片 **/
    private String saveImage;
    
    /** 背景图片 **/
    private String background;

    /** 封面图 **/
    private String coverImage;

    /** 活动开始时间 **/
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    /** 活动截止时间 **/
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** 创建时间 **/
    private Date createTime;
    
    /** 活动规则 **/
    private String ruleContent;
    
    /** APP活动规则 **/
    private String appRuleContent;
    
    /** 开奖视频地址 **/
    private String video;

    /** 备注 **/
    private String remark;
    
    /** 渠道,该字段为二进制位运算标识,0否1是,从右到左第一位表示H5,第二位表示小程序,第三位表示APP,第四位表示其他活动。如要查询APP则传参为0100,查询H5和小程序则传参0011以此类推 **/
    private Long channel; 
    
    /** 排序 **/
    private Long sort;
    
    /** 其他活动展示图 **/
    private String showImage;
    
    /** 状态[0-未上架,1-已上架] **/
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

    @Column(name = "name", length = 200)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "product_desc", length = 200)
    public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

    @Column(name = "image", length = 100)
    public String getImage() {
        return image;
    }

	public void setImage(String image) {
        this.image = image;
    }

    @Column(name = "banner", length = 100)
    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    @Column(name = "price", length = 50)
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Column(name = "thumbnail", length = 100)
    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Column(name = "share_image", length = 100)
    public String getShareImage() {
        return shareImage;
    }

    public void setShareImage(String shareImage) {
        this.shareImage = shareImage;
    }

    @Column(name = "share_title", length = 100)
    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    @Column(name = "share_desc", length = 200)
    public String getShareDesc() {
        return shareDesc;
    }

    public void setShareDesc(String shareDesc) {
        this.shareDesc = shareDesc;
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

    @Column(name = "rule_content")
	public String getRuleContent() {
		return ruleContent;
	}

	public void setRuleContent(String ruleContent) {
		this.ruleContent = ruleContent;
	}
	
	@Column(name = "save_image", length = 100)
	public String getSaveImage() {
		return saveImage;
	}

	public void setSaveImage(String saveImage) {
		this.saveImage = saveImage;
	}

	@Column(name = "background", length = 100)
	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		this.background = background;
	}
    
	@Column(name = "channel", length = 8)
	public Long getChannel() {
		return channel;
	}

	public void setChannel(Long channel) {
		this.channel = channel;
	}

	@Column(name = "sort", length = 11)
	public Long getSort() {
		return sort;
	}

	public void setSort(Long sort) {
		this.sort = sort;
	}
	
	@Column(name = "video", length = 500)
	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	@Column(name = "app_rule_content")
	public String getAppRuleContent() {
		return appRuleContent;
	}

	public void setAppRuleContent(String appRuleContent) {
		this.appRuleContent = appRuleContent;
	}

	@Column(name = "show_image", length = 100)
	public String getShowImage() {
		return showImage;
	}

	public void setShowImage(String showImage) {
		this.showImage = showImage;
	}
	
	@Column(name = "status", length = 2)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "cover_image", length = 200)
    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }
	

}
