# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [1.14.4-1.1.1] - 2020-03-14

### Added
- Libraries as dependencies added.
- Compatibility with safe-localization messages added.

### Changed
- Kotlin dependency updated to `1.3.70`.
- KotlinX Serialization version updated to `0.20.0`.
- Forge API version updated to `28.2.0`.
- `.gitignore` unignored `libs` directory.
- `@UseExperimental` annotation replaced with `@OptIn` in `ChatCooldown.kt`.

### Fixed

### Removed
- Essentials modules removed from `gradle.properties`.
- Essentials modules removed from dependencies in `build.gradle`.
- `jitpack.io` maven repository removed from repositories in `build.gradle`.
- `curseforge` maven repository removed from repositories in `build.gradle`.

## [1.14.4-1.1.0] - 2020-02-11

### Added
- Cooldown module availability checking.
- `/clear-chat` command implementation. 
- `MuteAPI` implementation.
- `/mute` command implementation.
- `ChatModel.kt` mute settings.
- `defaultReason` setting added into `mute` settings.
- `ignoredPlayers` setting added into `mute` settings.
- `notifyAllAboutMute` setting added into `mute` settings.
- Ignored players checking for mute.
- `/unmute` command implementation.
- `/unmute-all` command implementation.
- `notifyAllAboutUnmuteAll` setting added into `mute` settings.
- `/muted-players` command implementation.
- Saving muted players implementation.
- Localization for new commands.

### Changed
- `warpsConfig` renamed to `chatConfig` in `ChatModelUtils.kt`.

### Fixed
- Incorrect variable naming in `ChatModelUtils.kt`.
- Incorrect permissions checking in single player.

## [1.14.4-1.0.0] - 2020-02-08

### Changed
- Uses `permissionAPIClassPath` from CoreAPI.

### Fixed
- Inconsistent version number with semver.

## [1.14.4-0.3.0] - 2020-01-28

### Added
- Events configuration.
- Ability to disable join, left and advancement messages.

### Changed
- Advertising regex improved.

### Fixed
- Local chat incorrect behavior when ranged chat disabled. 

## [1.14.4-0.2.0] - 2020-01-20

### Added
- Pull request template file created by [@ita07](https://github.com/ita07).
- German translation by [@1LiterZinalco](https://github.com/1LiterZinalco).
- Swedish translation by [@robbinworks](https://github.com/robbinworks).
- Pt translation fixed and add french translation by [@dudangel](https://github.com/dudangel).
- Spanish translation by [@drcabral](https://github.com/drcabral)
- Resolved #2. (mention all players with `@all` in message.)
- Permission `ess.chat.mention.all` for mention all players.
- Chat messaging delay implemented.
- Simple Anti-advertising implemented.

### Changed
- Essentials dependencies updated, build.gradle cleanup.
- `kolinx-serialization` and kotlin runtime updated.
- Forge version updated to `28.1.114`.
- Version in mods.toml updated.
- Permissions is not mandatory dependency.
- `isCommonChat` function now is public.
- `JsonConfiguration` simplified in `ChatModelUtils.kt`.
- `ChatModelBase` renamed to `ChatModelUtils.kt`.

### Fixed
- Using command block parameters as mention (`@e` etc).
- Quick reply on message when common chat enabled.
- Incorrect version, not matches semver regex.

### Removed
- Resolved #1. Redundant checking on null.
- Ability to self mention, when mentioned all players by you.

## [1.14.4-0.1.0 ~~.0~~] - 2019-10-18

### Added
- Initial release of Project Essentials Chat as Project Essentials part.
