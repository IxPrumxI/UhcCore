package com.gmail.val59000mc.scenarios.scenariolisteners;

import com.gmail.val59000mc.players.UhcPlayer;
import com.gmail.val59000mc.scenarios.ScenarioListener;
import com.gmail.val59000mc.utils.OreType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

public class BloodDiamondsListener extends ScenarioListener{

	@EventHandler (ignoreCancelled = true, priority = EventPriority.LOW)
	public void onBlockBreak(BlockBreakEvent e){
		if (!OreType.DIAMOND.equals(e.getBlock().getType())){
			return;
		}

		UhcPlayer.damageIrreducible(e.getPlayer(), 1);
	}

}
