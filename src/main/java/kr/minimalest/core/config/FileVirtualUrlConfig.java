package kr.minimalest.core.config;

import kr.minimalest.core.domain.file.FileVirtualUrlResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileVirtualUrlConfig {

    @Bean
    public FileVirtualUrlResolver fileVirtualUrlResolver(
            @Value("${file.proxy.url}") String FILE_VIRTUAL_REQUEST
    ) {
        return new FileVirtualUrlResolver(FILE_VIRTUAL_REQUEST);
    }
}
