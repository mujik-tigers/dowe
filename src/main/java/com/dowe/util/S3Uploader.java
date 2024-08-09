package com.dowe.util;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class S3Uploader {

	private static final String BUCKET_NAME = "dowe-s3";
	private static final String DIRECTORY_SEPARATOR = "/";
	private static final String EXTENSION_SEPARATOR = ".";

	private final AmazonS3 amazonS3Client;

	public String upload(String directory, MultipartFile file) {
		String fileName = directory + DIRECTORY_SEPARATOR +
			RandomUtil.generateFileNamePrefix() + System.currentTimeMillis() +
			EXTENSION_SEPARATOR + StringUtils.getFilenameExtension(file.getOriginalFilename());

		try {
			PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, fileName, file.getInputStream(), generateObjectMetadata(file));

			amazonS3Client.putObject(putObjectRequest);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		return amazonS3Client.getUrl(BUCKET_NAME, fileName).toString();
	}

	private ObjectMetadata generateObjectMetadata(MultipartFile file) {
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentType(file.getContentType());
		objectMetadata.setContentLength(file.getSize());

		return objectMetadata;
	}

}
