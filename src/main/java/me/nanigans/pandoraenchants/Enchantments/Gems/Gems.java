package me.nanigans.pandoraenchants.Enchantments.Gems;

import me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchant;
import me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchantments.Enchantments;
import me.nanigans.pandoraenchants.Util.Glow;
import me.nanigans.pandoraenchants.Util.ItemUtil;
import me.nanigans.pandoraenchants.Util.JsonUtil;
import me.nanigans.pandoraenchants.Util.NBTData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.stream.Collectors;

import static me.nanigans.pandoraenchants.Util.JsonUtil.getFromMap;

public class Gems {
    public static final String nbt = "EGEM";


    public static ItemStack createGem(Map<String, Object> gemData, String name){

        final Map<String, Object> meta = getFromMap(gemData, "itemMeta");
        ItemStack gem = ItemUtil.createItem(meta.get("material").toString(), meta.get("displayName").toString(), nbt+"~"+name);
        final ItemMeta iMeta = gem.getItemMeta();
        if(meta.get("glow").toString().equals("true")){
            final Glow glow = new Glow(72);
            iMeta.addEnchant(glow, 1, true);
        }
        final List<String> lore = getFromMap(meta, "lore");
        if(lore != null && lore.size() > 0){
            iMeta.setLore(lore);
        }

        gem.setItemMeta(iMeta);
        return gem;

    }

    public static String getEnchantLevel(ItemStack gem){

        if(NBTData.containsNBT(gem, nbt)){

            final String nbt = NBTData.getNBT(gem, Gems.nbt);

        }

    }

    public static Map<String, List<CustomEnchant>> getEnchantments(ItemStack gem){

        if(NBTData.containsNBT(gem, nbt)){

            final String nbt = NBTData.getNBT(gem, Gems.nbt);
            Map<String, Object> data = JsonUtil.getData("Gems.json", nbt + ".enchantmentsGiven");
            Map<String, List<CustomEnchant>> enchantList = new HashMap<String, List<CustomEnchant>>(){{put("HELMET",
                    Collections.EMPTY_LIST); put("CHESTPLATE", Collections.EMPTY_LIST); put("LEGGINGS", Collections.EMPTY_LIST);
                    put("BOOTS", Collections.EMPTY_LIST);}};

            if(data != null && data.size() > 0)
            for (String s : data.keySet()) {
                enchantList.put(s, ((List<String>) getFromMap(data, s + ".enchantments")).stream().map(i -> Enchantments.valueOf(i).getEnchant()).collect(Collectors.toList()));
            }
            return enchantList;
        }

        return null;
    }

}
