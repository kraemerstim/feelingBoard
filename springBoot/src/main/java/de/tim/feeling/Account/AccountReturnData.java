package de.tim.feeling.Account;

public class AccountReturnData {
	private String username;
	private String chipUID;
	private String name;
	private String team;
	private String role;
	
	public AccountReturnData(Account account){
		username = account.getUsername();
		chipUID = account.getChipUID();
		name = account.getName();
		team = account.getTeam();
		role = account.getRole();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getChipUID() {
		return chipUID;
	}

	public void setChipUID(String chipUID) {
		this.chipUID = chipUID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
