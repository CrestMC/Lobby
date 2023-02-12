package me.blurmit.lobby.command;

import me.blurmit.lobby.Lobby;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class CommandBase extends BukkitCommand {

    protected CommandBase(String name) {
        super(name);
    }

    public void registerCommand() {
        JavaPlugin.getPlugin(Lobby.class).getCommandManager().register(getName(), this);
    }

}