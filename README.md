# Sessility

Sessility keeps track of sessile creatures, more often known as "AFK players".

<img src="https://user-images.githubusercontent.com/69266322/174215671-d3220070-ce4d-4d8d-87c9-0765fc3dc0b2.png" width="720px">

By default, any player idling for more than 120 seconds is considered sessile. Sessile players are given a distinct color in the player tab overlay to distinguish them from other players. The sessile idle time and tab display color are both configurable.

<img src="https://user-images.githubusercontent.com/69266322/174503421-b4c1652b-4ad4-4663-a690-d24a4f44a0f0.png" width="720px">

You can also configure custom messages when a player changes their sessility. These messages are disabled by default and will only display if a configured message is present.

## Configuration

```properties
# The idle time in seconds before a player is considered sessile.
# Default: 120
sessile-timeout=120

# The message to broadcast when a player becomes sessile.
# Default: (empty)
message-sessile=%s became a sessile creature

# The color of the sessile display name. This can either be a color name or a six-digit hex code.
# Default: gray
sessile-display-color=\#aaaaaa

# The message to broadcast when a player becomes motile (the opposite of sessile).
# Default: (empty)
message-motile=%s returned from the depths
```
