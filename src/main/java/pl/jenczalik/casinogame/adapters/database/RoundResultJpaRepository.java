package pl.jenczalik.casinogame.adapters.database;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoundResultJpaRepository extends JpaRepository<RoundResultEntity, UUID> {
    List<RoundResultEntity> findAllByGameId(UUID gameId);
}