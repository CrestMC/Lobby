package me.blurmit.lobby.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ItemBuilder {

    private final ItemStack itemStack;

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public static ItemBuilder of(Material material) {
        return new ItemBuilder(new ItemStack(material));
    }

    public static ItemBuilder edit(ItemStack itemStack) {
        return new ItemBuilder(itemStack);
    }

    public ItemBuilder consume(Consumer<ItemStack> consumer) {
        consumer.accept(itemStack);

        return this;
    }

    public ItemBuilder consumeMeta(Consumer<ItemMeta> consumer) {
        ItemMeta meta = itemStack.getItemMeta();
        consumer.accept(meta);
        itemStack.setItemMeta(meta);

        return this;
    }

    public ItemBuilder setName(String name) {
        return consumeMeta(meta -> meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name)));
    }

    public ItemBuilder setLore(List<String> lore) {
        return consumeMeta(meta -> {
            List<String> translatedLore = lore.stream().map(line -> ChatColor.translateAlternateColorCodes('&', line)).collect(Collectors.toList());
            meta.setLore(translatedLore);
        });
    }

    public ItemBuilder setLore(String... lore) {
        return consumeMeta(meta -> setLore(Arrays.asList(lore)));
    }

    public ItemBuilder addLore(String... lore) {
        return consumeMeta(meta -> {
            if (!meta.hasLore()) {
                setLore(lore);
            }

            List<String> loreList = meta.getLore();
            loreList.addAll(Arrays.asList(lore));
            setLore(loreList);
        });
    }

    public ItemBuilder setItemFlag(ItemFlag... flags) {
        return consumeMeta(meta -> meta.addItemFlags(flags));
    }

    public <T, Z> ItemBuilder addPersistentData(NamespacedKey key, PersistentDataType<T, Z> dataType, Z value) {
        return consumeMeta(meta -> meta.getPersistentDataContainer().set(key, dataType, value));
    }

    public ItemBuilder addPersistentByte(String key) {
        return addPersistentData(new NamespacedKey("item", key.toLowerCase().replace(" ", "-")), PersistentDataType.BYTE, (byte) 1);
    }

    public String getName() {
        return itemStack.getItemMeta() != null ? itemStack.getItemMeta().getDisplayName() : itemStack.getType().getData().getName();
    }

    public List<String> getLore() {
        return itemStack.getItemMeta() != null ? itemStack.getItemMeta().getLore() : new ArrayList<>();
    }

    public ItemStack build() {
        return itemStack.clone();
    }

}
