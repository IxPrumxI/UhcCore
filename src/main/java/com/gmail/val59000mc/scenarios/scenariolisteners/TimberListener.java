package com.gmail.val59000mc.scenarios.scenariolisteners;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.gmail.val59000mc.scenarios.Option;
import com.gmail.val59000mc.scenarios.ScenarioListener;
import com.gmail.val59000mc.utils.FloodFill;
import com.gmail.val59000mc.utils.UniversalMaterial;

public class TimberListener extends ScenarioListener {

	@Option(key = "calculate-axe-damage")
	private boolean calculateAxeDamage = true;

	@Option(key = "drop-planks")
	private boolean dropPlanks = false;

	@Option(key = "log-break-limit")
	private int logBreakLimit = 1000;

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		final Block block = e.getBlock();
		final ItemStack tool = e.getPlayer().getItemInHand();

		if (UniversalMaterial.isLog(block.getType()) && UniversalMaterial.isAxe(tool.getType())) {
			final Set<Vector> treeLogs = FloodFill.floodFind26(block, b -> UniversalMaterial.isLog(b.getType()), logBreakLimit);

			final World world = block.getWorld();
			for (Vector v : treeLogs) {
				breakLog(world.getBlockAt(v.getBlockX(), v.getBlockY(), v.getBlockZ()));
			}

			if (calculateAxeDamage) {
				tool.setDurability((short) (tool.getDurability() + treeLogs.size()));
			}
		}
	}

	private void breakLog(Block log) {
		if (dropPlanks) {
			log.setType(Material.AIR);
			final Location itemDropLocation = log.getLocation().add(0.5, 0.5, 0.5);
			final ItemStack itemDrop = new ItemStack(UniversalMaterial.OAK_PLANKS.getType(), 4);
			log.getWorld().dropItem(itemDropLocation, itemDrop);
		} else {
			log.breakNaturally();
		}
	}

}
