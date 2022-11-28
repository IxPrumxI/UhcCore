package com.gmail.val59000mc.discord;

import com.gmail.val59000mc.UhcCore;
import com.gmail.val59000mc.configuration.MainConfig;
import com.gmail.val59000mc.events.UhcGameStateChangedEvent;
import com.gmail.val59000mc.events.UhcPlayerStateChangedEvent;
import com.gmail.val59000mc.events.UhcStartingEvent;
import com.gmail.val59000mc.events.UhcWinEvent;
import com.gmail.val59000mc.game.GameManager;
import com.gmail.val59000mc.game.GameState;
import com.gmail.val59000mc.players.*;
import com.gmail.val59000mc.scenarios.ScenarioManager;
import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.DiscordReadyEvent;
import github.scarsz.discordsrv.dependencies.jda.api.EmbedBuilder;
import github.scarsz.discordsrv.dependencies.jda.api.Permission;
import github.scarsz.discordsrv.dependencies.jda.api.entities.*;
import github.scarsz.discordsrv.dependencies.jda.api.requests.restaction.ChannelAction;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class DiscordListener implements Listener {

  private final List<Role> allowedRoles = new ArrayList<>();

  private final List<Role> eventOrganizers = new ArrayList<>();

  private Category eventCategory;

  private TextChannel UHCChat;

  private VoiceChannel UHCVoice;

  public DiscordSRV getDiscordAPI() {
    return DiscordSRV.getPlugin();
  }

  public Guild getMainGuild() {
    return getDiscordAPI().getMainGuild();
  }

  public boolean isPublicEvent() {
    return allowedRoles.contains(getMainGuild().getPublicRole());
  }

  public GameManager getGameManager() {
    return GameManager.getGameManager();
  }

  public PlayerManager getPlayerManager() {
    return getGameManager().getPlayerManager();
  }

  public TeamManager getTeamManager() {
    return getGameManager().getTeamManager();
  }

  public ScenarioManager getScenarioManager() {
    return getGameManager().getScenarioManager();
  }

  public MainConfig getConfiguration() {
    return getGameManager().getConfig();
  }

  public DiscordListener() {
    DiscordSRV.api.subscribe(this);
  }

  @Subscribe
  public void discordReadyEvent(DiscordReadyEvent ignored) {
    updateAllowedRoles();
    updateEventOrganizers();
    updateEventCategory();
  }

  @EventHandler
  public void onUhcReadyEvent(UhcGameStateChangedEvent event) {
    if(event.getNewGameState() == GameState.WAITING && event.getOldGameState() == GameState.LOADING) {
      EmbedBuilder embed = new EmbedBuilder()
              .setTitle("New UHC Game")
              .addField("IP", getConfiguration().getString("discord.event-ip", "play.myserver.com"), true)
              .addField("Version", "1." + UhcCore.getVersion(), true);
      if (!isPublicEvent())
        embed.setDescription("Players must have one of these roles inorder to play in this event:\n" + allowedRoles.stream().map(IMentionable::getAsMention).collect(Collectors.joining(" - ")));

      UHCChat.sendMessage(embed.build()).queue();
    }
  }

  @EventHandler
  public void onUhcStartedEvent(UhcStartingEvent ignored) {
    for (VoiceChannel voiceChannel : eventCategory.getVoiceChannels()) {
      if (voiceChannel.equals(UHCVoice)) continue;
      voiceChannel.delete().queue();
    }

    EmbedBuilder embed = new EmbedBuilder()
            .setAuthor("UHC Game has started!")
            .setTitle("Teams:");

    Iterator<UhcTeam> teamsIterator = getTeamManager().getUhcTeams().stream().iterator();
    while (teamsIterator.hasNext()) {
      UhcTeam team = teamsIterator.next();
      String channelName = "Team " + team.getTeamNumber();
      if (team.getTeamName() != null) channelName = team.getTeamName();
      embed.addField(channelName, team.getMembers().stream().map(m -> m.getDiscordUser().getAsMention()).collect(Collectors.joining(" - ")), true);
      if (team.getMemberCount() == 1) continue;
      VoiceChannel teamChannel = eventCategory.createVoiceChannel(channelName).complete();
      team.setTeamChannel(teamChannel);
      teamChannel.putPermissionOverride(getMainGuild().getPublicRole()).setDeny(Permission.VIEW_CHANNEL).queue();

      for (UhcPlayer uhcPlayer : team.getMembers()) {
        Member member = uhcPlayer.getDiscordUser();
        teamChannel.putPermissionOverride(member).setAllow(Permission.VIEW_CHANNEL, Permission.VOICE_CONNECT).queue();
        if (member.getVoiceState() != null && member.getVoiceState().inVoiceChannel())
          getMainGuild().moveVoiceMember(member, teamChannel).queue();
        else {
          uhcPlayer.sendMessage(ChatColor.GREEN + "[UHC-Discord]" + ChatColor.RESET + " Please enter the voice channel for your team named: " + channelName + "\n" + teamChannel.createInvite().complete().getUrl());
        }
      }
    }

    UHCChat.sendMessage(embed.build()).queue();

  }

  @EventHandler
  public void onPlayerDeathEvent(PlayerDeathEvent event) {
    if (UHCChat == null) return;
    UhcPlayer uhcPlayer = getPlayerManager().getUhcPlayer(event.getEntity());
    Player killer = event.getEntity().getKiller();
    User user = uhcPlayer.getDiscordUser().getUser();
    MessageEmbed embed = new EmbedBuilder()
            .setAuthor(killer == null ? "You dead!" : getPlayerManager().getUhcPlayer(killer).getDiscordUser().getAsMention() + " Killed you!")
            .setThumbnail(user.getAvatarUrl())
            .setDescription(event.getDeathMessage())
            .setTimestamp(Instant.now())
            .build();

    UHCChat.sendMessage(user.getAsMention()).embed(embed).queue();
  }

  @EventHandler
  public void onPlayerRevive(UhcPlayerStateChangedEvent event) {
    if (UHCChat == null) return;
    if (event.getOldPlayerState() == null) return;
    if (event.getOldPlayerState().equals(PlayerState.DEAD) && event.getNewPlayerState().equals(PlayerState.PLAYING)) {
      UhcPlayer uhcPlayer = event.getPlayer();
      User user = uhcPlayer.getDiscordUser().getUser();
      MessageEmbed embed = new EmbedBuilder()
              .setTitle("You got revived!")
              .setThumbnail(user.getAvatarUrl())
              .setTimestamp(Instant.now())
              .build();

      UHCChat.sendMessage(user.getAsMention()).embed(embed).queue();
    }
  }

  @EventHandler
  public void onGameWin(UhcWinEvent event) {
    EmbedBuilder embed = new EmbedBuilder()
            .setAuthor("UHC Game has Ended!")
            .setTimestamp(Instant.now());
    if (getConfiguration().get(MainConfig.ENABLE_TEAMS_PLACEMENTS)) {
      embed.setTitle("Placements:");

      getTeamManager().getUhcTeams().sort(Comparator.comparingInt(UhcTeam::getPlacement).reversed());

      for (UhcTeam team : getTeamManager().getUhcTeams()) {
        if(team.getPlacement() == 0) continue;
        String teamName = "Team " + team.getTeamNumber();
        if (team.getTeamName() != null) teamName = team.getTeamName();
        embed.addField("#" + team.getPlacement() + " " + teamName, team.getMembers().stream().map(m -> m.getDiscordUser().getAsMention()).collect(Collectors.joining(" - ")), true);
      }
    } else {
      embed
              .setTitle("Winners:")
              .setDescription(event.getWinners().stream().map(m -> m.getDiscordUser().getAsMention()).collect(Collectors.joining(" - ")));
    }
    UHCChat.sendMessage(embed.build()).queue();

    for (VoiceChannel voiceChannel : eventCategory.getVoiceChannels()) {
      if (voiceChannel.equals(UHCVoice)) continue;
      for (Member member : voiceChannel.getMembers()) getMainGuild().moveVoiceMember(member, UHCVoice).queue();
      voiceChannel.delete().queueAfter(10, TimeUnit.SECONDS);
    }
  }

  public void updateEventCategory() {
    String CategoryID = getConfiguration().getString("discord.category");
    Category _category = null;
    if (CategoryID != null) _category = getMainGuild().getCategoryById(CategoryID);
    if (_category == null) {
      _category = getMainGuild().createCategory("UHC Event").complete();
      try{
        getConfiguration().set("discord.category", _category.getId());
        getConfiguration().save();
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else _category.getManager().setName("UHC Event").queue();
    if (_category.getChannels().size() > 0) {
      for (GuildChannel channel : _category.getChannels()) {
        if (channel.getName().equalsIgnoreCase("uhc lobby") || channel.getName().equalsIgnoreCase("uhc")) continue;
        if (getDiscordAPI().getChannels().containsValue(channel.getId())) continue;
        channel.delete().queue();
      }
    }
    eventCategory = _category;
    updateUHCChat();
    updateUHCVoice();
  }

  public void updateAllowedRoles() {
    allowedRoles.clear();
    List<String> allowedRolesIDs = getConfiguration().getStringList("discord.player-must-have-roles");
    if (allowedRolesIDs.size() == 0
            || allowedRolesIDs.get(0).equalsIgnoreCase("everyone")
            || allowedRolesIDs.contains(getMainGuild().getPublicRole().getId())) {
      allowedRoles.add(getMainGuild().getPublicRole());
    } else {
      for (String allowedRoleID : allowedRolesIDs) {
        Role role = getMainGuild().getRoleById(allowedRoleID);
        if (role != null) allowedRoles.add(role);
      }
    }
  }

  public void updateEventOrganizers() {
    eventOrganizers.clear();
    List<String> eventOrganizersIDs = getConfiguration().getStringList("discord.event-organizer-roles");
    for (String eventOrganizerRoleID : eventOrganizersIDs) {
      Role role = getMainGuild().getRoleById(eventOrganizerRoleID);
      if (role != null) eventOrganizers.add(role);
    }
  }

  public void updateUHCChat() {
    if (eventCategory == null) return;

    if (UHCChat == null) for (TextChannel textChannel : eventCategory.getTextChannels()) {
      if (textChannel.getName().equalsIgnoreCase("uhc")) {
        UHCChat = textChannel;
        break;
      }
    }
    if (UHCChat == null) {
      ChannelAction<TextChannel> channelAction = eventCategory.createTextChannel("uhc");
      for (Role eventOrganizer : eventOrganizers) {
        channelAction = channelAction.addPermissionOverride(eventOrganizer,
                Permission.getRaw(Permission.MESSAGE_WRITE, Permission.MESSAGE_MANAGE),
                Permission.getRaw(Permission.EMPTY_PERMISSIONS));
      }
      if (isPublicEvent()) {
        channelAction = channelAction.addPermissionOverride(getMainGuild().getPublicRole(),
                Permission.getRaw(Permission.MESSAGE_READ),
                Permission.getRaw(Permission.MESSAGE_MANAGE, Permission.MESSAGE_WRITE));
      } else {
        for (Role allowedRole : allowedRoles) {
          channelAction = channelAction.addPermissionOverride(allowedRole,
                  Permission.getRaw(Permission.MESSAGE_READ),
                  Permission.getRaw(Permission.MESSAGE_WRITE));
        }
        channelAction = channelAction.addPermissionOverride(getMainGuild().getPublicRole(),
                Permission.getRaw(Permission.EMPTY_PERMISSIONS),
                Permission.getRaw(Permission.MESSAGE_MANAGE, Permission.MESSAGE_WRITE, Permission.MESSAGE_READ));
      }
      UHCChat = channelAction.complete();
    } else updateUHCChatPermissions();
  }

  public void updateUHCChatPermissions() {
    if (UHCChat == null) {
      updateUHCChat();
      return;
    }

    for (Role eventOrganizer : eventOrganizers) {
      long allow = Permission.getRaw(Permission.MESSAGE_WRITE, Permission.MESSAGE_MANAGE, Permission.VIEW_CHANNEL);
      long deny = Permission.getRaw(Permission.EMPTY_PERMISSIONS);
      PermissionOverride permissionOverride = UHCChat.getPermissionOverride(eventOrganizer);
      if ((permissionOverride != null ? permissionOverride.getAllowedRaw() : 0) == allow
              && (permissionOverride != null ? permissionOverride.getDeniedRaw() : 0) == deny) continue;
      UHCChat.putPermissionOverride(eventOrganizer).setAllow(allow).setDeny(deny).queue();
    }

    if (isPublicEvent()) {
      long allow = Permission.getRaw(Permission.MESSAGE_READ);
      long deny = Permission.getRaw(Permission.MESSAGE_MANAGE, Permission.MESSAGE_WRITE);
      PermissionOverride permissionOverride = UHCChat.getPermissionOverride(getMainGuild().getPublicRole());
      if ((permissionOverride != null ? permissionOverride.getAllowedRaw() : 0) == allow
              && (permissionOverride != null ? permissionOverride.getDeniedRaw() : 0) == deny) return;
      UHCChat.putPermissionOverride(getMainGuild().getPublicRole()).setAllow(allow).setDeny(deny).queue();
    } else {
      for (Role allowedRole : allowedRoles) {
        long allow = Permission.getRaw(Permission.MESSAGE_READ);
        long deny = Permission.getRaw(Permission.MESSAGE_WRITE);
        PermissionOverride permissionOverride = UHCChat.getPermissionOverride(allowedRole);
        if ((permissionOverride != null ? permissionOverride.getAllowedRaw() : 0) == allow
                && (permissionOverride != null ? permissionOverride.getDeniedRaw() : 0) == deny) continue;
        UHCChat.putPermissionOverride(allowedRole).setAllow(allow).setDeny(deny).queue();
      }
      long allow = Permission.getRaw(Permission.EMPTY_PERMISSIONS);
      long deny = Permission.getRaw(Permission.MESSAGE_MANAGE, Permission.MESSAGE_WRITE, Permission.MESSAGE_READ);
      PermissionOverride permissionOverride = UHCChat.getPermissionOverride(getMainGuild().getPublicRole());
      if ((permissionOverride != null ? permissionOverride.getAllowedRaw() : 0) == allow
              && (permissionOverride != null ? permissionOverride.getDeniedRaw() : 0) == deny) return;
      UHCChat.putPermissionOverride(getMainGuild().getPublicRole()).setAllow(allow).setDeny(deny).queue();
    }
  }

  public void updateUHCVoice() {
    if (eventCategory == null) return;
    if (UHCVoice == null) for (VoiceChannel voiceChannel : eventCategory.getVoiceChannels()) {
      if (voiceChannel.getName().equalsIgnoreCase("uhc lobby")) {
        UHCVoice = voiceChannel;
        break;
      }
    }
    if (UHCVoice == null) {
      ChannelAction<VoiceChannel> channelAction = eventCategory.createVoiceChannel("UHC Lobby");
      for (Role eventOrganizer : eventOrganizers) {
        channelAction = channelAction.addPermissionOverride(eventOrganizer,
                Permission.ALL_VOICE_PERMISSIONS,
                Permission.getRaw(Permission.EMPTY_PERMISSIONS));
      }
      if (isPublicEvent()) {
        channelAction = channelAction.addPermissionOverride(getMainGuild().getPublicRole(),
                Permission.getRaw(Permission.VOICE_CONNECT),
                Permission.getRaw(getConfiguration().getBoolean("discord.can-players-speak-in-lobby", false) ? null : Permission.VOICE_SPEAK));
      } else {
        for (Role allowedRole : allowedRoles) {
          channelAction = channelAction.addPermissionOverride(allowedRole,
                  Permission.getRaw(Permission.VOICE_CONNECT, Permission.VIEW_CHANNEL),
                  Permission.getRaw(Permission.EMPTY_PERMISSIONS));
        }
        channelAction = channelAction.addPermissionOverride(getMainGuild().getPublicRole(),
                Permission.getRaw(getConfiguration().getBoolean("discord.can-players-speak-in-lobby", false) ? Permission.VOICE_SPEAK : null),
                Permission.getRaw(Permission.VOICE_MUTE_OTHERS, getConfiguration().getBoolean("discord.can-players-speak-in-lobby", false) ? null : Permission.VOICE_SPEAK, Permission.VIEW_CHANNEL));
      }
      UHCVoice = channelAction.complete();
    } else updateUHCVoicePermissions();
  }

  public void updateUHCVoicePermissions() {
    if (UHCVoice == null) {
      updateUHCVoice();
      return;
    }

    for (Role eventOrganizer : eventOrganizers) {
      long allow = Permission.ALL_VOICE_PERMISSIONS;
      long deny = Permission.getRaw(Permission.EMPTY_PERMISSIONS);
      PermissionOverride permissionOverride = UHCVoice.getPermissionOverride(eventOrganizer);
      if ((permissionOverride != null ? permissionOverride.getAllowedRaw() : 0) == allow
              && (permissionOverride != null ? permissionOverride.getDeniedRaw() : 0) == deny) continue;
      UHCVoice.putPermissionOverride(eventOrganizer).setAllow(allow).setDeny(deny).queue();
    }

    if (isPublicEvent()) {
      long allow = Permission.getRaw(Permission.VOICE_CONNECT);
      long deny = Permission.getRaw(getConfiguration().getBoolean("discord.can-players-speak-in-lobby", false) ? null : Permission.VOICE_SPEAK);
      PermissionOverride permissionOverride = UHCVoice.getPermissionOverride(getMainGuild().getPublicRole());
      if ((permissionOverride != null ? permissionOverride.getAllowedRaw() : 0) == allow
              && (permissionOverride != null ? permissionOverride.getDeniedRaw() : 0) == deny) return;
      UHCVoice.putPermissionOverride(getMainGuild().getPublicRole()).setAllow(allow).setDeny(deny).queue();
    } else {
      for (Role allowedRole : allowedRoles) {
        long allow = Permission.getRaw(Permission.VOICE_CONNECT, Permission.VIEW_CHANNEL);
        long deny = Permission.getRaw(Permission.EMPTY_PERMISSIONS);
        PermissionOverride permissionOverride = UHCVoice.getPermissionOverride(allowedRole);
        if ((permissionOverride != null ? permissionOverride.getAllowedRaw() : 0) == allow
                && (permissionOverride != null ? permissionOverride.getDeniedRaw() : 0) == deny) continue;
        UHCVoice.putPermissionOverride(allowedRole).setAllow(allow).setDeny(deny).queue();
      }
      long allow = Permission.getRaw(getConfiguration().getBoolean("discord.can-players-speak-in-lobby", false) ? Permission.VOICE_SPEAK : null);
      long deny = Permission.getRaw(Permission.VOICE_MUTE_OTHERS, getConfiguration().getBoolean("discord.can-players-speak-in-lobby", false) ? null : Permission.VOICE_SPEAK, Permission.VIEW_CHANNEL);
      PermissionOverride permissionOverride = UHCVoice.getPermissionOverride(getMainGuild().getPublicRole());
      if ((permissionOverride != null ? permissionOverride.getAllowedRaw() : 0) == allow
              && (permissionOverride != null ? permissionOverride.getDeniedRaw() : 0) == deny) return;
      UHCVoice.putPermissionOverride(getMainGuild().getPublicRole()).setAllow(allow).setDeny(deny).queue();
    }
  }
}
