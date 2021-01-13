package me.nanigans.pandoraenchants.Enchantments.Gems;

import me.nanigans.pandoraenchants.Util.Glow;
import me.nanigans.pandoraenchants.Util.ItemUtil;
import me.nanigans.pandoraenchants.Util.JsonUtil;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;

public class Gems {
    public static final String nbt = "EGEM";

    public static ItemStack createGem(Map<String, Object> gemData, String name){

        final Map<String, Object> meta = ((Map<String, Object>) gemData.get("itemMeta"));
        final ItemStack gem = ItemUtil.createItem(meta.get("material").toString(), meta.get("displayName").toString(), nbt+"~"+name);
        final ItemMeta iMeta = gem.getItemMeta();
        if(meta.get("glow").toString().equals("true")){
            final Glow glow = new Glow(71);
            iMeta.addEnchant(glow, 1, true);
        }
        final List<String> lore = JsonUtil.getData("Gems.json", name+".itemMeta.lore");
        if(lore != null && lore.size() > 0){
            iMeta.setLore(lore);
        }
        gem.setItemMeta(iMeta);
        return gem;

    }

}
