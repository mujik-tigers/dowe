package com.dowe.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.dowe.config.properties.OAuthProperties;

@Configuration
@EnableConfigurationProperties(value = {
	OAuthProperties.class
})
public class PropertiesConfig {
}
