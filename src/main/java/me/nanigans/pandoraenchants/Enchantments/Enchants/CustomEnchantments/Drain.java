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

import java.util.Map;

public class Drain extends CustomEnchant implements Listener {
    public Drain(int id) {
        super(id, "Drain");
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

                        final double damage = event.getDamage();
                        final EffectObject drainAmt = effectData.get("drainAmt");
                        Life_Steal.stealHealth(event, damager, level, drainAmt, damage, soundData.get("onDrain"), soundData.get("onReceive"), msgData.get("toReceiver"), msgData.get("toDrainer"));
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
