package me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchantments;

import me.nanigans.pandoraenchants.Enchantments.EffectObject;
import me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchant;
import me.nanigans.pandoraenchants.Util.JsonUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class Ravenous extends CustomEnchant implements Listener {
    public Ravenous(int id) {
        super(id, JsonUtil.getData(file, "Ravenous"));
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event){

        if(event.getEntity() instanceof LivingEntity && event.getDamager() instanceof Player){

            final Player damager = (Player) event.getDamager();

            final ItemStack itemInHand = damager.getEquipment().getItemInHand();
            if(itemInHand != null && itemInHand.getType() != Material.AIR){
                final Map<Enchantment, Integer> enchantments = itemInHand.getEnchantments();
                if(enchantments.containsKey(this)){
                    final Integer level = enchantments.get(this);

                    final EffectObject chance = effectData.get("chance");
                    if(calcChance(level, chance.getValue().doubleValue(), chance.isAmpEffect())){

                        final EffectObject hungerHeal = effectData.get("hungerHeal");
                        final int hunger = hungerHeal.getValue().intValue() + (hungerHeal.isAmpEffect() ? level : 0);
                        damager.setFoodLevel(damager.getFoodLevel()+hunger);
                        msgData.get("toReceiver").sendMessage(damager);
                        soundData.get("onReceive").playSound(damager);

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
