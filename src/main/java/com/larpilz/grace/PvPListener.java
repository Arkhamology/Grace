package com.larpilz.grace;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.projectiles.ProjectileSource;

public class PvPListener implements Listener {

    private final GracePeriodPlugin plugin;

    public PvPListener(GracePeriodPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!plugin.isActive()) return;
        if (!(event.getEntity() instanceof Player victim)) return;

        // Direct player-on-player hit
        if (event.getDamager() instanceof Player) {
            event.setCancelled(true);
            return;
        }

        // Projectile fired by a player (arrows, tridents, snowballs, etc.)
        if (event.getDamager() instanceof Projectile projectile) {
            ProjectileSource shooter = projectile.getShooter();
            if (shooter instanceof Player) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.addPlayerToBossBar(event.getPlayer());
    }
}
