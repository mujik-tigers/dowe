package com.dowe.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.dowe.config.properties.JwtProperties;
import com.dowe.config.properties.OAuthProperties;
import com.dowe.config.properties.S3Properties;

@Configuration
@EnableConfigurationProperties(value = {
	OAuthProperties.class,
	JwtProperties.class,
	S3Properties.class
})
public class PropertiesConfig {
}
