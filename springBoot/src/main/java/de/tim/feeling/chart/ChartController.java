package de.tim.feeling.chart;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
		ChartData<String, Double> chartData = getChartEntries(null, null);
		model.addAttribute("ChartData", chartData);
		return "chart";
	}

	@GetMapping("/search")
	public @ResponseBody ChartData<?, ?> getSearchResultViaAjax(Model model, @RequestParam String from,
			@RequestParam String to) throws ParseException {
		DateFormat format = DateFormat.getDateTimeInstance();
		//funktioniert gerade nicht mit dem Filtern
		ChartData<String, Double> chartData = getChartEntries(format.parse(from + " 00:00:00"),
				format.parse(to + " 23:59:59"));
		return chartData;
	}

	private ChartData<String, Double> getChartEntries(Date date1, Date date2) {
		HashMap<String, Double> labelMap = new HashMap<String, Double>();
		ChartSorting sorting = ChartSorting.DAY; 
		Account account = GetLoggedInUserAccount();
		List<Long> accountIDs = Arrays.asList(account.getId(), accountRepository.findFirstByChipUID("0").getId());
		List<ChartEntry> labels = entryRepository.findByAccountsAndGroupedByDay(accountIDs);
		if (labels.size() > 30)
		{
			labels = entryRepository.findByAccountsAndGroupedByWeek(accountIDs);
			sorting = ChartSorting.WEEK;
		}
		if (labels.size() > 30)
		{
			labels = entryRepository.findByAccountsAndGroupedByMonth(accountIDs);
			sorting = ChartSorting.MONTH;
		}
		
		ChartData<String, Double> chartData = new ChartData<String, Double>();
		for (ChartEntry entry: labels)
		{
			labelMap.put(entry.getString(sorting), entry.getFeeling());
		}
		
		TreeMap<String, Double> map = new TreeMap<>(labelMap);
		for (Map.Entry<String, ?> entry : map.entrySet()) {
		  chartData.addLabel(entry.getKey());
		}
		
		List<ChartEntry> entries = getEntries(account, sorting);
		DataSet<String, Double> dataSet = new DataSet<String, Double>();
		for (ChartEntry entry: entries)
		{
			dataSet.addNewDataSetCoords(entry.getString(sorting), entry.getFeeling());
		}
		dataSet.setBackgroundColor(color[0]);
		dataSet.setBorderColor(color[0]);
		dataSet.setType("line");
		chartData.addDataSet(dataSet);
		
		account = accountRepository.findFirstByChipUID("0");
		entries = getEntries(account, sorting);
		
		dataSet = new DataSet<String, Double>();
		for (ChartEntry entry: entries)
		{
			map.put(entry.getString(sorting), entry.getFeeling());
		}
		for (Map.Entry<String, ?> entry : map.entrySet()) {
			dataSet.addNewDataSetCoords(entry.getKey(), (Double)entry.getValue());
		}
		dataSet.setBackgroundColor(barColor[1]);
		dataSet.setBorderColor(color[1]);
		dataSet.setBorderWidth(1);
		dataSet.setLabel("Anonym");
		dataSet.setType("bar");
		chartData.addDataSet(dataSet);
		return chartData;
	}
	
	private List<ChartEntry> getEntries(Account account, ChartSorting sorting)
	{
		List<ChartEntry> entries = null;
		switch (sorting) {
		case DAY:
			entries = entryRepository.findByAccountAndGroupedByDay(account.getId());
			break;
		case WEEK:
			entries = entryRepository.findByAccountAndGroupedByWeek(account.getId());
			break;
		case MONTH:
			entries = entryRepository.findByAccountAndGroupedByMonth(account.getId());
			break;
		default:
			break;
		}
		return entries;
	}
}