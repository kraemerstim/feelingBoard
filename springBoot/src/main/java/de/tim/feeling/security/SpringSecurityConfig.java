package de.tim.feeling.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.AccessDeniedHandler;

import de.tim.feeling.Account.AccountRepository;


@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter{
	@Autowired
    private AccessDeniedHandler accessDeniedHandler;
	
	@Autowired
	DataSource dataSource;
	@Autowired 
	private AccountRepository accountRepository;

    // roles admin allow to access /admin/**
    // roles user allow to access /user/**
    // custom 403 access denied handler
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .authorizeRequests()
					.antMatchers("/", "/register", "/home", "/rest/**", "/chart/**", "/lib/**").permitAll()
//					.antMatchers("/admin/**").hasAnyRole("ADMIN")
					.antMatchers("/user/**").hasAnyRole("USER")
					.anyRequest().authenticated()
                .and()
                .formLogin()
					.loginPage("/login")
					.permitAll()
					.and()
                .logout()
					.permitAll()
					.and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(new CustomUserDetailsService(accountRepository));
    }
    
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return new CustomUserDetailsService(accountRepository);
    }
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource).usersByUsernameQuery(
    			"select username,password,enabled from account where username=?")
    		.authoritiesByUsernameQuery(
    			"select username, role from account where username=?");
    }
}
