package pl.jenczalik.casinogame.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class PlayerTest {

    @Test
    void given_null_uuid_when_player_created_then_should_throw_illegal_argument_exception() {
        // given, when, then
        final IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> Player.fromUuid(null));
        assertEquals(e.getMessage(), "player's UUID can not be null");
    }
}