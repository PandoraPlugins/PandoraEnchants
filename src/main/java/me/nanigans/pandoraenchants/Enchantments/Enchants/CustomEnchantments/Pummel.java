package me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchantments;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.struct.Relation;
import me.nanigans.pandoraenchants.Enchantments.EffectObject;
import me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchant;
import me.nanigans.pandoraenchants.Enchantments.MessageObject;
import me.nanigans.pandoraenchants.Enchantments.SoundObject;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Pummel extends CustomEnchant implements Listener {
    private final static Set<UUID> players = new HashSet<>();
    private final FPlayers instance = FPlayers.getInstance();

    public Pummel(int id) {
        super(id, "Pummel");
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event){

        if(event.getEntity() instanceof LivingEntity && event.getDamager() instanceof Player){

            final Player damager = (Player) event.getDamager();
            final ItemStack itemInHand = damager.getEquipment().getItemInHand();
            if(itemInHand.containsEnchantment(this)){
                final int level = itemInHand.getEnchantmentLevel(this);
                final EffectObject chance = effectData.get("chance");
                if (calcChance(level, chance.getValue().doubleValue(), chance.isAmpEffect())) {

                    final EffectObject radius = effectData.get("radius");
                    final double v = radius.getValue().doubleValue();
                    final List<Entity> nearbyEntities = damager.getNearbyEntities(v, v, v);
                    final FPlayer fPlayer = instance.getByPlayer(damager);

                    final EffectObject amplifier = effectData.get("amplifier");
                    final EffectObject duration = effectData.get("duration");
                    final int length = (int) (duration.getValue().longValue() + (duration.isAmpEffect() ? level * 100L : 0));
                    final PotionEffect potionEffect = new PotionEffect(PotionEffectType.SLOW,
                            length, amplifier.isAmpEffect() ? amplifier.getValue().intValue()+level : amplifier.getValue().intValue());

                    final SoundObject onReceive = soundData.get("onReceive");
                    final MessageObject toReceiver = msgData.get("toReceiver");
                    if(!onReceive.isToPlayerOnly() && nearbyEntities.size() > 0){
                        onReceive.playSound(damager);
                    }
                    for (Entity nearbyEntity : nearbyEntities) {

                        if(nearbyEntity instanceof Player){
                            final FPlayer byPlayer = instance.getByPlayer(((Player) nearbyEntity));

                            final Relation relationTo = fPlayer.getRelationTo(byPlayer);
                            if(relationTo == Relation.ENEMY || relationTo == Relation.NEUTRAL){
                                final Player player = byPlayer.getPlayer();
                                player.setSneaking(true);
                                player.setWalkSpeed(0.1F);
                                players.add(player.getUniqueId());
                                final DisableSneak disableSneak = new DisableSneak(player);
                                final Timer t = new Timer();
                                t.schedule(disableSneak, length);
                                onReceive.playSound(player);
                                toReceiver.sendMessage(player, "player~"+damager.getName());

                            }

                        }else if(nearbyEntity instanceof LivingEntity){
                            ((LivingEntity) nearbyEntity).addPotionEffect(potionEffect);
                        }

                    }
                    if(nearbyEntities.size() > 0)
                    msgData.get("toPummeller").sendMessage(damager, "numPlayers~"+nearbyEntities.size());

                }
            }

        }
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event){
        if(players.contains(event.getPlayer().getUniqueId())){
            event.setCancelled(true);
            event.getPlayer().setSneaking(true);
        }
    }


    private final class DisableSneak extends TimerTask{
        private final Player player;
        public DisableSneak(Player p){
            this.player = p;
        }


        @Override
        public void run() {
            players.remove(player.getUniqueId());
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.setSneaking(false);
                    player.setWalkSpeed(0.2F);
                }
            }.runTask(Pummel.this.plugin);

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
