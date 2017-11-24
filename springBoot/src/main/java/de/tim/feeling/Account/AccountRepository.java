package de.tim.feeling.Account;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.tim.feeling.Team.Team;

public interface AccountRepository extends CrudRepository<Account, Long> {
	Account findFirstByChipUID(String chipUID);
	Account findFirstByUsername(String username);
	Account findFirstByCodeAndCodeTimeOutAfter(String code, Date date1);
	Account findFirstByCode(String code);
	Account findFirstByCodeTimeOutAfter(Date date1);
	List<Account> findByTeam(Team team);
}
