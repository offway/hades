package cn.offway.hades.dto;

import java.io.Serializable;

/**
 * 抽奖券表
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
public class VTicketCount implements Serializable {

    /** 活动ID **/
    private Long productId;

    /** 微信用户ID **/
    private String unionid;

    /** 微信用户头像 **/
    private String minHeadUrl;

    /** 微信用户昵称 **/
    private String minNickName;

    /** 抽奖码数 **/
    private Long countCode;;

    
    
	public VTicketCount() {
		super();
	}

	public VTicketCount(Long productId, String minNickName, String unionid, String minHeadUrl, Long countCode) {
		super();
		this.productId = productId;
		this.unionid = unionid;
		this.minHeadUrl = minHeadUrl;
		this.minNickName = minNickName;
		this.countCode = countCode;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public String getMinHeadUrl() {
		return minHeadUrl;
	}

	public void setMinHeadUrl(String minHeadUrl) {
		this.minHeadUrl = minHeadUrl;
	}

	public String getMinNickName() {
		return minNickName;
	}

	public void setMinNickName(String minNickName) {
		this.minNickName = minNickName;
	}

	public Long getCountCode() {
		return countCode;
	}

	public void setCountCode(Long countCode) {
		this.countCode = countCode;
	}

}
