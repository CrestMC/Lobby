package me.blurmit.lobby.listeners;

import me.blurmit.lobby.Lobby;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class DoubleJumpListener implements Listener {

    private Lobby plugin;

    public DoubleJumpListener(Lobby plugin) {
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setAllowFlight(true);

        boolean isAutoFly = plugin.getConfig().getBoolean("Auto-Toggle-Fly");

        if (!isAutoFly) {
            return;
        }

        String autoFlyPermission = plugin.getConfig().getString("Auto-Fly-Permission");

        if (autoFlyPermission == null) {
            return;
        }

        if (!player.hasPermission(autoFlyPermission)) {
            return;
        }

        player.setFlying(true);
    }

    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        String autoFlyPermission = plugin.getConfig().getString("Auto-Fly-Permission");

        if (autoFlyPermission == null) {
            autoFlyPermission = "basics.command.fly";
        }

        if (player.hasPermission(autoFlyPermission)) {
            return;
        }

        if (!event.isFlying()) {
            return;
        }

        event.setCancelled(true);
        player.setVelocity(player.getLocation().getDirection().setY(1));
    }

}
