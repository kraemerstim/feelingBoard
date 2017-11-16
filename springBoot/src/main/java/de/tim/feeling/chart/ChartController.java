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

	@GetMapping("/")
	public String chart(Model model) throws ParseException {
		ChartData<Date, String> chartData = getChartEntries(null, null);
		model.addAttribute("ChartData", chartData);
		return "chart";
	}

	@GetMapping("/search")
	public @ResponseBody ChartData<?, ?> getSearchResultViaAjax(Model model, @RequestParam String from,
			@RequestParam String to) throws ParseException {
		DateFormat format = DateFormat.getDateTimeInstance();
		ChartData<Date, String> chartData = getChartEntries(format.parse(from + " 00:00:00"),
				format.parse(to + " 23:59:59"));
		return chartData;
	}

	private ChartData<Date, String> getChartEntries(Date date1, Date date2) {
		Account account = GetLoggedInUserAccount();
		ChartData<Date, String> chartData = new ChartData<Date, String>();
		for (int i = 0; i <= 5; i++)
			chartData.addLabel(translateFeelings(i));
		Iterable<Entry> entries = getEntries(account, date1, date2);
		if (entries.iterator().hasNext()) {
			DataSet<Date, String> dataSet = new DataSet<Date, String>();
			dataSet.setLabel(account.getName() != null ? account.getName() : "Du");
			for (Entry entry : entries) {
				dataSet.addNewDataSetCoords(new Date(entry.getTimestamp().getTime()), translateFeelings(entry.getFeeling()));
			}
			dataSet.setBackgroundColor(color[0]);
			dataSet.setBorderColor(color[0]);
			chartData.addDataSet(dataSet);
		}
		account = accountRepository.findFirstByChipUID("0");
		entries = getEntries(account, date1, date2);
		if (entries.iterator().hasNext()) {
			DataSet<Date, String> dataSet = new DataSet<Date, String>();
			dataSet.setLabel("Anonym");
			for (Entry entry : entries) {
				dataSet.addNewDataSetCoords(new Date(entry.getTimestamp().getTime()), translateFeelings(entry.getFeeling()));
			}
			dataSet.setBackgroundColor(color[1]);
			dataSet.setBorderColor(color[1]);
			chartData.addDataSet(dataSet);
		}
		return chartData;
	}
	
	private String translateFeelings(Integer entry)
	{
		String result = "error";
		switch (entry) {
		case 0:
			result = "super";
			break;
		case 1:
			result = "gut";
			break;
		case 2:
			result = "läuft";
			break;
		case 3:
			result = "grml";
			break;
		case 4:
			result = "grrrrrr";
			break;
		case 5:
			result = "$%&§";
			break;
		}
		return result;
	}

	private Iterable<Entry> getEntries(Account account, Date date1, Date date2)
	{
		Iterable<Entry> entries;
		if (date1 == null && date2 == null)
		  entries = entryRepository.findByAccount(account);
		else if (date2 == null)
			entries = entryRepository.findByAccountAndTimestampAfter(account, date1);
		else if (date1 == null)
			entries = entryRepository.findByAccountAndTimestampAfter(account, date2);
		else
			entries = entryRepository.findByAccountAndTimestampBetween(account, date1, date2);
		return entries;
	}
}