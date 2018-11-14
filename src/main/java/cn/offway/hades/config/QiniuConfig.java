package cn.offway.hades.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import cn.offway.hades.properties.QiniuProperties;

@Configuration
@EnableConfigurationProperties(QiniuProperties.class)
public class QiniuConfig {

}
