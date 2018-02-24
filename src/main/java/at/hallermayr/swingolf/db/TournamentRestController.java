package at.hallermayr.swingolf.db;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController

class TournamentRestController {

	@Autowired
	private TournamentRepository tournamentRepository;

	@Autowired
	private UserRepository userRepository;

	@RequestMapping(value = "/tournaments",method = RequestMethod.GET)
	Collection<Tournament> readBookmarks() {
		return Lists.newArrayList(this.tournamentRepository.findAll());
	}

	@RequestMapping(value = "/tournaments-and-dates",method = RequestMethod.GET)
	Collection<TournamentAndDate> readBookmarksAnd() {
		Iterable<Tournament> tournaments = this.tournamentRepository.findAll();
		return flattenTournamentsAndDates(tournaments);
	}

	@RequestMapping(value = "/tournaments-next3",method = RequestMethod.GET)
	Collection<TournamentAndDate> nextTournaments() {
		Iterable<Tournament> tournaments = this.tournamentRepository.findNextTournaments(DateUtil.getToday());
		List<Long> tournamentIdList = Lists.newArrayList(tournaments).stream().map(Tournament::getId).collect(Collectors.toList());
		tournaments = this.tournamentRepository.findAll(tournamentIdList);
		return flattenTournamentsAndDates(tournaments);
	}

	@RequestMapping(value = "/tournaments-previous3",method = RequestMethod.GET)
	Collection<TournamentAndDate> previousTournaments() {
		Iterable<Tournament> tournaments = this.tournamentRepository.findPreviousTournaments(DateUtil.getToday());
		List<Long> tournamentIdList = Lists.newArrayList(tournaments).stream().map(Tournament::getId).collect(Collectors.toList());
		tournaments = this.tournamentRepository.findAll(tournamentIdList);
		return flattenTournamentsAndDates(tournaments);
	}

	@RequestMapping(value = "/tournaments-previous3-by-player",method = RequestMethod.GET)
	Collection<TournamentAndDate> previousTournamentsPlayer(@RequestParam String license) {
		List<User> users = userRepository.findByLicense(license);
		// TODO workaround for multiple user representation in datamodel
		final List<Tournament> tournaments = new LinkedList<>();
		users.stream().forEach(user -> {
				tournaments.addAll(Lists.newArrayList(this.tournamentRepository.findPreviousTournamentsByUser(user.getId())));
		});

		List<Long> tournamentIdList = Lists.newArrayList(tournaments).stream().map(Tournament::getId).collect(Collectors.toList());
		Iterable<Tournament> result = this.tournamentRepository.findAll(tournamentIdList);
		return flattenTournamentsAndDates(result);
	}

	private Collection<TournamentAndDate> flattenTournamentsAndDates(Iterable<Tournament> tournaments) {
		return StreamSupport.stream(tournaments.spliterator(), false).map(tournament -> new TournamentAndDate(tournament)).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
	};

}