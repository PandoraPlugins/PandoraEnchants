package me.nanigans.pandoraenchants.Enchantments.Enchants;

import me.nanigans.pandoraenchants.Enchantments.Gems.Gems;
import me.nanigans.pandoraenchants.PandoraEnchants;
import me.nanigans.pandoraenchants.Util.NBTData;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CustomEnchant extends Enchantment{
    protected PandoraEnchants plugin;
    public static final String nbtEnchanted = "ENCHANTED";
    protected String file = "Enchants.json";

    public CustomEnchant(int id) {
        super(id);
        plugin = PandoraEnchants.getPlugin(PandoraEnchants.class);
    }


    /**
     * Converts effect data into a map, map, string object
     * @param data the data for effects
     * @return a map of the effects
     */
    protected Map<String, Map<String, Object>> convertEffectsToMap(Map<String, Object> data){

        final Map<String, Map<String, Object>> effects = new HashMap<>();

        for (Map.Entry<String, Object> effect : data.entrySet()) {
            final Map<String, Object> effectData = new HashMap<>();

            final Map<String, Object> stringObjectMap = (Map<String, Object>)effect.getValue();

            for (Map.Entry<String, Object> stringObjectEntry : stringObjectMap.entrySet()) {
                effectData.put(stringObjectEntry.getKey(), stringObjectEntry.getValue());
            }
            effects.put(effect.getKey(), effectData);
        }
        return effects;

    }

    protected boolean containsEnchant(ItemStack[] armorContents, CustomEnchant enchant){

        for (ItemStack armorContent : armorContents) {
            if(armorContent != null && armorContent.getType() != Material.AIR) {
                final Map<Enchantment, Integer> enchantments = armorContent.getEnchantments();
                return enchantments != null && enchantments.containsKey(enchant);
            }
        }
        return false;
    }

    protected int getLevelOfEnchant(ItemStack[] armorContents, CustomEnchant enchant){
        for (ItemStack armorContent : armorContents) {
            if(armorContent != null && armorContent.getType() != Material.AIR) {
                final Map<Enchantment, Integer> enchantments = armorContent.getEnchantments();//NBTData.getEnchantments(armorContent);
                return enchantments == null ? 1 : enchantments.get(enchant);
            }
        }
        return 1;
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
