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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Savior extends CustomEnchant implements Listener {
    public Savior(int id) {
        super(id, "Savior");
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event){

        if(event.getEntity() instanceof LivingEntity){

            final LivingEntity entity = (LivingEntity) event.getEntity();
            final int level = containsEnchant(entity.getEquipment().getArmorContents(), this);
            if (level != -1) {

                final EffectObject chance = effectData.get("chance");
                if (calcChance(level, chance.getValue().doubleValue(), chance.isAmpEffect())) {

                    final EffectObject lowHealth = effectData.get("lowHealth");
                    if(entity.getHealth() <= lowHealth.getValue().doubleValue() + (lowHealth.isAmpEffect() ? level/2D : 0)){

                        final EffectObject duration = effectData.get("duration");
                        final EffectObject speedAmp = effectData.get("speedAmp");
                        final EffectObject absorptionAmp = effectData.get("absorptionAmp");
                        final int dur = (int) (duration.getValue().longValue()/1000*20 + (duration.isAmpEffect() ? level*100 : 0));
                        final PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, dur, speedAmp.getValue().intValue());
                        final PotionEffect absorp = new PotionEffect(PotionEffectType.ABSORPTION, dur, absorptionAmp.getValue().intValue());

                        entity.addPotionEffect(speed);
                        entity.addPotionEffect(absorp);

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
