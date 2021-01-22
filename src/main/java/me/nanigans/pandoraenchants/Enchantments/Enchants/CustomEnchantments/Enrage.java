package me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchantments;

import me.nanigans.pandoraenchants.Enchantments.EffectObject;
import me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchant;
import me.nanigans.pandoraenchants.Util.JsonUtil;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class Enrage extends CustomEnchant implements Listener {
    public Enrage(int id) {
        super(id, JsonUtil.getData(file, "Enrage"));
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event){

        if(event.getEntity() instanceof LivingEntity && event.getDamager() instanceof LivingEntity){

            final LivingEntity damager = (LivingEntity) event.getDamager();
            final ItemStack itemInHand = damager.getEquipment().getItemInHand();
            final Map<Enchantment, Integer> enchantments = itemInHand.getEnchantments();
            if (enchantments.containsKey(this)) {
                final Integer level = enchantments.get(this);
                final EffectObject chance = effectData.get("chance");
                if (calcChance(level, chance.getValue().doubleValue(), chance.isAmpEffect())) {

                    final EffectObject lowHPObj = effectData.get("considerLowHP");
                    final double health = lowHPObj.getValue().doubleValue() + (lowHPObj.isAmpEffect() ? level : 0);

                    if(damager.getHealth() <= health){
                        final EffectObject damageAmp = effectData.get("damageAmp");
                        final int damage = damageAmp.getValue().intValue() + (damageAmp.isAmpEffect() ? level : 0);
                        event.setDamage(event.getDamage()+damage);
                    }

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
