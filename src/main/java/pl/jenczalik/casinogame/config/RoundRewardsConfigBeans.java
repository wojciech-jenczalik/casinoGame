package pl.jenczalik.casinogame.config;

import java.math.BigDecimal;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RoundRewardsConfig.class)
class RoundRewardsConfigBeans {
    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

    @Bean
    RoundRewardsConfig roundRewardsConfig() {
        final RoundRewardsConfig config = new RoundRewardsConfig();
        validateRoundRewardsConfig(config);
        return config;
    }

    private void validateRoundRewardsConfig(RoundRewardsConfig config) {
        final BigDecimal smallWinChance = config.getSmallWinChancePercentage();
        final BigDecimal mediumWinChance = config.getMediumWinChancePercentage();
        final BigDecimal bigWinChance = config.getBigWinChancePercentage();

        final BigDecimal totalWinChance = smallWinChance.add(mediumWinChance).add(bigWinChance);

        if (totalWinChance.compareTo(ONE_HUNDRED) > 0) {
            throw new IllegalStateException(String.format("win chances add up to over 100%% (small: %s%%, med: %s%%, big: %s%%)",
                    smallWinChance,
                    mediumWinChance,
                    bigWinChance));
        }

        final BigDecimal freeRoundWinChance = config.getFreeRoundWinChancePercentage();
        if (freeRoundWinChance.compareTo(ONE_HUNDRED) > 0) {
            throw new IllegalStateException(String.format("free round win chance is over 100%% (%s%%)", freeRoundWinChance));
        }
    }
}
