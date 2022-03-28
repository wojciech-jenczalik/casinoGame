package pl.jenczalik.casinogame.config;

import java.math.BigDecimal;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "cash.policy")
public class CashPolicyConfig {
    private BigDecimal defaultInitialBalance = BigDecimal.valueOf(5000d);
    private BigDecimal minBetValue = BigDecimal.valueOf(1d);
    private BigDecimal maxBetValue = BigDecimal.valueOf(10d);
}
