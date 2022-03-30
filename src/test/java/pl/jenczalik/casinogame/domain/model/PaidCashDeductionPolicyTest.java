package pl.jenczalik.casinogame.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import pl.jenczalik.casinogame.domain.services.CashDeductionPolicy;
import pl.jenczalik.casinogame.domain.services.PaidCashDeductionPolicy;

class PaidCashDeductionPolicyTest {
    // Subject
    private final CashDeductionPolicy paidPolicy = PaidCashDeductionPolicy.create();

    @Test
    void given_bet_of_5_and_balance_of_20_when_bet_deducted_balance_should_be_15() {
        // given, when
        final BigDecimal newBalance = paidPolicy.deductBetFromBalance(BigDecimal.valueOf(5d), BigDecimal.valueOf(20d));

        // then
        assertEquals(BigDecimal.valueOf(15d), newBalance);
    }
}