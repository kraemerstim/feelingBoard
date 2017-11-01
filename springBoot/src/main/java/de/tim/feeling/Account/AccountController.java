package de.tim.feeling.Account;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController    // This means that this class is a Controller
@RequestMapping(path="/rest/account") // This means URL's start with /demo (after Application path)
public class AccountController {
	@Autowired 
	private AccountRepository accountRepository;
	
	@Value("${tim.restkey}")
	private String restKey;
	
	@GetMapping(path="/all")
	public @ResponseBody Iterable<AccountReturnData> Accounts() {
		// This returns a JSON or XML with the users
		List<AccountReturnData> accounts = new ArrayList<AccountReturnData>(); 
		for (Account account : accountRepository.findAll()) {
			accounts.add(new AccountReturnData(account));
		}
		return accounts;
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
	AccountReturnData getAccountByChipID(@PathVariable String chipid, @RequestHeader("Key") String Key) {
		if (Key.compareTo(restKey)!=0)
			return null;
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