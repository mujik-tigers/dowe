package com.dowe.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.dowe.auth.property.GoogleOAuthProperties;

@Configuration
@EnableConfigurationProperties(value = {
	GoogleOAuthProperties.class
})
public class PropertiesConfig {
}
