package at.hallermayr.swingolf.db;

import org.neo4j.ogm.annotation.GraphId;

public class UserAndLicense {
    private Long id;

    private String firstname;

    private String lastname;

    private String email;

    private String license;

    public UserAndLicense(User user) {
        this.id=user.getId();
        this.firstname =user.getFirstname();
        this.lastname =user.getLastname();
        this.email = user.getEmail();
        this.license = user.getLicense() == null ? null: user.getLicense().getLicense();
    }

    public Long getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getLicense() {
        return license;
    }

    @Override
    public String toString() {
        return "UserAndLicense{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", license='" + license + '\'' +
                '}';
    }
}
