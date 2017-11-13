package de.tim.feeling.Entry;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.tim.feeling.Account.Account;

public interface EntryRepository extends CrudRepository<Entry, Long> {
	List<Entry> findByAccount(Account account); 
	List<Entry> findByAccountAndTimestampBetween(Account account, Date date1, Date date2);
	List<Entry> findByAccountAndTimestampBefore(Account account, Date date1);
	List<Entry> findByAccountAndTimestampAfter(Account account, Date date1);
}
