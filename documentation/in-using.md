> ## Documentation for basically using `Chat` module.

## 1. For playing and running minecraft:

#### 1.1 Download `Chat` mod module.

Visit **Chat** repository on github, visit **releases** tab and download `.jar` files of latest _pre-release_ / release (**recommended**)

Releases page: <https://github.com/ProjectEssentials/ProjectEssentials-Chat/releases>

#### 1.2 Install `Chat` modification.

The minecraft forge folder structure below will help you understand what is written below.

> ##### Important note: don't forget install mod dependencies!

- core: <https://github.com/ProjectEssentials/ProjectEssentials-Core/releases>
- permissions: <https://github.com/ProjectEssentials/ProjectEssentials-Permissions/releases>


```
.
├── assets
├── config
├── libraries
├── mods (that's how it should be)
│   ├── Project Essentials Chat-1.14.4-0.X.X.X.jar
│   ├── Project Essentials Core-MOD-1.14.4-1.X.X.X.jar.
│   └── Project Essentials Permissions-1.14.4-1.X.X.X.jar.
└── ...
```

Place your mods and Chat mod according to the structure above.

#### 1.3 Verifying mod on the correct installation.

Run the game, check the number of mods, if the list of mods contains `Project Essentials Chat` mod, then the mod has successfully passed the initialization of the modification.

After that, go into a single world, then try to write the any message in chat, if you see message `[LOCAL] [Nickname]: your message`, then the modification works as it should.

#### 1.4 Control your chat via minecraft commands.

**Not implemented yet.**

#### 1.5 Control your chat via configuration.

**I will write a little later.**

### For all questions, be sure to write issues!
