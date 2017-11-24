package de.tim.feeling.Account;

import de.tim.feeling.Team.Team;

public class UserData {
	private String name;
	private String newTeam;
	private Long selectedTeam;
	private Iterable<Team> teams;
	
	public UserData(Account account)
	{
		if (account != null)
		{
			name = account.getName();
			selectedTeam = account.getTeam() != null ? account.getTeam().getId() : -1;
		}
	}
	
	public UserData()
	{
		this(null);
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Iterable<Team> getTeams() {
		return teams;
	}

	public void setTeams(Iterable<Team> teams) {
		this.teams = teams;
	}

	public Long getSelectedTeam() {
		return selectedTeam;
	}

	public void setSelectedTeam(Long selectedTeam) {
		this.selectedTeam = selectedTeam;
	}

	public String getNewTeam() {
		return newTeam;
	}

	public void setNewTeam(String newTeam) {
		this.newTeam = newTeam;
	}
	
}
