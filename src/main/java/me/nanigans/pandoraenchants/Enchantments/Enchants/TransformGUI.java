package me.nanigans.pandoraenchants.Enchantments.Enchants;

import me.nanigans.pandoraenchants.Enchantments.Gems.Gems;
import me.nanigans.pandoraenchants.PandoraEnchants;
import me.nanigans.pandoraenchants.Util.ItemUtil;
import me.nanigans.pandoraenchants.Util.NBTData;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
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
import org.bukkit.inventory.meta.ItemMeta;

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
    private final HashMap<String, Integer> toolIndexes = new HashMap<String, Integer>() {{
        put("HELMET", 0);
        put("CHESTPLATE", 1);
        put("LEGGINGS", 2);
        put("BOOTS", 3);
        put("SWORD", 0);
        put("BOW", 1);
        put("AXE", 2);
        put("PICKAXE", 3);
        put("SPADE", 2);
        put("HOE", 3);
    }};
    private boolean returnAllItems = true;
    private Inventory inv;
    final private ItemStack[] enchantContents = new ItemStack[4];
    private ItemStack gem;
    private final ItemStack confirmButton = ItemUtil.createItem(Material.PAPER, ChatColor.GREEN+"Confirm?", "METHOD~confirmEnchant");

    private static final Map<String, ItemStack> placeHolders = new HashMap<String, ItemStack>(){{
        put("lime", ItemUtil.createItem("160/5", ChatColor.BLACK+""));
        put("red", ItemUtil.createItem("160/14", ChatColor.BLACK+""));
        put("blue", ItemUtil.createItem("160/3", ChatColor.BLACK+""));
        put("black", ItemUtil.createItem("160/8", ChatColor.BLACK+""));
    }};
    private static final Map<String, Integer[]> itemPositions = new HashMap<String, Integer[]>(){{
        put("gem", new Integer[]{positions.get(0)});
        put("armor", new Integer[]{positions.get(1), positions.get(2), positions.get(3), positions.get(4)});
        put("output", new Integer[]{37, 39, 41, 43});
    }};

    private final Map<String, Methods> methods = new HashMap<String, Methods>(){{
        put("confirmEnchant", TransformGUI.this::confirmEnchant);
    }};

    public TransformGUI(Player player){
        this.player = player;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        player.openInventory(createInventory());
    }


    /**
     * Enchants the armor in the <var>enchantedContents</var> to the enchantments that the gem contains
     * Will remove magic color to any enchants that are random
     */
    private void confirmEnchant(){

        if(gem != null) {
            for (Integer output : itemPositions.get("output")) {
                ItemStack enchantedItem = inv.getItem(output);
                if(enchantedItem != null && enchantedItem.getType() != Material.AIR) {
                    if (enchantedItem.getType() == Material.STAINED_GLASS_PANE) continue;
                    enchantedItem = NBTData.setNBT(enchantedItem, CustomEnchant.nbtEnchanted + "~" + Gems.getName(gem));
                    final ItemMeta meta = enchantedItem.getItemMeta();
                    final List<String> lore = meta.getLore();
                    if (lore != null && lore.stream().anyMatch(i -> i.contains(ChatColor.MAGIC.toString()))) {
                        for (int i = 0; i < lore.size(); i++) {
                            if (lore.get(i).contains(ChatColor.MAGIC.toString()))
                                lore.set(i, ChatColor.GRAY + ChatColor.stripColor(lore.get(i)));
                        }
                        meta.setLore(lore);
                        enchantedItem.setItemMeta(meta);
                    }
                    player.getInventory().addItem(enchantedItem);
                    returnAllItems = false;
                    player.closeInventory();
                }
            }
        }else{
            player.closeInventory();
            player.sendMessage(ChatColor.RED+"Please input a gem");//TODO: Add config for all messages and placeholder item names
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

                    }else if(isArmor(item) || isTool(item)){
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
                                final List<ItemStack> itemStacks = Arrays.asList(enchantContents);
                                if(itemStacks.contains(item)){
                                    final int index = itemStacks.indexOf(item);
                                    enchantContents[index] = null;
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
        returnAllItems(this.returnAllItems);
        HandlerList.unregisterAll(this);
    }

    private void returnAllItems(boolean force){
        if(force) {
            for (Integer position : positions) {
                if (inv.getItem(position) != null && inv.getItem(position).getType() != Material.STAINED_GLASS_PANE) {
                    player.getInventory().addItem(inv.getItem(position));
                }
            }
        }
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
        for (ItemStack itemStack : enchantContents) {
            if(itemStack != null)
            checkArmor(itemStack, false);
        }

    }

    /**
     * Sets the enchantment to the item and displays it in the output slots
     * Needs to remove the old enchant lore and update it to the new one
     * Needs to remove old enchantments and update it to the new ones
     * @param item the item to add the enchantment to
     * @param itemType the type of item i.e HELMET
     * @param index the index in which to add to enchantContent
     */
    private void setEnchantments(ItemStack item, String itemType, int index){
        ItemStack clone = item.clone();
        final ItemMeta meta = clone.getItemMeta();
        final Map<Enchantment, Integer> enchantments = NBTData.getEnchantments(clone);
        final List<String> lore = meta.getLore();
        if(enchantments != null) {
            final ItemStack[] finalClone = {clone};
            enchantments.forEach((i, j) -> {
                if (i instanceof CustomEnchant) {
                    finalClone[0].removeEnchantment(i);
                    finalClone[0] = NBTData.removeEnchant(finalClone[0], i.getId());
                    final String name = i.getName();
                    lore.remove(ChatColor.GRAY + name + " " + j);
                }
            });
        }
        meta.setLore(lore);
        clone.setItemMeta(meta);
        final List<CustomEnchant> enchants = Gems.getEnchantments(gem).get(itemType);
        clone = CustomEnchant.addEnchantLore(clone, enchants, itemType, gem);
        inv.setItem(itemPositions.get("output")[index], clone);
    }

    /**
     * Checks to see which type of armor it is and maps it into its respective slot
     * @param item the item to check
     * @param putBack wether or not to put the itemstack back into the players inventory if replaced
     */

    private void checkArmor(ItemStack item, boolean putBack){

        Map<String, Integer> toolIndexes = this.toolIndexes;
        final String itemName = item.getType().toString().split("_")[1];

        final Integer index = toolIndexes.get(itemName);
        if(putBack)
            putBack(item, index);
        enchantContents[index] = item;
        if(gem != null){
            setEnchantments(item, itemName, index);
        }

    }

    /**
     * Puts the item in the slot (indx) back in their inventory if they are trying to replace it
     * @param item the item to replace
     * @param indx the index mapped to enchantContent
     */
    private void putBack(ItemStack item, int indx){
        this.enchantContents[indx] = item;
        final Integer armor = itemPositions.get("armor")[indx];
        if(inv.getItem(armor) != null && inv.getItem(armor).getType() != Material.STAINED_GLASS_PANE) {
            player.getInventory().addItem(inv.getItem(armor));
        }
        inv.setItem(armor, item);
    }

    /**
     * removes one item from an itemstack or removes it from the inventory if it has an amount of 1
     * @param item the item to remove
     * @param inv the inventory to possible remove from
     */
    public static void removeItem(ItemStack item, Inventory inv){
        if(item.getAmount() == 1)
            inv.removeItem(item);
        else item.setAmount(item.getAmount()-1);

    }

    /**
     * checks to see if an itemstack is a tool
     * @param item the item to check
     * @return if it is a tool or not
     */
    private static boolean isTool(ItemStack item){
        if(item.getType() == Material.AIR) return false;
        final Item item1 = CraftItemStack.asNMSCopy(item).getItem();
        return item1 instanceof ItemSword || item1 instanceof ItemAxe || item1 instanceof ItemSpade || item1 instanceof ItemBow || item1 instanceof ItemPickaxe;
    }

    /**
     * checks if an itemstack is armor
     * @param item the item stack to check
     * @return if it is armor or not
     */
    public static boolean isArmor(ItemStack item) {
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
