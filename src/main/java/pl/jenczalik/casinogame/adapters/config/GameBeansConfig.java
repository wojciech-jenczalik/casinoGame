package pl.jenczalik.casinogame.adapters.config;

import java.security.SecureRandom;
import java.time.Clock;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.jenczalik.casinogame.config.CashPolicyConfig;
import pl.jenczalik.casinogame.config.RoundRewardsConfig;
import pl.jenczalik.casinogame.domain.model.CashDeductionPolicy;
import pl.jenczalik.casinogame.domain.model.FreeCashDeductionPolicy;
import pl.jenczalik.casinogame.domain.model.PaidCashDeductionPolicy;
import pl.jenczalik.casinogame.domain.ports.GameService;
import pl.jenczalik.casinogame.domain.ports.GameStateRepository;
import pl.jenczalik.casinogame.domain.ports.RoundResultRepository;
import pl.jenczalik.casinogame.domain.ports.RoundService;

@Configuration
class GameBeansConfig {

    @Bean
    GameService gameService(CashPolicyConfig cashPolicyConfig,
                            List<CashDeductionPolicy> cashDeductionPolicies,
                            GameStateRepository gameStateRepository,
                            RoundService roundService) {
        return new GameService(cashPolicyConfig, cashDeductionPolicies, gameStateRepository, roundService);
    }

    @Bean
    RoundService roundService(RoundRewardsConfig roundRewardsConfig,
                              RoundResultRepository roundResultRepository,
                              Clock clock) {
        return new RoundService(roundRewardsConfig, new SecureRandom(), roundResultRepository, clock);
    }

    @Bean
    CashDeductionPolicy freeCashDeductionPolicy() {
        return FreeCashDeductionPolicy.create();
    }

    @Bean
    CashDeductionPolicy paidCashDeductionPolicy() {
        return PaidCashDeductionPolicy.create();
    }

    @Bean
    Clock clock() {
        return Clock.systemDefaultZone();
    }
}
