package me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchantments;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.struct.Relation;
import me.nanigans.pandoraenchants.Enchantments.EffectObject;
import me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchant;
import me.nanigans.pandoraenchants.Enchantments.MessageObject;
import me.nanigans.pandoraenchants.Enchantments.SoundObject;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Cleave extends CustomEnchant implements Listener {

    private final FPlayers instance = FPlayers.getInstance();

    public Cleave(int id) {
        super(id, "Cleave");
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event){

        if(event.getEntity() instanceof LivingEntity && event.getDamager() instanceof Player){

            final Player damager = (Player) event.getDamager();

            final ItemStack item = damager.getEquipment().getItemInHand();
            if(item != null && item.getType() != Material.AIR){
                if (item.containsEnchantment(this)) {
                    final int level = item.getEnchantmentLevel(this);
                    final EffectObject chance = effectData.get("chance");
                    if (calcChance(level, chance.getValue().doubleValue(), chance.isAmpEffect())) {

                        final EffectObject radius = effectData.get("radius");
                        final double r = radius.getValue().doubleValue() + (radius.isAmpEffect() ? level/1.5 : 0);
                        final List<Entity> nearbyEntities = damager.getNearbyEntities(r, r, r);
                        final double damage1 = event.getDamage();
                        List<String> effectFaction = (List<String>) effectData.get("effectFaction").getOther();
                        final FPlayer fPlayer = instance.getByPlayer(damager);
                        final SoundObject onHit = soundData.get("onHit");
                        final MessageObject toDamaged = msgData.get("toDamaged");
                        final MessageObject toDamager = msgData.get("toDamager");
                        for (Entity nearbyEntity : nearbyEntities) {

                            if(nearbyEntity instanceof LivingEntity && !nearbyEntity.equals(event.getEntity())){

                                if(nearbyEntity instanceof Player){

                                    final Player p = (Player) nearbyEntity;
                                    final FPlayer byPlayer = instance.getByPlayer(p);
                                    for (String s : effectFaction) {
                                        final Relation relation = Relation.valueOf(s);
                                        if(fPlayer.getRelationTo(byPlayer) == relation){
                                            p.damage(damage1);
                                            onHit.playSound(p);
                                            toDamaged.sendMessage(p, "player~"+damager.getName());
                                        }
                                    }

                                }else{
                                    ((LivingEntity) nearbyEntity).damage(damage1);
                                }

                            }

                        }
                        toDamager.sendMessage(damager, "numEnts~"+nearbyEntities.size());


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
