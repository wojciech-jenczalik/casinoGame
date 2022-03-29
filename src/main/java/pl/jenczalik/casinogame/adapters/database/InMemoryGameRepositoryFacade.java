package pl.jenczalik.casinogame.adapters.database;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.jenczalik.casinogame.domain.model.GameState;
import pl.jenczalik.casinogame.domain.model.RoundResult;
import pl.jenczalik.casinogame.domain.ports.GameStateRepository;
import pl.jenczalik.casinogame.domain.ports.RoundResultRepository;

@Repository
@RequiredArgsConstructor
public class InMemoryGameRepositoryFacade implements RoundResultRepository, GameStateRepository {
    private final InMemoryRoundResultJpaRepository roundResultJpaRepository;
    private final InMemoryGameStateJpaRepository gameStateJpaRepository;

    @Override
    public Optional<GameState> getByGameIdAndPlayerId(UUID gameId, UUID playerId) {
        final Optional<GameStateEntity> gameStateEntity = gameStateJpaRepository.findByGameIdAndPlayerId(gameId, playerId);
        return gameStateEntity.map(GameStateEntity::toDomain);
    }

    @Override
    public Optional<GameState> getByGameId(UUID gameId) {
        final Optional<GameStateEntity> gameStateEntity = gameStateJpaRepository.findByGameId(gameId);
        return gameStateEntity.map(GameStateEntity::toDomain);
    }

    @Override
    public List<RoundResult> getRoundsByGameId(UUID gameId) {
        return roundResultJpaRepository.findAllByGameId(gameId).stream()
                .map(RoundResultEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public GameState save(GameState gameState) {
        final GameStateEntity savedEntity = gameStateJpaRepository.save(GameStateEntity.fromDomain(gameState));
        return savedEntity.toDomain();
    }

    @Override
    public RoundResult save(RoundResult roundResult) {
        final RoundResultEntity savedEntity = roundResultJpaRepository.save(RoundResultEntity.fromDomain(roundResult));
        return savedEntity.toDomain();
    }
}
