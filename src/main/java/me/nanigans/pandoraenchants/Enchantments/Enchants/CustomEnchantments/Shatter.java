package me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchantments;

import me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchant;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.projectiles.ProjectileSource;

public class Shatter extends CustomEnchant implements Listener {
    public Shatter(int id) {
        super(id, "Shatter");
    }

    @EventHandler
    public void onBowShoot(ProjectileLaunchEvent event){

        if (event.getEntity() instanceof Arrow) {

            final ProjectileSource shooter = event.getEntity().getShooter();
            if (shooter instanceof LivingEntity) {
                final LivingEntity shooter1 = (LivingEntity) shooter;
                final ItemStack item = shooter1.getEquipment().getItemInHand();

                if(item.containsEnchantment(this)){
                    final int level = item.getEnchantmentLevel(this);
                    final Arrow entity = (Arrow) event.getEntity();
                    entity.setMetadata("Enchantment", new FixedMetadataValue(plugin, this.getId()+"~"+level));
                }
            }
        }
    }

    @EventHandler
    public void onBowHit(EntityDamageByEntityEvent event){

        if(event.getDamager() instanceof Arrow && event.getEntity() instanceof LivingEntity){


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
