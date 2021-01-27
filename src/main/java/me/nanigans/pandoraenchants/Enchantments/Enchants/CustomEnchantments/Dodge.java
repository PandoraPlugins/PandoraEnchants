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

public class Dodge extends CustomEnchant implements Listener {
    public Dodge(int id) {
        super(id, "Dodge");
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event){

        if(event.getDamager() instanceof LivingEntity && event.getEntity() instanceof LivingEntity){

            final LivingEntity entity = (LivingEntity) event.getEntity();
            final int level = containsEnchant(entity.getEquipment().getArmorContents(), this);
            if (level != -1) {

                final EffectObject chance = effectData.get("chance");
                if (calcChance(level, chance.getValue().doubleValue(), chance.isAmpEffect())) {

                    event.setCancelled(true);
                    soundData.get("onDodge").playSound(entity);
                    final LivingEntity damager = (LivingEntity) event.getDamager();
                    msgData.get("toDodger").sendMessage(entity, "player~"+damager.getName());
                    msgData.get("toHitter").sendMessage(damager, "player~"+entity.getName());

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
