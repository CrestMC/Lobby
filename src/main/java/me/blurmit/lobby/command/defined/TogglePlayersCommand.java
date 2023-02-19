package me.blurmit.lobby.command.defined;

import me.blurmit.lobby.Lobby;
import me.blurmit.lobby.command.CommandBase;
import me.blurmit.lobby.menu.defined.ServersMenu;
import me.blurmit.lobby.util.ItemBuilder;
import me.blurmit.lobby.util.Messages;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TogglePlayersCommand extends CommandBase implements Listener {

    private Lobby plugin;
    private Set<UUID> hasPlayersHidden;

    public TogglePlayersCommand(Lobby plugin) {
        super(plugin.getName());
        setName("toggleplayers");
        setDescription("Makes all other players disappear");
        setUsage("/toggleplayers");
        setPermission("lobby.command.toggleplayers");
        setAliases(Arrays.asList("toggleplayervisibility", "hideplayers"));

        this.plugin = plugin;
        this.hasPlayersHidden = new HashSet<>();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
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
        ItemStack visibilityItem = getItem(player, "player-visibility-toggle");

        if (visibilityItem == null) {
            return true;
        }

        // TODO: Store this in a map instead of getting the item material
        // This is a terrible way of doing this, but I REALLY don't feel like storing the data in a Map right now
        if (visibilityItem.getType().equals(Material.LIME_DYE)) {
            plugin.getServer().getOnlinePlayers().forEach(onlinePlayer -> player.hidePlayer(plugin, onlinePlayer));
            hasPlayersHidden.add(player.getUniqueId());
            ItemBuilder.edit(visibilityItem).setMaterial(Material.GRAY_DYE).setName("&aShow Players &7(Right-click)");
        } else {
            plugin.getServer().getOnlinePlayers().forEach(onlinePlayer -> player.showPlayer(plugin, onlinePlayer));
            hasPlayersHidden.remove(player.getUniqueId());
            ItemBuilder.edit(visibilityItem).setMaterial(Material.LIME_DYE).setName("&aHide Players &7(Right-click)");
        }

        return true;
    }

    @Nullable
    private ItemStack getItem(Player player, String keyName) {
        NamespacedKey key = new NamespacedKey("item", keyName);
        return Arrays.stream(player.getInventory().getContents())
                .filter(Objects::nonNull)
                .filter(ItemStack::hasItemMeta)
                .filter(item -> item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.BYTE))
                .findFirst()
                .orElse(null);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        hasPlayersHidden.forEach(playerUUID -> {
            Player player = plugin.getServer().getPlayer(playerUUID);
            player.hidePlayer(plugin, event.getPlayer());
        });
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        hasPlayersHidden.remove(event.getPlayer().getUniqueId());
    }

}
