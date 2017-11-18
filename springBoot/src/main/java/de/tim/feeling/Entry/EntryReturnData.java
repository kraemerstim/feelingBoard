package de.tim.feeling.Entry;

import java.sql.Timestamp;

import de.tim.feeling.Account.AccountReturnData;

public class EntryReturnData {
	private Long id;
	private AccountReturnData account;
	private Timestamp timestamp;
	private Double feeling;
	
	public EntryReturnData(Entry entry){
		id = entry.getId();
		account = new AccountReturnData(entry.getAccount());
		timestamp = entry.getTimestamp();
		feeling = entry.getFeeling();
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
	public AccountReturnData getAccount() {
		return account;
	}
	public void setAccount(AccountReturnData account) {
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
