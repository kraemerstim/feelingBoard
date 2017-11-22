package de.tim.feeling.Account;

import de.tim.feeling.Team.Team;

public class UserData {
	private String name;
	private String newTeam;
	private String selectedTeam;
	private Iterable<Team> teams;
	
	public UserData(Account account)
	{
		if (account != null)
		{
			name = account.getName();
			setNewTeam(account.getTeam() != null ? account.getTeam().getName() : "");
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

	public String getSelectedTeam() {
		return selectedTeam;
	}

	public void setSelectedTeam(String selectedTeam) {
		this.selectedTeam = selectedTeam;
	}

	public String getNewTeam() {
		return newTeam;
	}

	public void setNewTeam(String newTeam) {
		this.newTeam = newTeam;
	}
	
}
