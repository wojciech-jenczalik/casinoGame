package pl.jenczalik.casinogame.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RoundRewardsConfig.class)
public class RoundRewardsConfigBeans {
    // TODO verify config correctness, e.g. if probabilities dont add up over 100%
    @Bean
    public RoundRewardsConfig roundRewardsConfig() {
        return new RoundRewardsConfig();
    }
}
