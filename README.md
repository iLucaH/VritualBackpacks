# VritualBackpacks
A lightweight contemporary asynchronous backpack plugin for you OP prison server.
This plugin allows all players to earn money in an efficient way by depositing their blocks mined into a virtual bank.
They will never see those blocks, but they will receive the money for them in periodic autosells.

Features:
- Autosells
- Global and temporary multipliers
   - Boosters and Booster Boxes
- Unlimited backpack storage for all players (contemporary feature).
- Particles, titles, sounds and rgb messages.

Dependencies: WorldGuard, Vault

Commands:
- /virtualbackpacks (alias: /vbps) (permission: virtualbackpacks.admin) (description: core plugin command)
    * /vbps reload - reloads the plugin
    * /vbps setprice <price> - sets the price of the item in hand.
    * /vbps remove - removes the price of the item in hand.
    * /vbps listprices - lists all the material prices.
  
    * /vbps setmulti <player> <multi> - sets a players global multiplier.
    * /vbps addmulti <player> <multi> - adds to a players global multiplier.
    * /vbps removemulti <player> <multi> - removes from a players global multiplier.
    * /vbps viewmulti <player> - shows a players multiplier.
  
    * /vbps addbooster <player> <multi> <duration> <time-unit> - gives a player a temporary multiplier.
    * /vbps givebooster <player> <multi> <duration> [amount] - gives a player a physical booster item.
    * /vbps giveboosterbox <player> <box> [amount] - gives a player a booster box.
    * /vbps listboosterboxes - lists all the registered booster boxes.
