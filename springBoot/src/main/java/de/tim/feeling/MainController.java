package de.tim.feeling;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import de.tim.feeling.Account.Account;
import de.tim.feeling.Account.UserData;
import de.tim.feeling.Account.AccountData;
import de.tim.feeling.Account.AccountRepository;
import de.tim.feeling.Contact.ContactEntry;
import de.tim.feeling.Contact.ContactEntryRepository;

@Controller
@RequestMapping(path = "/")
public class MainController extends ControllerBase {
	@Autowired
	private AccountRepository accountRepository;

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

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("accountData", new AccountData());
		return "register";
	}
	
	@GetMapping("/userData")
	public String editAccount(Model model) {
		model.addAttribute("userData", new UserData(GetLoggedInUserAccount()));
		return "userData";
	}
	
	@PostMapping("/userData")
	public String setUserData(UserData userData, HttpSession session) {
		Account account = GetLoggedInUserAccount();
		account.setName(userData.getName());
		accountRepository.save(account);

		return "redirect:/";
	}
	
	@GetMapping("/kontakt")
	public String contact(Model model) {
		model.addAttribute("contactEntry", new ContactEntry());
		return "kontakt";
	}

	@PostMapping("/register")
	public String registerResponse(AccountData accountData, Model model) {
		Account account = accountRepository.findFirstByCodeAndCodeTimeOutAfter(accountData.getCode(), new Date());
		if (account == null) {
			model.addAttribute("error", "Code nicht korrekt");
			return "register";
		}
		if (accountRepository.findFirstByUsername(accountData.getUsername()) != null) {
			model.addAttribute("error", "Benutzername existiert bereits");
			return "register";
		}
		
		account.setUsername(accountData.getUsername());
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		account.setPassword(passwordEncoder.encode(accountData.getPassword()));
		accountRepository.save(account);

		model.addAttribute("success", "Registrierung war Erfolgreich");
		
		return "register";
	}
	
	@PostMapping("/kontakt")
	public String fillContact(ContactEntry contactEntry, Model model) {
		contactEntryRepository.save(contactEntry);
		model.addAttribute("success", "Danke " + contactEntry.getName() + " für das Feedback");
		return "kontakt";
	}
}