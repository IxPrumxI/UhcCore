package com.gmail.val59000mc.scenarios.scenariolisteners;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.plugin.EventExecutor;

import com.gmail.val59000mc.UhcCore;
import com.gmail.val59000mc.events.PlayerStartsPlayingEvent;
import com.gmail.val59000mc.events.UhcGameStateChangedEvent;
import com.gmail.val59000mc.exceptions.UhcPlayerNotOnlineException;
import com.gmail.val59000mc.game.GameState;
import com.gmail.val59000mc.players.UhcPlayer;
import com.gmail.val59000mc.scenarios.Option;
import com.gmail.val59000mc.scenarios.ScenarioListener;
import com.gmail.val59000mc.utils.VersionUtils;

import io.papermc.lib.PaperLib;

public class AchievementHunter extends ScenarioListener implements EventExecutor {

	@Option(key = "health-at-start")
	private int healthAtStart = 10;
	@Option(key = "health-added")
	private int healthAdded = 1;

	@Override
	public void onEnable() {
		final Class<? extends Event> eventType;
		if (PaperLib.getMinecraftVersion() < 12) {
			eventType = PlayerAchievementAwardedEvent.class;
		} else {
			eventType = PlayerAdvancementDoneEvent.class;
		}

		Bukkit.getPluginManager().registerEvent(eventType, this, EventPriority.NORMAL, this, UhcCore.getPlugin());
	}

	@Override
	public void execute(Listener listener, Event event) {
		if (getGameManager().getGameState() == GameState.WAITING) {
			return;
		}

		if (shouldAddHeart(event)) {
			addHeart(((PlayerEvent) event).getPlayer());
		}
	}

	@EventHandler
	private void onGameStart(UhcGameStateChangedEvent e) {
		if (e.getNewGameState() != GameState.PLAYING) {
			return;
		}

		for (UhcPlayer uhcPlayer : e.getPlayerManager().getAllPlayingPlayers()) {
			try {
				Player player = uhcPlayer.getPlayer();
				player.setHealth(healthAtStart);
				VersionUtils.getVersionUtils().setPlayerMaxHealth(player, healthAtStart);
			} catch (UhcPlayerNotOnlineException ignored) {
				// Don't set max health for offline players.
			}
		}
	}

	@EventHandler
	public void onPlayerStartsPlaying(PlayerStartsPlayingEvent e) {
		try {
			Player player = e.getUhcPlayer().getPlayer();
			player.setHealth(healthAtStart);
			VersionUtils.getVersionUtils().setPlayerMaxHealth(player, healthAtStart);
		} catch (UhcPlayerNotOnlineException ignored) {
			// Don't set max health for offline players.
		}
	}

	private void addHeart(Player player) {
		VersionUtils.getVersionUtils().setPlayerMaxHealth(player, player.getMaxHealth() + healthAdded);
		player.setHealth(player.getHealth() + healthAdded);
	}

	private boolean shouldAddHeart(Event event) {
		if (PaperLib.getMinecraftVersion() < 12) {
			return event instanceof PlayerAchievementAwardedEvent;
		} else {
			return event instanceof PlayerAdvancementDoneEvent && isValidAdvancement(event);
		}
	}

	private boolean isValidAdvancement(Event event) {
		final NamespacedKey key = ((PlayerAdvancementDoneEvent) event).getAdvancement().getKey();
		return key.getNamespace().equals(NamespacedKey.MINECRAFT)
			&& !key.getKey().startsWith("recipes/")
			&& !key.getKey().endsWith("/root");
	}

}
