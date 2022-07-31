---
hide:
  - navigation
---

# Frequently Asked Questions (FAQ)

[q1]: #why-cant-i-join-my-server
[q2]: #why-does-my-server-crashfreeze-while-generating-the-world
[q3]: #how-and-where-does-uhccore-loadstore-worlds
[q4]: #where-can-i-find-the-default-configuration-values-how-do-i-reset-to-default
[q5]: #is-this-plugin-free-will-it-stay-that-way

[discord]: https://discord.gg/fDCaKMX

## Why can't I join my server?

Probably because the world is still being generated. By default, UhcCore
will pre-generate all chunks within the world border, which can take a while
depending on the border size and your server performance. Pre-generating
the world will greatly reduce the amount of lag while playing the game.

While the world is being generated, you can see the progress in your server
console, to get an idea of when it will be done.

??? example "Example: progress output in the server console"

    ```
    [16:28:48] [Server thread/INFO]: [UhcCore] Creating new world : 0e89517f-0988-47bf-82d6-4b3bb0ca36fd
    [16:28:55] [Server thread/INFO]: Preparing start region for dimension minecraft:0e89517f-0988-47bf-82d6-4b3bb0ca36fd
    [16:28:55] [Worker-Main-2/INFO]: Preparing spawn area: 0%
    [16:28:55] [Worker-Main-1/INFO]: Preparing spawn area: 0%
    [16:28:56] [Server thread/INFO]: Time elapsed: 722 ms
    [16:28:56] [Server thread/INFO]: [UhcCore] Creating new world : aa71a464-7d4b-4a86-8779-f1c6fce88289
    [16:28:59] [Server thread/INFO]: Preparing start region for dimension minecraft:aa71a464-7d4b-4a86-8779-f1c6fce88289
    [16:28:59] [Worker-Main-1/INFO]: Preparing spawn area: 0%
    [16:29:00] [Server thread/INFO]: Time elapsed: 1086 ms
    [16:29:00] [Server thread/INFO]: [UhcCore] Generating environment NORMAL
    [16:29:00] [Server thread/INFO]: [UhcCore] Loading a total 4225.0 chunks, up to chunk ( 32 , 32 )
    [16:29:00] [Server thread/INFO]: [UhcCore] Resting 20 ticks every 200 chunks
    [16:29:00] [Server thread/INFO]: [UhcCore] Loading map NORMAL 0.0%
    [16:29:00] [Server thread/INFO]: Done (38.684s)! For help, type "help"
    [16:29:00] [Server thread/INFO]: Timings Reset
    [16:31:37] [Craft Scheduler Thread - 4 - UhcCore/INFO]: [UhcCore] Loading map NORMAL 4.7% - 200/4225 chunks loaded
    [16:32:00] [Craft Scheduler Thread - 5 - UhcCore/INFO]: [UhcCore] Loading map NORMAL 9.4% - 400/4225 chunks loaded
    [16:32:19] [Craft Scheduler Thread - 6 - UhcCore/INFO]: [UhcCore] Loading map NORMAL 14.2% - 600/4225 chunks loaded
    [16:32:36] [Craft Scheduler Thread - 7 - UhcCore/INFO]: [UhcCore] Loading map NORMAL 18.9% - 800/4225 chunks loaded
    [16:32:51] [Craft Scheduler Thread - 8 - UhcCore/INFO]: [UhcCore] Loading map NORMAL 23.6% - 1000/4225 chunks loaded
    [16:33:04] [Craft Scheduler Thread - 9 - UhcCore/INFO]: [UhcCore] Loading map NORMAL 28.4% - 1200/4225 chunks loaded
    [16:33:16] [Craft Scheduler Thread - 10 - UhcCore/INFO]: [UhcCore] Loading map NORMAL 33.1% - 1400/4225 chunks loaded
    [16:33:30] [Craft Scheduler Thread - 11 - UhcCore/INFO]: [UhcCore] Loading map NORMAL 37.8% - 1600/4225 chunks loaded
    [16:33:41] [Craft Scheduler Thread - 12 - UhcCore/INFO]: [UhcCore] Loading map NORMAL 42.6% - 1800/4225 chunks loaded
    [16:33:55] [Craft Scheduler Thread - 13 - UhcCore/INFO]: [UhcCore] Loading map NORMAL 47.3% - 2000/4225 chunks loaded
    [16:34:09] [Craft Scheduler Thread - 14 - UhcCore/INFO]: [UhcCore] Loading map NORMAL 52.0% - 2200/4225 chunks loaded
    [16:34:20] [Craft Scheduler Thread - 15 - UhcCore/INFO]: [UhcCore] Loading map NORMAL 56.8% - 2400/4225 chunks loaded
    [16:34:31] [Craft Scheduler Thread - 16 - UhcCore/INFO]: [UhcCore] Loading map NORMAL 61.5% - 2600/4225 chunks loaded
    [16:34:45] [Craft Scheduler Thread - 17 - UhcCore/INFO]: [UhcCore] Loading map NORMAL 66.2% - 2800/4225 chunks loaded
    [16:35:00] [Craft Scheduler Thread - 18 - UhcCore/INFO]: [UhcCore] Loading map NORMAL 71.0% - 3000/4225 chunks loaded
    [16:35:18] [Craft Scheduler Thread - 19 - UhcCore/INFO]: [UhcCore] Loading map NORMAL 75.7% - 3200/4225 chunks loaded
    [16:35:35] [Craft Scheduler Thread - 20 - UhcCore/INFO]: [UhcCore] Loading map NORMAL 80.4% - 3400/4225 chunks loaded
    [16:35:51] [Craft Scheduler Thread - 21 - UhcCore/INFO]: [UhcCore] Loading map NORMAL 85.2% - 3600/4225 chunks loaded
    [16:36:08] [Craft Scheduler Thread - 22 - UhcCore/INFO]: [UhcCore] Loading map NORMAL 89.9% - 3800/4225 chunks loaded
    [16:36:29] [Craft Scheduler Thread - 23 - UhcCore/INFO]: [UhcCore] Loading map NORMAL 94.6% - 4000/4225 chunks loaded
    [16:36:48] [Craft Scheduler Thread - 24 - UhcCore/INFO]: [UhcCore] Loading map NORMAL 99.4% - 4200/4225 chunks loaded
    [16:36:50] [Server thread/INFO]: [UhcCore] Environment NORMAL 100% loaded
    [16:36:50] [Server thread/INFO]: [UhcCore] Generating environment NETHER
    [16:36:50] [Server thread/INFO]: [UhcCore] Loading a total 1225.0 chunks, up to chunk ( 17 , 17 )
    [16:36:50] [Server thread/INFO]: [UhcCore] Resting 20 ticks every 200 chunks
    [16:36:50] [Server thread/INFO]: [UhcCore] Loading map NETHER 0.0%
    [16:36:58] [Craft Scheduler Thread - 26 - UhcCore/INFO]: [UhcCore] Loading map NETHER 16.3% - 200/1225 chunks loaded
    [16:37:01] [Craft Scheduler Thread - 27 - UhcCore/INFO]: [UhcCore] Loading map NETHER 32.6% - 400/1225 chunks loaded
    [16:37:04] [Craft Scheduler Thread - 27 - UhcCore/INFO]: [UhcCore] Loading map NETHER 48.9% - 600/1225 chunks loaded
    [16:37:06] [Craft Scheduler Thread - 27 - UhcCore/INFO]: [UhcCore] Loading map NETHER 65.3% - 800/1225 chunks loaded
    [16:37:09] [Craft Scheduler Thread - 27 - UhcCore/INFO]: [UhcCore] Loading map NETHER 81.6% - 1000/1225 chunks loaded
    [16:37:15] [Craft Scheduler Thread - 27 - UhcCore/INFO]: [UhcCore] Loading map NETHER 97.9% - 1200/1225 chunks loaded
    [16:37:16] [Server thread/INFO]: [UhcCore] Environment NETHER 100% loaded
    [16:37:16] [Server thread/WARN]: A manual (plugin-induced) save has been detected while server is configured to auto-save. This may affect performance.
    [16:37:18] [Server thread/INFO]: [UhcCore] No WorldEdit/schematic installed so ending with deathmatch at 0 0
    [16:37:18] [Server thread/INFO]: [UhcCore] Players are now allowed to join
    ```

## Why does my server crash/freeze while generating the world?

The chunk pre-generator used by UhcCore does not play well with some
older versions of Minecraft (in particular, `1.12.2`). If you are running
UhcCore on such a version, or if your server doesn't have enough performance,
the server might crash or freeze during pre-generation. Here are a few tips
on how you can solve this problem:

1. If the server just appears frozen, wait a few more minutes.

2. If you're using Spigot, try using [Paper](https://papermc.io) instead.

3. Play around with the `pre-generate-world` settings in config.yml.

4. Try increasing `timeout-time` in [spigot.yml](https://www.spigotmc.org/wiki/spigot-configuration/).

One way to completely avoid this problem is to set `enable: false` under
`pre-generate-world` in config.yml. This will disable pre-generation completely,
which is NOT recommended. Doing so will likely increase the amount of lag while
playing, but it can also cause players to spawn below ground and suffocate to
death. As a workaround, you could use another plugin to pre-generate the world,
such as [Chunky](https://www.spigotmc.org/resources/chunky.81534/).

## Can I use this plugin on a free hosting service, such as Aternos?

While the plugin should "work", it might not work *well* on a free hosting
service, because you probably won't get a server with good performance for free.
What this means is that you should expect it to take a bit longer to generate
the world (see ["Why can't I join my server?"][q1]), among other things.

If your server crashes while generating the world, there's not much you
can do except upgrade to a server with better performance, or try
the tips mentioned in
["Why does my server crash/freeze while generating the world?"][q2].

## Why is my config/kit/craft not working or missing?

The most likely reason is that you have a syntax error in your YAML file,
which means that UhcCore can't read the configuration properly. To resolve
the issue, try the following steps:

1. Make sure you have saved the config file and restarted your server.

2. Check the YAML code for syntax errors. You can use an editor with
YAML support, or an online tool like [YAML Checker](https://yamlchecker.com).

3. Check the server console output for any errors or warnings.
Even if your config is valid YAML, it might contain an invalid setting value.

4. If the steps above didn't help, or you've run into an error message that you
don't understand, feel free to ask for help on [the Discord server][discord]!
Please tell us what you were trying to do, what (if any) error message you got,
and any other relevant details about what went wrong.

## Can I add this plugin to my existing server? Does it have multi-world support?

While there's nothing stopping you from adding the UhcCore plugin to an existing
server, keep in mind that the plugin has its own world management system,
and may delete player data from your default world. See
["How and where does UhcCore load/store worlds?"][q3] for more details.

UhcCore does not support multi-world plugins such as MultiWorld or Multiverse,
because it uses its own world management system, and may need to modify global
settings that would affect all worlds on the server.

!!! tip

    As an alternative, you can use a proxy server, which allows your players
    to switch between your UHC server and other servers with other worlds.
    Here are some alternatives to consider:

    - [Velocity][velocity]: A modern and fast alternative.

    - [Waterfall][waterfall]: An improved fork of BungeeCord.

    - [BungeeCord][bungeecord]: An old project with a large plugin ecosystem.

    You may also be interested in the `bungee-support` section in the
    config.yml file. As the name implies, those settings only work when using
    a BungeeCord (or Waterfall) proxy, but Velocity support could be added in
    a future UhcCore update.

[velocity]: https://velocitypowered.com
[waterfall]: https://github.com/PaperMC/Waterfall
[bungeecord]: https://github.com/SpigotMC/BungeeCord

## How do I add a custom lobby, deathmatch arena or underground nether?

UhcCore has support for loading custom [WorldEdit][worldedit] schematics
for these purposes. The schematics should be located in the `plugins/UhcCore/`
directory, and the filename should be `X.schem` or `X.schematic`, where `X`
is the name of the schematic (see below).

| Schematic name | Description | Related settings in config.yml |
| --- | --- | --- |
| `lobby` | Lobby used before the game starts. | `lobby-in-default-world` |
| `arena` | Deathmatch arena for the end of the game. | `arena-deathmatch` |
| `nether` | Underground nether template. | `underground-nether` |

!!! note

    Note that in order to use this feature, you must have WorldEdit installed
    on your UhcCore server. You can use WorldEdit on any server to create the
    schematic file, and then rename and copy the file to the right location.
    A usage guide for schematics [can be found here][schematic-docs], but you
    can also search the internet for more in-depth tutorials and video guides.

[worldedit]: https://enginehub.org/worldedit/
[schematic-docs]: https://worldedit.enginehub.org/en/latest/usage/clipboard/#loading-and-saving

## How and where does UhcCore load/store worlds?

Each time the server is started, UhcCore will create a world to play the
game in. By default, a random world is generated, but you can also use the
`pick-random-seed-from-list` or `pick-random-world-from-list` options in
config.yml to use a specific seed or custom map instead.

Regardless, UhcCore will create a new world with a random name for the
overworld, nether and end dimensions (if `enable-nether` and `enable-the-end`
respectively are set to `true` in config.yml). These random world names can be
found in the storage.yml file after the worlds have been created
(note that the overworld is called `normal`).

??? example "Example: storage.yml file"

    ```yaml
    worlds:
      normal: 61337d18-59ba-4a0e-a054-62a059da282a
      nether: 9898f9c1-1bd5-4181-a4f1-dec8587d0858
      the_end: 0e1361ca-9776-4730-a111-21e87302429d
    ```

Before creating the new world, UhcCore will also look in the storage.yml
file to see what the previous worlds were called, and then try to delete
those worlds automatically. The UhcCore worlds are currently always stored
in the main server directory, but you will be able to use a custom location
in the next UhcCore update.

!!! note

    One of the main takeaways from the above is that UhcCore ***does not use
    the default worlds***, specified using `level-name` in server.properties,
    excepting a few circumstances:

    - The `lobby-in-default-world` setting in config.yml.

    - Player data and other global information which is always stored in the
    default overworld by Spigot/Paper.

## How can I play on a custom map or use a custom world seed?

Please have a look at the `world-seeds` and `world-list` sections in your
config.yml file. By default, both of these features are disabled in the config,
but if you do enable them, make sure you *don't* enable both at the same time.

UhcCore can pick a random seed or map from the configured list, but you can
also choose to create a list with a single option, to always use the same one.

!!! note

    Note that if you use the `world-list` settings, UhcCore ***does not***
    actually load the world you list in the configuration. When the server
    starts, UhcCore will create ***a copy*** of the listed world (think of it
    as a template), and then load ***the copy*** as described in
    ["How and where does UhcCore load/store worlds?"][q3]. This means that
    you will need to load the world in singleplayer or on a different server
    if you want to make changes to the map.

    If the overworld, nether or end world is not present for the listed world,
    a new one will be generated instead of creating a copy.

## How can I configure the server to restart after each game?

When the game ends, UhcCore will issue the `/restart` command, or if that
fails, the `/stop` command. The restart command is [documented here][restart],
but you can also search the internet for more in-depth guides.

As an alternative, if you are using a server control panel (for example, one
provided by a hosting service), there may be an option for automatically
restarting stopped/crashed servers. But there is no standardized way of doing
this, so you will need to find out how it's done and whether or not it's
supported on your control panel yourself, by searching the internet.

[restart]: https://www.spigotmc.org/wiki/spigot-commands/

## How can I send you my log and/or config files?

If you are experiencing an issue, such as a bug or a crash, you will usually
need to send us your server log and/or config files. Without them, it's
really hard for us to tell what went wrong. Because these files are
usually quite large and contain a lot of text, you usually can't send them
to us directly in [the Discord server][discord]. There are a few alternatives:

- Upload the file automatically using the `/upload log` or `/upload config`
commands provided by UhcCore. The files are then uploaded to
<https://paste.md-5.net> and a message is sent to you in the chat with a link
to the file. This link can then be shared with a helper on Discord,
so they can see the file.

- You can also upload the files manually yourself, to <https://paste.md-5.net>
or an alternative like <https://paste.gg>, <https://pastebin.com> or
<https://gist.github.com>. The server log file is located at `logs/latest.log`,
and the main config file is located at `plugins/UhcCore/config.yml`.

- You can also send the log files as a private message to an `@Helper` on
the Discord server, but we highly recommend one of the alternatives described
above. If you only send the log/config as a private message, you can only get
help from one person, whereas anyone can help you if you send a paste link.

## Where can I find the default configuration values? How do I reset to default?

The default configuration values for most files in the `plugins/UhcCore`
directory [can be found here][configs]. Just make sure to change the version
string in the URL so that it matches the version of UhcCore you have installed.

In order to reset all of the settings in a configuration file to their default
values, just delete the file (or move/rename it, to keep a backup). When the
server is started, UhcCore will generate a new config file with the default
values.

[configs]: https://gitlab.com/uhccore/uhccore/-/tree/1.20.1/src/main/resources

## Where can I read more about configuration, commands etc? Is there a wiki?

There is currently not a complete wiki, but it will be added to this website
at some point in the (hopefully not-so-distant) future. The previous plugin
maintainer [did create a wiki][old-wiki] which you are free to use if it
helps you, but keep in mind that it is slightly outdated and far from complete.

As an alternative while the new wiki is being created, you can find information
about all configuration options as comments in the respective files (see
["Where can I find the default configuration values? How do I reset to default?"][q4]).
You can also find information about the UhcCore commands by running
`/help uhccore`, `/help uhccore 2` for the next help page and so on.
In order to view more details about a specific command, use `/help <command>`.

[old-wiki]: https://github.com/Mezy/UhcCore/wiki/

## Who is the main developer/maintainer/owner of this plugin?

That would be me! I am a Computer Science student who loves programming and
Minecraft, and have been using UhcCore on my private server for a while.
I took over the plugin ownership in June of 2022, because Mezy (the previous
owner) stopped maintaining the plugin due to a lack of interest in the project.

You can find me online on these places:

- Discord: `zerodind#5905`
- GitHub: <https://github.com/0dinD>
- GitLab: <https://gitlab.com/0dinD>
- SpigotMC: <https://www.spigotmc.org/members/zerodind.1572489/>

## How can I increase the chances of a feature being added?

I am not currently accepting donations or any other kinds of payment for
implementing a specific feature. That's because this is a hobby project of
mine which I do besides my studies and work, which means that my time is
limited.

With that said, there are a number of ways in which you can increase the
chances of a feature being added, or speed up the development rate:

- When requesting a feature, help me out by clearly describing how you think
it should work (for example, game mechanics, edge-cases to think about or
implementation alternatives). The more details you go into, the less work it is
for me when implementing the feature.

- While you *can* discuss feature requests and bug fixes on the Discord server,
consider [opening an issue][issues] on GitLab. This helps me keep track of
the feature request or bug, and makes sure it doesn't get lost in an old
Discord conversation if it takes a while before I have time to fix it.

-

[issues]: https://gitlab.com/uhccore/uhccore/-/issues

## When will the next update be released? What features will it have?

There is currently no live updated roadmap, and I generally won't be able
to give out an ETA unless the release is almost ready. That's simply because
I don't have that much time (and can't reliably predict when I have it),
seeing as this is a hobby project that I do besides my studies and work.

Having said that, you can always search through the message history on the
Discord server in case I've hinted at a thing or two, and I may answer some
questions about updates if you ask nicely (and accept "I don't know" for
an answer).

You can also read the `[Unreleased]` section at the top of
[the changelog][changelog] if it exists to see what features and bug fixes have
already been implemented and will be released in the next update. If you are
familiar with Git, you can also [view the list of commits][compare] since the
previous version (using its tag) and the head of the `main` branch. Just make
sure to select the right tag matching the version you want to compare from.

[changelog]: https://gitlab.com/uhccore/uhccore/-/blob/main/CHANGELOG.md
[compare]: https://gitlab.com/uhccore/uhccore/-/compare?from=1.20.1&to=main

## I want to help contribute code to this project, where do I start?

A good starting point is to read the [README][readme] and
[CONTRIBUTING][contributing] files. That information may help you get started,
but it doesn't go into all the details and is probably not as beginner-friendly
as it should be. Until I've had time to improve that piece of documentation,
I would highly encourage you to join [the Discord server][discord] and ask in
the `#dev` channel if you have any questions. You can also use this channel
to ask questions about and discuss the UhcCore source code.

[readme]: https://gitlab.com/uhccore/uhccore/-/blob/main/README.md
[contributing]: https://gitlab.com/uhccore/uhccore/-/blob/main/CONTRIBUTING.md

## Is this plugin free? Will it stay that way?

Yes! This plugin is licensed under the terms of the [GPLv3][gplv3], which
means that it is [free software][free-sw] (in addition to being free of
charge). It will most likely stay that way forever, because
[changing the license][converting] would require permission from *all*
previous code contributors.

[gplv3]: https://www.gnu.org/licenses/quick-guide-gplv3.html
[free-sw]: https://www.gnu.org/philosophy/free-sw.html
[converting]: https://www.gnu.org/licenses/gpl-faq.html#Consider

## Am I allowed to make my own version of (fork) the plugin?

Yes, this is one of the basic rights granted by the GPLv3 license (see
["Is this plugin free? Will it stay that way?"][q5]). However, do note
that if you decide to release your fork to the public, it must also be
licensed under the GPLv3 or a compatible license, and
[you must release the source code][release-source] to the public.

!!! note

    Using a privately made fork of the plugin on your public server
    without releasing the source code *is* allowed, as long as you
    [don't release the fork][release-source] to the public.

[release-source]: https://www.gnu.org/licenses/gpl-faq.html#GPLRequireSourcePostedPublic
