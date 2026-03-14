# BetterSit 🪑

**BetterSit** is a lightweight and feature‑rich plugin for **Paper 1.21.11** that allows players to sit, lie down, and even sit around campfires.  
All messages are fully customizable via language files supporting **MiniMessage**, **HEX colors**, and legacy `&` codes.

[![Paper](https://img.shields.io/badge/Paper-1.21-blue?style=flat-square)](https://papermc.io)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow?style=flat-square)](https://github.com/rimzzdev/BetterSit/blob/main/LICENSE)
[![Version](https://img.shields.io/badge/Version-1.4-green?style=flat-square)](https://modrinth.com/plugin/bettersit)

---

## ✨ Features

- ✅ `/sit` – sit down where you stand (toggle: use again or sneak to stand up)
- ✅ `/lay` – lie down on the ground (toggle)
- ✅ Right‑click on **stairs**, **slabs** (non‑pressure), **carpets** or **campfires** to sit on the center of the block / around the campfire
- ✅ **Campfire sitting** – automatically finds the nearest free spot around the campfire and rotates the player towards it
- ✅ Per‑player toggle for each block category: `/bsit toggle stairs|slab|carpet|campfire`
- ✅ Multi‑language support (English, Russian, and more)
- ✅ Modern text formatting – MiniMessage, HEX, gradients, etc.
- ✅ Configurable height offsets for sitting and lying
- ✅ Cooldown system for `/sit` and `/lay`
- ✅ Reload command: `/bsit reload`
- ✅ Version info: `/bsit version`
- ✅ Update checker – notifies admins in console and in chat (with clickable link) when a new version is available on **Modrinth**

---

## 📋 Requirements

- **Server:** Paper 1.21.11 (or any Paper 1.21.x version)
- **Java:** 21 or higher

---

## 🚀 Installation

1. Download the latest `.jar` from the [Modrinth page](https://modrinth.com/plugin/bettersit) or from [GitHub Releases](https://github.com/rimzzdev/BetterSit/releases).
2. Place the file into your server's `plugins/` folder.
3. Restart your server (or use `/reload`, though restart is recommended).
4. Done! Players can now use `/sit`, `/lay`, and right‑click on appropriate blocks.

---

## 🎮 Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/sit` | Sit down or stand up | `betterSit.sit` |
| `/lay` | Lie down or stand up | `betterSit.sit` |
| `/bsit toggle <stairs\|slab\|carpet\|campfire>` | Enable/disable right‑click sitting for yourself | `betterSit.sit` |
| `/bsit reload` | Reload config and languages | `betterSit.admin` |
| `/bsit version` | Show plugin version and link | `betterSit.admin` |

---

## 🔑 Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `betterSit.sit` | Allows using `/sit`, `/lay` and `/bsit toggle` | ✅ All players |
| `betterSit.admin` | Allows using `/bsit reload` and `/bsit version` | ❌ Ops only |

---

## ⚙️ Configuration (`config.yml`)

```yaml
# Language file (en, ru, or any other file in /languages folder)
language: en

# Center the player on the block when sitting/lying?
center-on-block: true

# Height offset for sitting (negative values make the player sit lower)
sit-height-offset: -0.2

# Height offset for lying (negative values make the player lie lower)
lay-height-offset: -0.8

# Cooldown between /sit or /lay uses in seconds (0 = disabled)
sit-cooldown: 0

# Allow sitting at campfires (right-click)
campfire-enabled: true
