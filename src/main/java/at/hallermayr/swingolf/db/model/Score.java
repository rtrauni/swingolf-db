package at.hallermayr.swingolf.db.model;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class Score {

    @GraphId
    private Long id;

    private String score;

    @Relationship(type = "WAS_PLAYER_BY", direction = Relationship.OUTGOING)
    User user;

    @Relationship(type = "WAS_PLAYED_IN_GAME", direction = Relationship.OUTGOING)
    Game game;

    protected Score() {
        // Empty constructor required as of Neo4j API 2.0.5
    }

    ;

    public Long getId() {
        return id;
    }

    public String getScore() {
        return score;
    }

    public User getUser() {
        return user;
    }

    public Game getGame() {
        return game;
    }

    @Override
    public String toString() {
        return "Score{" +
                "id=" + id +
                ", score=" + score +
                ", user=" + user +
                ", game=" + game +
                '}';
    }
}