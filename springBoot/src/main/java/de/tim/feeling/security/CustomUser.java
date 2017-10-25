package de.tim.feeling.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import de.tim.feeling.Account.Account;

public class CustomUser extends User implements UserDetails{
	private static final long serialVersionUID = 6295229428286544967L;
	
	private Long accountID;

	public CustomUser(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
	}
	
	public CustomUser(Account account) {
		super(account.getUsername(), account.getPassword(), getAuthorities(account));
		this.accountID = account.getId();
	}
	
	private static Set<GrantedAuthority> getAuthorities(Account account){
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(account.getRole());
        authorities.add(grantedAuthority);
        return authorities;
    }

	public Long getAccountID() {
		return accountID;
	}

	public void setAccountID(Long accountID) {
		this.accountID = accountID;
	}

}
