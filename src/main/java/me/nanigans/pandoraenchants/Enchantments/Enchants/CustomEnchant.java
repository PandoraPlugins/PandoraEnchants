package me.nanigans.pandoraenchants.Enchantments.Enchants;

import me.nanigans.pandoraenchants.Enchantments.Gems.Gems;
import me.nanigans.pandoraenchants.PandoraEnchants;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public abstract class CustomEnchant extends Enchantment{
    protected PandoraEnchants plugin;
    public static final String nbtEnchanted = "ENCHANTED";

    public CustomEnchant(int id) {
        super(id);
        plugin = PandoraEnchants.getPlugin(PandoraEnchants.class);
    }

    public static void addEnchantLore(ItemStack item, List<CustomEnchant> enchants, String itemType, ItemStack gem){

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

    }

}
