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

public class Shockwave extends CustomEnchant implements Listener {

    public Shockwave(int id) {
        super(id, "Shockwave");
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event){
        if(event.getEntity() instanceof LivingEntity && event.getDamager() instanceof LivingEntity){

            final LivingEntity entity = (LivingEntity) event.getEntity();
            final int level = containsEnchant(entity.getEquipment().getArmorContents(), this);
            if(level != -1){

                final EffectObject chance = effectData.get("chance");
                if (calcChance(level, chance.getValue().doubleValue(), chance.isAmpEffect())) {

                    final LivingEntity damager = (LivingEntity) event.getDamager();

                    final EffectObject knockBackAmp = effectData.get("knockBackAmp");
                    final double amp = knockBackAmp.getValue().doubleValue() + (knockBackAmp.isAmpEffect() ? level/3D : 0);
                    final Vector direction = damager.getLocation().getDirection().normalize().multiply(-amp);
                    damager.setVelocity(direction);
                    soundData.get("toDamager").playSound(entity);
                    msgData.get("toDamager").sendMessage(damager, "player~"+entity.getName());
                    msgData.get("toHit").sendMessage(entity, "player~"+damager.getName());
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
