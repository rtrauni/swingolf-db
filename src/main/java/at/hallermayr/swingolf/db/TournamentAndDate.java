package at.hallermayr.swingolf.db;

public class TournamentAndDate {
    private Long id;
    private final String name;
    private final Long from;
    private final Long to;

    public TournamentAndDate(Tournament tournament) {
        this.id=tournament.getId();
        this.name =tournament.getName();
        this.from =tournament.getDuration().getFrom();
        this.to =tournament.getDuration().getTo();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getFrom() {
        return from;
    }

    public Long getTo() {
        return to;
    }
}
