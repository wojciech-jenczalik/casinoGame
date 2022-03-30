package pl.jenczalik.casinogame.adapters.database;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.jenczalik.casinogame.domain.model.GameNotFoundException;
import pl.jenczalik.casinogame.domain.model.GameState;
import pl.jenczalik.casinogame.domain.model.RoundResult;
import pl.jenczalik.casinogame.domain.ports.GameStateRepository;
import pl.jenczalik.casinogame.domain.ports.RoundResultRepository;

@Repository
@RequiredArgsConstructor
public class GameRepositoryFacade implements RoundResultRepository, GameStateRepository {
    private final RoundResultJpaRepository roundResultJpaRepository;
    private final GameStateJpaRepository gameStateJpaRepository;

    @Override
    public GameState getByGameIdAndPlayerId(UUID gameId, UUID playerId) {
        final GameStateEntity gameStateEntity = gameStateJpaRepository.findByGameIdAndPlayerId(gameId, playerId)
                .orElseThrow(() -> new GameNotFoundException(String.format("game with ID [%s] not found for user with ID [%s]", gameId, playerId)));
        return gameStateEntity.toDomain();
    }

    @Override
    public GameState getByGameId(UUID gameId) {
        final GameStateEntity gameStateEntity = gameStateJpaRepository.findByGameId(gameId)
                .orElseThrow(() -> new GameNotFoundException(String.format("game with ID [%s] not found", gameId)));
        return gameStateEntity.toDomain();
    }

    @Override
    public List<GameState> getAllByPlayerId(UUID playerId) {
        return gameStateJpaRepository.findAllByPlayerId(playerId).stream()
                .map(GameStateEntity::toDomain)
                .collect(Collectors.toList());
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
