package me.nanigans.pandoraenchants.Enchantments.Enchants;

import me.nanigans.pandoraenchants.PandoraEnchants;
import me.nanigans.pandoraenchants.Util.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class TransformGUI implements Listener {
    private final Player player;
    private final static PandoraEnchants plugin = PandoraEnchants.getPlugin(PandoraEnchants.class);
    private Inventory inv;
    private final Map<String, ItemStack> placeHolders = new HashMap<String, ItemStack>(){{
        put("lime", ItemUtil.createItem("160/5", ChatColor.BLACK+""));
        put("red", ItemUtil.createItem("160/14", ChatColor.BLACK+""));
        put("blue", ItemUtil.createItem("160/3", ChatColor.BLACK+""));
        put("black", ItemUtil.createItem("160/8", ChatColor.BLACK+""));
    }};

    public TransformGUI(Player player){
        this.player = player;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        player.openInventory(createInventory());
    }


    public Inventory createInventory(){

        inv = Bukkit.createInventory(player, 54, "Enchantments");
        inv.setItem(10, ItemUtil.renameItem(placeHolders.get("black").clone(), ChatColor.GOLD+"Place Gem Here"));
        placeItem(13, 16, ItemUtil.renameItem(placeHolders.get("black").clone(), "Place Armor Here"), inv);
        inv.setItem(37, ItemUtil.renameItem(placeHolders.get("black"), "Output Helmet"));
        inv.setItem(39, ItemUtil.renameItem(placeHolders.get("black"), "Output Chestplate"));
        inv.setItem(41, ItemUtil.renameItem(placeHolders.get("black"), "Output Leggings"));
        inv.setItem(43, ItemUtil.renameItem(placeHolders.get("black"), "Output Boots"));
        setPlaceHolders();
        return inv;
    }

    private void setPlaceHolders(){

        placeItem(0, 2, placeHolders.get("lime"), inv);
        placeItem(3, 8, placeHolders.get("red"), inv);
        inv.setItem(9, placeHolders.get("lime"));
        inv.setItem(11, placeHolders.get("lime"));
        inv.setItem(12, placeHolders.get("red"));
        inv.setItem(17, placeHolders.get("red"));
        placeItem(18, 20, placeHolders.get("lime"), inv);
        placeItem(21, 26, placeHolders.get("red"), inv);
        placeItem(27, 35, placeHolders.get("blue"), inv);
        inv.setItem(36, placeHolders.get("blue"));
        inv.setItem(38, placeHolders.get("blue"));
        inv.setItem(40, placeHolders.get("blue"));
        inv.setItem(42, placeHolders.get("blue"));
        inv.setItem(44, placeHolders.get("blue"));
        placeItem(45, 53, placeHolders.get("blue"), inv);


    }

    private static void placeItem(int from, int to, ItemStack item, Inventory inv){
        for(int i = from; i <= to; i++){
            inv.setItem(i, item);
        }
    }

}
