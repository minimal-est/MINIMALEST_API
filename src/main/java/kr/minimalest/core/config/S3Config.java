package kr.minimalest.core.config;

import io.awspring.cloud.s3.*;
import kr.minimalest.core.domain.file.storage.S3StorageResourceFinder;
import kr.minimalest.core.domain.file.storage.StorageResourceFinder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

    @Bean
    public S3OutputStreamProvider s3OutputStreamProvider(S3Client s3Client, S3ObjectContentTypeResolver s3ObjectContentTypeResolver) {
        return new DiskBufferingS3OutputStreamProvider(s3Client, s3ObjectContentTypeResolver);
    }

    @Bean
    public S3ObjectContentTypeResolver s3ObjectContentTypeResolver() {
        return new PropertiesS3ObjectContentTypeResolver();
    }

    @Bean
    public StorageResourceFinder s3StorageResourceFinder(
            @Value("${spring.cloud.aws.bucket.name}") String bucketName,
            S3Operations s3Operations
    ) {
        return new S3StorageResourceFinder(bucketName, s3Operations);
    }
}
