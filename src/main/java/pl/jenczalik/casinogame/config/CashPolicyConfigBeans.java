package pl.jenczalik.casinogame.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(CashPolicyConfig.class)
public class CashPolicyConfigBeans {
    @Bean
    public CashPolicyConfig cashPolicyConfig() {
        return new CashPolicyConfig();
    }
}
