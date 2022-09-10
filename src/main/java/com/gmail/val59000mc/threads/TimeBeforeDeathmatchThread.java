package com.gmail.val59000mc.threads;

import com.gmail.val59000mc.UhcCore;
import com.gmail.val59000mc.game.GameManager;
import com.gmail.val59000mc.game.GameState;
import com.gmail.val59000mc.game.handlers.DeathmatchHandler;
import com.gmail.val59000mc.languages.Lang;
import com.gmail.val59000mc.utils.TimeUtils;
import com.gmail.val59000mc.utils.UniversalSound;
import org.bukkit.Bukkit;

public class TimeBeforeDeathmatchThread implements Runnable{

	private final GameManager gameManager;
	private final DeathmatchHandler deathmatchHandler;

	public TimeBeforeDeathmatchThread(GameManager gameManager, DeathmatchHandler deathmatchHandler) {
		this.gameManager = gameManager;
		this.deathmatchHandler = deathmatchHandler;
	}

	@Override
	public void run() {
		long remainingTime = gameManager.getRemainingTime();

		remainingTime--;
		gameManager.setRemainingTime(remainingTime);

		if(0 < remainingTime && remainingTime <= 60 && (remainingTime%10 == 0 || remainingTime <= 10)){
			gameManager.broadcastInfoMessage(Lang.GAME_STARTING_DEATHMATCH_IN.replace("%time%", Long.toString(remainingTime)));
			gameManager.getPlayerManager().playSoundToAll(UniversalSound.CLICK.getSound());
		}

		if (remainingTime == 0){
			deathmatchHandler.startDeathmatch();
		}else if(remainingTime > 0 && gameManager.getGameState() == GameState.PLAYING) {
			Bukkit.getScheduler().runTaskLater(UhcCore.getPlugin(), this, TimeUtils.SECOND_TICKS);
		}
	}

}
