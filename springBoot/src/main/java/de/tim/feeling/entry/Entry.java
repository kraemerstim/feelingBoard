package de.tim.feeling.entry;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import de.tim.feeling.account.Account;


@Entity
public class Entry {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private Account account;
	
	private Timestamp timestamp;
	private Double feeling;
	
	public Entry() {
		super();
	}
	
	public Entry(Double aFeeling, Date aTimestamp) {
		super();
		timestamp = new Timestamp(aTimestamp.getTime());
		setFeeling(aFeeling);
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public Double getFeeling() {
		return feeling;
	}
	public void setFeeling(Double feeling) {
		this.feeling = feeling;
	}
    
	
}
