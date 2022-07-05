package com.gmail.val59000mc.scoreboard;

import com.gmail.val59000mc.UhcCore;
import com.gmail.val59000mc.utils.FileUtils;
import com.gmail.val59000mc.configuration.YamlFile;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScoreboardLayout {

	private static final Logger LOGGER = Logger.getLogger(ScoreboardLayout.class.getCanonicalName());

	private List<String> waiting;
	private List<String> playing;
	private List<String> deathmatch;
	private List<String> spectating;
	private String title;

	public void loadFile(){
		YamlFile cfg;

		try{
			cfg = FileUtils.saveResourceIfNotAvailable(UhcCore.getPlugin(), "scoreboard.yml");
		} catch (IOException | InvalidConfigurationException ex) {
			LOGGER.log(Level.WARNING, "Unable to load scoreboard.yml", ex);

			// Set default values.
			waiting = new ArrayList<>();
			playing = new ArrayList<>();
			deathmatch = new ArrayList<>();
			spectating = new ArrayList<>();
			title = ChatColor.RED + "Error";
			return;
		}

		waiting = getOpsideDownLines(cfg.getStringList("waiting"));
		playing = getOpsideDownLines(cfg.getStringList("playing"));
		deathmatch = getOpsideDownLines(cfg.getStringList("deathmatch"));
		spectating = getOpsideDownLines(cfg.getStringList("spectating"));
		title = ChatColor.translateAlternateColorCodes('&', cfg.getString("title", ""));
	}

	public List<String> getLines(ScoreboardType scoreboardType){
		if (scoreboardType.equals(ScoreboardType.WAITING)){
			return waiting;
		}
		if (scoreboardType.equals(ScoreboardType.PLAYING)){
			return playing;
		}
		if (scoreboardType.equals(ScoreboardType.DEATHMATCH)){
			return deathmatch;
		}
		if (scoreboardType.equals(ScoreboardType.SPECTATING)){
			return spectating;
		}
		return null;
	}

	/**
	 * This method can be used by third party plugins to edit the scoreboard lines.
	 * @param scoreboardType The type the lines should be edited for.
	 * @param lines A list with strings that should be displayed on the scoreboard, can't be more than 15 lines!
	 */
	public void setLines(ScoreboardType scoreboardType, List<String> lines){
		Validate.isTrue(lines.size() <= 15, "Scoreboards can't have more than 15 lines!");

		switch (scoreboardType){
			case WAITING:
				waiting = getOpsideDownLines(lines);
				break;
			case PLAYING:
				playing = getOpsideDownLines(lines);
				break;
			case DEATHMATCH:
				deathmatch = getOpsideDownLines(lines);
				break;
			case SPECTATING:
				spectating = getOpsideDownLines(lines);
				break;
		}
	}

	public String getTitle(){
		return title;
	}

	private List<String> getOpsideDownLines(List<String> list){
		List<String> newList = new ArrayList<>();

		for (int i = list.size()-1; i >= 0; i--){
			if (newList.size() == 15){
				LOGGER.warning("Scoreboard lines can't have more than 15 lines!");
				break;
			}
			newList.add(ChatColor.translateAlternateColorCodes('&',list.get(i)));
		}

		return newList;
	}

}
