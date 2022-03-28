package pl.jenczalik.casinogame.domain.ports;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import pl.jenczalik.casinogame.domain.model.GameStartDetails;
import pl.jenczalik.casinogame.domain.model.GameState;
import pl.jenczalik.casinogame.domain.model.GameType;
import pl.jenczalik.casinogame.domain.model.Player;

class GameServiceTest {

    // Subject
    GameService gameService = new GameService();

    @Test
    void given_a_player_with_id_and_free_game_type_when_game_started_then_should_have_5000_credits() {
        // given
        final UUID uuid = UUID.randomUUID();
        final GameStartDetails gameStartDetails = new GameStartDetails(
                GameType.FREE,
                Player.fromUuid(uuid)
        );

        // when
        final GameState gameState = gameService.startGame(gameStartDetails);

        // then
        assertEquals(gameState.getPlayer().getId(), uuid);
        assertEquals(gameState.getBalance(), BigDecimal.valueOf(5000d));
    }

    @Test
    void given_a_null_player_when_game_started_then_throw_illegal_state_exception() {
        // given
        final GameStartDetails gameStartDetails = new GameStartDetails(
                GameType.FREE,
                null
        );

        // when
        final IllegalStateException e = assertThrows(IllegalStateException.class, () -> gameService.startGame(gameStartDetails));
        assertEquals(e.getMessage(), "could not start new game. Player is null");
    }
}
