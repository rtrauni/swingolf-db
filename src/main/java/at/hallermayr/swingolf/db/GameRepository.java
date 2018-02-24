package at.hallermayr.swingolf.db;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import java.util.List;

public interface GameRepository extends GraphRepository<Game> {

}