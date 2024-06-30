package com.dowe.auth.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.dowe.auth.config.properties.KakaoOAuthProperties;

@Configuration
@EnableConfigurationProperties(value = {
	KakaoOAuthProperties.class,
})
public class PropertiesConfig {

}
