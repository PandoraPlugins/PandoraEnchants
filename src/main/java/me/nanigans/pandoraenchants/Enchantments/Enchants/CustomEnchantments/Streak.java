package me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchantments;

import me.nanigans.pandoraenchants.Enchantments.EffectObject;
import me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchant;
import me.nanigans.pandoraenchants.Util.JsonUtil;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Streak extends CustomEnchant implements Listener {

    private final static Map<UUID, ComboTimer> hitNums = new HashMap<>();

    public Streak(int id) {
        super(id, JsonUtil.getData(file, "Streak"));
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event){

        if(event.getDamager() instanceof LivingEntity && event.getEntity() instanceof LivingEntity){

            final LivingEntity hitter = (LivingEntity) event.getDamager();
            final LivingEntity hit = (LivingEntity) event.getEntity();

            final ItemStack itemInHand = hitter.getEquipment().getItemInHand();
            if(itemInHand.containsEnchantment(this)) {
                final int level = itemInHand.getEnchantmentLevel(this);

                final EffectObject comboDuration = effectData.get("comboDuration");
                final long length = comboDuration.getValue().longValue() + (comboDuration.isAmpEffect() ? level * 100 : 0);
                if (!hitNums.containsKey(hitter.getUniqueId())) {
                    final ComboTimer value = new ComboTimer(hitter);
                    hitNums.put(hitter.getUniqueId(), value);
                    final Timer t = new Timer();
                    t.schedule(value, length);
                }else{

                    final ComboTimer comboTimer = hitNums.get(hitter.getUniqueId());
                    comboTimer.setNumHit(comboTimer.getNumHit()+1);
                    final ComboTimer restart = comboTimer.restart();
                    hitNums.replace(hitter.getUniqueId(), restart);
                    final Timer t = new Timer();
                    t.schedule(restart, length);
                }

                if (hitNums.containsKey(hit.getUniqueId())) {
                    final ComboTimer comboTimer = hitNums.get(hit.getUniqueId());
                    comboTimer.setNumHit(0);
                    comboTimer.cancel();
                }

                final ComboTimer comboTimer = hitNums.get(hitter.getUniqueId());
                if(comboTimer.getNumHit() >= effectData.get("comboHitNum").getValue().intValue()) {

                    final EffectObject damageInc = effectData.get("damageInc");
                    final double v = damageInc.getValue().doubleValue() + (damageInc.isAmpEffect() ? level : 0);
                    event.setDamage(v);
                    msgData.get("comboStreak").sendMessage(hitter, "hitNum~"+comboTimer.getNumHit());
                }

            }
        }

    }

    private static final class ComboTimer extends TimerTask{

        private int numHit = 0;
        private final LivingEntity entity;
        private boolean restarting = false;
        public ComboTimer(LivingEntity ent){
            this.entity = ent;
        }

        @Override
        public void run() {
            hitNums.remove(entity.getUniqueId());
            this.cancel();
        }

        @Override
        public boolean cancel() {
            if(!restarting) {
                hitNums.remove(entity.getUniqueId());
                numHit = 0;
            }
            return super.cancel();
        }

        public ComboTimer restart(){
            restarting = true;
            ComboTimer t = new ComboTimer(entity);
            t.setNumHit(numHit);
            this.cancel();
            return t;
        }

        public int getNumHit() {
            return numHit;
        }

        public void setNumHit(int numHit) {
            this.numHit = numHit;
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
