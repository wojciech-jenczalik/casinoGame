package pl.jenczalik.casinogame.adapters.api;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.jenczalik.casinogame.domain.model.GameHistory;

@Getter
@AllArgsConstructor
class GameHistoryResponse {
    private final GameStateResponse gameState;
    private final List<RoundResultResponse> roundResults;

    static GameHistoryResponse fromDomain(GameHistory domain) {
        final GameStateResponse gameStateResponse = GameStateResponse.fromDomain(domain.getGameState());
        final List<RoundResultResponse> roundResultResponses = domain.getRoundResults().stream()
                .map(RoundResultResponse::fromDomain)
                .collect(Collectors.toList());

        return new GameHistoryResponse(gameStateResponse, roundResultResponses);
    }
}
