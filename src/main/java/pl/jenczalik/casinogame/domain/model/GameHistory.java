package pl.jenczalik.casinogame.domain.model;

import java.util.List;

import lombok.Value;

@Value
public class GameHistory {
    GameState gameState;
    List<RoundResult> roundResults;
}
