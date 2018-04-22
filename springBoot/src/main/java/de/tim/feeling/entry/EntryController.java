package de.tim.feeling.entry;

import java.net.URI;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import de.tim.feeling.account.Account;
import de.tim.feeling.account.AccountRepository;
import de.tim.feeling.achievement.Achievement;
import de.tim.feeling.achievement.AchievementRepository;

@RestController
@RequestMapping(path = "/rest/entry")
public class EntryController {
	@Autowired
	private EntryRepository entryRepository;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private AchievementRepository achievementRepository;

	@Value("${secret.restkey}")
	private String restKey;
	
	@GetMapping(path = "/all")
	public @ResponseBody Iterable<EntryRestReturnData> getAllEntries(@RequestHeader("Key") String Key) {
		if (Key.compareTo(restKey)!=0)
			  return null;
		List<EntryRestReturnData> entries = new ArrayList<EntryRestReturnData>();
		for (Entry entry : entryRepository.findAll()) {
			entries.add(new EntryRestReturnData(entry));
		}
		return entries;
	}

	@GetMapping(path = "/{id}")
	EntryRestReturnData getEntryByID(@PathVariable Long id, @RequestHeader("Key") String Key) {
		if (Key.compareTo(restKey)!=0)
			  return null;
		Entry entry = this.entryRepository.findById(id).orElse(null);
		if (entry == null)
			return null;
		return new EntryRestReturnData(entry);
	}

	@PostMapping
	ResponseEntity<?> addEntry(@RequestBody EntryRestInput input, @RequestHeader("Key") String Key) {
		if (Key.compareTo(restKey)!=0)
			return ResponseEntity.badRequest().build();
		Entry entry = new Entry(input.getFeeling(), new Timestamp(System.currentTimeMillis()));
		Account account = accountRepository.findById(input.getAccountID()).orElse(null);
		entry.setAccount(account);
		if (account == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		else {
			Entry result = entryRepository.save(entry);
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(result.getId())
					.toUri();
			BodyBuilder builder = ResponseEntity.created(location);
			String achievement = getNewAchievement(account);
			accountRepository.save(account);
			if (!achievement.isEmpty())
				builder.header("achievement", achievement);
			return builder.build();
		}
	}

	/**
	 * Überprüft ob ein neues Achievement "erreicht" wird. 
	 * Ja: Der Locktimer wird auf einen Tag gesetzt. 
	 * Nein: Der Locktimer wird auf 20 Minuten gesetzt.
	 * @param account Der Account für den das Achievement geprüft wird.
	 * @return Name des Achievements, bzw. ein Leerstring wenn keines erreicht wurde.
	 */
	public String getNewAchievement(Account account) {
		ArrayList<Achievement> allAchievements = (ArrayList<Achievement>) achievementRepository.findAll();
		
		boolean failed = false;
		boolean newTimeout = false;
		Timestamp newAchievementTimeout = new Timestamp(System.currentTimeMillis());
		Achievement selectedAchievement = null;
		Timestamp currentLock = account.getNewAchievementLocktime();
		if (currentLock == null)
			currentLock = new Timestamp(0);
		SecureRandom rnd = new SecureRandom();
		// Fehlerfälle:
		// Admin oder anonym? nie Achievements verteilen!
		if (account.getId() < 2)
			failed = true;
		
		// Ist das Achievement-Lock noch aktiv?
		if (currentLock.after(new Timestamp(System.currentTimeMillis())))
			failed = true;

		// 1. wird ein Achievement erreicht?
		// ((mögliche achievements-erreichte achievements)/mögliche achievements)*0.3
		// -> standard = 0.3 und dann relativ zu den erreichten Achievements
		// + nur einmal am Tag
		if (!failed) {
			double probability = 0.3 * (allAchievements.size() - account.getUnlockedAchievements().size());
			probability /= allAchievements.size();
			if (rnd.nextDouble() > probability)
				failed = true;
		}

		//Kein neues Achievement?
		//-> neues Timeout setzen
		if (failed) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(newAchievementTimeout);
			cal.add(Calendar.MINUTE, 20);
			if (currentLock.before(cal.getTime())) {
				newAchievementTimeout.setTime(cal.getTime().getTime());
				newTimeout = true;
			}
		}

		if (!failed) {
			// Achievement erreicht: Berechnen welches erreicht wurde und zurückgeben
			ArrayList<Achievement> achievements = new ArrayList<Achievement>();
			for (Achievement achievement : allAchievements) {
				boolean found = false;
				for (Achievement unlockedAchievement : account.getUnlockedAchievements()) {
					if (unlockedAchievement.getId() == achievement.getId()) {
						found = true;
						break;
					}
				}
				if (found == false)
					for (int i = 0; i < achievement.getProbability(); i++)
						achievements.add(achievement);
			}
			if (achievements.size() == 0)
				failed = true;
			else {
				selectedAchievement = achievements.get(rnd.nextInt(achievements.size()));
			    Timestamp codeTimeout = new Timestamp(System.currentTimeMillis());
			    Calendar cal = Calendar.getInstance();
			    cal.setTime(codeTimeout);
			    cal.add(Calendar.DAY_OF_WEEK, 1);
			    newAchievementTimeout.setTime(cal.getTime().getTime());
				newTimeout = true;
			}
		}

		if (selectedAchievement != null)
			account.getUnlockedAchievements().add(selectedAchievement);
		if (newTimeout)
			account.setNewAchievementLocktime(newAchievementTimeout);
		accountRepository.save(account);

		return selectedAchievement != null ? selectedAchievement.getName() : "";
	}
}