package at.hallermayr.swingolf.db;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController

class UserRestController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private GameRepository gameRepository;

	@Autowired
	private TournamentRepository tournamentRepository;
	@Autowired
	private ScoreRepository scoreRepository;
	private Collection<UserAndLicense> users;

	@RequestMapping(value = "/users",method = RequestMethod.GET)
	Collection<User> readBookmarks() {
		return Lists.newArrayList(this.userRepository.findAll());
	}

	@RequestMapping(value = "/users-and-license",method = RequestMethod.GET)
	Collection<UserAndLicense> readBookmarksAnd() {
		if (this.users==null) {
			Iterable<User> users = this.userRepository.findAll();
			this.users = flattenUsersAndLicense(users);
		}
		return this.users;
	}

	@RequestMapping(value = "/usersByTournament",method = RequestMethod.GET)
	Collection<UserAndLicense> findByGame(@RequestParam Long tournamentId) {
		Tournament tournament = tournamentRepository.findOne(tournamentId);
		Game game = tournament.getGame();
		return flattenUsersAndLicense(load(this.userRepository.findByGame(game.getId())));
	}

	@RequestMapping(value = "/usersByTournamentSortedByScore",method = RequestMethod.GET)
	Collection<UserAndLicenseAndScore> findByGametr(@RequestParam Long tournamentId) {
		Tournament tournament = tournamentRepository.findOne(tournamentId);
		Game game = gameRepository.findOne(tournament.getGame().getId());
		List<Long> scoreIds = scoreRepository.findByGame(game.getId()).stream().map(score ->
				score.getId()
		).collect(Collectors.toList());
		Map<Long, Integer> result = StreamSupport.stream(scoreRepository.findAll(scoreIds).spliterator(),false).collect(Collectors.groupingBy(score -> score.getUser().getId(), Collectors.summingInt(value -> Integer.valueOf(value.getScore()))));
		Collection<UserAndLicense> players = findByGame(tournamentId);
		List<UserAndLicenseAndScore> usersAndLicenseAndScore = players.stream().map(userAndLicense -> new UserAndLicenseAndScore(userAndLicense, result.get(userAndLicense.getId()))).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
		Comparator<UserAndLicenseAndScore> compareFunction = (o1, o2) -> (o1.getScore().intValue() - o2.getScore().intValue());
		return usersAndLicenseAndScore.stream().sorted().collect(Collectors.toList());
	}

	private Iterable<User> load(List<User> byGame) {
		List<Long> tournamentIdList = Lists.newArrayList(byGame).stream().map(User::getId).collect(Collectors.toList());
		return userRepository.findAll(tournamentIdList);
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Set<Object> seen = ConcurrentHashMap.newKeySet();
		return t -> seen.add(keyExtractor.apply(t));
	}

	private Collection<UserAndLicense> flattenUsersAndLicense(Iterable<User> users) {
		return StreamSupport.stream(users.spliterator(), false).filter(distinctByKey(user -> user.getLicense().getLicense())).map(user -> new UserAndLicense(user)).collect(Collectors.toList());
	};

}