package me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchantments;

import me.nanigans.pandoraenchants.Enchantments.EffectObject;
import me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchant;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Swap extends CustomEnchant implements Listener {
    public Swap(int id) {
        super(id, "Swap");
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event){

        if(event.getEntity() instanceof LivingEntity && event.getDamager() instanceof LivingEntity){

            final LivingEntity damager = (LivingEntity) event.getDamager();
            final ItemStack hand = damager.getEquipment().getItemInHand();
            if (hand.containsEnchantment(this)) {
                final int level = hand.getEnchantmentLevel(this);

                final EffectObject chance = effectData.get("chance");
                if (calcChance(level, chance.getValue().doubleValue(), chance.isAmpEffect())) {

                    final LivingEntity entity = (LivingEntity) event.getEntity();
                    final Location location = entity.getLocation().clone();
                    final Location location1 = damager.getLocation().clone();
                    damager.teleport(location);
                    entity.teleport(location1);

                    soundData.get("toSwappedPlayers").playSound(entity);
                    soundData.get("toSwappedPlayers").playSound(damager);
                    msgData.get("toPlayersSwapped").sendMessage(damager, "player~"+entity.getName());
                    msgData.get("toPlayersSwapped").sendMessage(entity, "player~"+damager.getName());

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
