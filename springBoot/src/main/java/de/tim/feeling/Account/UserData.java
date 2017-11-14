package de.tim.feeling.Account;

public class UserData {
	private String name;

	public UserData(Account account)
	{
		if (account != null)
			name = account.getName();
	}
	
	public UserData()
	{
		this(null);
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
