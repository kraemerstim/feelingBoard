package de.tim.feeling.account;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import de.tim.feeling.achievement.Achievement;
import de.tim.feeling.team.Team;


@Entity
public class Account {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String chipUID;
	private String name;
	
	@ManyToOne
	private Team team;
	
	@ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                CascadeType.PERSIST,
                CascadeType.MERGE
            })
	@JoinTable(name="account_achievement", joinColumns = @JoinColumn(name = "account_id"), inverseJoinColumns = @JoinColumn(name = "achievement_id"))
	private Set<Achievement> unlockedAchievements;
	
	private String password;
	private String role;
	private String username;
	private String code;
	private Timestamp codeTimeOut;
	private boolean enabled;
	private Timestamp newAchievementLocktime;
	
	public Account() {
		super();
		enabled = true;
		role = "user";
	}
	
	public String getPassword() {
		return password;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Timestamp getCodeTimeOut() {
		return codeTimeOut;
	}

	public void setCodeTimeOut(Timestamp codeTimeOut) {
		this.codeTimeOut = codeTimeOut;
	}

	public Timestamp getNewAchievementLocktime() {
		return newAchievementLocktime;
	}

	public void setNewAchievementLocktime(Timestamp newAchievementLocktime) {
		this.newAchievementLocktime = newAchievementLocktime;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getChipUID() {
		return chipUID;
	}

	public void setChipUID(String chipUID) {
		this.chipUID = chipUID;
	}
	
	public Set<Achievement> getUnlockedAchievements() {
		return unlockedAchievements;
	}

	public void setUnlockedAchievements(Set<Achievement> unlockedAchievements) {
		this.unlockedAchievements = unlockedAchievements;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void refreshCode() {
		if (getUsername() == null || getUsername().isEmpty()){
			String CodeValues = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
			SecureRandom rnd = new SecureRandom();
			StringBuilder sb = new StringBuilder(4);
		    for( int i = 0; i < 4; i++ ) 
		    	sb.append(CodeValues.charAt(rnd.nextInt(CodeValues.length())));
		    setCode(sb.toString());
		    Timestamp codeTimeout = new Timestamp(System.currentTimeMillis());
		    Calendar cal = Calendar.getInstance();
		    cal.setTime(codeTimeout);
		    cal.add(Calendar.DAY_OF_WEEK, 1);
		    codeTimeout.setTime(cal.getTime().getTime());
		    setCodeTimeOut(codeTimeout);
		}
		else
		{
			setCodeTimeOut(null);
			setCode(null);
		}
	}
}
