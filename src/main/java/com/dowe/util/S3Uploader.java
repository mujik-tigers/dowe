package com.dowe.util;

import java.io.IOException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Utilities;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Uploader {

  private final S3Client s3Client;

  @Value("${cloud.aws.s3.bucket}")
  private String bucketName;

  public String upload(String directory, MultipartFile file) {
    String fileName = generateRandomFileName(directory, file);
    try {
      PutObjectRequest putObjectRequest = getPutObjectRequest(fileName);
      RequestBody requestBody = getFileRequestBody(file);
      s3Client.putObject(putObjectRequest, requestBody);
    } catch (IOException e) {
      log.error("Failed to upload file to S3: {}", e.getMessage());
      throw new RuntimeException("Failed to upload file", e);
    }

    return findUploadKeyUrl(fileName);
  }

  private String generateRandomFileName(String directory, MultipartFile file) {
    return directory + "/" +
        RandomUtil.generateFileNamePrefix() + "_" + System.currentTimeMillis() +
        "." + StringUtils.getFilenameExtension(file.getOriginalFilename());
  }

  private PutObjectRequest getPutObjectRequest(String key) {
    return PutObjectRequest.builder()
        .bucket(bucketName)
        .key(key)
        .contentType("image/png")
        .build();
  }

  private RequestBody getFileRequestBody(MultipartFile file) throws IOException {
    return RequestBody.fromInputStream(file.getInputStream(), file.getSize());
  }

  private String findUploadKeyUrl(String key) {
    S3Utilities s3Utilities = s3Client.utilities();
    GetUrlRequest getUrlRequest = GetUrlRequest.builder()
        .bucket(bucketName)
        .key(key)
        .build();

    URL url = s3Utilities.getUrl(getUrlRequest);
    return url.toString();
  }
}
