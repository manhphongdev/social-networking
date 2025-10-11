package vn.socialnet.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class S3Service {
    private final String bucket;
    private final String cdn;
    private final S3Client s3;

    public S3Service(
            @Value("${aws.access-key}") String accessKey,
            @Value("${aws.secret-key}") String secretKey,
            @Value("${aws.s3.region}") String region,
            @Value("${aws.s3.bucket}") String bucket,
            @Value("${aws.cloudfront.url}") String cdn) {
        this.bucket = bucket;
        this.cdn = cdn;

        AwsBasicCredentials creds = AwsBasicCredentials.create(accessKey, secretKey);
        this.s3 = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(creds))
                .build();
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String key = "uploads/" + UUID.randomUUID() + "-" + file.getOriginalFilename();

        s3.putObject(PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .contentType(file.getContentType())
                        .build(),
                RequestBody.fromBytes(file.getBytes()));

        return cdn + "/" + key;
    }

    public List<String> listFiles() {
        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(bucket)
                .prefix("uploads/")
                .build();

        ListObjectsV2Response response = s3.listObjectsV2(request);
        return response.contents().stream()
                .map(S3Object::key)
                .collect(Collectors.toList());
    }

    public void deleteFile(String fileUrl) {
        try {
            String key = extractKeyFromUrl(fileUrl);
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();


            log.debug("Deleting file from S3: bucket={}, key={}", bucket, key);
            s3.deleteObject(deleteRequest);

            log.info("File deleted successfully: {}", key);
        } catch (S3Exception e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error when deleting file {}: {}", fileUrl, e.getMessage());
            throw new RuntimeException("S3 delete failed", e);
        }
    }

    private String extractKeyFromUrl(String fileUrl) {
        // Example: https://mybucket.s3.ap-southeast-1.amazonaws.com/posts/abc.png
        int index = fileUrl.indexOf(".amazonaws.com/");
        if (index == -1) {
            throw new IllegalArgumentException("Invalid S3 URL: " + fileUrl);
        }
        return fileUrl.substring(index + ".amazonaws.com/".length());
    }
}

