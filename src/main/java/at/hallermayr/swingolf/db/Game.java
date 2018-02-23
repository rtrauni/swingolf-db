package at.hallermayr.swingolf.db;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;

@NodeEntity
public class Game {

	@GraphId private Long id;

	private String name;
	private Long date;

//	@Relationship(type="WAS_PLAYED_IN_GAME", direction = Relationship.INCOMING)
//	private List<Score> score;

	protected Game() {
		// Empty constructor required as of Neo4j API 2.0.5
	};

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Long getDate() {
		return date;
	}

//	public List<Score> getScore() {
//		return score;
//	}

	@Override
	public String toString() {
		return "Game{" +
				"id=" + id +
				", name='" + name + '\'' +
				", date=" + date +
				'}';
	}
}