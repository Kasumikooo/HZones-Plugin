<div align="center">

<img width="1874" height="438" alt="banner-hzones" src="https://github.com/user-attachments/assets/f6acdd02-f8c0-4b31-8b80-bdffae0a8619" />

Create custom zones with configurable PvP behavior, PlaceholderAPI support, visual player status indicators, and seamless server integration.

![Minecraft](https://img.shields.io/badge/Minecraft-1.21+-green)
![Java](https://img.shields.io/badge/Java-21-orange)
![Platform](https://img.shields.io/badge/Platform-Paper-blue)
![Status](https://img.shields.io/badge/Status-Active-success)

</div>

---

## About

**HZones** is a lightweight and efficient zone management plugin designed for Minecraft servers that require different PvP behaviors across regions.

Players can move between custom zones, toggle PvP status where allowed, and instantly see visual indicators through colored player names.

Perfect for:

- Survival Servers
- MMORPG Servers

---

## Features

### Custom Zones

Create and manage zones directly in-game.

- Multiple zone support
- Persistent storage
- Fast zone detection
- Easy administration

---

### PvP Management

Control how PvP behaves inside specific zones.

Players can:

- Enable PvP
- Disable PvP
- See their current PvP state instantly

---

### Player Status Colors

HZones automatically updates player names depending on their PvP status.

| Status | Color |
|----------|----------|
| PvP Disabled | 🟢 Green |
| PvP Enabled | 🔴 Red |

This affects:

- Display Name
- Tab List Name
- Custom Name

---

### Real-Time Updates

Whenever a player changes PvP status:

- Name color updates instantly
- Zone state refreshes automatically
- No reconnect required

---

### PlaceholderAPI Support

HZones includes PlaceholderAPI integration.

Available placeholders:

| Placeholder | Description |
|------------|-------------|
| `%hzones_zone%` | Current zone name |
| `%hzones_pvp%` | Current PvP status |

Example:

```yaml
Zone: %hzones_zone%
PvP: %hzones_pvp%
```

---

## Installation

### Requirements

- Java 21+
- Paper 1.21+
- PlaceholderAPI (Optional)
- WorldGuard (Required)
- WorldEdit (Required)

### Steps

1. Download the latest release.
2. Place `HZones.jar` inside your `plugins` folder.
3. Restart the server.
4. Configure the plugin.
5. Enjoy.

---

## Commands

### Zone Commands

| Command | Description |
|----------|----------|
| `/hzones zone create <name> <type>` | Create a zone |
| `/hzones zone delete <name>` | Delete a zone |
| `/hzones zone list` | List all zones |

### PvP Commands

| Command | Description |
|----------|----------|
| `/pvp` | Toggle PvP status |

### Utility Commands

| Command | Description |
|----------|----------|
| `/hzones reload all` | Reload plugin configuration |

## Permissions

| Permission | Description |
|----------|----------|
| `hzones.admin.create` | Create zones |
| `hzones.admin.list` | list zones |
| `hzones.admin.delete` | Delete zones |
| `hzones.reload.all` | Reload plugin |

## Zone Types

| Zone | PvP | Loot Drop | Description |
|------|-----|-----------|-------------|
| 🏙️ `City` | ❌ | ❌ | Safe areas such as spawn towns and cities. |
| 🟢 `Green` | ❌ | ❌ | PvP is completely disabled. |
| 🟡 `Yellow` | ❌ | ❌ | Players can freely toggle their PvP status. |
| 🔴 `Red` | ✅ | ❌ | PvP is always enabled, but players keep their loot on death. |
| ⚫ `Black` | ✅ | ✅ | Full-loot PvP zone. Players drop their items on death. |

---

## Features Showcase

### Peaceful Player

<img width="250" height="28" alt="image" src="https://github.com/user-attachments/assets/113d9181-0e82-4940-a7a2-4a59cd3389d5" />


### PvP Enabled

<img width="250" height="28" alt="image" src="https://github.com/user-attachments/assets/4134f174-707d-476a-99d3-328436b3da58" />


### PlaceholderAPI

```text
Current Zone: Spawn
PvP Status: Enabled
```

---


## Performance

HZones was designed with performance in mind.

✔ Lightweight  
✔ Minimal memory usage  
✔ Fast zone lookups  
✔ Suitable for large servers

---

## Developer Friendly

Clean codebase designed for future expansion.

Potential future additions:

- Region priorities
- World support improvements
- Custom zone flags
- API for external plugins
- ActionBar integration

---

## Issues & Suggestions

Found a bug or have an idea?

Please open an issue on GitHub and include:

- Server version
- Plugin version
- Error logs (if applicable)
- Steps to reproduce

---

## Support

If you enjoy the project:

⭐ Star the repository  
🐛 Report issues  
💡 Suggest features

---

<div align="center">

### Made by Kasumiko

</div>
