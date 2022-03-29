package pl.jenczalik.casinogame.adapters.api;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.jenczalik.casinogame.domain.model.GameHistory;
import pl.jenczalik.casinogame.domain.model.GameStartDetails;
import pl.jenczalik.casinogame.domain.model.GameState;
import pl.jenczalik.casinogame.domain.model.PlayRoundDetails;
import pl.jenczalik.casinogame.domain.model.Player;
import pl.jenczalik.casinogame.domain.ports.GameService;

@Component
@RequiredArgsConstructor
class GameFacade {
    private final GameService gameService;

    GameStateResponse startGame(GameStartRequest request) {
        final UUID playerId = request.getPlayerId();
        final Player player = playerId == null ? Player.newPlayer() : Player.fromId(playerId);
        final GameStartDetails startDetails = new GameStartDetails(request.getGameType(), player);
        final GameState gameState = gameService.startGame(startDetails);
        return GameStateResponse.fromDomain(gameState);
    }

    GameStateResponse playRound(UUID gameId, PlayRoundRequest request) {
        final PlayRoundDetails playRoundDetails = new PlayRoundDetails(gameId, request.getPlayerId(), request.getBet());
        final GameState gameState = gameService.playRound(playRoundDetails);
        return GameStateResponse.fromDomain(gameState);
    }

    GameHistoryResponse getGameHistory(UUID gameId) {
        final GameHistory gameHistory = gameService.getGameHistory(gameId);
        return GameHistoryResponse.fromDomain(gameHistory);
    }

    List<GameHistoryResponse> getGamesHistoryForPlayer(UUID playerId) {
        final List<GameHistory> gamesHistoryForPlayer = gameService.getGamesHistoryForPlayer(playerId);
        return gamesHistoryForPlayer.stream()
                .map(GameHistoryResponse::fromDomain)
                .collect(Collectors.toList());
    }
}
