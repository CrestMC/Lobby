package me.blurmit.lobby.command.defined;

import me.blurmit.lobby.Lobby;
import me.blurmit.lobby.command.CommandBase;
import me.blurmit.lobby.menu.defined.ServersMenu;
import me.blurmit.lobby.util.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ServersCommand extends CommandBase {

    private Lobby plugin;

    public ServersCommand(Lobby plugin) {
        super(plugin.getName());
        setName("servers");
        setDescription("Displays a list of all available servers to player");
        setUsage("/servers");
        setPermission("lobby.command.servers");
        setAliases(Arrays.asList("gamemodes", "games", "modes"));

        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!sender.hasPermission(getPermission())) {
            sender.sendMessage(Messages.NO_PERMISSION + "");
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.ONLY_PLAYERS + "");
            return true;
        }

        Player player = (Player) sender;
        plugin.getMenuManager().displayMenu(player, new ServersMenu(plugin));
        return true;
    }

}
