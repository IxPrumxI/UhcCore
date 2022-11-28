# Changelog

## [1.20.2] - 2022-09-11

### New features

- Updated the plugin to support Minecraft 1.19.2
- Added the `eye-attempts` and `eye-probability` options to `dragon_rush` in `scenarios.yml`
- Added the `enable-victory` option to `config.yml`
- Added the `logging-level` option to `config.yml`
- Added some more translation strings to `lang.yml` under `team.inventory` and `team.colors`
- Added `display.team-chat` to `lang.yml`
- Added `scenarios.team_inventory.title` to `lang.yml`
- Added `players.death-message` to `lang.yml`
- UhcCore announcement messages can now be disabled by setting them to an empty string in `lang.yml`
- Worlds generated by UhcCore are now saved to the same location as other
worlds, if a custom `world-container` is configured in Spigot/Paper
- Added the `use-default-world-spawn-for-lobby` option to `config.yml`
- Added an announcement message for the deathmatch countdown
    - Can be changed using `game.starting-deathmatch-in` in `lang.yml`

### Bug fixes

- Fixed a bug in the Love at First Sight scenario where teams created in the lobby would carry through
- Fixed a bug in the Achievement Hunter scenario where not all advancements would be counted
- Fixed a bug in the Randomized Drops scenario where dropped items would sometimes glitch up through blocks
- Fixed a bug where players would be removed from the Best PvE list even if the damage was blocked
- Fixed a bug in the Horseless scenario where players could ride zombie/skeleton horses, donkeys and mules
- Fixed a bug in the Weakest Link scenario where players with the resistance effect could survive
- Fixed a bug in the Blood Diamonds and Sky High scenarios where health would "vanish" without playing the damage effect
- Fixed a bug where absorption and totems of undying would not protect against Blood Diamonds and Sky High damage
- Fixed a bug in the Randomized Drops scenario where some blocks would not drop anything at all
- Fixed a bug with scenario voting where players could lose a vote by clicking an item in their hotbar
- Fixed a crash that occurred when starting the game if voting was enabled but there were no scenarios to vote for
- Fixed a scenario error message where "Silent Night" was incorrectly named "Anonymous"
- Fixed a `NullPointerException` that would happen when trying to revive a nonexistent player
- Fixed an `IllegalPluginAccessException` which could sometimes happen when shutting down the server
- Fixed a bug with the JSON item deserializer where color codes in display names were not working
- Fixed incorrect scenario IDs in the default `scenarios.yml` template
- Fixed a bug where "Scoreboard line is too long" would be spammed while starting the game
- Removed some more log spam
- Clarified a few confusing log messages

## [1.20.1] - 2022-06-14

### New features

- Updated the plugin to support Minecraft 1.19
- Added the `player-death-sound` option (see `config.yml` for more details)
- Added the `/heal` command (see `/help heal` for more details)
    - Permission name: `uhc-core.commands.heal`

### Bug fixes

- Fixed a missing error message for the `/teaminventory` command

## [1.20.0] - 2022-06-08

For older releases and changelogs, see
<https://github.com/Mezy/UhcCore/releases>.

### New features

- Updated the plugin to support Minecraft 1.18

### Bug fixes

- Fixed a bug with the `spawn-offline-players` setting where killing
the zombie of an offline player would not kill them or drop their loot
- Fixed compatibility with the nether and deepslate ore types

### Other

- Optimized and updated the build system to support Java 17
- Removed the automatic plugin update functionality
- Removed the bStats metrics (for now)