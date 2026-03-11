# BetterSit 🪑

**BetterSit** is a simple and lightweight plugin for **Paper 1.21.11** that allows players to sit anywhere using the `/sit` command. Stand up by sneaking (pressing Shift).

[![Paper](https://img.shields.io/badge/Paper-1.21.11-blue?style=flat-square)](https://papermc.io)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow?style=flat-square)](https://opensource.org/licenses/MIT)
[![Version](https://img.shields.io/badge/Version-1.1-green?style=flat-square)](https://github.com/raidenshik/BetterSit/releases)

---

## ✨ Features

- ✅ Simple `/sit` command – sit down right where you stand
- ✅ Stand up automatically by sneaking (Shift)
- ✅ Multi-language support (English, Russian, and more)
- ✅ Modern text formatting – MiniMessage, HEX colors (`<#FF55FF>`), gradients
- ✅ Configurable height offset and block centering
- ✅ Cooldown system for `/sit` command
- ✅ Reload command (`/bettersit`) – no server restart needed
- ✅ Automatic update checker (notifies admins about new versions)

---

## 📋 Requirements

- **Server:** Paper 1.21.11 (or any Paper 1.21.x version)
- **Java:** 21 or higher

---

## 🚀 Installation

1. Download the latest `.jar` file from the [Releases](https://github.com/raidenshik/BetterSit/releases) page.
2. Place the file into your server's `plugins/` folder.
3. Restart your server (or use `/reload`, though restart is recommended).
4. Done! Players can now use `/sit`.

---

## 🎮 Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/sit` | Sit down on the spot | `betterSit.sit` |
| `/bettersit` or `/bsit` | Reload config and languages | `betterSit.admin` |

---

## 🔑 Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `betterSit.sit` | Allows using `/sit` | ✅ All players |
| `betterSit.admin` | Allows using `/bettersit` (reload) | ❌ Ops only |

---

## ⚙️ Configuration (`config.yml`)

```yaml
# Language file (en, ru, or any other file in /languages folder)
language: en

# Center the player on the block when sitting?
# true = player sits in the middle of the block
# false = player sits exactly where they stood
center-on-block: true

# Height offset (negative values make the player sit lower)
sit-height-offset: -0.2

# Cooldown between /sit uses in seconds (0 = disabled)
sit-cooldown: 0
