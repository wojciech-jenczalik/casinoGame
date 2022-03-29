package pl.jenczalik.casinogame.domain.model;

import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class Player {
    @Getter
    private final UUID id;

    private Player(UUID id) {
        this.id = id;
    }

    public static Player newPlayer() {
        return fromUuid(UUID.randomUUID());
    }

    public static Player fromUuid(UUID uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("player's UUID can not be null");
        }
        return new Player(uuid);
    }
}
