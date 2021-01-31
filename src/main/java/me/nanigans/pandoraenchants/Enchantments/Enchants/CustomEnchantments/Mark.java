package me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchantments;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.struct.Relation;
import me.nanigans.pandoraenchants.Enchantments.EffectObject;
import me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchant;
import me.nanigans.pandoraenchants.Enchantments.MessageObject;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Mark extends CustomEnchant implements Listener {

    private final static Map<UUID, MarkTimer> markedMap = new HashMap<>();
    private final FPlayers instance = FPlayers.getInstance();

    public Mark(int id) {
        super(id, "Mark");
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event){
        if(event.getEntity() instanceof LivingEntity && event.getDamager() instanceof LivingEntity){

            //marking process

            final LivingEntity damager = (LivingEntity) event.getDamager();
            final ItemStack item = damager.getEquipment().getItemInHand();
            if (item.containsEnchantment(this)) {

                final int level = item.getEnchantmentLevel(this);
                final EffectObject chance = effectData.get("chance");
                if(calcChance(level, chance.getValue().doubleValue(), chance.isAmpEffect())){

                    final EffectObject markDuration = effectData.get("markDuration");
                    final long duration = markDuration.getValue().longValue() + (markDuration.isAmpEffect() ? level* 100L : 0);

                    final Timer timer = new Timer();
                    final LivingEntity entity = (LivingEntity) event.getEntity();

                    if(!markedMap.containsKey(entity.getUniqueId())) {
                        final MarkTimer markTimer = new MarkTimer(entity, damager, level);
                        markedMap.put(entity.getUniqueId(), markTimer);
                        timer.schedule(markTimer, duration);
                    }else{
                        markedMap.get(entity.getUniqueId()).cancel();
                        final MarkTimer restart = new MarkTimer(entity, damager, level);
                        timer.schedule(restart, duration);
                        markedMap.put(entity.getUniqueId(), restart);
                    }
                    msgData.get("toMarked").sendMessage(entity, "player~"+damager.getName());
                    msgData.get("toMarker").sendMessage(damager, "player~"+entity.getName());
                    soundData.get("onMark").playSound(entity);

                }

            }

            //allies hit process

            final LivingEntity entity = (LivingEntity) event.getEntity();
            if(markedMap.containsKey(entity.getUniqueId())){

                final MarkTimer markTimer = markedMap.get(entity.getUniqueId());
                if(!markTimer.getMarker().equals(damager) && damager instanceof Player){
                    final LivingEntity marker = markTimer.getMarker();
                    if(marker instanceof Player && entity instanceof Player){
                        final Player mPlayer = (Player) marker;
                        final FPlayer byPlayer = instance.getByPlayer(mPlayer);
                        final FPlayer byPlayerDamager = instance.getByPlayer(((Player) damager));
                        final Relation relationTo = byPlayer.getRelationTo(byPlayerDamager);
                        if(relationTo == Relation.ALLY || relationTo == Relation.MEMBER){
                            final EffectObject damageIncrease = effectData.get("damageIncrease");
                            final int level = markTimer.getLevel();
                            event.setDamage(event.getDamage() +
                                    damageIncrease.getValue().doubleValue() + (damageIncrease.isAmpEffect() ? level/2D : 0));

                        }
                    }


                }

            }
        }

    }

    private final class MarkTimer extends TimerTask{

        private final LivingEntity marked;
        private final LivingEntity marker;
        private final MessageObject unMarkMsg = Mark.this.msgData.get("onUnMark");
        private final int level;

        public MarkTimer(LivingEntity marked, LivingEntity marker, int level) {
            this.marked = marked;
            this.marker = marker;
            this.level = level;
        }

        @Override
        public void run() {
                markedMap.remove(marked.getUniqueId());
                unMarkMsg.sendMessage(marked);
        }

        public int getLevel() {
            return level;
        }

        public LivingEntity getMarker() {
            return marker;
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
