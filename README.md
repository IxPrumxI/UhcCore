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

## Downloads

You can download the plugin from either of these sources:
- [Spigot plugin page](https://www.spigotmc.org/resources/uhccore.102507/history)
- [Releases on GitLab](https://gitlab.com/uhccore/uhccore/-/releases)
- [Permalink to latest release](https://gitlab.com/uhccore/uhccore/-/releases/permalink/latest/downloads/plugin-jar)

## Setup guide

UhcCore can be run on a Spigot-compatible Minecraft server such as
[Spigot](https://www.spigotmc.org) or [Paper](https://papermc.io) for
Minecraft versions 1.8.8 to 1.19.3. Note that the plugin is incompatible with
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

Make sure to read [the FAQ][faq] if you're having trouble, or you can also
ask for help on [the Discord server][discord].

[faq]: https://uhccore.zerodind.net/faq/
[discord]: https://discord.gg/fDCaKMX

## Community

If you have any questions about the plugin, want to discuss features
or bugs, or just chat with the community, feel free to join
[the UhcCore Discord server][discord]!

## Submitting feature requests and bug reports

If you have an idea for a new feature, or found a bug that needs to be
fixed, you can [create an issue][issue-tracker] at the GitLab repository.
Please be as descriptive as possible and include all relevant information,
such as error messages and/or server logs when submitting a bug report.
You are also welcome to discuss the matter on
[the Discord server][discord], in the `#bugs` or `#suggestions` channel.

[issue-tracker]: https://gitlab.com/uhccore/uhccore/-/issues

## Documentation and API

Please see the FAQ for more details:

- ["Where can I read more about configuration, commands etc? Is there a wiki?"][faq-wiki]
- ["Does UhcCore have an addon API?"][faq-api]
- ["Can I add my own custom scenarios?"][faq-scenarios]

[faq-wiki]: https://uhccore.zerodind.net/faq/#where-can-i-read-more-about-configuration-commands-etc-is-there-a-wiki
[faq-api]: https://uhccore.zerodind.net/faq/#does-uhccore-have-an-addon-api
[faq-scenarios]: https://uhccore.zerodind.net/faq/#can-i-add-my-own-custom-scenarios

## Contributing

Contributions are highly appreciated and can be sent as merge requests to
[the GitLab repository][gitlab-repo]! See [CONTRIBUTING.md][contributing-md]
and [the FAQ][faq-contrib] more for more details.

[gitlab-repo]: https://gitlab.com/uhccore/uhccore
[contributing-md]: https://gitlab.com/uhccore/uhccore/-/blob/main/CONTRIBUTING.md
[faq-contrib]: https://uhccore.zerodind.net/faq/#i-want-to-help-contribute-code-to-this-project-where-do-i-start

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
