package at.hallermayr.swingolf.db;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class User {

	@GraphId private Long id;

	private String firstname;

    private String lastname;

    private String email;

	private String handicap;

	private String category;

	@Relationship(type="HAS_LICENSE", direction = Relationship.OUTGOING)
	License license;

	protected User() {
		// Empty constructor required as of Neo4j API 2.0.5
	};

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public License getLicense() {
		return license;
	}

	public void setLicense(License license) {
		this.license = license;
	}

	public String getHandicap() {
		return handicap;
	}

	public void setHandicap(String handicap) {
		this.handicap = handicap;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", firstname='" + firstname + '\'' +
				", lastname='" + lastname + '\'' +
				", email='" + email + '\'' +
				", license=" + license +
				'}';
	}
}