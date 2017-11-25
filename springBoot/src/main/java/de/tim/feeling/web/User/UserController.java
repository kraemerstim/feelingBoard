package de.tim.feeling.web.User;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import de.tim.feeling.Account.Account;
import de.tim.feeling.Account.AccountRepository;
import de.tim.feeling.Team.Team;
import de.tim.feeling.Team.TeamRepository;
import de.tim.feeling.web.ControllerBase;

@Controller
@RequestMapping(path = "/user")
public class UserController extends ControllerBase {

	@Autowired
	private TeamRepository teamRepository;
	@Autowired
	private AccountRepository accountRepository;
	
	@GetMapping("/")
	public String editAccount(Model model) {
		UserData userData = new UserData(GetLoggedInUserAccount());
		userData.setTeams(teamRepository.findAll());
		model.addAttribute("userData", userData);
		return "user";
	}
	
	@PostMapping("/")
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
		else if (account.getTeam() == null || userData.getSelectedTeam() != account.getTeam().getId())
		{
			Team team = teamRepository.findOne(userData.getSelectedTeam());
			account.setTeam(team);
		}
		accountRepository.save(account);
		
		List<Team> emptyTeams = teamRepository.findEmptyTeams();
		for (Team emptyTeam : emptyTeams)
		{
			teamRepository.delete(emptyTeam);
		}

		return "redirect:/";
	}
}