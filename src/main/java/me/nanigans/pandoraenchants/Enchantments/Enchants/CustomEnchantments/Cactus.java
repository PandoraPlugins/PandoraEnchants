package me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchantments;

import me.nanigans.pandoraenchants.Enchantments.EffectObject;
import me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchant;
import org.apache.commons.lang.math.DoubleRange;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Cactus extends CustomEnchant implements Listener {
    public Cactus(int id) {
        super(id, "Cactus");
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event){

        if(event.getEntity() instanceof LivingEntity && event.getDamager() instanceof LivingEntity){

            final LivingEntity entity = (LivingEntity) event.getEntity();
            final int level = containsEnchant(entity.getEquipment().getArmorContents(), this);
            if(level != -1){

                final EffectObject chance = effectData.get("chance");

                if (calcChance(level, chance.getValue().doubleValue(), chance.isAmpEffect())) {

                    double damage = (Math.random() * 4);
                    final LivingEntity damager = (LivingEntity) event.getDamager();
                    damager.damage(damage);
                    soundData.get("onHit").playSound(damager);
                }

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
