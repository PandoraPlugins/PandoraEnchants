package me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchantments;

import me.nanigans.pandoraenchants.Enchantments.EffectObject;
import me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchant;
import me.nanigans.pandoraenchants.Enchantments.MessageObject;
import me.nanigans.pandoraenchants.Enchantments.SoundObject;
import me.nanigans.pandoraenchants.Util.JsonUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class Life_Steal extends CustomEnchant implements Listener {
    public Life_Steal(int id) {
        super(id, "Life_Steal");
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event){

        if(event.getDamager() instanceof LivingEntity && event.getEntity() instanceof LivingEntity){

            final LivingEntity damager = (LivingEntity) event.getDamager();

            final ItemStack itemInHand = damager.getEquipment().getItemInHand();
            if(itemInHand != null && itemInHand.getType() != Material.AIR){

                final Map<Enchantment, Integer> enchantments = itemInHand.getEnchantments();
                if (enchantments.containsKey(this)) {

                    final Integer level = enchantments.get(this);
                    final EffectObject chance = effectData.get("chance");
                    if (calcChance(level, chance.getValue().doubleValue(), chance.isAmpEffect())) {
                        final EffectObject drainAmt = effectData.get("takeAmt");

                        final double damage = event.getDamage() + drainAmt.getValue().doubleValue();
                        stealHealth(event, damager, level, drainAmt, damage, soundData.get("onDrain"), soundData.get("onReceive"), msgData.get("toReceiver"), msgData.get("toDrainer"));

                    }

                }

            }

        }

    }

    static void stealHealth(EntityDamageByEntityEvent event, LivingEntity damager, Integer level, EffectObject drainAmt, double damage, SoundObject onDrain, SoundObject onReceive, MessageObject toReceiver, MessageObject toDrainer) {
        damager.setHealth(Math.min(damager.getMaxHealth(),
                damager.getHealth()+(damage+(drainAmt.isAmpEffect() ? level/2D : 0))));

        final String name = event.getEntity().getName();
        final LivingEntity entity = (LivingEntity) event.getEntity();

        onDrain.playSound(damager);
        onReceive.playSound(entity);
        toReceiver.sendMessage(entity, "player~"+ damager.getName());
        toDrainer.sendMessage(damager, "player~"+name);
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
