package me.nanigans.pandoraenchants.Util;

import org.apache.commons.lang3.EnumUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ItemUtil {

    public static ItemStack setLore(ItemStack item, String... lore){

        ItemMeta meta = item.getItemMeta();
        meta.setLore(Arrays.stream(lore).collect(Collectors.toList()));
        item.setItemMeta(meta);
        return item;

    }

    public static ItemStack renameItem(ItemStack item, String name){
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Creates a new item from a string material id
     * @param material the material of the item "ID/ID"
     * @param name the name of the item
     * @param nbt any nbt values
     * @return a new itemstack
     */
    public static ItemStack createItem(String material, String name, String... nbt){

        if(EnumUtils.isValidEnum(Material.class, material)){
            return createItem(Material.valueOf(material), name, nbt);
        }

        ItemStack item = new ItemStack(Material.getMaterial(Integer.parseInt(material.split("/")[0])),
                1, Byte.parseByte(material.split("/")[1]));
        if(name != null) {
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            item.setItemMeta(meta);
        }
        item = NBTData.setNBT(item, nbt);
        return item;

    }
    /**
     * Creates a new item from a material
     * @param material the material of the item
     * @param name the name of the item
     * @param nbt any nbt values
     * @return a new itemstack
     */
    public static ItemStack createItem(Material material, String name, String... nbt){

        ItemStack item = new ItemStack(material);
        if(name != null) {
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            item.setItemMeta(meta);
        }
        item = NBTData.setNBT(item, nbt);
        return item;

    }
}
