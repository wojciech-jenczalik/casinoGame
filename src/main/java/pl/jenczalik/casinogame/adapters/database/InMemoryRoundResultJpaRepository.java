package pl.jenczalik.casinogame.adapters.database;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InMemoryRoundResultJpaRepository extends JpaRepository<RoundResultEntity, UUID> {
}