package me.nanigans.pandoraenchants.Enchantments.Enchants;

import me.nanigans.pandoraenchants.Enchantments.Gems.Gems;
import me.nanigans.pandoraenchants.PandoraEnchants;
import me.nanigans.pandoraenchants.Util.NBTData;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class CustomEnchant extends Enchantment{
    protected PandoraEnchants plugin;
    public static final String nbtEnchanted = "ENCHANTED";

    public CustomEnchant(int id) {
        super(id);
        plugin = PandoraEnchants.getPlugin(PandoraEnchants.class);
    }

    /**
     * Enchants the item and returns it
     *
     * Once enchants are added, we need to re-add it to the ItemStack itself because for some reason it doesn't work with ItemMeta the first time
     *
     * @param item the item to enchant
     * @param enchants the list of enchantments to add to it
     * @param itemType the type of the item
     * @param gem the gem to get the enchant data from
     * @return the newly enchanted item stack
     */
    public static ItemStack addEnchantLore(ItemStack item, List<CustomEnchant> enchants, String itemType, ItemStack gem){

        final ItemMeta meta = item.getItemMeta();
        final List<String> lore = meta.getLore() == null ? new ArrayList<>() : meta.getLore();
        for (Enchantment enchant : enchants) {
            String level = Gems.getEnchantLevel(gem, itemType, enchant.getName());
            lore.add(0, ChatColor.GRAY+enchant.getName() + " " + level);
            final int level1 = Integer.parseInt(ChatColor.stripColor(level));
            meta.addEnchant(enchant, level1, true);

        }

        meta.setLore(lore);
        item.setItemMeta(meta);
        for (Map.Entry<Enchantment, Integer> enchantmentIntegerEntry : meta.getEnchants().entrySet()) {
            item = NBTData.addEnchantLore(item, enchantmentIntegerEntry.getKey().getId(), enchantmentIntegerEntry.getValue());
        }
        return item;

    }

}
