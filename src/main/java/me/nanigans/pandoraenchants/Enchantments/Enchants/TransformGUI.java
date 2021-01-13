package me.nanigans.pandoraenchants.Enchantments.Enchants;

import me.nanigans.pandoraenchants.PandoraEnchants;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TransformGUI implements Listener {
    private final Player player;
    private final static PandoraEnchants plugin = PandoraEnchants.getPlugin(PandoraEnchants.class);
    private Inventory inv;

    public TransformGUI(Player player){
        this.player = player;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


    public Inventory createInventory(){

        final Inventory inv = Bukkit.createInventory(player, 54, "Enchantments");


        return null;
    }


    private static void placeItem(int from, int to, ItemStack item, Inventory inv){
        for(int i = from; i < to; i++){
            inv.setItem(i, item);
        }
    }

}
