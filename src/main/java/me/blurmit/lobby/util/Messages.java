package me.blurmit.lobby.util;

import me.blurmit.lobby.Lobby;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public enum Messages {

    NO_PERMISSION() {
        @Override
        public String toString() {
            return colorize("No-Permission");
        }
    },

    ONLY_PLAYERS() {
        @Override
        public String toString() {
            return colorize("Only-Players");
        }
    };

    public static String colorize(String configKey) {
        return ChatColor.translateAlternateColorCodes('&', JavaPlugin.getPlugin(Lobby.class).getConfig().getString("Messages." + configKey));
    }

}
