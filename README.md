<div align="center">

<img src="icons/New Project (39).png" alt="Grace" width="420">

### Simple grace periods for Minecraft servers.

<a href="https://modrinth.com/plugin/grace">
  <img src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3.3.1/assets/cozy/available/modrinth_64h.png" alt="Modrinth" height="56">
</a>
<a href="https://www.spigotmc.org/resources/grace.137735/">
  <img src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3.3.1/assets/cozy/available/spigot_64h.png" alt="SpigotMC" height="56">
</a>
<a href="https://github.com/arkhamology/grace/releases">
  <img src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3.3.1/assets/cozy/available/github_64h.png" alt="GitHub" height="56">
</a>

</div>

---

Grace is a small Paper plugin for running grace periods.

Start a timer and PvP is disabled until it runs out. Everyone gets a bossbar showing how much time is left, and you can pause or stop it whenever you need to.

## Features

- Bossbar countdown
- Green → yellow → red timer
- Blocks melee and projectile PvP
- Custom durations like `30s`, `10m`, `1h30m`
- Pause and resume support
- Start and end sounds

## Commands

| Command | |
|---|---|
| `/grace start <time>` | Start grace |
| `/grace pause` | Pause the timer |
| `/grace resume` | Resume it |
| `/grace stop` | End grace early |
| `/grace status` | Check remaining time |

**Aliases:** `/gp`, `/graceperiod`  
**Permission:** `graceperiod.admin`

## Support

<img src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3.3.1/assets/cozy/supported/paper_64h.png" alt="Paper" height="56">

Currently made for **Paper 1.21.x** with **Java 21+**.

## Planned

Support for more server platforms is planned:

- Purpur
- Fabric
- Folia
- Pufferfish
- Spigot

## Building

```bash
git clone https://github.com/arkhamology/grace.git
cd grace
mvn package
```

The jar will be in `target/`.

## License

MIT — see [`LICENSE`](LICENSE).

---

<div align="center">

_by arkhamology <3_

</div>
