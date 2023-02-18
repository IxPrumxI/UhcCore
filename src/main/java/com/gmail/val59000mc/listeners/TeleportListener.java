package com.gmail.val59000mc.listeners;

import com.gmail.val59000mc.configuration.MainConfig;
import com.gmail.val59000mc.exceptions.UhcPlayerNotOnlineException;
import com.gmail.val59000mc.game.GameManager;
import com.gmail.val59000mc.game.GameState;
import com.gmail.val59000mc.languages.Lang;
import com.gmail.val59000mc.players.PlayerManager;
import com.gmail.val59000mc.players.UhcPlayer;
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
		if (e.getCause() == TeleportCause.SPECTATE){
			PlayerManager playerManager = GameManager.getGameManager().getPlayerManager();
			MainConfig config = GameManager.getGameManager().getConfig();
			MainConfig.SPECTATING_MODES mode = config.get(MainConfig.SPECTATING_MODE);
			if (mode.equals(MainConfig.SPECTATING_MODES.TEAMMATE_SPECTATOR_GAMEMODE)) {
				UhcPlayer player = playerManager.getOrCreateUhcPlayer(e.getPlayer());
				UhcPlayer spectatedPlayer = playerManager.getOrCreateUhcPlayer((Player) e.getPlayer().getSpectatorTarget());
				// If not in the same team as the player being spectated, teleport back to the nearest teammate.
				if (!spectatedPlayer.getTeam().equals(player.getTeam()) && player.getTeam().getOnlinePlayingMembers().size() > 0) {
					try{
						e.getPlayer().setSpectatorTarget(player.getClosestTeammate().getPlayer());
						e.getPlayer().sendMessage(Lang.PLAYERS_SPECTATE_TEAMMATE_ONLY_ERROR);
						e.setCancelled(true);
					} catch (UhcPlayerNotOnlineException ex) {
						// ignore, as we will allow the teleport.
					}
				}
			}

			if(!GameManager.getGameManager().getConfig().get(MainConfig.SPECTATING_TELEPORT)) {
				Player player = e.getPlayer();
				if (!player.hasPermission("uhc-core.commands.teleport-admin")) {
					e.setCancelled(true);
					player.sendMessage(Lang.COMMAND_SPECTATING_TELEPORT_ERROR);
				}
			}
		}
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
