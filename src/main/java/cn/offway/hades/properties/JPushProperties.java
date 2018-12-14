package cn.offway.hades.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jpush")
public class JPushProperties {

	/** API access secret of the appKey. **/
	private String masterSecret;
	
	/** The KEY of one application on JPush.**/
	private String appKey;
	
	private boolean apnsProduction;

	public String getMasterSecret() {
		return masterSecret;
	}

	public void setMasterSecret(String masterSecret) {
		this.masterSecret = masterSecret;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public boolean getApnsProduction() {
		return apnsProduction;
	}

	public void setApnsProduction(boolean apnsProduction) {
		this.apnsProduction = apnsProduction;
	}
	
}
