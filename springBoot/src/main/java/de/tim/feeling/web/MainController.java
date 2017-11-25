package de.tim.feeling.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import de.tim.feeling.Contact.ContactEntry;
import de.tim.feeling.Contact.ContactEntryRepository;

@Controller
@RequestMapping(path = "/")
public class MainController extends ControllerBase {	
	@Autowired
	private ContactEntryRepository contactEntryRepository;
	
	@ModelAttribute
	public void addAttributes(Model model) {
		InsertHeaderModelAttributes(model);
	}

	@GetMapping("/")
	public String home() {
		return "home";
	}

	@GetMapping("/kontakt")
	public String contact(Model model) {
		model.addAttribute("contactEntry", new ContactEntry());
		return "kontakt";
	}
	
	@PostMapping("/kontakt")
	public String fillContact(ContactEntry contactEntry, Model model) {
		contactEntryRepository.save(contactEntry);
		model.addAttribute("success", "Danke " + contactEntry.getName() + " für das Feedback");
		return "kontakt";
	}
}