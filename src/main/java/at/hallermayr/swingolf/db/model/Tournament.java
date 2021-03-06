package at.hallermayr.swingolf.db.model;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class Tournament {

    @GraphId
    private Long id;

    private String name;

    private String correcture;

    @Relationship(type = "HAS_DATE", direction = Relationship.OUTGOING)
    Duration duration;

    @Relationship(type = "HAS_GAME", direction = Relationship.OUTGOING)
    Game game;

    protected Tournament() {
        // Empty constructor required as of Neo4j API 2.0.5
    }

    ;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCorrecture() {
        return correcture;
    }

    public Duration getDuration() {
        return duration;
    }

    public Game getGame() {
        return game;
    }

    @Override
    public String toString() {
        return "Tournament{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", duration=" + duration +
                '}';
    }
}