package de.tim.feeling.Team;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TeamRepository extends CrudRepository<Team, Long> {
	Team findFirstByName(String name);
	
	//leere teams finden
	@Query("Select new de.tim.feeling.Team.Team(t.id, t.name) FROM Team t left join t.accounts a where a.team is null")
    List<Team> findEmptyTeams();
}
