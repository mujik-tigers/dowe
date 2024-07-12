package com.dowe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.dowe.config.converter.ProviderConverter;
import com.dowe.util.interceptor.AccessTokenInterceptor;
import com.dowe.util.interceptor.AuthorizationHeaderInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final AuthorizationHeaderInterceptor authorizationHeaderInterceptor;
	private final AccessTokenInterceptor accessTokenInterceptor;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins("http://localhost:3000")
			.allowedMethods("*")
			.allowCredentials(true)
			.exposedHeaders(HttpHeaders.AUTHORIZATION)
			.maxAge(3000);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authorizationHeaderInterceptor)
			.addPathPatterns("/**")
			.excludePathPatterns("/docs/index.html", "/oauth/**", "/error")
			.order(Ordered.HIGHEST_PRECEDENCE);

		registry.addInterceptor(accessTokenInterceptor)
			.addPathPatterns("/**")
			.excludePathPatterns("/docs/index.html", "/oauth/**", "/refresh", "/error")
			.order(Ordered.LOWEST_PRECEDENCE);
	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new ProviderConverter());
	}

	@Bean
	public RestClient restClient() {
		return RestClient.create();
	}

}
