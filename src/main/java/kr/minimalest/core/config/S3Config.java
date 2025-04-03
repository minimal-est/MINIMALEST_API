package kr.minimalest.core.config;

import io.awspring.cloud.s3.*;
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
}
