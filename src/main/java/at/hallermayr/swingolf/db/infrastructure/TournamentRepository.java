package at.hallermayr.swingolf.db.infrastructure;

import at.hallermayr.swingolf.db.model.Tournament;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import java.util.List;

public interface TournamentRepository extends GraphRepository<Tournament> {
    @Query("MATCH (tournament:Tournament)-[:HAS_DATE]->(duration:Duration) WHERE duration.from >= {0} RETURN tournament ORDER BY duration.from ASC LIMIT 3")
    List<Tournament> findNextTournaments(Long date);

    @Query("MATCH (tournament:Tournament)-[:HAS_DATE]->(duration:Duration) WHERE duration.from < {0} RETURN tournament ORDER BY duration.from DESC LIMIT 3")
    List<Tournament> findPreviousTournaments(Long date);

    @Query("MATCH (user:User)<-[:WAS_PLAYED_BY]-(score:Score)-[:WAS_PLAYED_IN_GAME]->(game:Game)<-[:HAS_GAME]-(tournament:Tournament) WHERE id(user) = {0} RETURN tournament ORDER BY game.date ASC")
    List<Tournament> findPreviousTournamentsByUser(Long userId);
}