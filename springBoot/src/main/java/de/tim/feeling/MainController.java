package de.tim.feeling;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import de.tim.feeling.chart.ChartData;

@Controller // This means that this class is a Controller
@RequestMapping(path = "/")
public class MainController {
	
	@GetMapping("/")
	public String home(Model model) {
		ChartData chartData = new ChartData();
		chartData.initWithFakeData();
		model.addAttribute("ChartData", chartData);
		return "home";
	}

}