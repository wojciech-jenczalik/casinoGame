package pl.jenczalik.casinogame.adapters.database;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface InMemoryGameStateJpaRepository extends JpaRepository<GameStateEntity, UUID> {
    Optional<GameStateEntity> findByGameIdAndPlayerId(UUID gameId, UUID playerId);
    Optional<GameStateEntity> findByGameId(UUID gameId);
}
