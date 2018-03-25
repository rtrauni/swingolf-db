package at.hallermayr.swingolf.db;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    Collection<User> readBookmarks() {
        return Lists.newArrayList(this.userRepository.findAll());
    }

    @RequestMapping(value = "/users-and-license", method = RequestMethod.GET)
    Collection<UserAndLicense> readBookmarksAnd() {
        if (this.users == null) {
            Iterable<User> users = this.userRepository.findAll();
            this.users = flattenUsersAndLicense(users);
        }
        return this.users;
    }

    @RequestMapping(value = "/usersByTournament", method = RequestMethod.GET)
    Collection<UserAndLicense> findByGame(@RequestParam Long tournamentId) {
        Tournament tournament = tournamentRepository.findOne(tournamentId);
        Game game = tournament.getGame();
        return flattenUsersAndLicense(load(this.userRepository.findByGame(game.getId())));
    }

    @RequestMapping(value = "/usersByTournamentSortedByScore", method = RequestMethod.GET)
    Collection<UserAndLicenseAndScore> findByGametr(@RequestParam Long tournamentId) {
        Tournament tournament = tournamentRepository.findOne(tournamentId);
        Game game = gameRepository.findOne(tournament.getGame().getId());
        List<Long> scoreIds = scoreRepository.findByGame(game.getId()).stream().map(score ->
                score.getId()
        ).collect(Collectors.toList());
        Map<Long, Integer> result = StreamSupport.stream(scoreRepository.findAll(scoreIds).spliterator(), false).collect(Collectors.groupingBy(score -> score.getUser().getId(), Collectors.summingInt(value -> Integer.valueOf(value.getScore()))));
        Collection<UserAndLicense> players = findByGame(tournamentId);
        List<UserAndLicenseAndScore> usersAndLicenseAndScore = players.stream().map(userAndLicense -> new UserAndLicenseAndScore(userAndLicense, result.get(userAndLicense.getId()))).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        Comparator<UserAndLicenseAndScore> compareFunction = (o1, o2) -> (o1.getScore().intValue() - o2.getScore().intValue());
        return usersAndLicenseAndScore.stream().sorted().collect(Collectors.toList());
    }

    @RequestMapping(value = "/bestScoreThiyYearByPlayer", method = RequestMethod.GET)
    Integer findByUser(@RequestParam String license) {

        List<User> users = userRepository.findByLicense(license);
        // TODO workaround for multiple user representation in datamodel
        final List<Score> scores = new LinkedList<>();
        users.stream().forEach(user -> {
            scores.addAll(scoreRepository.findByUser(user.getId()));
        });

        List<Long> scoreIds = scores.stream().map(Score::getId).collect(Collectors.toList());
        Iterable<Score> result = scoreRepository.findAll(scoreIds);
        List<Score> thisYearScores = StreamSupport.stream(result.spliterator(), false).filter(score -> thisYear(score.getGame())).collect(Collectors.toList());
        HashMap<Long, Integer> group = new HashMap<>();
        for (Score thisYearScore : thisYearScores) {
            if (!group.containsKey(thisYearScore.getGame().getId())){
                group.put(thisYearScore.getGame().getId(),0);
            }
            group.put(thisYearScore.getGame().getId(),group.get(thisYearScore.getGame().getId())+Integer.valueOf(thisYearScore.getScore()));
        }
        Optional<Integer> minScore = group.values().stream().min(Integer::min);
        return minScore.orElse(null);
    }

    @RequestMapping(value = "/bestScoreByPlayer", method = RequestMethod.GET)
    Integer findByUser2(@RequestParam String license) {
        List<User> users = userRepository.findByLicense(license);
        // TODO workaround for multiple user representation in datamodel
        final List<Score> scores = new LinkedList<>();
        users.stream().forEach(user -> {
            scores.addAll(scoreRepository.findByUser(user.getId()));
        });

        List<Long> scoreIds = scores.stream().map(Score::getId).collect(Collectors.toList());
        Iterable<Score> result = scoreRepository.findAll(scoreIds);
        List<Score> thisYearScores = StreamSupport.stream(result.spliterator(), false).collect(Collectors.toList());
        HashMap<Long, Integer> group = new HashMap<>();
        for (Score thisYearScore : thisYearScores) {
            if (!group.containsKey(thisYearScore.getGame().getId())){
                group.put(thisYearScore.getGame().getId(),0);
            }
            group.put(thisYearScore.getGame().getId(),group.get(thisYearScore.getGame().getId())+Integer.valueOf(thisYearScore.getScore()));
        }
        Optional<Integer> minScore = group.values().stream().min(Integer::min);
        return minScore.orElse(null);
    }

    private boolean thisYear(Game game) {
        if (game == null) {
            return false;
        }
        return new GregorianCalendar().get(Calendar.YEAR) * 1000 < Integer.valueOf(game.getDate());
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
    }

    ;

}