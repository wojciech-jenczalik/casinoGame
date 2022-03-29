package pl.jenczalik.casinogame.domain.ports;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.jenczalik.casinogame.domain.model.RoundResult;

public interface RoundResultRepository {
    @Transactional(propagation = Propagation.MANDATORY)
    RoundResult save(RoundResult roundResult);
}
