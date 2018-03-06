package de.tim.feeling.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

import de.tim.feeling.account.Account;
import de.tim.feeling.account.AccountRepository;
import de.tim.feeling.web.security.CustomUser;

public class ControllerBase {

	@Autowired
    private AccountRepository accountRepository;
   
    
	public void InsertHeaderModelAttributes(Model model) {
//		Account account = GetLoggedInUserAccount();
//		if (account != null) {
//			// Räume hinzufügen
//			Set<Room> rooms = new HashSet<Room>();
//			for (Player player : account.getPlayers()) {
//				rooms.add(player.getRoom());
//			}
//
//			model.addAttribute("joinedRooms", rooms);
//		}
	}

	public Account GetLoggedInUserAccount() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal != null && principal instanceof CustomUser) {
			return  accountRepository.findOne(((CustomUser) principal).getAccountID());
		}

		return null;
	}
}
