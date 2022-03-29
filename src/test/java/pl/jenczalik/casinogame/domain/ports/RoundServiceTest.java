package pl.jenczalik.casinogame.domain.ports;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.Clock;
import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.jenczalik.casinogame.config.RoundRewardsConfig;
import pl.jenczalik.casinogame.domain.model.RoundResult;
import pl.jenczalik.casinogame.domain.services.RoundService;

@ExtendWith(MockitoExtension.class)
class RoundServiceTest {
    private static final UUID RANDOM_GAME_UUID = UUID.randomUUID();
    private final RoundRewardsConfig roundRewardsConfig = new RoundRewardsConfig();
    @Mock
    private RoundResultRepository roundResultRepository;
    @Mock
    private SecureRandom secureRandomMock;
    private final Clock clock = Clock.systemDefaultZone();

    // Subject
    private RoundService roundService;

    @BeforeEach
    void setup() {
        when(roundResultRepository.save(any())).then(AdditionalAnswers.returnsFirstArg());
        roundService = new RoundService(roundRewardsConfig, secureRandomMock, roundResultRepository, clock);
    }

    @ParameterizedTest
    @MethodSource("populateTestArguments")
    void roundServiceTestSuite(
            int cashWinRandomRoll,
            int freeRoundRandomRoll,
            BigDecimal bet,
            BigDecimal expectedWinnings,
            boolean expectedFreeRoundWon) {

        // given
        when(secureRandomMock.nextInt(100)).thenReturn(cashWinRandomRoll, freeRoundRandomRoll);

        // when
        final RoundResult roundResult = roundService.playRound(bet, RANDOM_GAME_UUID);

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