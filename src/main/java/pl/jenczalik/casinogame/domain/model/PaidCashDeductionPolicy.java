package pl.jenczalik.casinogame.domain.model;

import java.math.BigDecimal;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import pl.jenczalik.casinogame.config.CashPolicyConfig;

@Log4j2
@RequiredArgsConstructor
public class PaidCashDeductionPolicy implements CashDeductionPolicy {
    private final CashPolicyConfig cashPolicyConfig;

    @Override
    public GameType getGameType() {
        return GameType.PAID;
    }

    @Override
    public BigDecimal deductBetFromBalance(BigDecimal bet, BigDecimal balance) {
        validateBet(bet, balance);
        log.debug("deducting bet ({}) from balance ({})", bet, balance);
        return balance.subtract(bet);
    }

    private void validateBet(BigDecimal bet, BigDecimal balance) {
        final BigDecimal minBetValue = cashPolicyConfig.getMinBetValue();
        final BigDecimal maxBetValue = cashPolicyConfig.getMaxBetValue();

        if (bet.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(String.format("bet (%s) can not be a negative amount", bet));
        }
        if (bet.compareTo(minBetValue) < 0) {
            throw new IllegalArgumentException(String.format("bet (%s) can not be less than %s", bet, minBetValue));
        }
        if (bet.compareTo(maxBetValue) > 0) {
            throw new IllegalArgumentException(String.format("bet (%s) can not be more than %s", bet, maxBetValue));
        }
        if (bet.compareTo(balance) > 0) {
            throw new IllegalArgumentException(String.format("bet (%s) can not be greater than current balance (%s)", bet, balance));
        }
        log.debug("bet valid");
    }
}
