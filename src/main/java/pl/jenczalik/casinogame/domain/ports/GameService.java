package pl.jenczalik.casinogame.domain.ports;

import pl.jenczalik.casinogame.domain.model.GameStartDetails;
import pl.jenczalik.casinogame.domain.model.GameState;
import pl.jenczalik.casinogame.domain.model.GameType;
import pl.jenczalik.casinogame.domain.model.Player;

public class GameService {

    public GameState startGame(GameStartDetails gameStartDetails) {
        Player player = gameStartDetails.getPlayer();
        if (player == null) {
            throw new IllegalStateException("could not start new game. Player is null");
        }

        GameType gameType = gameStartDetails.getGameType();
        switch (gameType) {
            case FREE:
                return GameState.newFreeGameWithPlayer(gameStartDetails.getPlayer());

            case PAID:
                return GameState.newPaidGameWithPlayer(gameStartDetails.getPlayer());

            default:
                throw new IllegalStateException(String.format("could not start new game. Unsupported game type: %s", gameType));
        }
    }
}
