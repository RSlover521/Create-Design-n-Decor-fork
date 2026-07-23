# 🏗️ Design n' Decor: Legacy Fix 🔧

**Design n' Decor: Legacy Fix** is a compatibility-focused fork of **Design n' Decor** for **Minecraft 1.20.1**, **Forge**, and **Create 6**. 🚂⚙️

This project exists to help older worlds survive the transition from previous Design n' Decor versions to newer Create 6-based environments by restoring missing registry entries and preserving legacy decorative and industrial content. 🏭🏠

Many long-running Minecraft worlds rely on exact registry IDs. When blocks are removed, renamed, or reorganized, worlds may lose decorations, machines, and builds that took hours or years to create. ⛏️💔

**Design n' Decor: Legacy Fix aims to keep those worlds alive.** ❤️

---

# ✨ What the mod does

This fork restores and preserves legacy Design n' Decor content while maintaining compatibility with the modern Create 6 ecosystem.

## 🔄 Legacy Registry Restoration

- Restores approximately **453 legacy block and item registrations** from older Design n' Decor versions.
- Preserves old `design_decor:<name>` registry references used by existing worlds.
- Remaps compatible IDs from:

```
design_decor:<name>
```

to:

```
dndecor:<name>
```

when an exact replacement exists. 🔁

Mappings are deliberately conservative:
- ✅ Exact compatible replacements are restored.
- ✅ Matching registry paths are preserved whenever possible.
- ❌ Unknown entries are not replaced with unrelated blocks.

The goal is **world preservation**, not forced conversion. 🌎

---

# 🧱 Restored Content

This fork restores many legacy decoration and industrial building families, including:

🏰 **Castel Blocks**
- Castel bricks
- Castel tiles
- Slabs
- Stairs
- Walls

🎨 **Colored Decoration Systems**
- Colored metal plates
- Colored metal sheets
- Wallpapers

⚙️ **Create Integration Content**
- Material variants of crushing wheels
- Millstones
- Boilers
- Large chains

🏭 **Industrial Decorations**
- Catwalks
- Railings
- Lamps 💡
- Lights
- Floodlights
- Screws
- Bolts
- Containers

🚦 **Legacy Sign Collection**
- Numbers
- Letters
- Warning signs
- Symbols
- Decorative signs

🏗️ **Standalone Blocks**
- Industrial gears
- Gas tanks
- Framed glass
- Industrial floors
- Supports
- Utility blocks

And many more restored legacy registrations. 🛠️

---

# 👥 Who is this for?

This fork is intended for:

✅ Minecraft 1.20.1 worlds  
✅ Forge users  
✅ Create 6 migration projects  
✅ Existing worlds that previously used older Design n' Decor versions  
✅ Servers that want to preserve years of construction progress 🏙️

If you are starting a brand-new world with no legacy Design n' Decor content, the upstream version may be a better choice. 🌱

---

# 📦 Requirements

Required:

- 🟩 Minecraft **1.20.1**
- 🔥 Forge **47.x**
- ⚙️ Create **6.0.6–6.0.x**

Development is currently tested with:

```
Minecraft: 1.20.1
Forge: 47.4.8
Create: 6.0.8
Java: 17
```

---

# 🚨 Installation & World Migration

> ⚠️ **BACK UP YOUR WORLD FIRST!** ⚠️

Registry migration affects how Minecraft resolves saved blocks and items. A world saved after migration may not safely return to the previous mod version.

Recommended migration process:

1. 💾 Create a complete backup of your world.
2. 🗑️ Remove the existing Design n' Decor JAR.
3. 📥 Install Design n' Decor: Legacy Fix.
4. ⚙️ Install compatible Forge and Create 6 versions.
5. 📜 Check the Forge log for unresolved mappings.
6. 🧪 Load a copy of your world first.
7. 🏗️ Verify important builds, machines, inventories, and contraptions.
8. ✅ Only replace your main world after successful testing.

---

⚠️ **Important**

This fork uses the same `dndecor` mod ID.

Do **not** install multiple Design n' Decor builds together.

Example:

❌ Incorrect:

```
mods/
├── Design-n-Decor.jar
└── Design-n-Decor-Legacy-Fix.jar
```

✅ Correct:

```
mods/
└── Design-n-Decor-Legacy-Fix.jar
```

---

# 🔧 Compatibility Notes

The compatibility system focuses on legacy registry preservation.

Features:

✅ Legacy block and item ID restoration  
✅ Safer world migration  
✅ Create 6 compatibility  
✅ Preservation of existing builds

Limitations:

- Exact behavior parity with every historical version is not guaranteed.
- Resource packs, scripts, or addons depending on old implementation details may require updates.
- Unsupported registry entries will remain unresolved rather than being incorrectly replaced.

If Forge reports a missing mapping:

Please include:

- 📌 Missing registry ID
- 📌 Previous Design n' Decor version
- 📌 Forge log excerpt
- 📌 Minecraft version

when reporting an issue.

---

# 🛠️ Building From Source

Requirements:

- ☕ Java 17
- 📦 Gradle wrapper included

Build:

```powershell
.\gradlew.bat build
```

Output:

```
build/libs/
```

Run automated tests:

```powershell
.\gradlew.bat runGameTestServer
```

---

# 🚧 Project Status

Current status:

🟡 **Beta Compatibility Release**

The main restoration work is complete, but compatibility testing continues.

Please:

- 💾 Keep backups
- 🧪 Test on copied worlds first
- 🐛 Report unresolved legacy registrations

Every report helps improve compatibility for more worlds. 🌎✨

---

# ❤️ Credits

Original Design n' Decor:

- 👤 Luna
- 👥 Original contributors

Design n' Decor: Legacy Fix:

- 🔧 Maintained by DrMangoTea and RSlover521

Original project credit and asset ownership are preserved.

---

# 📜 License

This project contains original code changes and utilizes assets from the original Design n' Decor project.

Please refer to the repository license files and distributed metadata for:

- Original asset licenses
- Code licenses
- Fork modifications

Respect the original creators and their work. ❤️