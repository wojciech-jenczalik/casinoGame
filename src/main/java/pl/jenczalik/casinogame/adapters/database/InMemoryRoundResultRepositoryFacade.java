package pl.jenczalik.casinogame.adapters.database;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.jenczalik.casinogame.domain.model.RoundResult;
import pl.jenczalik.casinogame.domain.ports.RoundResultRepository;

@Repository
@RequiredArgsConstructor
public class InMemoryRoundResultRepositoryFacade implements RoundResultRepository {
    private final InMemoryRoundResultJpaRepository jpaRepository;

    @Override
    public RoundResult save(RoundResult roundResult) {
        final RoundResultEntity savedEntity = jpaRepository.save(RoundResultEntity.fromDomain(roundResult));
        return savedEntity.toDomain();
    }
}
