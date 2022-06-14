package com.gmail.val59000mc.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.gmail.val59000mc.exceptions.UhcPlayerDoesNotExistException;
import com.gmail.val59000mc.exceptions.UhcPlayerNotOnlineException;
import com.gmail.val59000mc.players.PlayerManager;
import com.gmail.val59000mc.players.UhcPlayer;

public class HealCommandExecutor implements CommandExecutor {

	private final PlayerManager playerManager;

	public HealCommandExecutor(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender.hasPermission("uhc-core.commands.heal")) {
			if (args.length == 0) { // Heal all players
				for (UhcPlayer player : this.playerManager.getOnlinePlayingPlayers()) {
					try {
						player.healFully();
					} catch (UhcPlayerNotOnlineException ignored) {
						// Should not happen
					}
				}
			} else if (args.length == 1) { // Heal 1 player
				final String playerName = args[0];
				try {
					playerManager.getUhcPlayer(playerName).healFully();
				} catch (UhcPlayerDoesNotExistException | UhcPlayerNotOnlineException ignored) {
					sender.sendMessage(ChatColor.RED + "Player '" + playerName + "' not found");
				}
			} else { // Invalid usage
				sender.sendMessage("Usage: " + command.getUsage().replace("<command>", label));
			}
		}
		return true;
	}

}
