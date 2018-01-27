package at.hallermayr.swingolf.db;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@SpringBootApplication
@EnableNeo4jRepositories
public class Application {

	private final static Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner demo(UserRepository UserRepository, UserRestController userRestController) {
		return args -> {

			for (User user : UserRepository.findAll()) {
				System.out.println(user);
			}
			for (UserAndLicense userAndLicense : userRestController.readBookmarksAnd()) {
				System.out.println(userAndLicense);
			}
		};
	}

}