package me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchantments;

import me.nanigans.pandoraenchants.Enchantments.EffectObject;
import me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchant;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Reinforced extends CustomEnchant implements Listener {
    public Reinforced(int id) {
        super(id, "Reinforced");
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event){

        if(event.getEntity() instanceof LivingEntity && event.getDamager() instanceof LivingEntity){

            final LivingEntity hit = (LivingEntity) event.getEntity();
            final int level = containsEnchant(hit.getEquipment().getArmorContents(), this);
            if (level != -1) {

                final LivingEntity damager = (LivingEntity) event.getDamager();

                final Vector direction = hit.getLocation().getDirection();
                final Vector direction1 = damager.getLocation().getDirection();
                if(direction.dot(direction1) > 0){

                    final EffectObject decrease = effectData.get("percentTakenOff");
                    final double value = event.getDamage() * (decrease.getValue().doubleValue() + (decrease.isAmpEffect() ? level*2 : 0))/100;
                    event.setDamage(event.getDamage() - value);

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
