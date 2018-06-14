package de.tim.feeling.achievement;

import java.util.Set;

import javax.persistence.*;
import javax.persistence.ManyToMany;

import de.tim.feeling.account.Account;;

@Entity
public class Achievement {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	private String description;
	private String activePicture;
	private String inactivePicture;
	private int probability; 
	
	@ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                CascadeType.PERSIST,
                CascadeType.MERGE
            },
            mappedBy = "unlockedAchievements")
	private Set<Account> unlockedByAccounts;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getActivePicture() {
		return activePicture;
	}

	public void setActivePicture(String activePicture) {
		this.activePicture = activePicture;
	}

	public Set<Account> getUnlockedByAccounts() {
		return unlockedByAccounts;
	}

	public void setUnlockedByAccounts(Set<Account> unlockedByAccounts) {
		this.unlockedByAccounts = unlockedByAccounts;
	}

	public String getInactivePicture() {
		return inactivePicture;
	}

	public void setInactivePicture(String inactivePicture) {
		this.inactivePicture = inactivePicture;
	}

	public int getProbability() {
		return probability;
	}

	public void setProbability(int probability) {
		this.probability = probability;
	}
}
