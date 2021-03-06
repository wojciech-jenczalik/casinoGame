package pl.jenczalik.casinogame.adapters.database;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface GameStateJpaRepository extends JpaRepository<GameStateEntity, UUID> {
    Optional<GameStateEntity> findByGameIdAndPlayerId(UUID gameId, UUID playerId);
    Optional<GameStateEntity> findByGameId(UUID gameId);
    List<GameStateEntity> findAllByPlayerId(UUID playerId);
}
