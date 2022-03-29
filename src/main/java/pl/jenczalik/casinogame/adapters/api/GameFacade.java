package pl.jenczalik.casinogame.adapters.api;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
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
        return GameStateResponse.fromGameState(gameState);
    }

    GameStateResponse playRound(UUID gameId, PlayRoundRequest request) {
        final PlayRoundDetails playRoundDetails = new PlayRoundDetails(gameId, request.getPlayerId(), request.getBet());
        final GameState gameState = gameService.playRound(playRoundDetails);
        return GameStateResponse.fromGameState(gameState);
    }
}
