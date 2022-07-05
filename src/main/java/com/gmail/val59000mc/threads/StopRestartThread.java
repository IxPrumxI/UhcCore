package com.gmail.val59000mc.threads;

import com.gmail.val59000mc.UhcCore;
import com.gmail.val59000mc.configuration.MainConfig;
import com.gmail.val59000mc.game.GameManager;
import com.gmail.val59000mc.languages.Lang;

import java.util.logging.Logger;

import org.bukkit.Bukkit;

public class StopRestartThread implements Runnable{

	private static final Logger LOGGER = Logger.getLogger(StopRestartThread.class.getCanonicalName());

	private long timeBeforeStop;

	public StopRestartThread(){
		this.timeBeforeStop = GameManager.getGameManager().getConfig().get(MainConfig.TIME_BEFORE_RESTART_AFTER_END);
	}

	@Override
	public void run() {
		if (timeBeforeStop < 0){
			return; // Stop thread
		}

		GameManager gm = GameManager.getGameManager();

		if(timeBeforeStop == 0){
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
		}else{
			if(timeBeforeStop<5 || timeBeforeStop%10 == 0){
				LOGGER.info("Server will shutdown in "+timeBeforeStop+"s");
				gm.broadcastInfoMessage(Lang.GAME_SHUTDOWN.replace("%time%", ""+timeBeforeStop));
			}

			timeBeforeStop--;
			Bukkit.getScheduler().scheduleSyncDelayedTask(UhcCore.getPlugin(), this,20);
		}
	}

}
