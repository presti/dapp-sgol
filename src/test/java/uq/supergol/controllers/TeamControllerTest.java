package uq.supergol.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.junit.Assert;
import uq.supergol.model.Player;
import uq.supergol.model.Position;
import uq.supergol.model.Team;

@RunWith(SpringJUnit4ClassRunner.class)
public class TeamControllerTest extends BaseControllerTest {

	@Test
	public void teamNotFound() throws Exception {
		getMockMvc().perform(get("/teams/1"))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void playerNotFound() throws Exception {
		Team team = addTeam();
		getMockMvc().perform(post("/teams/" + team.getId() + "/player")
				.content("-1")
				.contentType(getContentType()))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void readSingleTeam() throws Exception {
		Team team = addTeam();
		getMockMvc().perform(get("/teams/" + team.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(getContentType()))
				.andExpect(jsonPath("$.id").value(team.getId().intValue()));
	}

	@Test
	public void readTeams() throws Exception {
		Team team0 = addTeam();
		Team team1 = addTeam();

		getMockMvc().perform(get("/teams"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(getContentType()))
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0].id").value(team0.getId().intValue()))
				.andExpect(jsonPath("$[1].id").value(team1.getId().intValue()));
	}

	@Test
	public void createTeam() throws Exception {
		getMockMvc().perform(post("/teams")
				.contentType(getContentType())
				.content(json(new Team("team"))))
				.andExpect(status().isCreated());
	}

	@Test
	public void playerAdd() throws Exception {
		Team team = addTeam();
		addPlayer("Jorge", Position.Defender, "Boca");
		Player player = addPlayer("Dario", Position.Defender, "Estudiantes");
		Assert.assertFalse(team.getPlayers().contains(player));
		getMockMvc().perform(post("/teams/" + team.getId() + "/player")
				.content(String.valueOf(player.getId()))
				.contentType(getContentType()))
				.andExpect(status().isOk());
		Assert.assertTrue(teamContainsPlayer(team.getId(), player.getId()));
	}
	
	@Test
	public void playerRemove() throws Exception {
		Team team = addTeam();
		Player player = addPlayer("Dario", Position.Defender, "Boca");
		getMockMvc().perform(post("/teams/" + team.getId() + "/player")
				.content(String.valueOf(player.getId()))
				.contentType(getContentType()))
				.andExpect(status().isOk());
		Assert.assertTrue(teamContainsPlayer(team.getId(), player.getId()));
		
		getMockMvc().perform(post("/teams/" + team.getId() + "/playerremove")
				.content(String.valueOf(player.getId()))
				.contentType(getContentType()))
				.andExpect(status().isOk());
		Assert.assertFalse(teamContainsPlayer(team.getId(), player.getId()));
	}
	
	@Test
	public void captainAdded() throws Exception {
		Team team = addTeam();
		Player player = addPlayer("Jorge", Position.Defender, "River");

		//adding player
		getMockMvc().perform(post("/teams/" + team.getId() + "/player")
				.content(String.valueOf(player.getId()))
				.contentType(getContentType()))
				.andExpect(status().isOk());
		
		//adding captain
		getMockMvc().perform(post("/teams/" + team.getId() + "/captain")
				.content(String.valueOf(player.getId()))
				.contentType(getContentType()))
				.andExpect(status().isOk());
		
		Assert.assertTrue(teamContainsCaptain(team.getId(), player.getId()));
	}
	
	@Test
	public void pointsAdded() throws Exception {
		Team team = addTeam();
		Assert.assertEquals(0, team.getTotalPoints());
		int pointsToAdd = 3;
		getMockMvc().perform(post("/teams/" + team.getId() + "/totalpoints")
				.content(String.valueOf(pointsToAdd))
				.contentType(getContentType()))
				.andExpect(status().isOk());
		Assert.assertEquals(pointsToAdd, getTeamPoints(team.getId()));

		int newPointsToAdd = 1;
		getMockMvc().perform(post("/teams/" + team.getId() + "/totalpoints")
				.content(String.valueOf(newPointsToAdd))
				.contentType(getContentType()))
				.andExpect(status().isOk());
		Assert.assertEquals(pointsToAdd + newPointsToAdd, getTeamPoints(team.getId()));
	}
	
}
