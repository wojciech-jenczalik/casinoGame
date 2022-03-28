package pl.jenczalik.casinogame.domain.model;

import lombok.Value;

@Value
public class GameStartDetails {
    GameType gameType;
    Player player;
}
