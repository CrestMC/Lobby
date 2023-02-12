package me.blurmit.lobby;

import me.blurmit.lobby.command.CommandManager;
import me.blurmit.lobby.listeners.DoubleJumpListener;
import me.blurmit.lobby.menu.MenuManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Lobby extends JavaPlugin {

    private CommandManager commandManager;
    private MenuManager menuManager;

    @Override
    public void onEnable() {
        getLogger().info("Loading configuration...");
        saveDefaultConfig();

        getLogger().info("Loading commands...");
        commandManager = new CommandManager(this);
        commandManager.registerCommands();

        getLogger().info("Loading menus...");
        menuManager = new MenuManager(this);

        getLogger().info("Registering listeners...");
        new DoubleJumpListener(this);

        getLogger().info(getName() + " has been successfully enabled.");
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public MenuManager getMenuManager() {
        return menuManager;
    }

}
