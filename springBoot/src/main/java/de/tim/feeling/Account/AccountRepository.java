package de.tim.feeling.Account;

import java.util.Date;

import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Long> {
	Account findFirstByChipUID(String chipUID);
	Account findFirstByUsername(String username);
	Account findFirstByCodeAndCodeTimeOutAfter(String code, Date date1);
	Account findFirstByCode(String code);
	Account findFirstByCodeTimeOutAfter(Date date1);
}
