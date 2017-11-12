package de.tim.feeling.Account;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(path="/rest/account")
public class AccountController {
	@Autowired 
	private AccountRepository accountRepository;
	
	@Value("${tim.restkey}")
	private String restKey;
	
	@GetMapping(path="/all")
	public @ResponseBody Iterable<AccountReturnData> Accounts(@RequestHeader("Key") String Key) {
		if (Key.compareTo(restKey)!=0)
			  return null;
		List<AccountReturnData> accounts = new ArrayList<AccountReturnData>(); 
		for (Account account : accountRepository.findAll()) {
			accounts.add(new AccountReturnData(account));
		}
		return accounts;
	}
	
	@GetMapping(path="/{id}")
	AccountReturnData getAccountByID(@PathVariable Long id, @RequestHeader("Key") String Key) {
		if (Key.compareTo(restKey)!=0)
		  return null;
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
		return new AccountReturnData(account);
	}
	
	@PutMapping(path="/chipid/{chipid}")
	ResponseEntity<?> refreshCode(@PathVariable String chipid, @RequestHeader("Key") String Key) {
		if (Key.compareTo(restKey)!=0)
			  return ResponseEntity.badRequest().build();
		Account account = this.accountRepository.findFirstByChipUID(chipid);
		if (account == null)
			return ResponseEntity.notFound().build();
		do{
			account.refreshCode();
		} while (accountRepository.findFirstByCodeAndCodeTimeOutAfter(account.getCode(), new Date()) != null);
		accountRepository.save(account);
		return ResponseEntity.accepted().build();
	}
	
	@PostMapping
	ResponseEntity<?> add(@RequestBody Account input, @RequestHeader("Key") String Key) {
		if (Key.compareTo(restKey)!=0)
			return ResponseEntity.badRequest().build();
		Account result = accountRepository.save(input);
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{id}")
				.buildAndExpand(result.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
}