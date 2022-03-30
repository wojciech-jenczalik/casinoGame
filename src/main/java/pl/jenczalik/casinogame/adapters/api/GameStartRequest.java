package pl.jenczalik.casinogame.adapters.api;

import java.util.UUID;

import lombok.Data;

@Data
public class GameStartRequest {
    private UUID playerId;
}
