package de.tim.feeling.Account;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Long> {
	Account findFirstByChipUID(String chipUID);
	Account findFirstByUsername(String username);
	Account findFirstByCodeAndCodeTimeOutBefore(String code, Date date1);
}
