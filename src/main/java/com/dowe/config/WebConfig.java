package com.dowe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.dowe.config.converter.ProviderConverter;
import com.dowe.util.interceptor.AccessTokenInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final AccessTokenInterceptor accessTokenInterceptor;

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new ProviderConverter());
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(accessTokenInterceptor)
			.addPathPatterns("/**")
			.excludePathPatterns("/docs/index.html");
	}

	@Bean
	public RestClient restClient() {
		return RestClient.create();
	}

}
