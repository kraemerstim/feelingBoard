package de.tim.feeling.Entry;

import java.sql.Timestamp;

import de.tim.feeling.Account.AccountRestReturnData;

public class EntryRestReturnData {
	private Long id;
	private AccountRestReturnData account;
	private Timestamp timestamp;
	private Double feeling;
	
	public EntryRestReturnData(Entry entry){
		id = entry.getId();
		account = new AccountRestReturnData(entry.getAccount());
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
	public AccountRestReturnData getAccount() {
		return account;
	}
	public void setAccount(AccountRestReturnData account) {
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
