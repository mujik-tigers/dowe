package com.dowe.util.s3;

import java.net.URL;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Component
@RequiredArgsConstructor
public class S3PresignedUrlGenerator {

  private final S3Presigner s3Presigner;

  @Value("${cloud.aws.s3.bucket}")
  private String bucketName;

  public String generatePresignedUrl(
      String directory,
      String imagePrefix,
      Long teamId
  ) {

    String key = directory + imagePrefix + teamId;

    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(bucketName)
        .key(key)
        .acl("public-read")
        .build();

    PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
        .putObjectRequest(putObjectRequest)
        .signatureDuration(Duration.ofMinutes(15))
        .build();

    URL presignedUrl = s3Presigner.presignPutObject(presignRequest).url();

    return presignedUrl.toString();

  }

}
