# Sessility

Sessility keeps track of sessile creatures, more often known as "AFK players".

<img src="https://user-images.githubusercontent.com/69266322/174215671-d3220070-ce4d-4d8d-87c9-0765fc3dc0b2.png" width="720px">

By default, any player idling for more than 120 seconds is considered sessile. Sessile players are grayed out in the player tab overlay.

## Configuration

```properties
# The idle time in seconds before a player is considered sessile.
# Default: 120
sessile-timeout=120

# The color of the sessile display name.
# Default: gray
sessile-display-color=gray
```
