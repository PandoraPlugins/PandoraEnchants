package me.nanigans.pandoraenchants.Enchantments.Enchants;

import me.nanigans.pandoraenchants.Enchantments.Gems.Gems;
import me.nanigans.pandoraenchants.PandoraEnchants;
import me.nanigans.pandoraenchants.Util.ItemUtil;
import me.nanigans.pandoraenchants.Util.NBTData;
import net.minecraft.server.v1_8_R3.ItemArmor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransformGUI implements Listener {
    private final Player player;
    private final static PandoraEnchants plugin = PandoraEnchants.getPlugin(PandoraEnchants.class);
    private Inventory inv;
    final private ItemStack[] armorContent = new ItemStack[4];

    private final Map<String, ItemStack> placeHolders = new HashMap<String, ItemStack>(){{
        put("lime", ItemUtil.createItem("160/5", ChatColor.BLACK+""));
        put("red", ItemUtil.createItem("160/14", ChatColor.BLACK+""));
        put("blue", ItemUtil.createItem("160/3", ChatColor.BLACK+""));
        put("black", ItemUtil.createItem("160/8", ChatColor.BLACK+""));
    }};
    private final Map<String, Integer[]> itemPositions = new HashMap<String, Integer[]>(){{
        put("gem", new Integer[]{10});
        put("armor", new Integer[]{13, 14, 15, 16});
        put("output", new Integer[]{37, 39, 41, 43});
    }};

    public TransformGUI(Player player){
        this.player = player;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        player.openInventory(createInventory());
    }


    @EventHandler
    public void inventoryClick(InventoryClickEvent event){

        if(event.getWhoClicked().getUniqueId().equals(player.getUniqueId())){

            final ItemStack item = event.getCurrentItem();
            final Inventory clickedInventory = event.getClickedInventory();

            if(clickedInventory != null && item != null){
                player.playSound(player.getLocation(), Sound.valueOf("CLICK"), 1, 1);

                if(clickedInventory.equals(player.getInventory())){
                    if(event.getAction() != InventoryAction.PLACE_ALL)
                    event.setCancelled(true);
                    if(NBTData.containsNBT(item, Gems.nbt)){
                        final ItemStack clone = item.clone();
                        clone.setAmount(1);
                        removeItem(item, clickedInventory);
                        inv.setItem(itemPositions.get("gem")[0], clone);
                    }else if(isArmor(item)){
                        final ItemStack clone = item.clone();
                        clone.setAmount(1);
                        removeItem(item, clickedInventory);
                        checkArmor(clone);
                    }
                }

                if(clickedInventory.equals(this.inv)){

                    final List<Integer> positions = Arrays.asList(10, 13, 14, 15, 16);
                    if(!positions.contains(event.getSlot())){
                        event.setCancelled(true);
                    }
                    if(!event.isCancelled()){
                        if(item.getType() != Material.STAINED_GLASS) {
                            event.setCancelled(true);
                            inv.removeItem(item);
                            player.getInventory().addItem(item);
                        }else event.setCancelled(true);
                    }

                }

            }

        }

    }

    /**
     * Checks to see which type of armor it is and maps it into its respective slot
     * @param item the item to check
     */
    private void checkArmor(ItemStack item){

        switch (item.getType().toString().split("_")[1]) {
            case "HELMET":
                inv.setItem(itemPositions.get("armor")[0], item);
                break;
            case "CHESTPLATE":
                inv.setItem(itemPositions.get("armor")[1], item);
                break;
            case "LEGGINGS":
                inv.setItem(itemPositions.get("armor")[2], item);
                break;
            case "BOOTS":
                inv.setItem(itemPositions.get("armor")[3], item);
                break;

        }

    }

    private void removeItem(ItemStack item, Inventory inv){
        if(item.getAmount() == 1)
            inv.removeItem(item);
        else item.setAmount(item.getAmount()-1);

    }

    private static boolean isArmor(ItemStack item) {
        if(item.getType() == Material.AIR) return false;//check if item is armor
        return (CraftItemStack.asNMSCopy(item).getItem() instanceof ItemArmor);
    }

    /**
     * Creates the default inventory for when a player first opens this inventory
     * @return a new default inventory
     */
    private Inventory createInventory(){

        inv = Bukkit.createInventory(player, 54, "Enchantments");
        inv.setItem(10, ItemUtil.renameItem(placeHolders.get("black").clone(), ChatColor.GOLD+"Place Gem Here"));
        placeItem(13, 16, ItemUtil.renameItem(placeHolders.get("black").clone(), "Place Armor Here"), inv);
        inv.setItem(37, ItemUtil.renameItem(placeHolders.get("black"), "Output Helmet"));
        inv.setItem(39, ItemUtil.renameItem(placeHolders.get("black"), "Output Chestplate"));
        inv.setItem(41, ItemUtil.renameItem(placeHolders.get("black"), "Output Leggings"));
        inv.setItem(43, ItemUtil.renameItem(placeHolders.get("black"), "Output Boots"));
        inv.setItem(49,
                ItemUtil.setLore(ItemUtil.createItem(Material.NETHER_STAR, ChatColor.GREEN+"Confirm", "METHOD~confirm"), "Not Available"));
        setPlaceHolders();
        return inv;
    }

    /**
     * Sets the placeholders for the inventory
     */
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
