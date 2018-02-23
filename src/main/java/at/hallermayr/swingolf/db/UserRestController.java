package at.hallermayr.swingolf.db;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController

class UserRestController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TournamentRepository tournamentRepository;

	@RequestMapping(value = "/users",method = RequestMethod.GET)
	Collection<User> readBookmarks() {
		return Lists.newArrayList(this.userRepository.findAll());
	}
	@RequestMapping(value = "/users-and-license",method = RequestMethod.GET)
	Collection<UserAndLicense> readBookmarksAnd() {
		Iterable<User> users = this.userRepository.findAll();
		return flattenUsersAndLicense(users);
	}

	@RequestMapping(value = "/usersByTournament",method = RequestMethod.GET)
	Collection<UserAndLicense> findByGame(@RequestParam Long tournamentId) {
		Tournament tournament = tournamentRepository.findOne(tournamentId);
		Game game = tournament.getGame();
		return flattenUsersAndLicense(load(this.userRepository.findByGame(game.getId())));
	}

	private Iterable<User> load(List<User> byGame) {
		List<Long> tournamentIdList = Lists.newArrayList(byGame).stream().map(User::getId).collect(Collectors.toList());
		return userRepository.findAll(tournamentIdList);
	}

	private Collection<UserAndLicense> flattenUsersAndLicense(Iterable<User> users) {
		return StreamSupport.stream(users.spliterator(), false).map(user -> new UserAndLicense(user)).collect(Collectors.toList());
	};

}