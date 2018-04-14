package at.hallermayr.swingolf.db.model;

public class UserAndLicense implements Comparable<UserAndLicense> {
    private String handicap;
    private String category;
    protected Long id;

    private String firstname;

    private String lastname;

    private String email;

    private String license;

    public UserAndLicense(UserAndLicense userAndLicense) {
        this.id = userAndLicense.getId();
        this.firstname = userAndLicense.firstname;
        this.lastname = userAndLicense.lastname;
        this.email = userAndLicense.email;
        this.license = userAndLicense.license;
        this.category = userAndLicense.category;
        this.handicap = userAndLicense.handicap;
    }

    public UserAndLicense(User user) {
        this.id = user.getId();
        this.firstname = user.getFirstname();
        this.lastname = user.getLastname();
        this.email = user.getEmail();
        this.category = user.getCategory();
        this.handicap = user.getHandicap();
        this.license = user.getLicense() == null ? null : user.getLicense().getLicense();
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

    public String getHandicap() {
        return handicap;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "UserAndLicense{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", license='" + license + '\'' +
                ", category='" + category + '\'' +
                ", handicap='" + handicap + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserAndLicense that = (UserAndLicense) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }


    @Override
    public int compareTo(UserAndLicense o) {
        return this.getId().compareTo(o.getId());
    }
}
