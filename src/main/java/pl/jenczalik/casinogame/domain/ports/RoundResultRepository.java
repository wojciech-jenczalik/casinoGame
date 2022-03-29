package pl.jenczalik.casinogame.domain.ports;

import pl.jenczalik.casinogame.domain.model.RoundResult;

public interface RoundResultRepository {
    RoundResult save(RoundResult roundResult);
}
