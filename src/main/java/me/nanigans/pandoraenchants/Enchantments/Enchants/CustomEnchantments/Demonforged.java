package me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchantments;

import me.nanigans.pandoraenchants.Enchantments.EffectObject;
import me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchant;
import me.nanigans.pandoraenchants.Enchantments.Enchants.TransformGUI;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

public class Demonforged extends CustomEnchant implements Listener {
    public Demonforged(int id) {
        super(id, "Demonforged");
    }

    @EventHandler
    public void onDurabilityLoss(PlayerItemDamageEvent event){

        final Player player = event.getPlayer();
        if(player.getLastDamageCause() != null){
        final Entity entity = player.getLastDamageCause().getEntity();
        if(entity instanceof LivingEntity) {
            final LivingEntity damager = (LivingEntity) entity;

            if (TransformGUI.isArmor(event.getItem())) {

                final ItemStack item = damager.getEquipment().getItemInHand();
                if (item != null && item.getType() != Material.AIR) {

                    if (item.containsEnchantment(this)) {
                        final int level = item.getEnchantmentLevel(this);
                        final EffectObject chance = effectData.get("chance");
                        if (calcChance(level, chance.getValue().doubleValue(), chance.isAmpEffect())) {

                            final EffectObject durability = effectData.get("durabilityAmpPercent");

                            final int duraPercent = durability.getValue().intValue() + (durability.isAmpEffect() ? level * 5 : 0);
                            final int damage = event.getDamage();
                            final int damageAdd = damage + (damage * duraPercent / 100);
                            event.setDamage(damageAdd);
                            soundData.get("onDamage").playSound(player);

                        }
                    }

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
