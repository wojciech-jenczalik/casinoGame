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
        if (id == null) {
            throw new IllegalArgumentException("player's ID can not be null");
        }
        this.id = id;
    }

    public static Player newPlayer() {
        return fromId(UUID.randomUUID());
    }

    public static Player fromId(UUID id) {
        return new Player(id);
    }
}
