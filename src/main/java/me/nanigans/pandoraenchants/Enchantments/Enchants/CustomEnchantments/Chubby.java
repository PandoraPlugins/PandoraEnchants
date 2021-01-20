package me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchantments;

import com.inkzzz.spigot.armorevent.PlayerArmorUnequipEvent;
import me.nanigans.pandoraenchants.Enchantments.EnchantmentObject;
import me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchant;
import me.nanigans.pandoraenchants.Util.JsonUtil;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class Chubby extends CustomEnchant implements Listener {
    private final EnchantmentObject enchantData;
    private final String name;
    public Chubby(int id) {
        super(id);
        enchantData = new EnchantmentObject(JsonUtil.getData(file, "Chubby"));
        name = JsonUtil.getData(file, "enchantData.name");
    }

    @EventHandler
    public void onArmorRemove(PlayerArmorUnequipEvent event){

        final Player player = event.getPlayer();
        final int level = containsEnchant(player.getInventory().getArmorContents(), this);
        if(level != -1){



        }

    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getMaxLevel() {
        return 0;
    }

    @Override
    public int getStartLevel() {
        return 0;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return null;
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return false;
    }
}
