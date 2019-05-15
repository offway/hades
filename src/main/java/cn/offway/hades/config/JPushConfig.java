package cn.offway.hades.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.jiguang.common.ClientConfig;
import cn.jpush.api.JPushClient;
import cn.offway.hades.properties.JPushProperties;


/**
 * 极光推送配置
 * @author wn
 *
 */
@Configuration
@EnableConfigurationProperties(JPushProperties.class)
public class JPushConfig {
	
	@Autowired
	private JPushProperties jPushProperties;
	
	@Bean
	public JPushClient  jpushClient(){
		ClientConfig config = ClientConfig.getInstance();
		config.setApnsProduction(jPushProperties.getApnsProduction());
		return new JPushClient(jPushProperties.getMasterSecret(), jPushProperties.getAppKey(), null, config);
	}

}
