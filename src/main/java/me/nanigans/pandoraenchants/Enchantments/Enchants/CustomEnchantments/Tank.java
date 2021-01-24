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

public class Tank extends CustomEnchant implements Listener {
    public Tank(int id) {
        super(id, "Tank");
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {

        if (event.getEntity() instanceof LivingEntity && event.getDamager() instanceof LivingEntity) {

            final LivingEntity hit = (LivingEntity) event.getEntity();
            final LivingEntity damager = (LivingEntity) event.getDamager();

            final ItemStack itemInHand = damager.getEquipment().getItemInHand();
            if (itemInHand != null && itemInHand.getType().toString().contains("AXE")) {

                final int level = containsEnchant(hit.getEquipment().getArmorContents(), this);
                final EffectObject chance = effectData.get("chance");
                if (level != -1 && calcChance(level, chance.getValue().doubleValue(), chance.isAmpEffect())) {

                    final double damage = event.getDamage();
                    final EffectObject ddPercent = effectData.get("damageDecreasePercent");
                    final double v = (ddPercent.isAmpEffect() ? ddPercent.getValue().doubleValue() * level : ddPercent.getValue().doubleValue());
                    event.setDamage(damage - (damage * (v / 100D)));
                    soundData.get("onSuppress").playSound(hit);
                    msgData.get("onSuppressed").sendMessage(hit);

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
