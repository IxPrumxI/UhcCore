# UhcCore

UHC, or Ultra Hardcore, is a classic PvP gamemode in which players or teams
fight to be the last one standing in survival mode, with natural health
regeneration disabled. The only sources of healing are golden apples, potions
and other such items which grant status effects, so players must be careful
not to take unnecessary damage!

The original gamemode was [invented by the Mindcrack community][uhc-origins]
over 10 years ago, but this plugin adds many new features:

- 40+ scenarios that alter the game in fun ways, with a voting system
  - Nether start
  - Randomized block drops
  - Upside down crafting
  - Dragon rush
  - Superheroes
  - And many more...
- Easy GUI menu for team creation (no commands required)
- Configurable start kits
- Configurable crafting recipes
- Shrinking world border and deathmatch options
- Option to pre-generate worlds to avoid lag during the game
- Option to remove oceans from the terrain generation
- 2:1 nether scale instead of the default 8:1
- And many more can be found in the configuration files...

[uhc-origins]: https://www.reddit.com/r/mindcrack/comments/syqitq/the_origins_of_the_uhc_mod_10_years_ago/

## Setup guide

UhcCore can be run on a Spigot-compatible Minecraft server such as
[Spigot](https://www.spigotmc.org) or [Paper](https://papermc.io) for
Minecraft versions 1.8 to 1.18. Note that the plugin is incompatible with
world management plugins such as MultiWorld or Multiverse, so you will
need to run it on a standalone server.

**Setup steps**

1. Download the plugin to the `plugins/` directory and start the server.
2. Wait for the world to pre-generate (see console output for progress).
3. Your players can now join! The game starts when enough players have joined
(according to the configuration file), or when the `/start` command is issued.

After running the plugin for the first time, configuration files will be
generated in the `plugins/UhcCore/` directory. Changes can be taken into
effect using the `/uhccore reload` command, but do note that some settings
require a server restart in order to take effect.

## Community

If you have any questions about the plugin, want to discuss features
or bugs, or just chat with the community, feel free to join
[the UhcCore Discord server][discord-invite]!

[discord-invite]: https://discord.gg/fDCaKMX

## Submitting feature requests and bug reports

If you have an idea for a new feature, or found a bug that needs to be
fixed, you can [create an issue][issue-tracker] at the GitLab repository.
Please be as descriptive as possible and include all relevant information,
such as error messages and/or server logs when submitting a bug report.
You are also welcome to discuss the matter on
[the Discord server][discord-invite], in the `#bugs` or `#suggestions` channel.

[issue-tracker]: https://gitlab.com/uhccore/uhccore/-/issues

## Documentation

The documentation is currently a bit lacking, but an update is in the works.
Until then, the wiki pages from the previous plugin maintainer can be found at
<https://github.com/Mezy/UhcCore/wiki>, and you are welcome with your
questions on [the Discord server][discord-invite].

## UhcCore API

There is currently no formal API for UhcCore, but several UhcCore plugins have
been made in the past by using the main plugin JAR as a dependency and
listening for game events. Should you wish to do this, the plugin can be
added as a dependency in Maven or Gradle from the following repository:

```
https://gitlab.com/api/v4/groups/uhccore/-/packages/maven
```

The Maven coordinates of the plugin JAR are:

```
net.zerodind:uhccore:1.20.0
```

Keep in mind that there is neither any complete documentation nor any
guarantee that the plugin classes won't change in future releases.
Stay tuned for a stable API (but there is no ETA yet)!

## Contributing

This plugin is fully free and open source software, and you may fork it as
long as you comply with the license terms. Contributions are highly appreciated
and can be sent as merge requests to [the GitLab repository][gitlab-repo]!
See [CONTRIBUTING.md][contributing-md] for more details.

[gitlab-repo]: https://gitlab.com/uhccore/uhccore
[contributing-md]: https://gitlab.com/uhccore/uhccore/-/blob/main/CONTRIBUTING.md

## Building the plugin from source code

*Note: You will need to install a Java Development Kit (JDK)
in order to build the source code.*

This plugin uses the [Gradle](https://gradle.org) build tool.
To build the plugin from source code, first open a terminal and navigate
to the root directory of the repository. Next, run one of the following
commands to build the project depending on your operating system:

**Build command on Windows**

```
gradlew build
```

**Build command on Linux and macOS**

```
./gradlew build
```

After running the build command, you should hopefully see a
`BUILD SUCCESSFUL` message. The resulting plugin JAR file
is located in the `build/libs/` directory.

## Acknowledgments

Special thanks to Mezy and val59000mc, the original authors, who maintained
the plugin prior to version `1.20.0`. See below for historical reference:

- [Mezy's plugin page](https://www.spigotmc.org/resources/uhccore-automated-uhc-for-minecraft-1-8-8-1-16.47572/)
  - [GitHub repository](https://github.com/Mezy/UhcCore/)
- [val59000mc's plugin page](https://www.spigotmc.org/resources/playuhc.3956/)
  - [Archived Bitbucket repository](https://archive.softwareheritage.org/browse/origin/directory/?origin_url=https://bitbucket.org/val59000/playuhc.git)

## License

```
Copyright (C) 2015 Valentin Baert
Copyright (C) 2017-2021 Pieter de Bot and others
Copyright (C) 2022 Odin Dahlström and others

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
```
