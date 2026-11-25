package cl.novabank.transferencias.config;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration(proxyBeanMethods = false)
@Profile("cloud")
@EnableScheduling
public class ConfiguracionAws {
    @Bean
    SqsClient sqsClient() {
        return SqsClient.builder()
                .overrideConfiguration(config -> config.apiCallTimeout(Duration.ofSeconds(8)))
                .build();
    }

    @Bean
    S3Client s3Client(@Value("${S3_PATH_STYLE:false}") boolean pathStyle) {
        return S3Client.builder()
                .forcePathStyle(pathStyle)
                .overrideConfiguration(config -> config.apiCallTimeout(Duration.ofSeconds(8)))
                .build();
    }
}
