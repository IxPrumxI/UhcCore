package com.gmail.val59000mc.listeners;

import com.gmail.val59000mc.configuration.Dependencies;
import com.gmail.val59000mc.exceptions.UhcPlayerDoesNotExistException;
import com.gmail.val59000mc.game.GameManager;
import com.gmail.val59000mc.players.PlayerManager;
import com.gmail.val59000mc.players.UhcPlayer;
import github.scarsz.discordsrv.dependencies.jda.api.entities.ISnowflake;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Member;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Role;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.Collections;
import java.util.List;


public class PlayerPreLoginListener implements Listener {
    private final List<Role> allowedRoles = Dependencies.getDiscordListener().getAllowedRoles();

    @EventHandler
    public void AsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event) throws UhcPlayerDoesNotExistException {
		PlayerManager playerManager = GameManager.getGameManager().getPlayerManager();
        UhcPlayer uhcPlayer = playerManager.getUhcPlayer(event.getUniqueId());
        Member member = uhcPlayer.getDiscordUser();
        if (!Collections.disjoint(Collections.singletonList(member.getRoles().stream().mapToLong(ISnowflake::getIdLong).toArray()), allowedRoles)) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "You aren't allowed to play in this event.");
		}
    }
}
