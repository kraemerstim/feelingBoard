package de.tim.feeling.Entry;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import de.tim.feeling.Account.Account;

public interface EntryRepository extends CrudRepository<Entry, Long> {
	List<Entry> findByAccount(Account account); 
	List<Entry> findByAccountAndTimestampBetween(Account account, Date date1, Date date2);
	List<Entry> findByAccountAndTimestampBefore(Account account, Date date1);
	List<Entry> findByAccountAndTimestampAfter(Account account, Date date1);
	
	//Custom queries
	@Query("SELECT new de.tim.feeling.Entry.Entry(AVG(feeling), DATE(timestamp))FROM Entry where account_id = ?1 group by DATE(timestamp)")
	List<Entry> findByAccountAndGroupedByDay(Long account_id);
}
