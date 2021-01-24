package me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchantments;

import me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchant;
import me.nanigans.pandoraenchants.Enchantments.EffectObject;
import me.nanigans.pandoraenchants.Util.JsonUtil;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.ThreadLocalRandom;

public class Clarity extends CustomEnchant implements Listener {

    public Clarity(int id) {
        super(id, "Clarity");

    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {

        if (event.getEntity() instanceof LivingEntity) {

            final LivingEntity entity = (LivingEntity) event.getEntity();
            final ItemStack[] armorContents = entity.getEquipment().getArmorContents();
            final int level = containsEnchant(armorContents, this);
            if(level != -1){
                final EffectObject chance = effectData.get("chance");
                if (ThreadLocalRandom.current().nextDouble(100D) - (chance.isAmpEffect() ? 10D * level : 0) < chance.getValue().doubleValue()) {
                    entity.removePotionEffect(PotionEffectType.BLINDNESS);
                    soundData.get("onReceive").playSound(entity);
                    msgData.get("toReceiver").sendMessage(entity);
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
    public boolean conflictsWith(Enchantment other) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return false;
    }
}
