> ## Installation instructions.

For start the modification, you need installed Forge, it is desirable that the version matches the supported versions. You can download Forge from the [link](https://files.minecraftforge.net/maven/net/minecraftforge/forge/index_1.14.4.html).
Move the downloaded mod to the `mods` folder (installation example below).

Also do not forget to install dependencies, only two types of dependencies
    - mandatory (game will not start without a mod)
    - recommended (without a mod, game can start, but I recommend using it)

Downloads: [Cooldown](https://github.com/ProjectEssentials/ProjectEssentials-Cooldown) · [Core](https://github.com/ProjectEssentials/ProjectEssentials-Core) · [Permissions](https://github.com/ProjectEssentials/ProjectEssentials-Permissions)

```
.
├── assets
├── config
├── libraries
├── mods (that's how it should be)
│   ├── Project Essentials Core-MOD-1.14.4-1.X.X.X.jar (mandatory)
│   ├── Project Essentials Cooldown-1.14.4-1.X.X.X.jar (recommended)
│   ├── Project Essentials Permissions-1.14.4-1.X.X.X.jar (recommended)
│   └── Project Essentials Chat-1.14.4-1.X.X.X.jar
└── ...
```

Now try to start the game, go to the `mods` tab, if this modification is displayed in the `mods` tab, then the mod has been successfully installed.

### Configuration

Just in case. Default configuration.

You can get default configuration by removing file in path `.minecraft/config/ProjectEssentials/chat.json`, while mod loading if configuration file not exists it ll be recreated.

```json
{
    "moderation": {
        "blockedWords": [
            "fuck",
            "shit"
        ],
        "blockedChars": [
        ],
        "modifyBlockedWords": true,
        "blockedWordsMask": "**beep**",
        "maxMessageLength": 128,
        "messagingCooldownEnabled": true,
        "messagingCooldown": 5,
        "advertisingAllowed": false,
        "advertisingRegex": "(http|ftp|https)://([\\w_-]+(?:(?:\\.[\\w_-]+)+))([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?"
    },
    "messaging": {
        "messageGlobalPattern": "&8[&2GLOBAL&8] &a┃ &8[&6%player&8]&7: &f%message",
        "messageLocalPattern": "&8[&cLOCAL&8] &4┃ &8[&7%player&8]&7: &7§o%message",
        "messageCommonPattern": "&8[&7%player&8]&7: &f%message",
        "enableRangedChat": false,
        "localChatRange": 100,
        "chatEnabled": true
    },
    "mentions": {
        "mentionsEnabled": true,
        "mentionInActionBar": true,
        "mentionMessage": "&7you are mentioned by &l&7%player&7 player, in the chat.",
        "mentionAtFormat": "&c",
        "mentionNameFormat": "&b"
    }
}
```

### Describing configuration

```
    Property name: moderation.blockedWords

    Accepts data type: Array with String type

    Description: contains all blocked words.
```

```
    Property name: moderation.blockedChars

    Accepts data type: Array with String type

    Description: contains all blocked chars.
```

```
    Property name: moderation.modifyBlockedWords

    Accepts data type: Boolean

    Description: if value true then blocked words will be replaced on `moderation.blockedWordsMask`, if value false then message will't sent.
```

```
    Property name: moderation.blockedWordsMask

    Accepts data type: String

    Description: see `moderation.modifyBlockedWords`. Just replace swear word on it string.
```

```
    Property name: moderation.maxMessageLength

    Accepts data type: Int

    Description: Declare max message lenght, if message length excess this value then message will't sent.
```

```
    Property name: moderation.messagingCooldownEnabled

    Accepts data type: Boolean

    Description: If value true, then delay in chat will be enabled.
```

```
    Property name: moderation.messagingCooldown

    Accepts data type: Int

    Description: Declare cooldown delay. See `moderation.messagingCooldownEnabled`
```

```
    Property name: moderation.advertisingAllowed

    Accepts data type: Boolean

    Description: If value true, then messages with link will't sent.
```

```
    Property name: moderation.advertisingRegex

    Accepts data type: Regex (But actualy String)

    Description: Regular expression for detecting url's in message.
```

```
    Property name: messaging.messageGlobalPattern

    Accepts data type: String

    Description: Message pattern for global chat.
```

```
    Property name: messaging.messageLocalPattern

    Accepts data type: String

    Description: Message pattern for local chat.
```

```
    Property name: messaging.messageCommonPattern

    Accepts data type: String

    Description: Message pattern for common chat. Uses if ranged chat disabled. See `messaging.enableRangedChat`
```

```
    Property name: messaging.enableRangedChat

    Accepts data type: Boolean

    Description: If value true then ranged chat will be enabled.
```

```
    Property name: messaging.localChatRange

    Accepts data type: Int

    Description: Max range local chat in `block` (for all axis). Uses if ranged chat enabled.
```

```
    Property name: messaging.chatEnabled

    Accepts data type: Boolean

    Description: If false then nobody will send a message in chat.
```

```
    Property name: mentions.mentionsEnabled

    Accepts data type: Boolean

    Description: If true then players can use mentions in chat.
```

```
    Property name: mentions.mentionInActionBar

    Accepts data type: Boolean

    Description: If true then players will be notified about mention in action bar.
```

```
    Property name: mentions.mentionMessage

    Accepts data type: String

    Description: Mention pattern message for action bar, uses if mentions in action bar enabled.
```

```
    Property name: mentions.mentionAtFormat

    Accepts data type: String

    Description: Pattern message for `@` symbol.
```

```
    Property name: mentions.mentionNameFormat

    Accepts data type: String

    Description: Pattern message for after `@` symbol in message. (sorry for really bad english)
```

### If you have any questions or encounter a problem, be sure to open an [issue](https://github.com/ProjectEssentials/ProjectEssentials-Chat/issues/new/choose)!