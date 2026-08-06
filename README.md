<h1 align="center">Grace Period</h1>

<p align="center">Simple Grace period Plugin.</p>

<p align="center">
  <a href="https://modrinth.com/plugin/grace"><img src="icons/modrinth.png" alt="Modrinth" width="50" style="image-rendering: pixelated; display: inline-block; vertical-align: middle;"></a><a href="https://www.spigotmc.org/resources/grace.137735/"><img src="icons/domain.png" alt="SpigotMC" width="50" style="image-rendering: pixelated; display: inline-block; vertical-align: middle;"></a><a href="https://github.com/larpilz/grace/releases/tag/release"><img src="icons/github.png" alt="Latest Release" width="50" style="image-rendering: pixelated; display: inline-block; vertical-align: middle;"></a>
</p>
<p align="center">
  </a>
  <a href="LICENSE">
    <img src="https://img.shields.io/badge/license-MIT-blue.svg" alt="License">
  </a>

---

## What it does

- **Bossbar countdown** at the top of the screen, color shifts green → yellow → red as it runs down
- **No PvP** while it's active — blocks melee *and* projectiles (arrows, tridents, snowballs, etc.)
- **Custom duration** — `30s`, `5m`, `1h30m`, whatever format you like
- **Pause / resume** the timer without losing progress
- **Start and end sounds** so people actually notice when it kicks in or wears off

## Commands

| Command | What it does |
|---|---|
| `/grace start <time>` | Starts a grace period, e.g. `/grace start 10m` |
| `/grace pause` | Freezes the timer where it is |
| `/grace resume` | Picks the timer back up |
| `/grace stop` | Ends it early |
| `/grace status` | Shows time remaining |

**Aliases:** `/gp`, `/graceperiod`  
**Permission:** `graceperiod.admin` (defaults to op)

Accepted time formats: plain seconds (`90`), or a mix of `h`/`m`/`s` (`1h30m`, `45s`, `5m`).

## Installation

1. Grab the jar from [Modrinth](https://modrinth.com), [SpigotMC](https://spigotmc.org), or the [Releases page](https://github.com)
2. Drop it in your server's `plugins/` folder
3. Restart the server

Requires Paper (or a Paper fork) 1.21.x and Java 21+.

## Building from source

```bash
git clone https://github.com
cd grace
mvn package
```

The built jar will be at `target/grace-1.0.0.jar`.

## Contributing

Issues and PRs are welcome. If you're adding a feature, open an issue first so we're on the same page before you put the work in.

## License

MIT — see [LICENSE](LICENSE) for details.
