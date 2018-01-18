package at.hallermayr.swingolf.db;

import java.util.List;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends GraphRepository<User> {

    User findByFirstname(String firstname);
}