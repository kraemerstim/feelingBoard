package de.tim.feeling.Entry;

import java.net.URI;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import de.tim.feeling.Account.AccountRepository;

@RestController    // This means that this class is a Controller
@RequestMapping(path="/entry") // This means URL's start with /demo (after Application path)
public class EntryController {
	@Autowired 
	private EntryRepository entryRepository;
	@Autowired 
	private AccountRepository accountRepository;
	
	@GetMapping(path="/all")
	public @ResponseBody Iterable<Entry> Entries() {
		// This returns a JSON or XML with the users
		return entryRepository.findAll();
	}
	
	@GetMapping(path="/{id}")
	Entry getEntryByID(@PathVariable Long id) {
		Entry entry = this.entryRepository.findOne(id);
		return entry;
	}
	
	@PostMapping
	ResponseEntity<?> add(@RequestBody EntryInput input) {
		input.setDateTimeField(new Timestamp(System.currentTimeMillis()));
		Entry entry = new Entry();
		entry.setFeeling(input.getFeeling());
		entry.setTimestamp(input.getTimestamp());
		entry.setAccount(accountRepository.findOne(input.getAccountID()));
		if (entry.getAccount() == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		else{
			Entry result = entryRepository.save(entry);
			URI location = ServletUriComponentsBuilder
					.fromCurrentRequest().path("/{id}")
					.buildAndExpand(result.getId()).toUri();
			return ResponseEntity.created(location).build();
		}
	}
}