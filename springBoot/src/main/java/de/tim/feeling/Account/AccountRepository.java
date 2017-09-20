package de.tim.feeling.Account;

import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Long> {
	
	Account findFirstByChipUID(String chipUID);
	
}
