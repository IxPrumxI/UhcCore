package com.gmail.val59000mc.utils;

import com.gmail.val59000mc.game.GameManager;
import com.gmail.val59000mc.maploader.MapLoader;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerPortalEvent;

public class VersionUtils_1_14 extends VersionUtils_1_13{

	@Override
	public void handleNetherPortalEvent(PlayerPortalEvent event){
		Location loc = event.getFrom();
		MapLoader mapLoader = GameManager.getGameManager().getMapLoader();

		if (event.getFrom().getWorld().getEnvironment() == World.Environment.NETHER){
			loc.setWorld(mapLoader.getUhcWorld(World.Environment.NORMAL));
			loc.setX(loc.getX() * 2d);
			loc.setZ(loc.getZ() * 2d);
			event.setTo(loc);
		}else{
			loc.setWorld(mapLoader.getUhcWorld(World.Environment.NETHER));
			loc.setX(loc.getX() / 2d);
			loc.setZ(loc.getZ() / 2d);
			event.setTo(loc);
		}
	}

	@Override
	protected boolean isAir(Material material) {
		return material.isAir();
	}

	@Override
	public boolean isFlower(Block block) {
		// LILY_OF_THE_VALLEY and CORNFLOWER and WITHER_ROSE were introduced in 1.14
		if(block.getType() == Material.LILY_OF_THE_VALLEY || block.getType() == Material.CORNFLOWER || block.getType() == Material.WITHER_ROSE) {
			return true;
		}

		return super.isFlower(block);
	}
}
