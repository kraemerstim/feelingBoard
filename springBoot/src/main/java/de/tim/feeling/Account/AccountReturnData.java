package de.tim.feeling.Account;

import de.tim.feeling.Team.Team;

public class AccountReturnData {
	private String username;
	private String chipUID;
	private String name;
	private Team team;
	private String role;
	private Long id;
	private String code;
	
	public AccountReturnData(Account account){
		username = account.getUsername();
		chipUID = account.getChipUID();
		name = account.getName();
		team = account.getTeam();
		role = account.getRole();
		code = account.getCode();
		setId(account.getId());
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

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
