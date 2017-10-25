package de.tim.feeling.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import de.tim.feeling.Account.Account;
import de.tim.feeling.Account.AccountRepository;

public class CustomUserDetailsService implements UserDetailsService{

	private AccountRepository accountRepository;
	
    public CustomUserDetailsService(AccountRepository accountRepository){
        this.accountRepository=accountRepository;
    }
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
            Account account = accountRepository.findFirstByUsername(username);
            if (account == null) {
                return null;
            }
            return new CustomUser(account);
        }
        catch (Exception e){
            throw new UsernameNotFoundException("User not found");
        }
	}
}
