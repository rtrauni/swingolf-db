package at.hallermayr.swingolf.db.infrastructure;

import at.hallermayr.swingolf.db.model.Game;
import org.springframework.data.neo4j.repository.GraphRepository;

public interface GameRepository extends GraphRepository<Game> {

}