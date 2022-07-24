package com.gmail.val59000mc.scenarios.scenariolisteners;

import com.gmail.val59000mc.languages.Lang;
import com.gmail.val59000mc.scenarios.ScenarioListener;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.spigotmc.event.entity.EntityMountEvent;

public class HorselessListener extends ScenarioListener{

	@EventHandler
	public void onHorseRide(EntityMountEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = ((Player) e.getEntity()).getPlayer();
			if (isHorseLike(e.getMount().getType())) {
				p.sendMessage(Lang.SCENARIO_HORSELESS_ERROR);
				e.setCancelled(true);
			}
		}
	}

	private boolean isHorseLike(EntityType entityType) {
		switch (entityType) {
			case HORSE:
			case ZOMBIE_HORSE:
			case SKELETON_HORSE:
			case DONKEY:
			case MULE: return true;
			default: return false;
		}
	}

}
