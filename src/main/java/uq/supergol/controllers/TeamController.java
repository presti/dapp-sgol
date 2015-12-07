package uq.supergol.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import uq.supergol.model.Team;
import uq.supergol.repositories.PlayerRepository;
import uq.supergol.repositories.TeamRepository;

@RestController
@RequestMapping("/teams")
public class TeamController extends BaseController {
	
	@Autowired
	TeamController(PlayerRepository playerRepository, TeamRepository teamRepository) {
		super(playerRepository, teamRepository);
	}
		
	@RequestMapping(value = "/{teamId}", method = RequestMethod.GET)
	Team readTeam(@PathVariable Long teamId) {
		return getTeam(teamId);
	}

	@RequestMapping(method = RequestMethod.GET)
	Collection<Team> readTeams() {
		return teamRepository.findAll();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	ResponseEntity<?> addTeam(@RequestBody Team input) {
		return getResponseEntity(saveTeam(input));
	}

	@RequestMapping(value = "/{teamId}/points", method = RequestMethod.POST)
	ResponseEntity<?> addPoints(@PathVariable long teamId, @RequestBody int points) {
		return getResponseEntity(saveTeam(getTeam(teamId).addPoints(points)));
	}
	
	@RequestMapping(value = "/{teamId}/player", method = RequestMethod.POST)
	ResponseEntity<?> addPlayer(@PathVariable long teamId, @RequestBody long playerId) {
		return getResponseEntity(saveTeam(getTeam(teamId).addPlayer(getPlayer(playerId))));
	}
	
	@RequestMapping(value = "/{teamId}/captain", method = RequestMethod.POST)
	ResponseEntity<?> addCaptain(@PathVariable long teamId, @RequestBody long captainId) {
		return getResponseEntity(saveTeam(getTeam(teamId).setCaptain(getPlayer(captainId))));
	}
	
}
