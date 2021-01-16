package me.nanigans.pandoraenchants.Events;

import me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchant;
import me.nanigans.pandoraenchants.Util.NBTData;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class PlayerEvents implements Listener {

    @EventHandler
    public void onArmorEquip(PlayerArmorEquipEvent event) {

        final Player player = event.getPlayer();
        final ItemStack[] armorContents = player.getInventory().getArmorContents();
        final ItemStack item = event.getItemStack();

        final boolean containNBT = NBTData.containsNBT(item, CustomEnchant.nbtEnchanted);
        boolean canEquip = true;
        if (containNBT) {
            final String newArmorNBT = NBTData.getNBT(item, CustomEnchant.nbtEnchanted);
            for (ItemStack armorContent : armorContents) {
                if (armorContent != null) {
                    if (NBTData.containsNBT(armorContent, CustomEnchant.nbtEnchanted)) {
                        final String nbt = NBTData.getNBT(armorContent, CustomEnchant.nbtEnchanted);
                        if (nbt != null)
                            if (!nbt.equals(newArmorNBT)) {
                                canEquip = false;
                                break;
                            }
                    }
                }
            }
        }
        if (!canEquip) {
            event.setCancelled(true);
            player.closeInventory();
            final ItemStack[] armorContents1 = player.getInventory().getArmorContents();
            for (int i = 0; i < armorContents1.length; i++) {
                if (armorContents1[i].equals(item)) {
                    armorContents1[i] = null;
                }
            }
            player.getInventory().setArmorContents(armorContents1);
            player.getInventory().removeItem(item);
            if (!player.getInventory().addItem(item).isEmpty()) {
                player.getWorld().dropItemNaturally(player.getLocation(), item);
            }

            player.sendMessage(ChatColor.RED + "Cannot equip armor as it is not compatible with your other armor");

        }

    }

}
