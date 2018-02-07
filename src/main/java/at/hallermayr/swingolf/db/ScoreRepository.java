package at.hallermayr.swingolf.db;

import org.springframework.data.neo4j.repository.GraphRepository;

import java.util.List;

public interface ScoreRepository extends GraphRepository<Score> {
    List<Score> findByScore(String score);

    List<Score> findByGame(Game game);
}