package de.tim.feeling.Entry;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import de.tim.feeling.Account.Account;
import de.tim.feeling.web.chart.ChartEntry;

public interface EntryRepository extends CrudRepository<Entry, Long> {
	List<Entry> findByAccount(Account account); 
	List<Entry> findByAccountAndTimestampBetween(Account account, Date date1, Date date2);
	List<Entry> findByAccountAndTimestampBefore(Account account, Date date1);
	List<Entry> findByAccountAndTimestampAfter(Account account, Date date1);
	
	//Custom queries
	//Timestamps 
	//nach Tag gruppiert
	@Query("Select new de.tim.feeling.web.chart.ChartEntry(YEAR(timestamp), MONTH(timestamp), WEEK(timestamp), DAY(timestamp), AVG(feeling)) FROM Entry where account_id in :ids group by DATE(timestamp)")
    List<ChartEntry> findByAccountsAndGroupedByDay(@Param("ids") List<Long> accountIDs);
	
	//nach Woche gruppiert
	@Query("Select new de.tim.feeling.web.chart.ChartEntry(YEAR(timestamp), MONTH(timestamp), WEEK(timestamp), DAY(timestamp), AVG(feeling)) FROM Entry where account_id in :ids group by YEAR(timestamp), WEEK(timestamp)")
    List<ChartEntry> findByAccountsAndGroupedByWeek(@Param("ids") List<Long> accountIDs);
	
	//nach Monat gruppiert
	@Query("Select new de.tim.feeling.web.chart.ChartEntry(YEAR(timestamp), MONTH(timestamp), WEEK(timestamp), DAY(timestamp), AVG(feeling)) FROM Entry where account_id in :ids group by YEAR(timestamp), MONTH(timestamp)")
	List<ChartEntry> findByAccountsAndGroupedByMonth(@Param("ids") List<Long> accountIDs);
}
