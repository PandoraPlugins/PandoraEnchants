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

import java.util.List;

public class Curse extends CustomEnchant implements Listener {
    public Curse(int id) {
        super(id, "Curse");
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event){

        if(event.getEntity() instanceof LivingEntity && event.getDamager() instanceof LivingEntity){

            final LivingEntity entity = (LivingEntity) event.getEntity();

            final int level = containsEnchant(entity.getEquipment().getArmorContents(), this);
            if (level != -1) {

                final EffectObject chance = effectData.get("chance");
                if (calcChance(level, chance.getValue().doubleValue(), chance.isAmpEffect())) {

                    final double health = entity.getHealth()-event.getDamage();
                    final EffectObject lowHP = effectData.get("lowHP");
                    if(health <= lowHP.getValue().doubleValue() - (lowHP.isAmpEffect() ? level : 0)){

                        final EffectObject potionEffects = effectData.get("potionEffects");
                        final List<String> effects = (List<String>) potionEffects.getOther();

                        final EffectObject duration = effectData.get("duration");
                        final int length = (int) ((duration.getValue().longValue() + (duration.isAmpEffect() ? level * 100L : 0))/1000*20);
                        for (String effect : effects) {

                            final PotionEffectType byName = PotionEffectType.getByName(effect);
                            if(byName != null){
                                final PotionEffect pEffect = new PotionEffect(byName, length, (potionEffects.isAmpEffect() ? level : 1));
                                entity.addPotionEffect(pEffect);
                            }
                        }
                        soundData.get("onReceive").playSound(entity);
                        msgData.get("toReceiver").sendMessage(entity);
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
