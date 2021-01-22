package me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchantments;

import me.nanigans.pandoraenchants.Enchantments.EffectObject;
import me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchant;
import me.nanigans.pandoraenchants.Util.JsonUtil;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemStack;


public class Anemi extends CustomEnchant implements Listener {
    public Anemi(int id) {
        super(id, JsonUtil.getData(file, "Anemi"));
    }

    @EventHandler
    public void onHungerLoose(FoodLevelChangeEvent event){

        final HumanEntity entity = event.getEntity();
        final int level = containsEnchant(entity.getEquipment().getArmorContents(), this);
        if (level != -1) {

            final EffectObject chance = effectData.get("chance");
            if (calcChance(level, chance.getValue().doubleValue(), chance.isAmpEffect())) {
                event.setCancelled(true);
            }

        }

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getMaxLevel() {
        return enchantData.getMaxLevel();
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
