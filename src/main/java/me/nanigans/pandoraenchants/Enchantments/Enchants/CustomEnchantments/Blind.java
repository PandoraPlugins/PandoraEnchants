package me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchantments;

import me.nanigans.pandoraenchants.Enchantments.EffectObject;
import me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchant;
import me.nanigans.pandoraenchants.Util.JsonUtil;
import org.bukkit.Material;
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

public class Blind extends CustomEnchant implements Listener {

    public Blind(int id) {
        super(id, "Blind");
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event){

        if(event.getEntity() instanceof LivingEntity && event.getDamager() instanceof LivingEntity){

            final LivingEntity entity = ((LivingEntity) event.getEntity());
            final LivingEntity damager = ((LivingEntity) event.getDamager());

            final ItemStack itemInHand = damager.getEquipment().getItemInHand();
            if(itemInHand != null && itemInHand.getType() != Material.AIR){

                final Map<Enchantment, Integer> enchantments = itemInHand.getEnchantments();
                if(enchantments.containsKey(this)){
                    final Integer level = enchantments.get(this);

                    final EffectObject chance = effectData.get("chance");
                    if(ThreadLocalRandom.current().nextDouble(100D) - (chance.isAmpEffect() ? level*10D : 0) < chance.getValue().doubleValue()){

                        getDuration(entity, level, effectData.get("duration"), effectData.get("blindness"));

                        soundData.get("onBlindReceive").playSound(entity);
                        msgData.get("onBlindReceive").sendMessage(entity, "player~"+damager.getName());
                        msgData.get("onBlindGiveTo").sendMessage(damager, "player~"+entity.getName());


                    }

                }

            }

        }

    }

    static void getDuration(LivingEntity entity, Integer level, EffectObject duration2, EffectObject blindness2) {
        final long length = duration2.getValue().longValue() / 1000 * 20;
        final PotionEffect effect = new PotionEffect(PotionEffectType.BLINDNESS, (int) (length + (duration2.isAmpEffect() ? length*(level/10) : 0)),
                blindness2.getValue().intValue() * (blindness2.isAmpEffect() ? level : 1));
        entity.addPotionEffect(effect);
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
