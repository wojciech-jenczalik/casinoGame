package pl.jenczalik.casinogame.adapters.database;

import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.jenczalik.casinogame.domain.model.GameState;
import pl.jenczalik.casinogame.domain.ports.GameStateRepository;

@Repository
@RequiredArgsConstructor
public class InMemoryGameStateRepositoryFacade implements GameStateRepository {
    private final InMemoryGameStateJpaRepository jpaRepository;

    @Override
    public GameState save(GameState gameState) {
        final GameStateEntity savedEntity = jpaRepository.save(GameStateEntity.fromDomain(gameState));
        return savedEntity.toDomain();
    }

    @Override
    public Optional<GameState> getByGameIdAndPlayerId(UUID gameId, UUID playerId) {
        final Optional<GameStateEntity> gameStateEntity = jpaRepository.findByGameIdAndPlayerId(gameId, playerId);
        return gameStateEntity.map(GameStateEntity::toDomain);
    }
}
