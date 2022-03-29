package pl.jenczalik.casinogame.domain.ports;

import java.util.List;
import java.util.UUID;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.jenczalik.casinogame.domain.model.RoundResult;

public interface RoundResultRepository {
    @Transactional(propagation = Propagation.MANDATORY)
    RoundResult save(RoundResult roundResult);

    List<RoundResult> getRoundsByGameId(UUID gameId);
}
