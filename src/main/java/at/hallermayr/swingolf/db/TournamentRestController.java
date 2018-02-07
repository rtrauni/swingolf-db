package at.hallermayr.swingolf.db;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController

class TournamentRestController {

	@Autowired
	private TournamentRepository tournamentRepository;

	@RequestMapping(value = "/tournaments",method = RequestMethod.GET)
	Collection<Tournament> readBookmarks() {
		return Lists.newArrayList(this.tournamentRepository.findAll());
	}

	@RequestMapping(value = "/tournaments-and-dates",method = RequestMethod.GET)
	Collection<TournamentAndDate> readBookmarksAnd() {
		Iterable<Tournament> tournaments = this.tournamentRepository.findAll();
		return flattenTournamentsAndDates(tournaments);
	}

	private Collection<TournamentAndDate> flattenTournamentsAndDates(Iterable<Tournament> tournaments) {
		return StreamSupport.stream(tournaments.spliterator(), false).map(tournament -> new TournamentAndDate(tournament)).collect(Collectors.toList());
	};

}