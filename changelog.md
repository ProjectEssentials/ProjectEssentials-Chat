# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [1.15.2-1.1.1] - 2020-03-14

### Added
- Libraries as dependencies added.
- Compatibility with safe-localization messages added.

### Changed
- Kotlin dependency updated to `1.3.70`.
- KotlinX Serialization version updated to `0.20.0`.
- Forge API version updated to `28.2.0`.
- `.gitignore` unignored `libs` directory.
- `@UseExperimental` annotation replaced with `@OptIn` in `ChatCooldown.kt`.

### Removed
- Essentials modules removed from `gradle.properties`.
- Essentials modules removed from dependencies in `build.gradle`.
- `jitpack.io` maven repository removed from repositories in `build.gradle`.
- `curseforge` maven repository removed from repositories in `build.gradle`.

## [1.15.2-1.1.0] - 2020-02-11

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

## [1.15.2-1.0.0] - 2020-02-08

### Added
- Initial release.
