# Changelog

## [Unreleased]

### New features

- Added the `logging-level` option (see `config.yml` for more details)

### Bug fixes

- Fixed a bug with scenario voting where players could lose a vote by clicking an item in their hotbar
- Fixed a scenario error message where "Silent Night" was incorrectly named "Anonymous"
- Removed some log spam

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
