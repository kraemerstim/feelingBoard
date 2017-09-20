package de.tim.feeling.Entry;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import de.tim.feeling.Account.Account;


@Entity
public class Entry {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	private Account account;
	
	private Timestamp timestamp;
	private Integer feeling;
	
	public Entry() {
		super();
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Timestamp getDateTimeField() {
		return timestamp;
	}
	public void setDateTimeField(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	public Integer getFeeling() {
		return feeling;
	}
	public void setFeeling(Integer feeling) {
		this.feeling = feeling;
	}
    
	
}
