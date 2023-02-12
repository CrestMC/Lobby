package me.blurmit.lobby.menu.defined;

import me.blurmit.lobby.Lobby;
import me.blurmit.lobby.menu.Menu;
import me.blurmit.lobby.menu.MenuButton;
import me.blurmit.lobby.menu.MenuType;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServersMenu extends Menu {

    private Lobby plugin;
    private Map<String, ConfigurationSection> servers;

    public ServersMenu(Lobby plugin) {
        this.plugin = plugin;
        this.servers = loadServers();

        servers.forEach((server, info) -> {
            String name = info.getString("Name");
            List<String> description = info.getStringList("Description");
            int slot = info.getInt("Slot");
            String materialName = info.getString("Material");

            if (materialName == null) {
                materialName = "STONE";
            }

            Material material = Material.getMaterial(materialName);
            addButton(new MenuButton(name).setLore(description).setSlot(slot).setMaterial(material));
        });
    }

    @Override
    public String getName() {
        return plugin.getConfig().getString("Servers-Title");
    }

    @Override
    public int getSlots() {
        return plugin.getConfig().getInt("Servers-Slots");
    }

    @Override
    public void callButton(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        servers.values().stream().filter(serverItem -> serverItem.getInt("Slot") == event.getSlot()).forEach(serverItem -> {
            String command = serverItem.getString("Command");

            if (command == null) {
                return;
            }

            player.performCommand(command);
            player.closeInventory();
        });
    }

    @Override
    public MenuType getType() {
        return MenuType.FILLED;
    }

    private Map<String, ConfigurationSection> loadServers() {
        ConfigurationSection serversConfigSection = plugin.getConfig().getConfigurationSection("Servers");
        Map<String, ConfigurationSection> servers = new HashMap<>();

        if (serversConfigSection == null) {
            return new HashMap<>();
        }

        serversConfigSection.getKeys(false).forEach(server -> {
            ConfigurationSection data = (ConfigurationSection) serversConfigSection.get(server);
            servers.put(server, data);
        });

        return servers;
    }

}
