package com.gmail.val59000mc.scenarios.scenariolisteners;

import com.gmail.val59000mc.configuration.MainConfig;
import com.gmail.val59000mc.exceptions.UhcPlayerNotOnlineException;
import com.gmail.val59000mc.game.GameManager;
import com.gmail.val59000mc.players.PlayerState;
import com.gmail.val59000mc.players.UhcPlayer;
import com.gmail.val59000mc.scenarios.Option;
import com.gmail.val59000mc.scenarios.Scenario;
import com.gmail.val59000mc.scenarios.ScenarioListener;
import com.gmail.val59000mc.utils.UniversalMaterial;
import com.gmail.val59000mc.utils.VersionUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DragonRushListener extends ScenarioListener {

	private static final Logger LOGGER = Logger.getLogger(DragonRushListener.class.getCanonicalName());

	@Option(key = "eye-attempts")
	private int eyeAttempts = 12;

	@Option(key = "eye-probability")
	private double eyeProbability = 0.34;

	private PortalStructure portalStructure = null;

	@Override
	public void onEnable() {
		if (!GameManager.getGameManager().getConfig().get(MainConfig.ENABLE_THE_END)) {
			Bukkit.broadcastMessage(ChatColor.RED + "[UhcCore] For DragonRush the end needs to be enabled first!");
			getScenarioManager().disableScenario(Scenario.DRAGON_RUSH);
			return;
		}

		final int eyeAttempts = Math.max(this.eyeAttempts, 0);
		if (eyeAttempts != this.eyeAttempts) {
			LOGGER.warning("dragon_rush.eye-attempts must be a positive integer");
		}
		final double eyeProbability = Math.min(Math.max(this.eyeProbability, 0), 1);
		if (eyeProbability != this.eyeProbability) {
			LOGGER.warning("dragon_rush.eye-probability must be a number between 0 and 1");
		}

		portalStructure = new PortalStructure(determinePortalLocation());
		portalStructure.generate(eyeAttempts, eyeProbability);
	}

	@Override
	public void onDisable() {
		if (portalStructure != null) {
			portalStructure.delete();
		}
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {
		if (e.getEntityType() != EntityType.ENDER_DRAGON) {
			return;
		}

		if (e.getEntity().getKiller() == null) {
			return;
		}

		Player killer = e.getEntity().getKiller();
		UhcPlayer uhcKiller = getPlayerManager().getUhcPlayer(killer);

		List<UhcPlayer> spectators = new ArrayList<>();

		for (UhcPlayer playingPlayer : getPlayerManager().getAllPlayingPlayers()) {
			if (!playingPlayer.isInTeamWith(uhcKiller)) {
				spectators.add(playingPlayer);
			}
		}

		for (UhcPlayer spectator : spectators) {
			spectator.setState(PlayerState.DEAD);

			try {
				Player all = spectator.getPlayer();
				all.setGameMode(GameMode.SPECTATOR);
				all.teleport(killer);
			} catch (UhcPlayerNotOnlineException ignored) {
				// Player logged out, ignoring
			}
		}

		getPlayerManager().checkIfRemainingPlayers();
	}

	private Location determinePortalLocation() {
		final World world = getGameManager().getMapLoader().getUhcWorld(World.Environment.NORMAL);

		int portalY = 0;
		for (int x = -4; x < 4; x++) {
			for (int z = -4; z < 4; z++) {
				final int y = getHighestBlock(world, x, z);
				if (y > portalY) {
					portalY = y;
				}
			}
		}

		return new Location(world, 0, portalY+1, 0);
	}

	private int getHighestBlock(World world, int x, int z) {
		// world#getHighestBlockYAt has different behavior depending on
		// the version, so we need our own, stable implementation.
		// See: https://hub.spigotmc.org/jira/browse/SPIGOT-5523

		// Start search at y=150 to avoid placing it on top of the glass box lobby
		int y = 150;
		while (world.getBlockAt(x, y, z).getType() == Material.AIR) {
			y--;
		}
		return y;
	}


	private static class PortalStructure {

		private final Location portalLocation;

		private PortalStructure(Location portalLocation) {
			this.portalLocation = portalLocation;
		}

		private Stream<Block> getPortalFrameBlocks() {
			return Stream.of(
				// BlockFace.NORTH
				new Vector(1, 0, 2),
				new Vector(0, 0, 2),
				new Vector(-1, 0, 2),

				// BlockFace.EAST
				new Vector(-2, 0, 1),
				new Vector(-2, 0, 0),
				new Vector(-2, 0, -1),

				// BlockFace.SOUTH
				new Vector(1, 0, -2),
				new Vector(0, 0, -2),
				new Vector(-1, 0, -2),

				// BlockFace.WEST
				new Vector(2, 0, 1),
				new Vector(2, 0, 0),
				new Vector(2, 0, -1)
			).map(vec -> portalLocation.clone().add(vec).getBlock());
		}

		private BlockFace getFrameFace(int i) {
			switch (i / 3) {
				case 0: return BlockFace.NORTH;
				case 1: return BlockFace.EAST;
				case 2: return BlockFace.SOUTH;
				case 3: return BlockFace.WEST;
				default: throw new RuntimeException("Bad index");
			}
		}

		private Stream<Block> getPortalBlocks() {
			return Stream.of(
				new Vector(1, 0, 1),
				new Vector(1, 0, 0),
				new Vector(1, 0, -1),
				new Vector(0, 0, 1),
				new Vector(0, 0, 0),
				new Vector(0, 0, -1),
				new Vector(-1, 0, 1),
				new Vector(-1, 0, 0),
				new Vector(-1, 0, -1)
			).map(vec -> portalLocation.clone().add(vec).getBlock());
		}

		private void placeFrames() {
			final List<Block> frames = getPortalFrameBlocks().collect(Collectors.toList());
			for (int i = 0; i < frames.size(); i++) {
				final Block frame = frames.get(i);
				final BlockFace frameFace = getFrameFace(i);
				frame.setType(UniversalMaterial.END_PORTAL_FRAME.getType());
				VersionUtils.getVersionUtils().setEndPortalFrameOrientation(frame, frameFace);
			}
		}

		private void placeEyes(int eyeAttempts, double eyeProbability) {
			final List<Block> frames = getPortalFrameBlocks().collect(Collectors.toCollection(ArrayList::new));
			int attempt = eyeAttempts;
			while (attempt > 0) {
				if (ThreadLocalRandom.current().nextDouble() < eyeProbability) {
					final Block frame = frames.remove(ThreadLocalRandom.current().nextInt(frames.size()));
					VersionUtils.getVersionUtils().setEye(frame, true);
					if (frames.size() == 0) {
						placePortal();
						return;
					}
				}
				attempt--;
			}
		}

		private void placePortal() {
			getPortalBlocks()
				.forEach(b -> b.setType(UniversalMaterial.END_PORTAL.getType()));
		}

		private void generate(int eyeAttempts, double eyeProbability) {
			placeFrames();
			placeEyes(eyeAttempts, eyeProbability);
		}

		private void delete() {
			Stream.concat(getPortalFrameBlocks(), getPortalBlocks())
				.forEach(b -> b.setType(Material.AIR));
		}

	}

}
