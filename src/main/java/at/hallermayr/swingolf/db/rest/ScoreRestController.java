package at.hallermayr.swingolf.db.rest;

import at.hallermayr.swingolf.db.infrastructure.GameRepository;
import at.hallermayr.swingolf.db.infrastructure.ScoreRepository;
import at.hallermayr.swingolf.db.model.Game;
import at.hallermayr.swingolf.db.model.Score;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
class ScoreRestController {

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private GameRepository gameRepository;

    @RequestMapping(value = "/scores", method = RequestMethod.GET)
    Collection<Score> readBookmarks() {
        return Lists.newArrayList(this.scoreRepository.findByScore("10"));
    }

    @RequestMapping(value = "/scoresByGame", method = RequestMethod.GET)
    Collection<Score> findByGame(@RequestParam Long gameId) {
        Game game = gameRepository.findOne(gameId);
        return Lists.newArrayList(this.scoreRepository.findByGame(game.getId()));
    }

}