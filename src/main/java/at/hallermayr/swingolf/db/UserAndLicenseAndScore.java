package at.hallermayr.swingolf.db;

public class UserAndLicenseAndScore extends UserAndLicense {
    private Integer score;
    public UserAndLicenseAndScore(UserAndLicense userAndLicense, Integer score) {
        super(userAndLicense);
        this.score = score;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    @Override
    public int compareTo(UserAndLicense o) {
        return this.getScore().compareTo(((UserAndLicenseAndScore)o).getScore());
    }
}
