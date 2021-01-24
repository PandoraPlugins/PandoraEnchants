package me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchantments;

import me.nanigans.pandoraenchants.Enchantments.EffectObject;
import me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchant;
import me.nanigans.pandoraenchants.Util.JsonUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Timer;
import java.util.TimerTask;

public class Vampire extends CustomEnchant implements Listener {
    public Vampire(int id) {
        super(id, "Vampire");
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event){

        if (event.getDamager() instanceof LivingEntity && event.getEntity() instanceof LivingEntity) {

            final LivingEntity damager = ((LivingEntity) event.getDamager());

            final ItemStack itemInHand = damager.getEquipment().getItemInHand();
            if (itemInHand != null && itemInHand.getType() != Material.AIR) {

                if(itemInHand.containsEnchantment(this)){

                    final int level = itemInHand.getEnchantmentLevel(this);

                    final EffectObject chance = effectData.get("chance");
                    if(calcChance(level, chance.getValue().doubleValue(), chance.isAmpEffect())){

                        final EffectObject timeOut = effectData.get("timeOut");
                        final long value = timeOut.getValue().longValue();
                        final long l = value - (timeOut.isAmpEffect() ? level*100 : 0);
                        final TimedHit timedHit = new TimedHit(damager, level);
                        final Timer timer = new Timer();
                        timer.schedule(timedHit, l);

                    }

                }

            }

        }

    }

    private final class TimedHit extends TimerTask{

        final LivingEntity ent;
        final EffectObject healAmt;
        final int level;
        public TimedHit(LivingEntity ent, int level){
            this.ent = ent;
            healAmt = effectData.get("healthAmt");
            this.level = level;
        }

        @Override
        public void run() {

            ent.setHealth(Math.min(ent.getMaxHealth(), ent.getHealth() + (healAmt.getValue().doubleValue() * (healAmt.isAmpEffect() ? level : 1))));
            this.cancel();
            soundData.get("onReceive").playSound(ent);
            msgData.get("toReceiver").sendMessage(ent);
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
