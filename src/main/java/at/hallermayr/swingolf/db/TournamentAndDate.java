package at.hallermayr.swingolf.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;

public class TournamentAndDate {
    SimpleDateFormat parser=new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
    private Long id;
    private final String name;
    private String from;
    private String to;

    public TournamentAndDate(Tournament tournament) {
        this.id=tournament.getId();
        this.name =tournament.getName();
        try {
            this.from = formatter.format(parser.parse(new Long(tournament.getDuration().getFrom()).toString())).toString();
            this.to =formatter.format(parser.parse(new Long(tournament.getDuration().getFrom()).toString())).toString();
            } catch (ParseException e) {
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}
