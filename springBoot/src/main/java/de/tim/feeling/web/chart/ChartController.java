package de.tim.feeling.web.chart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import de.tim.feeling.account.Account;
import de.tim.feeling.account.AccountRepository;
import de.tim.feeling.entry.EntryRepository;
import de.tim.feeling.web.ControllerBase;

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
	private static String[] barColor = { "rgba(55,102,204,0.5)", "rgba(220,57,18,0.5)", "rgba(255,153,00,0.5)",
			"rgba(16, 150, 24, 0.5)", "rgba(153, 0, 153, 0.5)", "rgba(59, 62, 172, 0.5)", "rgba(0, 153, 198, 0.5)",
			"rgba(221, 68, 119, 0.5)", "rgba(102, 170, 0, 0.5)", "rgba(184, 46, 46, 0.5)", "rgba(49, 99, 149, 0.5)",
			"rgba(153, 68, 153, 0.5)", "rgba(34, 170, 153, 0.5)", "rgba(170, 170, 17, 0.5)", "rgba(102, 51, 204, 0.5)",
			"rgba(230, 115, 0, 0.5)", "rgba(139, 7, 7, 0.5)", "rgba(50, 146, 98, 0.5)", "rgba(85, 116, 166, 0.5)",
			"rgba(59, 62, 172, 0.5)" };

	@GetMapping("/")
	public String chart(Model model) {
		ChartData<String, Double> chartData = getChartEntries();
		model.addAttribute("ChartData", chartData);
		return "chart";
	}

	private ChartData<String, Double> getChartEntries() {
		TreeMap<String, Double> labelMap = new TreeMap<String, Double>();
		ChartSorting sorting = ChartSorting.DAY;
		Account userAccount = GetLoggedInUserAccount();
		Account anonymAccount = accountRepository.findById((long) 1).orElse(null);
		List<Long> teamAccountIDs = new ArrayList<Long>();
		if (userAccount.getTeam() != null) {
			List<Account> accounts = accountRepository.findByTeam(userAccount.getTeam());
			teamAccountIDs = accounts.stream().map(Account::getId).collect(Collectors.toList());
		}
		List<Long> labelAccountIDs = new ArrayList<Long>(Arrays.asList(userAccount.getId(), anonymAccount.getId()));
		labelAccountIDs.addAll(teamAccountIDs);

		List<ChartEntry> labels = getEntries(labelAccountIDs, sorting);
		
		if (labels.size() > 60) {
			sorting = ChartSorting.WEEK;
			labels = getEntries(labelAccountIDs, sorting);
		}
		if (labels.size() > 60) {
			sorting = ChartSorting.MONTH;
			labels = getEntries(labelAccountIDs, sorting);
		}

		ChartData<String, Double> chartData = new ChartData<String, Double>();
		for (ChartEntry entry : labels) {
			String label = entry.getString(sorting);
			labelMap.put(label, 0.0);
			chartData.addLabel(label);
		}

		// Account
		List<ChartEntry> entries = getEntries(userAccount, sorting);
		DataSet<String, Double> dataSet = getDataSet(entries, sorting, null, 0, "line", userAccount.getName() == null ? "Du" : userAccount.getName());
		chartData.addDataSet(dataSet);

		// Team
		if (!teamAccountIDs.isEmpty()) {
			entries = getEntries(teamAccountIDs, sorting);
			dataSet = getDataSet(entries, sorting, labelMap, 1, "bar", userAccount.getTeam().getName());
			chartData.addDataSet(dataSet);
		}

		// Anonym
		entries = getEntries(anonymAccount, sorting);
		dataSet = getDataSet(entries, sorting, labelMap, 2, "bar", "Anonym");
		chartData.addDataSet(dataSet);

		return chartData;
	}

	private DataSet<String, Double> getDataSet(
			List<ChartEntry> entries, 
			ChartSorting sorting,
			TreeMap<String, Double> map, 
			int id, 
			String type, 
			String label) {
		DataSet<String, Double> dataSet = new DataSet<String, Double>();
		if (map == null) {
			for (ChartEntry entry : entries) {
				dataSet.addNewDataSetCoords(entry.getString(sorting), entry.getFeeling());
			}
		} else {
			for (ChartEntry entry : entries) {
				map.put(entry.getString(sorting), entry.getFeeling());
			}
			for (Map.Entry<String, Double> entry : map.entrySet()) {
				dataSet.addNewDataSetCoords(entry.getKey(), (Double) entry.getValue());
				entry.setValue(0.0);
			}
		}
		dataSet.setBackgroundColor(barColor[id]);
		dataSet.setBorderColor(color[id]);
		dataSet.setBorderWidth(1);
		dataSet.setLabel(label);
		dataSet.setType(type);
		return dataSet;
	}

	private List<ChartEntry> getEntries(Account account, ChartSorting sorting) {
		List<Long> accountIDs = Arrays.asList(account.getId());
		List<ChartEntry> entries = getEntries(accountIDs, sorting);
		return entries;
	}

	private List<ChartEntry> getEntries(List<Long> accountIDs, ChartSorting sorting) {
		List<ChartEntry> entries = null;
		switch (sorting) {
		case DAY:
			entries = entryRepository.findByAccountsAndGroupedByDay(accountIDs);
			break;
		case WEEK:
			entries = entryRepository.findByAccountsAndGroupedByWeek(accountIDs);
			break;
		case MONTH:
			entries = entryRepository.findByAccountsAndGroupedByMonth(accountIDs);
			break;
		default:
			break;
		}
		return entries;
	}
}