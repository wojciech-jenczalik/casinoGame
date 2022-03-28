package pl.jenczalik.casinogame.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import pl.jenczalik.casinogame.config.CashPolicyConfig;

class PaidCashDeductionPolicyTest {
    // Subject
    private final CashDeductionPolicy paidPolicy = new PaidCashDeductionPolicy(new CashPolicyConfig());

    @Test
    void given_bet_smaller_than_balance_and_within_configured_constraints_then_should_subtract_from_balance() {
        // given, when
        final BigDecimal newBalance = paidPolicy.deductBetFromBalance(BigDecimal.valueOf(5d), BigDecimal.valueOf(20d));

        // then
        assertEquals(BigDecimal.valueOf(15d), newBalance);
    }

    @Test
    void given_negative_bet_then_should_throw_illegal_argument_exception() {
        // given, when, then
        final IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> paidPolicy.deductBetFromBalance(BigDecimal.valueOf(-1d), BigDecimal.valueOf(20d)));

        assertEquals(e.getMessage(), "bet (-1.0) can not be a negative amount");
    }

    @Test
    void given_bet_larger_than_configured_limit_then_should_throw_illegal_argument_exception() {
        // given, when, then
        final IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> paidPolicy.deductBetFromBalance(BigDecimal.valueOf(15d), BigDecimal.valueOf(20d)));

        assertEquals(e.getMessage(), "bet (15.0) can not be more than 10.0");
    }

    @Test
    void given_bet_larger_than_balance_then_should_throw_illegal_argument_exception() {
        // given, when, then
        final IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> paidPolicy.deductBetFromBalance(BigDecimal.valueOf(10d), BigDecimal.valueOf(5d)));

        assertEquals(e.getMessage(), "bet (10.0) can not be greater than current balance (5.0)");
    }
}