# Sessility

Sessility is a Minecraft Fabric utility mod that keeps track of sessile creatures, more often known as "AFK players".

> ### sessile
>
> <small>adjective</small>
>
> 1. (of an organism) permanently rooted in place; lacking mobility
> 2. (of a plant) attached without a stalk

## Feature summary

- Change the color of sessile players in the player tab list
- Exclude sessile players from the player sleeping requirement
- Configure the amount of idle time before a player is considered sessile
- Broadcast a chat message when a player becomes either sessile or motile
- Hide sessile players in the player tab list, server player list, or server player count
- Choose if action advancements or player rotation should be used to determine motility

<img src="https://user-images.githubusercontent.com/69266322/174215671-d3220070-ce4d-4d8d-87c9-0765fc3dc0b2.png" width="720px">

By default, any player idling for more than 120 seconds is considered sessile. Sessile players are given a distinct color in the player tab overlay to distinguish them from other players. The sessile idle time and tab display color are both configurable.

<img src="https://user-images.githubusercontent.com/69266322/174503421-b4c1652b-4ad4-4663-a690-d24a4f44a0f0.png" width="720px">

You can also configure custom messages when a player changes their sessility. These messages are disabled by default and will only display if a configured message is present.

<img src="https://user-images.githubusercontent.com/69266322/252195747-01ba7214-8ef2-4ac4-8210-4f02754f9e0e.png" width="720px">

Sessility also allows you to configure whether or not sessile players are displayed in the server player list (shown above), the server player count, and the player tab list. Each one of these properties can be toggled individually.

## Configuration file

```properties
# The idle time in seconds before a player is considered sessile.
# Default: 120
sessile-timeout=300

# The message to broadcast when a player becomes sessile.
# Default: (empty)
message-sessile=%s became a sessile creature

# Whether or not to hide sessile players in the server player list.
# Default: false
hide-sessile-in-server-list=false

# The color of the sessile display name. This can either be a color name or a six-digit hex code.
# Default: gray
sessile-display-color=\#aaaaaa

# Whether or not to exclude sessile players from the player sleeping requirement.
# Default: true
skip-sessile-in-sleep-count=true

# Whether or not to detect player advancement actions, such jumps, as motility.
# Default: true
detect-advancement-action=true

# Whether or not to detect player rotation as motility.
# Default: true
detect-rotation=true

# Whether or not to detect player action (hitting, breaking blocks or moving) as motility.
# Default: true
detect-action=true

# Whether or not to hide sessile players in the player tab list.
# Default: false
hide-sessile-in-tab-list=false

# The message to broadcast when a player becomes motile (the opposite of sessile).
# Default: (empty)
message-motile=%s returned from the depths

# Whether or not to skip sessile players in the server player count.
# Default: false
skip-sessile-in-player-count=false
```
