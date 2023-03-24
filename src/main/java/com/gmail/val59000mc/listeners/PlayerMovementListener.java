package com.gmail.val59000mc.listeners;

import com.gmail.val59000mc.configuration.MainConfig;
import com.gmail.val59000mc.configuration.SpectatingMode;
import com.gmail.val59000mc.exceptions.UhcPlayerNotOnlineException;
import com.gmail.val59000mc.players.PlayerManager;
import com.gmail.val59000mc.players.PlayerState;
import com.gmail.val59000mc.players.UhcPlayer;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;

public class PlayerMovementListener implements Listener{

	private final PlayerManager playerManager;
	private final MainConfig config;

	public PlayerMovementListener(PlayerManager playerManager, MainConfig config){
		this.playerManager = playerManager;
		this.config = config;
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event){
		handleFrozenPlayers(event);
		handleSpectatorNearAlive(event);
	}

	private void handleFrozenPlayers(PlayerMoveEvent e){
		UhcPlayer uhcPlayer = playerManager.getUhcPlayer(e.getPlayer());
		if (uhcPlayer.isFrozen()){
			Location freezeLoc = uhcPlayer.getFreezeLocation();
			Location toLoc = e.getTo();

			if (toLoc.getBlockX() != freezeLoc.getBlockX() || toLoc.getBlockZ() != freezeLoc.getBlockZ()){
				Location newLoc = toLoc.clone();
				newLoc.setX(freezeLoc.getBlockX() + .5);
				newLoc.setZ(freezeLoc.getBlockZ() + .5);

				e.getPlayer().teleport(newLoc);
			}
		}
	}

	private void handleSpectatorNearAlive(PlayerMoveEvent event){
		if(event.getPlayer().hasPermission("uhc-core.commands.teleport-admin")) return;
		SpectatingMode mode = config.get(MainConfig.SPECTATING_MODE);
		if (mode == SpectatingMode.TEAMMATE_RADIUS) {
			int radius = config.get(MainConfig.SPECTATING_RADIUS);
			handleTeammateRadius(event, radius);
		} else if (mode == SpectatingMode.TEAMMATE_SPECTATOR_GAMEMODE) {
			handleTeammateSpectator(event);
		} else if (mode == SpectatingMode.SPECTATOR_GAMEMODE) {
			handleSpectatorGamemode(event);
		}
	}

	private void handleTeammateRadius(PlayerMoveEvent event, int radius) {
		try {
			UhcPlayer uhcPlayer = playerManager.getOrCreateUhcPlayer(event.getPlayer());
			if (uhcPlayer.getState().equals(PlayerState.DEAD)) {
				UhcPlayer closestTeammate = uhcPlayer.getClosestTeammate();
				if (closestTeammate.getPlayer().getLocation().distance(uhcPlayer.getPlayer().getLocation()) > radius) {
					uhcPlayer.getPlayer().teleport(closestTeammate.getPlayer());
				}
			} else {
				List<UhcPlayer> spectators = uhcPlayer.getTeam().getMembers(p -> p.getState().equals(PlayerState.DEAD) && p.isOnline());
				for (UhcPlayer spectator : spectators) {
					UhcPlayer closestTeammate = spectator.getClosestTeammate();
					if (closestTeammate.getPlayer().getLocation().distance(spectator.getPlayer().getLocation()) > radius) {
						spectator.getPlayer().teleport(closestTeammate.getPlayer());
					}
				}
			}
		} catch (UhcPlayerNotOnlineException e) {
			throw new RuntimeException(e);
		}
	}

	// This is such that when the player first dies, once they move they will be forced to spectate their closest teammate.
	private void handleTeammateSpectator(PlayerMoveEvent event) {
		try {
			UhcPlayer uhcPlayer = playerManager.getOrCreateUhcPlayer(event.getPlayer());
			if (uhcPlayer.isDeath() && uhcPlayer.getTeam().getOnlinePlayingMembers().size() > 0) {
				UhcPlayer closestTeammate = uhcPlayer.getClosestTeammate();
				uhcPlayer.getPlayer().setSpectatorTarget(closestTeammate.getPlayer());
			}
		} catch (UhcPlayerNotOnlineException e) {
			throw new RuntimeException(e);
		}
	}

	// This is to only allow the player to spectate using the spectator GUI.
	private void handleSpectatorGamemode(PlayerMoveEvent event) {
		try {
			UhcPlayer uhcPlayer = playerManager.getOrCreateUhcPlayer(event.getPlayer());
			if (uhcPlayer.isDeath()) {
				UhcPlayer closestTeammate = uhcPlayer.getClosestTeammate();
				uhcPlayer.getPlayer().setSpectatorTarget(closestTeammate.getPlayer());
			}
		} catch (UhcPlayerNotOnlineException e) {
			throw new RuntimeException(e);
		}
	}
}
