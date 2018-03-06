package de.tim.feeling.account;

import java.math.BigInteger;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
	public @ResponseBody Iterable<AccountRestReturnData> getAllAccounts(@RequestHeader("Key") String Key) {
		if (Key.compareTo(restKey)!=0)
			  return null;
		List<AccountRestReturnData> returnAccounts = new ArrayList<AccountRestReturnData>(); 
		for (Account account : accountRepository.findAll()) {
			returnAccounts.add(new AccountRestReturnData(account));
		}
		return returnAccounts;
	}
	
	@GetMapping(path="/{id}")
	AccountRestReturnData getAccountByID(@PathVariable Long id, @RequestHeader("Key") String Key) {
		if (Key.compareTo(restKey)!=0)
		  return null;
		Account account = this.accountRepository.findOne(id);
		if (account == null)
			return null;
		else
			return new AccountRestReturnData(account);
	}
	
	@GetMapping(path="/chipid/{chipid}")
	AccountRestReturnData getAccountByChipID(@PathVariable String chipid, @RequestHeader("Key") String Key) {
		if (Key.compareTo(restKey)!=0)
			return null;
		Account account = this.accountRepository.findFirstByChipUID(hashString(chipid));
		if (account == null)
			return null;
		return new AccountRestReturnData(account);
	}
	
	@PutMapping(path="/chipid/{chipid}")
	ResponseEntity<?> refreshCode(@PathVariable String chipid, @RequestHeader("Key") String Key) {
		if (Key.compareTo(restKey)!=0)
			  return ResponseEntity.badRequest().build();
		Account account;
		account = this.accountRepository.findFirstByChipUID(hashString(chipid));
		if (account == null || chipid.equals("0"))
			return ResponseEntity.notFound().build();
		do{
			account.refreshCode();
		} while (accountRepository.findFirstByCodeAndCodeTimeOutAfter(account.getCode(), new Date()) != null);
		accountRepository.save(account);
	
		return ResponseEntity.accepted().build();
	}
	
	@PostMapping
	ResponseEntity<?> addAccount(@RequestBody Account input, @RequestHeader("Key") String Key) {
		if (Key.compareTo(restKey)!=0)
			return ResponseEntity.badRequest().build();
		input.setChipUID(hashString(input.getChipUID()));
		Account result = accountRepository.save(input);
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{id}")
				.buildAndExpand(result.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	public String hashString(String source)
	{
		MessageDigest md;
		try {
			md = MessageDigest.getInstance( "SHA-256" );
			md.update( source.getBytes( StandardCharsets.UTF_8 ) );
			byte[] digest = md.digest();
			String hex = String.format( "%064x", new BigInteger( 1, digest ) );
			return hex;
		} catch (NoSuchAlgorithmException e) {
			return "";
		}
	}

}