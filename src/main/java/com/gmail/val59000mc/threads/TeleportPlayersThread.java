package com.gmail.val59000mc.threads;

import com.gmail.val59000mc.configuration.MainConfig;
import com.gmail.val59000mc.exceptions.UhcPlayerNotOnlineException;
import com.gmail.val59000mc.game.GameManager;
import com.gmail.val59000mc.players.UhcPlayer;
import com.gmail.val59000mc.players.UhcTeam;

import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TeleportPlayersThread implements Runnable{

	private static final Logger LOGGER = Logger.getLogger(TeleportPlayersThread.class.getCanonicalName());

	private final GameManager gameManager;
	private final UhcTeam team;

	public TeleportPlayersThread(GameManager gameManager, UhcTeam team) {
		this.gameManager = gameManager;
		this.team = team;
	}

	@Override
	public void run() {

		for(UhcPlayer uhcPlayer : team.getMembers()){
			Player player;
			try {
				player = uhcPlayer.getPlayer();
			}catch (UhcPlayerNotOnlineException ex){
				continue;
			}

			LOGGER.info("Teleporting "+player.getName());

			for(PotionEffect effect : gameManager.getConfig().get(MainConfig.POTION_EFFECT_ON_START)){
				player.addPotionEffect(effect);
			}

			uhcPlayer.freezePlayer(team.getStartingLocation());

			// Add 2 blocks to the Y location to prevent players from spawning underground.
			Location location = team.getStartingLocation().clone().add(0, 2, 0);
			player.teleport(location);

			player.removePotionEffect(PotionEffectType.BLINDNESS);
			player.setFireTicks(0);
			uhcPlayer.setHasBeenTeleportedToLocation(true);
		}
	}

}
