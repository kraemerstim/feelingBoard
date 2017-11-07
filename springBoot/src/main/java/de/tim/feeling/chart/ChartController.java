package de.tim.feeling.chart;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.tim.feeling.Account.Account;
import de.tim.feeling.Account.AccountRepository;
import de.tim.feeling.Entry.Entry;
import de.tim.feeling.Entry.EntryRepository;

@Controller // This means that this class is a Controller
@RequestMapping(path = "/chart")
public class ChartController {
	@Autowired 
	private EntryRepository entryRepository;
	@Autowired 
	private AccountRepository accountRepository;
	
	private static String[] color = {"#3366CC","#DC3912","#FF9900","#109618","#990099","#3B3EAC","#0099C6","#DD4477","#66AA00","#B82E2E","#316395","#994499","#22AA99","#AAAA11","#6633CC","#E67300","#8B0707","#329262","#5574A6","#3B3EAC"};
			
	@GetMapping("/")
	public String chart(Model model) throws ParseException {
		//DateFormat format = DateFormat.getDateTimeInstance();
		ChartData<Date> chartData = new ChartData<Date>();
		Iterable<Account> accounts = accountRepository.findAll();
		int index = 0;
		for (Account account : accounts) {
			//Iterable<Entry> entries = entryRepository.findByAccountAndTimestampBetween(account, format.parse("02.09.2017 19:30:00"), format.parse("02.09.2017 20:30:00"));
			Iterable<Entry> entries = entryRepository.findByAccount(account);
			if (!entries.iterator().hasNext())
				continue;
			DataSet<Date> dataSet = new DataSet<Date>();
			dataSet.setLabel(account.getName());
			for (Entry entry : entries) {
				dataSet.addNewDataSetCoords(new Date(entry.getTimestamp().getTime()), entry.getFeeling());
			}
			dataSet.setBackgroundColor(color[index%20]);
			dataSet.setBorderColor(color[index%20]);
			chartData.addDataSet(dataSet);
			index++;
		}
		model.addAttribute("ChartData", chartData);
		return "chart";
	}
	
	@GetMapping("/{id}")
	public String chartById(@PathVariable Long id, Model model) {
		ChartData<Date> chartData = new ChartData<Date>();
		Account account = accountRepository.findOne(id);
		Iterable<Entry> entries = entryRepository.findByAccount(account);
		DataSet<Date> dataSet = new DataSet<Date>();
		dataSet.setLabel(account.getName());
		for (Entry entry : entries) {
			dataSet.addNewDataSetCoords(new Date(entry.getTimestamp().getTime()), entry.getFeeling());
		}
		dataSet.setBackgroundColor(color[0]);
		dataSet.setBorderColor(color[0]);
		chartData.addDataSet(dataSet);

		model.addAttribute("ChartData", chartData);
		return "chart";
	}
	
	 @GetMapping("/search")
	    public @ResponseBody ChartData<?> getSearchResultViaAjax(Model model, @RequestParam String from, @RequestParam String to) throws ParseException {
		 	DateFormat format = DateFormat.getDateTimeInstance();
		    ChartData<Date> result = new ChartData<Date>();
		    Iterable<Account> accounts = accountRepository.findAll();
		    int index = 0;
		    for (Account account : accounts) {
				Iterable<Entry> entries = entryRepository.findByAccountAndTimestampBetween(account, format.parse(from + " 00:00:00"), format.parse(to + " 23:59:59"));
				if (!entries.iterator().hasNext())
					continue;
				DataSet<Date> dataSet = new DataSet<Date>();
				dataSet.setLabel(account.getName());
				for (Entry entry : entries) {
					dataSet.addNewDataSetCoords(new Date(entry.getTimestamp().getTime()), entry.getFeeling());
				}
				dataSet.setBackgroundColor(color[index%20]);
				dataSet.setBorderColor(color[index%20]);
				result.addDataSet(dataSet);
				index++;
			}

	        return result;
	    }
}