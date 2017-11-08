package de.tim.feeling;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import de.tim.feeling.Account.Account;
import de.tim.feeling.Account.AccountData;
import de.tim.feeling.Account.AccountRepository;

@Controller
@RequestMapping(path = "/")
public class MainController extends ControllerBase {
	@Autowired
	private AccountRepository accountRepository;

	@ModelAttribute
	public void addAttributes(Model model) {
		InsertHeaderModelAttributes(model);
	}

	@GetMapping("/")
	public String home() {
		return "home";
	}

	@GetMapping("/login")
	public String login(Model model) {
		return "login";
	}

	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("accountData", new AccountData());
		return "register";
	}

	// Muss noch überprüft werden ob der code stimmt und dann dem account
	// zuordnen
	@PostMapping("/register")
	public String registerResponse(AccountData accountData, HttpSession session, Model model) {
		Account account = accountRepository.findFirstByCodeAndCodeTimeOutBefore(accountData.getCode(), new Date());
		if (account == null) {
			model.addAttribute("error", "Code nicht korrekt");
			return "register";
		}
		if (accountRepository.findFirstByUsername(accountData.getUsername()) != null) {
			model.addAttribute("error", "Benutzername existiert bereits");
			return "register";
		}
		
		account.setUsername(accountData.getUsername());
		account.setPassword(accountData.getPassword());
		accountRepository.save(account);

		session.setAttribute("id", account.getId());
		session.setAttribute("isLoggedIn", true);
		return "redirect:/";
	}
}