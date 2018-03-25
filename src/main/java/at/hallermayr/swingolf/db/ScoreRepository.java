package at.hallermayr.swingolf.db;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import java.util.List;

public interface ScoreRepository extends GraphRepository<Score> {
    List<Score> findByScore(String score);

    @Query("MATCH (score:Score)-[:WAS_PLAYED_IN_GAME]->(game:Game) WHERE ID(game) = {0} RETURN score")
    List<Score> findByGame(Long gameId);

    @Query("MATCH (score:Score)-[:WAS_PLAYED_BY]->(user:User) WHERE ID(user) = {0} RETURN score")
    List<Score> findByUser(Long userId);
}