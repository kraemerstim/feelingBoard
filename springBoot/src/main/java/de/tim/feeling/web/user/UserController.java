package de.tim.feeling.web.user;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import de.tim.feeling.account.Account;
import de.tim.feeling.account.AccountRepository;
import de.tim.feeling.team.Team;
import de.tim.feeling.team.TeamRepository;
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
	
	@GetMapping("/achievements")
	public String viewAchievement(Model model) {
		return "redirect:/user/";
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
			Team team = teamRepository.findById(userData.getSelectedTeam()).orElse(null);
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