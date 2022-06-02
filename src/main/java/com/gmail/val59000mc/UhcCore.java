package com.gmail.val59000mc;

import java.util.Optional;

import com.gmail.val59000mc.game.GameManager;
import com.gmail.val59000mc.utils.FileUtils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.zerodind.uhccore.nms.CreateNmsAdapterException;
import net.zerodind.uhccore.nms.NmsAdapter;
import net.zerodind.uhccore.nms.NmsAdapterFactory;

public class UhcCore extends JavaPlugin{

	private static final int MIN_VERSION = 8;
	private static final int MAX_VERSION = 19;

	private static UhcCore pl;
	private static Optional<NmsAdapter> nmsAdapter;
	private static int version;
	private GameManager gameManager;

	@Override
	public void onEnable(){
		pl = this;

		loadServerVersion();
		loadNmsAdapter();
		gameManager = new GameManager();
		Bukkit.getScheduler().runTaskLater(this, () -> gameManager.loadNewGame(), 1);

		// Delete files that are scheduled for deletion
		FileUtils.removeScheduledDeletionFiles();
	}

	private void loadNmsAdapter() {
		try {
			final NmsAdapter adapter = NmsAdapterFactory.create();
			getLogger().info("Loaded NMS adapter: " + adapter.getClass().getName());
			nmsAdapter = Optional.of(adapter);
		} catch (CreateNmsAdapterException e) {
			getLogger().info(e.getMessage());
			nmsAdapter = Optional.empty();
		}
	}

	// Load the Minecraft version.
	private void loadServerVersion(){
		String versionString = Bukkit.getBukkitVersion();
		version = 0;

		for (int i = MIN_VERSION; i <= MAX_VERSION; i ++){
			if (versionString.contains("1." + i)){
				version = i;
			}
		}

		if (version == 0) {
			version = MIN_VERSION;
			Bukkit.getLogger().warning("[UhcCore] Failed to detect server version! " + versionString + "?");
		}else {
			Bukkit.getLogger().info("[UhcCore] 1." + version + " Server detected!");
		}
	}

	public static int getVersion() {
		return version;
	}

	public static UhcCore getPlugin(){
		return pl;
	}

	public static Optional<NmsAdapter> getNmsAdapter() {
		return nmsAdapter;
	}

	@Override
	public void onDisable(){
		gameManager.getScenarioManager().disableAllScenarios();

		Bukkit.getLogger().info("[UhcCore] Plugin disabled");
	}

}
