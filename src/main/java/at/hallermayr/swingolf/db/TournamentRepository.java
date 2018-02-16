package at.hallermayr.swingolf.db;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import java.util.List;

public interface TournamentRepository extends GraphRepository<Tournament> {
    @Query("MATCH (tournament:Tournament)-[:HAS_DATE]->(duration:Duration) WHERE duration.from >= {0} RETURN tournament ORDER BY duration.from ASC LIMIT 3")
    List<Tournament> findNextTournaments(Long date);

    @Query("MATCH (tournament:Tournament)-[:HAS_DATE]->(duration:Duration) WHERE duration.from < {0} RETURN tournament ORDER BY duration.from DESC LIMIT 3")
    List<Tournament> findPreviousTournaments(Long date);
}