package at.hallermayr.swingolf.db.model;

import org.neo4j.ogm.annotation.GraphId;

public class License {

    @GraphId
    private Long id;

    private String license;

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    @Override
    public String toString() {
        return "License{" +
                "id=" + id +
                ", license='" + license + '\'' +
                '}';
    }
}
