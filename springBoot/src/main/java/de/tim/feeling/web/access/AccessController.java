package de.tim.feeling.web.access;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import de.tim.feeling.account.Account;
import de.tim.feeling.account.AccountRepository;
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
		model.addAttribute("accountRegisterData", new AccountRegisterData());
		return "register";
	}
	
	@PostMapping("/register")
	public String registerResponse(AccountRegisterData accountRegisterData, Model model) {
		Account account = accountRepository.findFirstByCodeAndCodeTimeOutAfter(accountRegisterData.getCode(), new Date());
		if (account == null) {
			model.addAttribute("error", "Code nicht korrekt");
			return "register";
		}
		if (accountRepository.findFirstByUsername(accountRegisterData.getUsername()) != null) {
			model.addAttribute("error", "Benutzername existiert bereits");
			return "register";
		}
		
		account.setUsername(accountRegisterData.getUsername());
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		account.setPassword(passwordEncoder.encode(accountRegisterData.getPassword()));
		account.setCode(null);
		account.setCodeTimeOut(null);
		accountRepository.save(account);

		model.addAttribute("success", "Registrierung war Erfolgreich");
		return "login";
	}
}