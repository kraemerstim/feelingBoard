package de.tim.feeling.web.access;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import de.tim.feeling.Account.Account;
import de.tim.feeling.Account.AccountRepository;
import de.tim.feeling.web.ControllerBase;

@Controller
@RequestMapping(path = "/")
public class AccessController extends ControllerBase {

	@Autowired
	private AccountRepository accountRepository;
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("accountData", new AccountRegisterData());
		return "register";
	}
	
	@PostMapping("/register")
	public String registerResponse(AccountRegisterData accountData, Model model) {
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
}