package com.gmail.val59000mc.listeners;

import com.gmail.val59000mc.configuration.MainConfig;
import com.gmail.val59000mc.configuration.SpectatingMode;
import com.gmail.val59000mc.exceptions.UhcPlayerNotOnlineException;
import com.gmail.val59000mc.game.GameManager;
import com.gmail.val59000mc.game.GameState;
import com.gmail.val59000mc.languages.Lang;
import com.gmail.val59000mc.players.PlayerManager;
import com.gmail.val59000mc.players.UhcPlayer;
import com.gmail.val59000mc.players.UhcTeam;
import com.gmail.val59000mc.scenarios.Scenario;
import com.gmail.val59000mc.utils.LocationUtils;
import com.gmail.val59000mc.utils.VersionUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class TeleportListener implements Listener{

	private final GameManager gm;

	public TeleportListener(GameManager gm) {
		this.gm = gm;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerPortalEvent (PlayerPortalEvent event){
		Player player = event.getPlayer();

		// Disable nether/end in deathmatch
		if (gm.getGameState() == GameState.DEATHMATCH){
			event.setCancelled(true);
			return;
		}

		if (event.getCause() == TeleportCause.NETHER_PORTAL) {

			if (!gm.getConfig().get(MainConfig.ENABLE_NETHER)){
				player.sendMessage(Lang.PLAYERS_NETHER_OFF);
				event.setCancelled(true);
				return;
			}

			// No Going back!
			if (gm.getScenarioManager().isEnabled(Scenario.NO_GOING_BACK) && event.getFrom().getWorld().getEnvironment() == Environment.NETHER){
				player.sendMessage(Lang.SCENARIO_NOGOINGBACK_ERROR);
				event.setCancelled(true);
				return;
			}

			// Handle event using versions utils as on 1.14+ PortalTravelAgent got removed.
			VersionUtils.getVersionUtils().handleNetherPortalEvent(event);

		}else if (event.getCause() == TeleportCause.END_PORTAL){

			if (gm.getConfig().get(MainConfig.ENABLE_THE_END) && event.getFrom().getWorld().getEnvironment() == Environment.NORMAL){
				// Teleport to end
				World endWorld = gm.getMapLoader().getUhcWorld(Environment.THE_END);
				Location end = new Location(endWorld, -42, 48, -18);

				createEndSpawnAir(end);
				createEndSpawnObsidian(end);

				event.setTo(end);
			}
		}
	}

	@EventHandler
	public void onPlayerChangedWorld(PlayerChangedWorldEvent e){
		GameManager gm = GameManager.getGameManager();
		Player player = e.getPlayer();

		if (gm.getConfig().get(MainConfig.ENABLE_THE_END) && e.getFrom().getName().equals(gm.getMapLoader().getUhcWorldUuid(Environment.THE_END))){
			World world = gm.getMapLoader().getUhcWorld(Environment.NORMAL);

			double maxDistance = 0.9 * gm.getMapLoader().getBorderSize();
			Location loc = LocationUtils.findRandomSafeLocation(world, maxDistance);

			player.teleport(loc);
		}
	}

	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent e){
		PlayerManager playerManager = GameManager.getGameManager().getPlayerManager();
		MainConfig config = GameManager.getGameManager().getConfig();
		SpectatingMode mode = config.get(MainConfig.SPECTATING_MODE);
		if(
			e.getPlayer().hasPermission("uhc-core.commands.teleport-admin") ||
			!(this.gm.getGameState().equals(GameState.PLAYING) || this.gm.getGameState().equals(GameState.DEATHMATCH)) ||
			mode.equals(SpectatingMode.FREE) ||
			!e.getCause().equals(TeleportCause.SPECTATE)) return;
		UhcPlayer player = playerManager.getOrCreateUhcPlayer(e.getPlayer());
		if(!player.isDeath() || player.getTeam().getOnlinePlayingMembers().size() == 0) return;

		if(mode == SpectatingMode.TEAMMATE_RADIUS) {
			handleTeammateRadius(e, player);
		} else if(mode == SpectatingMode.TEAMMATE_SPECTATOR_GAMEMODE) {
			handleSpectatorGamemode(e, player);
		} else if(mode == SpectatingMode.NO_SPECTATOR_GUI) {
			handleNoSpectatorGUI(e);
		}
	}

	// Reimplement the commented code above.
	private void handleTeammateRadius(PlayerTeleportEvent e, UhcPlayer player) {
		// Make sure the player is not teleporting to a teammate.
		// If they are, teleport them to the closest teammate.
		UhcTeam team = player.getTeam();
		if(e.getPlayer().getSpectatorTarget() != null) {
			UhcPlayer spectatedPlayer = GameManager.getGameManager().getPlayerManager().getOrCreateUhcPlayer((Player) e.getPlayer().getSpectatorTarget());
			if(team.equals(spectatedPlayer.getTeam())) return;

			try {
				e.getPlayer().teleport(player.getClosestTeammate().getPlayer());
			} catch (UhcPlayerNotOnlineException ex) {
				// ignore, shouldn't happen.
			}
		}
	}

	private void handleSpectatorGamemode(PlayerTeleportEvent e, UhcPlayer player) {
		// Make sure the player is always spectating a teammate.
		// If they are not, force them to spectate the closest teammate.
		UhcTeam team = player.getTeam();
		if(e.getPlayer().getSpectatorTarget() != null) {
			UhcPlayer spectatedPlayer = GameManager.getGameManager().getPlayerManager().getOrCreateUhcPlayer((Player) e.getPlayer().getSpectatorTarget());
			if(team.equals(spectatedPlayer.getTeam())) return;
		}

		// This will get executed if the player is not spectating a teammate.
		try {
			e.getPlayer().setSpectatorTarget(player.getClosestTeammate().getPlayer());
		} catch (UhcPlayerNotOnlineException ex) {
			// ignore, shouldn't happen.
		}
	}

	private void handleNoSpectatorGUI(PlayerTeleportEvent e) {
		// Make sure the player can't teleport using the spectator gui menu.
		e.setCancelled(true);
		e.getPlayer().setSpectatorTarget(null);
	}

	private void createEndSpawnAir(Location loc){
		int topBlockX = (-41);
		int bottomBlockX = (-44);

		int topBlockY = (50);
		int bottomBlockY = (48);

		int topBlockZ = (-17);
		int bottomBlockZ = (-20);

		for(int x = bottomBlockX; x <= topBlockX; x++) {

			for(int z = bottomBlockZ; z <= topBlockZ; z++) {

				for(int y = bottomBlockY; y <= topBlockY; y++) {

					Block block = loc.getWorld().getBlockAt(x, y, z);

					block.setType(Material.AIR);
				}
			}
		}
	}

	private void createEndSpawnObsidian(Location loc){
		int topBlockX = (-41);
		int bottomBlockX = (-44);

		int topBlockY = (47);
		int bottomBlockY = (47);

		int topBlockZ = (-17);
		int bottomBlockZ = (-20);

		for(int x = bottomBlockX; x <= topBlockX; x++) {

			for(int z = bottomBlockZ; z <= topBlockZ; z++) {

				for(int y = bottomBlockY; y <= topBlockY; y++) {

					Block block = loc.getWorld().getBlockAt(x, y, z);

					block.setType(Material.OBSIDIAN);
				}
			}
		}
	}

}
