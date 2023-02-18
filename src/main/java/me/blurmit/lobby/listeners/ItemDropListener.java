package me.blurmit.lobby.listeners;

import me.blurmit.lobby.Lobby;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ItemDropListener implements Listener {

    private final Lobby plugin;

    public ItemDropListener(Lobby plugin) {
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        boolean canDropItems = plugin.getConfig().getBoolean("Allow-Item-Drops");
        event.setCancelled(!canDropItems);
    }

}
