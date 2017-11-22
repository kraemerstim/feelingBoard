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
import de.tim.feeling.Team.Team;
import de.tim.feeling.Team.TeamRepository;

@Controller
@RequestMapping(path = "/")
public class MainController extends ControllerBase {
	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private TeamRepository teamRepository;
	
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

	@GetMapping("/charttest")
	public String testChart() {
		return "chartTest";
	}
	
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("accountData", new AccountData());
		return "register";
	}
	
	@GetMapping("/userData")
	public String editAccount(Model model) {
		UserData userData = new UserData(GetLoggedInUserAccount());
		userData.setTeams(teamRepository.findAll());
		model.addAttribute("userData", userData);
		return "userData";
	}
	
	@PostMapping("/userData")
	public String setUserData(UserData userData, HttpSession session) {
		Account account = GetLoggedInUserAccount();
		account.setName(userData.getName());
		
		if (!userData.getNewTeam().isEmpty())
		{
			Team team = teamRepository.findFirstByName(userData.getNewTeam());
			if (team == null)
			{
				team = new Team();
				team.setName(userData.getNewTeam());
				teamRepository.save(team);
			}
			account.setTeam(team);
		}
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
		account.setCode(null);
		account.setCodeTimeOut(null);
		accountRepository.save(account);

		model.addAttribute("success", "Registrierung war Erfolgreich");
		return "login";
	}
	
	@PostMapping("/kontakt")
	public String fillContact(ContactEntry contactEntry, Model model) {
		contactEntryRepository.save(contactEntry);
		model.addAttribute("success", "Danke " + contactEntry.getName() + " für das Feedback");
		return "kontakt";
	}
}