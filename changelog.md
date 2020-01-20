# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

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