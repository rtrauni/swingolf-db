package at.hallermayr.swingolf.db.infrastructure;

import at.hallermayr.swingolf.db.model.User;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import java.util.List;

public interface UserRepository extends GraphRepository<User> {

    @Query("MATCH (user:User)<-[:WAS_PLAYED_BY]-(score:Score)-[:WAS_PLAYED_IN_GAME]->(game:Game) WHERE id(game) = {0} RETURN user")
    List<User> findByGame(Long gameId);

    @Query("MATCH (user:User)-[:HAS_LICENSE]->(license:License) WHERE license.license = {0} RETURN user")
    List<User> findByLicense(String license);
}