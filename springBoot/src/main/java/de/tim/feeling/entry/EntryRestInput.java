package de.tim.feeling.entry;

public class EntryRestInput extends Entry {
	private long accountID;

	public EntryRestInput() {
		super();
	}

	public long getAccountID() {
		return accountID;
	}
	public void setAccountID(long accountID) {
		this.accountID = accountID;
	}
	
}
