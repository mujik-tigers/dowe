package com.dowe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import com.dowe.config.converter.FormUrlEncodedHttpMessageConverter;

@Configuration
public class RestClientConfig {

	@Bean
	public RestClient restClient() {
		return RestClient.builder()
			.messageConverters(converters -> converters.add(new FormUrlEncodedHttpMessageConverter()))
			.build();
	}

}
