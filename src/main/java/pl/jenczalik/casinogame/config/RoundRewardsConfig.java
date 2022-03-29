package pl.jenczalik.casinogame.config;

import java.math.BigDecimal;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "round.rewards")
public class RoundRewardsConfig {
    /** e.g. 10d, as in 10% */
    private BigDecimal smallWinChancePercentage = BigDecimal.valueOf(10d);
    private BigDecimal smallWinMultiplier = BigDecimal.valueOf(3d);

    /** e.g. 10d, as in 10% */
    private BigDecimal mediumWinChancePercentage = BigDecimal.valueOf(10d);
    private BigDecimal mediumWinMultiplier = BigDecimal.valueOf(10d);

    /** e.g. 10d, as in 10% */
    private BigDecimal bigWinChancePercentage = BigDecimal.valueOf(10d);
    private BigDecimal bigWinMultiplier = BigDecimal.valueOf(50d);

    /** e.g. 10d, as in 10% */
    private BigDecimal freeRoundWinChancePercentage = BigDecimal.valueOf(10d);
}
