package com.larpilz.grace;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GracePeriodPlugin extends JavaPlugin implements CommandExecutor, TabCompleter {

    private BossBar bossBar;
    private BukkitTask countdownTask;
    private int totalSeconds;
    private int remainingSeconds;
    private boolean active = false;
    private boolean paused = false;

    // Matches things like "30", "30s", "5m", "1h", "1h30m"
    private static final Pattern TIME_PATTERN = Pattern.compile("(\\d+)([smh]?)");

    // Suggested durations shown when tab-completing /grace start
    private static final List<String> DURATION_SUGGESTIONS =
            Arrays.asList("30s", "1m", "5m", "10m", "15m", "30m", "1h");

    @Override
    public void onEnable() {
        getCommand("grace").setExecutor(this);
        getCommand("grace").setTabCompleter(this);
        getServer().getPluginManager().registerEvents(new PvPListener(this), this);
        getLogger().info("Grace enabled.");
    }

    @Override
    public void onDisable() {
        stopGracePeriod(false);
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§eUsage: /grace <start|stop|pause|resume|status> [duration]");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "start" -> {
                if (args.length < 2) {
                    sender.sendMessage("§eUsage: /grace start <duration>  (e.g. 60, 90s, 5m, 1h)");
                    return true;
                }
                Integer seconds = parseTime(args[1]);
                if (seconds == null || seconds <= 0) {
                    sender.sendMessage("§cInvalid duration. Try formats like 60, 90s, 5m, 1h30m.");
                    return true;
                }
                startGracePeriod(seconds);
                Bukkit.broadcastMessage("§a§lGrace period started! §fNo PvP for §e" + formatTime(seconds) + "§f.");
            }
            case "stop" -> {
                if (!active) {
                    sender.sendMessage("§cThere is no active grace period.");
                    return true;
                }
                stopGracePeriod(true);
                Bukkit.broadcastMessage("§c§lGrace period ended early. §fPvP is now enabled.");
            }
            case "pause" -> {
                if (!active) {
                    sender.sendMessage("§cThere is no active grace period.");
                    return true;
                }
                if (paused) {
                    sender.sendMessage("§cThe grace period is already paused.");
                    return true;
                }
                pauseGracePeriod();
                Bukkit.broadcastMessage("§e§lGrace period paused §fat §e" + formatTime(remainingSeconds) + "§f.");
            }
            case "resume" -> {
                if (!active) {
                    sender.sendMessage("§cThere is no active grace period.");
                    return true;
                }
                if (!paused) {
                    sender.sendMessage("§cThe grace period isn't paused.");
                    return true;
                }
                resumeGracePeriod();
                Bukkit.broadcastMessage("§a§lGrace period resumed! §fNo PvP for §e" + formatTime(remainingSeconds) + "§f.");
            }
            case "status" -> {
                if (active && paused) {
                    sender.sendMessage("§eGrace period paused: §e" + formatTime(remainingSeconds) + " §eremaining.");
                } else if (active) {
                    sender.sendMessage("§aGrace period active: §e" + formatTime(remainingSeconds) + " §aremaining.");
                } else {
                    sender.sendMessage("§7No grace period is currently active.");
                }
            }
            default -> sender.sendMessage("§eUsage: /grace <start|stop|pause|resume|status> [duration]");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], Arrays.asList("start", "stop", "pause", "resume", "status"), completions);
        } else if (args.length == 2 && args[0].equalsIgnoreCase("start")) {
            StringUtil.copyPartialMatches(args[1], DURATION_SUGGESTIONS, completions);
        }

        return completions;
    }

    private void startGracePeriod(int seconds) {
        // Cancel any existing grace period first
        if (active) {
            stopGracePeriod(false);
        }

        this.totalSeconds = seconds;
        this.remainingSeconds = seconds;
        this.active = true;
        this.paused = false;

        bossBar = Bukkit.createBossBar("Grace Period: " + formatTime(remainingSeconds), BarColor.GREEN, BarStyle.SOLID);
        bossBar.setProgress(1.0);
        for (Player p : Bukkit.getOnlinePlayers()) {
            bossBar.addPlayer(p);
        }
        bossBar.setVisible(true);

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p.getLocation(), Sound.ITEM_TOTEM_USE, 1.0f, 1.0f);
        }

        countdownTask = Bukkit.getScheduler().runTaskTimer(this, this::tick, 0L, 20L);
    }

    private void pauseGracePeriod() {
        paused = true;
        if (countdownTask != null) {
            countdownTask.cancel();
            countdownTask = null;
        }
        if (bossBar != null) {
            bossBar.setColor(BarColor.PURPLE);
            bossBar.setTitle("Grace Period (Paused): " + formatTime(remainingSeconds));
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 0.8f);
        }
    }

    private void resumeGracePeriod() {
        paused = false;
        if (bossBar != null) {
            bossBar.setTitle("Grace Period: " + formatTime(remainingSeconds));
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.2f);
        }
        countdownTask = Bukkit.getScheduler().runTaskTimer(this, this::tick, 0L, 20L);
    }

    private void tick() {
        if (!active || paused) return;

        remainingSeconds--;

        if (remainingSeconds <= 0) {
            stopGracePeriod(true);
            Bukkit.broadcastMessage("§c§lGrace period has ended! §fPvP is now enabled.");
            return;
        }

        double progress = (double) remainingSeconds / (double) totalSeconds;
        bossBar.setProgress(Math.max(0.0, Math.min(1.0, progress)));
        bossBar.setTitle("Grace Period: " + formatTime(remainingSeconds));

        // Change color as time runs low
        if (progress < 0.2) {
            bossBar.setColor(BarColor.RED);
        } else if (progress < 0.5) {
            bossBar.setColor(BarColor.YELLOW);
        } else {
            bossBar.setColor(BarColor.GREEN);
        }
    }

    private void stopGracePeriod(boolean playEndSound) {
        boolean wasActive = active;
        active = false;
        paused = false;
        if (countdownTask != null) {
            countdownTask.cancel();
            countdownTask = null;
        }
        if (bossBar != null) {
            bossBar.removeAll();
            bossBar.setVisible(false);
            bossBar = null;
        }
        if (wasActive && playEndSound) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.playSound(p.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 1.0f);
            }
        }
    }

    /** Called from PlayerJoinListener-style logic if you add one; adds a joining player to the active bossbar. */
    public void addPlayerToBossBar(Player player) {
        if (active && bossBar != null) {
            bossBar.addPlayer(player);
        }
    }

    private Integer parseTime(String input) {
        Matcher matcher = TIME_PATTERN.matcher(input.toLowerCase());
        int totalSeconds = 0;
        boolean found = false;

        while (matcher.find()) {
            found = true;
            int value = Integer.parseInt(matcher.group(1));
            String unit = matcher.group(2);
            switch (unit) {
                case "h" -> totalSeconds += value * 3600;
                case "m" -> totalSeconds += value * 60;
                case "s", "" -> totalSeconds += value;
            }
        }

        return found ? totalSeconds : null;
    }

    private String formatTime(int seconds) {
        int h = seconds / 3600;
        int m = (seconds % 3600) / 60;
        int s = seconds % 60;
        StringBuilder sb = new StringBuilder();
        if (h > 0) sb.append(h).append("h ");
        if (m > 0 || h > 0) sb.append(m).append("m ");
        sb.append(s).append("s");
        return sb.toString();
    }
}
