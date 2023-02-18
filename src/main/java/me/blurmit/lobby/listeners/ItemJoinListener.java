package me.blurmit.lobby.listeners;

import me.blurmit.lobby.Lobby;
import me.blurmit.lobby.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemJoinListener implements Listener {

    private final Lobby plugin;

    private static final NamespacedKey COMMAND_KEY = new NamespacedKey("item", "command");

    private final Map<String, ConfigurationSection> itemsConfig;
    private final Map<Integer, ItemStack> items;

    public ItemJoinListener(Lobby plugin) {
        this.plugin = plugin;

        this.itemsConfig = loadItems();
        this.items = new HashMap<>();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        itemsConfig.forEach((item, data) -> {
            String name = data.getString("Name");
            List<String> lore = data.getStringList("Lore");
            Material material = Material.getMaterial(data.getString("Material"));
            String command = data.getString("Command");
            int slot = data.getInt("Slot");
            items.put(slot, ItemBuilder.of(material)
                    .setName(name)
                    .setLore(lore)
                    .addPersistentByte(item)
                    .addPersistentData(COMMAND_KEY, PersistentDataType.STRING, command)
                    .build());
        });
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!plugin.getConfig().getBoolean("Give-Join-Items")) {
            return;
        }

        items.forEach((slot, item) -> {
            if (slot < 0 || slot > 40) {
                return;
            }

            event.getPlayer().getInventory().setItem(slot, item);
        });
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (!event.hasItem()) {
            return;
        }

        if (item.getType().isAir()) {
            return;
        }

        if (!item.hasItemMeta()) {
            return;
        }

        boolean isItem = items.values().stream().anyMatch(item::equals);

        if (!isItem) {
            return;
        }

        event.setCancelled(true);

        String command = item.getItemMeta().getPersistentDataContainer().get(COMMAND_KEY, PersistentDataType.STRING);
        player.performCommand(command);
    }

    private Map<String, ConfigurationSection> loadItems() {
        ConfigurationSection itemsSection = plugin.getConfig().getConfigurationSection("Join-Items");
        Map<String, ConfigurationSection> items = new HashMap<>();

        if (itemsSection == null) {
            return new HashMap<>();
        }

        itemsSection.getKeys(false).forEach(item -> {
            ConfigurationSection data = (ConfigurationSection) itemsSection.get(item);
            items.put(item, data);
        });

        return items;
    }

    private boolean isItem(ItemStack itemStack, String keyName) {
        NamespacedKey key = new NamespacedKey("item", keyName);
        return itemStack.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.BYTE);
    }

}
