package at.hallermayr.swingolf.db;

import org.neo4j.ogm.annotation.GraphId;

public class Duration {
    @GraphId
    private Long id;
    private Long from;
    private Long to;

    public Long getId() {
        return id;
    }

    public Long getFrom() {
        return from;
    }

    public Long getTo() {
        return to;
    }
}
