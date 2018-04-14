package at.hallermayr.swingolf.db.infrastructure;

import at.hallermayr.swingolf.db.model.Score;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import java.util.List;

public interface ScoreRepository extends GraphRepository<Score> {
    List<Score> findByScore(String score);

    @Query("MATCH (score:Score)-[:WAS_PLAYED_IN_GAME]->(game:Game) WHERE id(game) = {0} RETURN score")
    List<Score> findByGame(Long gameId);

    @Query("MATCH (score:Score)-[:WAS_PLAYED_BY]->(user:User) WHERE id(user) = {0} RETURN score")
    List<Score> findByUser(Long userId);
}