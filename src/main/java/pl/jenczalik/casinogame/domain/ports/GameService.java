package pl.jenczalik.casinogame.domain.ports;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import pl.jenczalik.casinogame.config.CashPolicyConfig;
import pl.jenczalik.casinogame.domain.model.CashDeductionPolicy;
import pl.jenczalik.casinogame.domain.model.GameStartDetails;
import pl.jenczalik.casinogame.domain.model.GameState;
import pl.jenczalik.casinogame.domain.model.GameType;
import pl.jenczalik.casinogame.domain.model.Player;

public class GameService {
    private final CashPolicyConfig cashPolicyConfig;
    private final Map<GameType, CashDeductionPolicy> cashDeductionPoliciesMap;

    public GameService(CashPolicyConfig cashPolicyConfig, List<CashDeductionPolicy> cashDeductionPolicies) {
        this.cashPolicyConfig = cashPolicyConfig;
        this.cashDeductionPoliciesMap = cashDeductionPolicies.stream()
                .collect(Collectors.toMap(
                        CashDeductionPolicy::getGameType,
                        Function.identity()
                ));
    }

    public GameState startGame(GameStartDetails gameStartDetails) {
        final Player player = gameStartDetails.getPlayer();
        if (player == null) {
            throw new IllegalStateException("could not start new game. Player is null");
        }

        final GameType gameType = gameStartDetails.getGameType();
        CashDeductionPolicy desiredPolicy;
        switch (gameType) {
            case FREE:
                desiredPolicy = cashDeductionPoliciesMap.get(GameType.FREE);
                break;

            case PAID:
                desiredPolicy = cashDeductionPoliciesMap.get(GameType.PAID);
                break;

            default:
                throw new IllegalStateException(String.format("could not start new game. Unsupported game type: %s", gameType));
        }

        return GameState.newGame(desiredPolicy, gameStartDetails.getPlayer(), cashPolicyConfig.getDefaultInitialBalance());
    }
}
