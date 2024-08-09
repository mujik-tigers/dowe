package com.dowe.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@ConfigurationProperties("aws.s3")
@RequiredArgsConstructor
@Getter
public class S3Properties {

	private final String accessKey;
	private final String secretKey;

}
