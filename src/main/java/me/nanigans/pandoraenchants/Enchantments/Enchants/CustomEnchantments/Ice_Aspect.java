package me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchantments;

import me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchant;
import me.nanigans.pandoraenchants.Enchantments.EffectObject;
import me.nanigans.pandoraenchants.Util.JsonUtil;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Ice_Aspect extends CustomEnchant implements Listener {


    public Ice_Aspect(int id) {
        super(id, "Ice_Aspect");
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event){

        if(event.getEntity() instanceof LivingEntity && event.getDamager() instanceof LivingEntity){

            final LivingEntity damager = (LivingEntity) event.getDamager();
            final ItemStack itemInHand = damager.getEquipment().getItemInHand();
            if(itemInHand != null && itemInHand.getEnchantments().containsKey(this)) {
                final LivingEntity entity = (LivingEntity) event.getEntity();
                final Map<Enchantment, Integer> enchantments = itemInHand.getEnchantments();
                final Integer level = enchantments.get(this);

                final EffectObject chance = effectData.get("chance");
                if (ThreadLocalRandom.current().nextDouble(100D) - (chance.isAmpEffect() ? 10D*level : 0) < chance.getValue().doubleValue()){

                    final EffectObject slowness = effectData.get("slowness");
                    final EffectObject duration = effectData.get("duration");
                    int value = (int) (duration.getValue().longValue()/1000D * 20);
                    PotionEffect effect = new PotionEffect(PotionEffectType.SLOW, (value + (duration.isAmpEffect() ? level*10 : 0)),
                            slowness.getValue().intValue() * (slowness.isAmpEffect() ? level : 1));
                    entity.addPotionEffect(effect);
                    soundData.get("onReceive").playSound(entity);
                    msgData.get("toReceiver").sendMessage(entity, "player~"+damager.getName());
                    msgData.get("toGiver").sendMessage(damager, "player~"+entity.getName());

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
    public boolean conflictsWith(Enchantment other) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return false;
    }
}
