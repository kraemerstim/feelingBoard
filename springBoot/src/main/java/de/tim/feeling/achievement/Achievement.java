package de.tim.feeling.achievement;

import java.util.Set;

import javax.persistence.*;
import javax.persistence.ManyToMany;

import de.tim.feeling.Account.Account;;

@Entity
public class Achievement {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String name;
	private String description;
	private String picture;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name="achievement_account", joinColumns = @JoinColumn(name = "achievement_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "account_id", referencedColumnName = "id"))
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

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public Set<Account> getUnlockedByAccounts() {
		return unlockedByAccounts;
	}

	public void setUnlockedByAccounts(Set<Account> unlockedByAccounts) {
		this.unlockedByAccounts = unlockedByAccounts;
	}
}
