package me.nanigans.pandoraenchants.Enchantments.Enchants;

import me.nanigans.pandoraenchants.PandoraEnchants;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public abstract class CustomEnchant extends Enchantment{
    protected PandoraEnchants plugin;
    public CustomEnchant(int id) {
        super(id);
        plugin = PandoraEnchants.getPlugin(PandoraEnchants.class);
    }

    public static void addEnchantLore(ItemStack item, List<CustomEnchant> enchants, String level){

        final ItemMeta meta = item.getItemMeta();
        final List<String> lore = meta.getLore() == null ? new ArrayList<>() : meta.getLore();
        for (Enchantment enchant : enchants) {
            lore.add(0, ChatColor.GRAY+enchant.getName() + " " + level);
            if(NumberUtils.isNumber(level)) {//we determine if it is a random level and if it is then we use the magic color code to hide it
                final int level1 = Integer.parseInt(ChatColor.stripColor(level));
                meta.addEnchant(enchant, (int) (Math.random() * level1), true);
            }

        }

        meta.setLore(lore);
        item.setItemMeta(meta);

    }

}
