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
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@FunctionalInterface
interface Methods{
    void execute();
}

public class TransformGUI implements Listener {
    private final Player player;
    private final static PandoraEnchants plugin = PandoraEnchants.getPlugin(PandoraEnchants.class);
    private static final List<Integer> positions = Arrays.asList(10, 13, 14, 15, 16);
    private Inventory inv;
    final private ItemStack[] armorContent = new ItemStack[4];
    private ItemStack gem;
    private final ItemStack confirmButton = ItemUtil.setLore(ItemUtil.createItem(Material.PAPER, ChatColor.GREEN+"Confirm?", "METHOD~confirmEnchant"),
            ChatColor.BLUE+"Status: "+ChatColor.RED+"Invalid");

    private final Map<String, ItemStack> placeHolders = new HashMap<String, ItemStack>(){{
        put("lime", ItemUtil.createItem("160/5", ChatColor.BLACK+""));
        put("red", ItemUtil.createItem("160/14", ChatColor.BLACK+""));
        put("blue", ItemUtil.createItem("160/3", ChatColor.BLACK+""));
        put("black", ItemUtil.createItem("160/8", ChatColor.BLACK+""));
    }};
    private final Map<String, Integer[]> itemPositions = new HashMap<String, Integer[]>(){{
        put("gem", new Integer[]{positions.get(0)});
        put("armor", new Integer[]{positions.get(1), positions.get(2), positions.get(3), positions.get(4)});
        put("output", new Integer[]{37, 39, 41, 43});
    }};

    private final Map<String, Methods> methods = new HashMap<String, Methods>(){{
        put("confirmEnchant", TransformGUI.this::confirmEnchant);
    }};

    private boolean status = false;

    public TransformGUI(Player player){
        this.player = player;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        player.openInventory(createInventory());
    }


    private void confirmEnchant(){

        if(status){



        }

    }

    private void executeMethod(String name){

        if(methods.containsKey(name))
            methods.get(name).execute();

    }

    /**
     * Handles inventory clicks
     * @param event inv click event
     */
    @EventHandler
    public void inventoryClick(InventoryClickEvent event){

        if(event.getWhoClicked().getUniqueId().equals(player.getUniqueId())){
            /*
            this will set the current inventory's item if it is a gem, it'll set it in the gem slot
            or the armor slot
             */

            final ItemStack item = event.getCurrentItem();
            final Inventory clickedInventory = event.getClickedInventory();

            if(clickedInventory != null && item != null){
                player.playSound(player.getLocation(), Sound.valueOf("CLICK"), 1, 1);

                if(clickedInventory.equals(player.getInventory())){
                    if(event.getAction() != InventoryAction.PLACE_ALL)
                    event.setCancelled(true);
                    if(NBTData.containsNBT(item, Gems.nbt)){

                        setGem(item, clickedInventory);

                    }else if(isArmor(item)){
                        final ItemStack clone = item.clone();
                        clone.setAmount(1);
                        removeItem(item, clickedInventory);
                        checkArmor(clone, true);
                    }
                }

                if(clickedInventory.equals(this.inv)){
                    /*
                    this will put the item in the clicked slot back into the players inventory
                     */
                    final List<Integer> positions = TransformGUI.positions;
                    if(!positions.contains(event.getSlot())){
                        event.setCancelled(true);
                    }
                    if(!event.isCancelled()){
                        event.setCancelled(true);
                        if(item.getType() != Material.STAINED_GLASS_PANE) {
                            inv.removeItem(item);
                            player.getInventory().addItem(item);

                            if(item.equals(this.gem)){//here we check if the gem is removed and it if is, we remove everything from the outputs
                                for (Integer output : itemPositions.get("output")) {
                                    inv.setItem(output, null);
                                }
                                gem = null;
                            }else {//check if armor is removed then we remove it from the output if it exists
                                final List<ItemStack> itemStacks = Arrays.asList(armorContent);
                                if(itemStacks.contains(item)){
                                    final int index = itemStacks.indexOf(item);
                                    armorContent[index] = null;
                                    inv.setItem(itemPositions.get("output")[index], null);
                                }
                            }
                        }
                    }

                }
                if(NBTData.containsNBT(item, "METHOD")){
                    this.executeMethod(NBTData.getNBT(item, "METHOD"));
                }

            }

        }

    }

    /**
     * When we close the inventory, we need to give the players items back and close the event
     * @param event inv close event
     */
    @EventHandler
    public void closeInv(InventoryCloseEvent event){

        for (Integer position : positions) {
            if (inv.getItem(position) != null && inv.getItem(position).getType() != Material.STAINED_GLASS_PANE) {
                player.getInventory().addItem(inv.getItem(position));
            }
        }
        HandlerList.unregisterAll(this);

    }

    /**
     * Prevents the player from picking up items while in the inventory
     * @param event entity pickup item event
     */
    @EventHandler
    public void pickUpItem(PlayerPickupItemEvent event){
        if(event.getPlayer().getUniqueId().equals(player.getUniqueId())){
            event.setCancelled(true);
        }
    }

    /**
     * Handles gem placement. Will replace whatever is in the inventory with a new gem and give the old gem back to the player
     * @param item the gem item
     * @param inventoryClicked the inventory that was clicked
     */
    private void setGem(ItemStack item, Inventory inventoryClicked){

        final ItemStack clone = item.clone();
        clone.setAmount(1);
        removeItem(item, inventoryClicked);
        final Integer[] gems = itemPositions.get("gem");
        if(inv.getItem(gems[0]) != null && inv.getItem(gems[0]).getType() != Material.STAINED_GLASS_PANE){
            player.getInventory().addItem(inv.getItem(gems[0]));
        }
        inv.setItem(gems[0], clone);
        gem = item;
        for (ItemStack itemStack : armorContent) {
            if(itemStack != null)
            checkArmor(itemStack, false);
        }

    }

    private void setEnchantments(ItemStack item, String itemType, int index){
        final ItemStack clone = item.clone();
        CustomEnchant.addEnchantLore(clone, Gems.getEnchantments(gem).get(itemType), itemType, gem);
        inv.setItem(itemPositions.get("output")[index], clone);
    }

    /**
     * Checks to see which type of armor it is and maps it into its respective slot
     * @param item the item to check
     * @param putBack
     */

    private void checkArmor(ItemStack item, boolean putBack){

        switch (item.getType().toString().split("_")[1]) {
            case "HELMET":
                if(putBack)
                putBack(item, 0);
                armorContent[0] = item;
                if(gem != null){
                    setEnchantments(item, "HELMET", 0);
                }
                break;
            case "CHESTPLATE":
                if(putBack)
                    putBack(item, 1);
                armorContent[1] = item;
                if(gem != null){
                    setEnchantments(item, "CHESTPLATE", 1);
                }
            break;
            case "LEGGINGS":
                if(putBack)
                    putBack(item, 2);
                armorContent[2] = item;
                if(gem != null){
                    setEnchantments(item, "LEGGINGS", 2);
                }
                break;
            case "BOOTS":
                if(putBack)
                    putBack(item, 3);
                armorContent[3] = item;
                if(gem != null){
                    setEnchantments(item, "BOOTS", 3);
                }
                break;
        }

    }

    private void putBack(ItemStack item, int indx){//puts the item in the slot back in their inventory if they try to replace it
        this.armorContent[indx] = item;
        final Integer armor = itemPositions.get("armor")[indx];
        if(inv.getItem(armor) != null && inv.getItem(armor).getType() != Material.STAINED_GLASS_PANE) {
            player.getInventory().addItem(inv.getItem(armor));
        }
        inv.setItem(armor, item);
    }

    private void removeItem(ItemStack item, Inventory inv){
        if(item.getAmount() == 1)//removes one item from an itemstack or removes it from the inventory
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
        setPlaceHolders();
        inv.setItem(49, confirmButton);

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
