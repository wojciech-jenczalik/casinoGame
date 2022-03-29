package pl.jenczalik.casinogame.domain.ports;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.jenczalik.casinogame.config.RoundRewardsConfig;
import pl.jenczalik.casinogame.domain.framework.SecureRandomStub;
import pl.jenczalik.casinogame.domain.model.RoundResult;

class RoundServiceTest {
    private final RoundRewardsConfig roundRewardsConfig = new RoundRewardsConfig();
    private final SecureRandomStub secureRandomStub = new SecureRandomStub();

    @SuppressWarnings("FieldCanBeLocal") // for brevity
    // Subject
    private RoundService roundService;

    @ParameterizedTest
    @MethodSource("populateTestArguments")
    void roundServiceTestSuite(
            int cashWinRandomRoll,
            int freeRoundRandomRoll,
            BigDecimal bet,
            BigDecimal expectedWinnings,
            boolean expectedFreeRoundWon) {

        // given
        secureRandomStub.populateWithInts(cashWinRandomRoll, freeRoundRandomRoll);
        roundService = new RoundService(roundRewardsConfig, secureRandomStub);

        // when
        final RoundResult roundResult = roundService.playRound(bet);

        // then
        assertEquals(0, roundResult.getWinnings().compareTo(expectedWinnings));
        assertEquals(expectedFreeRoundWon, roundResult.isFreeRoundWon());
    }

    private static Stream<Arguments> populateTestArguments() {
        return Stream.of(
                Arguments.of(0, 0, BigDecimal.valueOf(2), BigDecimal.valueOf(6), true),
                Arguments.of(5, 5, BigDecimal.valueOf(2), BigDecimal.valueOf(6), true),
                Arguments.of(10, 15, BigDecimal.valueOf(2), BigDecimal.valueOf(20), false),
                Arguments.of(15, 15, BigDecimal.valueOf(2), BigDecimal.valueOf(20), false),
                Arguments.of(20, 15, BigDecimal.valueOf(2), BigDecimal.valueOf(100), false),
                Arguments.of(25, 15, BigDecimal.valueOf(5), BigDecimal.valueOf(250), false),
                Arguments.of(30, 15, BigDecimal.valueOf(2), BigDecimal.valueOf(0), false),
                Arguments.of(35, 5, BigDecimal.valueOf(2), BigDecimal.valueOf(0), true),
                Arguments.of(100, 5, BigDecimal.valueOf(2), BigDecimal.valueOf(0), true)
        );
    }
}