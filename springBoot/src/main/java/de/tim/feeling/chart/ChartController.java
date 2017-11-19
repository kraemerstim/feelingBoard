package de.tim.feeling.chart;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.tim.feeling.ControllerBase;
import de.tim.feeling.Account.Account;
import de.tim.feeling.Account.AccountRepository;
import de.tim.feeling.Entry.Entry;
import de.tim.feeling.Entry.EntryRepository;

@Controller
@RequestMapping(path = "/chart")
public class ChartController extends ControllerBase {
	@Autowired
	private EntryRepository entryRepository;
	@Autowired
	private AccountRepository accountRepository;

	private static String[] color = { "#3366CC", "#DC3912", "#FF9900", "#109618", "#990099", "#3B3EAC", "#0099C6",
			"#DD4477", "#66AA00", "#B82E2E", "#316395", "#994499", "#22AA99", "#AAAA11", "#6633CC", "#E67300",
			"#8B0707", "#329262", "#5574A6", "#3B3EAC" };
	private static String[] barColor = { "rgba(55,102,204,0.5)", "rgba(220,57,18,0.5)", "rgba(255,153,00,0.5)", "#109618", "#990099", "#3B3EAC", "#0099C6",
			"#DD4477", "#66AA00", "#B82E2E", "#316395", "#994499", "#22AA99", "#AAAA11", "#6633CC", "#E67300",
			"#8B0707", "#329262", "#5574A6", "#3B3EAC" };

	@GetMapping("/")
	public String chart(Model model) throws ParseException {
		ChartData<Date, Double> chartData = getChartEntries(null, null);
		model.addAttribute("ChartData", chartData);
		return "chart";
	}

	@GetMapping("/search")
	public @ResponseBody ChartData<?, ?> getSearchResultViaAjax(Model model, @RequestParam String from,
			@RequestParam String to) throws ParseException {
		DateFormat format = DateFormat.getDateTimeInstance();
		ChartData<Date, Double> chartData = getChartEntries(format.parse(from + " 00:00:00"),
				format.parse(to + " 23:59:59"));
		return chartData;
	}

	private ChartData<Date, Double> getChartEntries(Date date1, Date date2) {
		Account account = GetLoggedInUserAccount();
		ChartData<Date, Double> chartData = new ChartData<Date, Double>();
		DataSet<Date, Double> dataSet = getDataSet(account, date1, date2, false);
		dataSet.setBackgroundColor(color[0]);
		dataSet.setBorderColor(color[0]);
		chartData.addDataSet(dataSet);
		
		account = accountRepository.findFirstByChipUID("0");
		dataSet = getDataSet(account, date1, date2, true);
		dataSet.setBackgroundColor(barColor[1]);
		dataSet.setBorderColor(color[1]);
		dataSet.setBorderWidth(1);
		dataSet.setLabel("Anonym");
		dataSet.setType("bar");
		chartData.addDataSet(dataSet);
		return chartData;
	}
	
	private DataSet<Date, Double> getDataSet(Account account, Date date1, Date date2, boolean groupByDay)
	{
		Iterable<Entry> entries = getEntries(account, date1, date2, groupByDay);
		DataSet<Date, Double> dataSet = new DataSet<Date, Double>();
		if (entries.iterator().hasNext()) {
			dataSet.setLabel(account.getName() != null ? account.getName() : "Du");
			for (Entry entry : entries) {
				dataSet.addNewDataSetCoords(new Date(entry.getTimestamp().getTime()), entry.getFeeling());
			}
		}
		return dataSet;
	}

	private Iterable<Entry> getEntries(Account account, Date date1, Date date2, boolean groupByDay)
	{
		Iterable<Entry> entries;
		if (groupByDay)
			entries = entryRepository.findByAccountAndGroupedByDay(account.getId());
		else if (date1 == null && date2 == null)
		  entries = entryRepository.findByAccount(account);
		else if (date2 == null)
			entries = entryRepository.findByAccountAndTimestampAfter(account, date1);
		else if (date1 == null)
			entries = entryRepository.findByAccountAndTimestampBefore(account, date2);
		else
			entries = entryRepository.findByAccountAndTimestampBetween(account, date1, date2);
		return entries;
	}
}