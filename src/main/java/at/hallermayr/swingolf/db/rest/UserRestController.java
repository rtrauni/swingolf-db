package at.hallermayr.swingolf.db.rest;

import at.hallermayr.swingolf.db.infrastructure.GameRepository;
import at.hallermayr.swingolf.db.infrastructure.ScoreRepository;
import at.hallermayr.swingolf.db.infrastructure.TournamentRepository;
import at.hallermayr.swingolf.db.infrastructure.UserRepository;
import at.hallermayr.swingolf.db.model.*;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.text.DecimalFormat;
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

    @PostConstruct
    public void init() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                readBookmarksAnd();
            }
        }).start();
    }

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

    @RequestMapping(value = "/userByLicense", method = RequestMethod.GET)
    Collection<UserAndLicense> userbyid(@RequestParam String license) {
        User user = userRepository.findByLicense(license).stream().findFirst().get();
        return Collections.singletonList(flattenUsersAndLicense(load(Collections.singletonList(user))).stream().findFirst().get());
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

    @RequestMapping(value = "/bestScoreByTournament", method = RequestMethod.GET)
    Collection<String> findByGametr2(@RequestParam Long tournamentId) {
        Tournament tournament = tournamentRepository.findOne(tournamentId);
        Game game = gameRepository.findOne(tournament.getGame().getId());
        List<Long> scoreIds = scoreRepository.findByGame(game.getId()).stream().map(score ->
                score.getId()
        ).collect(Collectors.toList());
        Map<Long, Integer> result = StreamSupport.stream(scoreRepository.findAll(scoreIds).spliterator(), false).collect(Collectors.groupingBy(score -> score.getUser().getId(), Collectors.summingInt(value -> Integer.valueOf(value.getScore()))));
        Collection<UserAndLicense> players = findByGame(tournamentId);
        List<UserAndLicenseAndScore> usersAndLicenseAndScore = players.stream().map(userAndLicense -> new UserAndLicenseAndScore(userAndLicense, result.get(userAndLicense.getId()))).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        Comparator<UserAndLicenseAndScore> compareFunction = (o1, o2) -> (o1.getScore().intValue() - o2.getScore().intValue());
        Optional<UserAndLicenseAndScore> first = usersAndLicenseAndScore.stream().sorted().findFirst();
        return first.isPresent() ? Collections.singletonList(first.get().getScore().toString()) : Collections.singletonList("-");
    }

    @RequestMapping(value = "/averageScoreByTournament", method = RequestMethod.GET)
    Collection<String> findByGametr3(@RequestParam Long tournamentId) {
        Tournament tournament = tournamentRepository.findOne(tournamentId);
        Game game = gameRepository.findOne(tournament.getGame().getId());
        List<Long> scoreIds = scoreRepository.findByGame(game.getId()).stream().map(score ->
                score.getId()
        ).collect(Collectors.toList());
        Map<Long, Integer> result = StreamSupport.stream(scoreRepository.findAll(scoreIds).spliterator(), false).collect(Collectors.groupingBy(score -> score.getUser().getId(), Collectors.summingInt(value -> Integer.valueOf(value.getScore()))));
        Collection<UserAndLicense> players = findByGame(tournamentId);
        List<UserAndLicenseAndScore> usersAndLicenseAndScore = players.stream().map(userAndLicense -> new UserAndLicenseAndScore(userAndLicense, result.get(userAndLicense.getId()))).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        Comparator<UserAndLicenseAndScore> compareFunction = (o1, o2) -> (o1.getScore().intValue() - o2.getScore().intValue());
        OptionalDouble average = usersAndLicenseAndScore.stream().mapToInt(value -> value.getScore()).average();
        return average.isPresent() ? Collections.singletonList("" + format(average.getAsDouble())) : Collections.singletonList("-");
    }

    @RequestMapping(value = "/bestTrackByTournament", method = RequestMethod.GET)
    Collection<String> findByGametr4(@RequestParam Long tournamentId) {
        Tournament tournament = tournamentRepository.findOne(tournamentId);
        Game game = gameRepository.findOne(tournament.getGame().getId());
        List<Long> scoreIds = scoreRepository.findByGame(game.getId()).stream().map(score ->
                score.getId()
        ).collect(Collectors.toList());
        OptionalInt min = StreamSupport.stream(scoreRepository.findAll(scoreIds).spliterator(), false).mapToInt(value -> Integer.valueOf(value.getScore())).min();
        return min.isPresent() ? Collections.singletonList("" + min.getAsInt()) : Collections.singletonList("-");
    }

    @RequestMapping(value = "/averageTrackByTournament", method = RequestMethod.GET)
    Collection<String> findByGametr5(@RequestParam Long tournamentId) {
        Tournament tournament = tournamentRepository.findOne(tournamentId);
        Game game = gameRepository.findOne(tournament.getGame().getId());
        List<Long> scoreIds = scoreRepository.findByGame(game.getId()).stream().map(score ->
                score.getId()
        ).collect(Collectors.toList());
        OptionalDouble average = StreamSupport.stream(scoreRepository.findAll(scoreIds).spliterator(), false).mapToInt(value -> Integer.valueOf(value.getScore())).average();
        return average.isPresent() ? Collections.singletonList("" + format(average.getAsDouble())) : Collections.singletonList("-");
    }

    private String format(double asDouble) {
        DecimalFormat df = new DecimalFormat("#.#");
        return df.format(asDouble);
    }

    @RequestMapping(value = "/bestScoreThiyYearByPlayer", method = RequestMethod.GET)
    Collection<String> findByUser(@RequestParam String license) {

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
            if (!group.containsKey(thisYearScore.getGame().getId())) {
                group.put(thisYearScore.getGame().getId(), 0);
            }
            group.put(thisYearScore.getGame().getId(), group.get(thisYearScore.getGame().getId()) + Integer.valueOf(thisYearScore.getScore()));
        }
        Optional<Integer> minScore = group.values().stream().min(Integer::min);
        return minScore.isPresent() ? Collections.singletonList(minScore.get().toString()) : Collections.singletonList("-");
    }

    @RequestMapping(value = "/bestScoreByPlayer", method = RequestMethod.GET)
    Collection<String> findByUser2(@RequestParam String license) {
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
            if (!group.containsKey(thisYearScore.getGame().getId())) {
                group.put(thisYearScore.getGame().getId(), 0);
            }
            group.put(thisYearScore.getGame().getId(), group.get(thisYearScore.getGame().getId()) + Integer.valueOf(thisYearScore.getScore()));
        }
        Optional<Integer> minScore = group.values().stream().min(Integer::min);
        return minScore.isPresent() ? Collections.singletonList(minScore.get().toString()) : Collections.singletonList("-");
    }

    private boolean thisYear(Game game) {
        if (game == null) {
            return false;
        }
        return new GregorianCalendar().get(Calendar.YEAR) * 10000 < Integer.valueOf(game.getDate());
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