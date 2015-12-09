package uq.supergol.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Player extends BaseEntity {
	
	protected String name;
	protected String realTeam;
	@Enumerated
	protected Position position;
	@ElementCollection(fetch = FetchType.EAGER)
	protected List<Integer> pointsPerRound = new ArrayList<Integer>();
	@ManyToMany(fetch = FetchType.EAGER)
	@JsonIgnore
	protected Set<Team> teams = new HashSet<Team>();
	
	Player() {}
	
	public Player(String name, Position position) {
		this.name = name;
		this.position = position;
	}
	
	public Player addTeam(Team team) {
		this.getTeams().add(team);
		return this;
	}
	
	public Player addPointsOfRound(int points) {
		getPointsPerRound().add(points);
		return this;
	}
	
	@Override
	public String toString() {
		return toString(", name=%s, position=%s", getName(), getPosition());
	}
	
	public String getName() 					{	return name;	}
	public String getRealTeam() 				{	return realTeam;	}
	public Set<Team> getTeams()					{	return teams;	}
	public List<Integer> getPointsPerRound()	{	return pointsPerRound;	}
	public Position getPosition()				{	return position;	}
	
}
