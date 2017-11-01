package de.tim.feeling.Account;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController    // This means that this class is a Controller
@RequestMapping(path="/rest/account") // This means URL's start with /demo (after Application path)
public class AccountController {
	@Autowired 
	private AccountRepository accountRepository;
	
	@GetMapping(path="/all")
	public @ResponseBody Iterable<Account> Accounts() {
		// This returns a JSON or XML with the users
		return accountRepository.findAll();
	}
	
	@GetMapping(path="/{id}")
	AccountReturnData getAccountByID(@PathVariable Long id) {
		Account account = this.accountRepository.findOne(id);
		if (account == null)
			return null;
		else
			return new AccountReturnData(account);
	}
	
	@GetMapping(path="/chipid/{chipid}")
	AccountReturnData getAccountByChipID(@PathVariable String chipid) {
		Account account = this.accountRepository.findFirstByChipUID(chipid);
		if (account == null)
			return null;
		else
			return new AccountReturnData(account);
	}
	
	@PostMapping
	ResponseEntity<?> add(@RequestBody Account input) {
		Account result = accountRepository.save(input);
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{id}")
				.buildAndExpand(result.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
}