package me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchantments;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.struct.Relation;
import me.nanigans.pandoraenchants.Enchantments.EffectObject;
import me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchant;
import me.nanigans.pandoraenchants.Util.JsonUtil;
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

public class Thrash extends CustomEnchant implements Listener {

    private final FPlayers instance = FPlayers.getInstance();

    public Thrash(int id) {
        super(id, JsonUtil.getData(file, "Thrash"));
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event){

        if(event.getDamager() instanceof Player && event.getEntity() instanceof LivingEntity){

            final Player damager = (Player) event.getDamager();
            final ItemStack itemInHand = damager.getItemInHand();
            if (itemInHand.containsEnchantment(this)) {
                final int level = itemInHand.getEnchantmentLevel(this);

                final EffectObject chance = effectData.get("chance");
                if (calcChance(level, chance.getValue().doubleValue(), chance.isAmpEffect())) {

                    final EffectObject radius = effectData.get("radius");
                    final double rad = radius.getValue().doubleValue() + (radius.isAmpEffect() ? level / 2D : 0);

                    final List<Entity> nearbyEntities = damager.getNearbyEntities(rad, rad, rad);
                    final FPlayer p = instance.getByPlayer(damager);
                    for (Entity nearbyEntity : nearbyEntities) {
                        if(nearbyEntity instanceof LivingEntity){

                            final LivingEntity ent = (LivingEntity) nearbyEntity;
                            if(ent instanceof Player) {
                                final Player player = (Player) ent;
                                final FPlayer fPlayer = instance.getByPlayer(player);
                                final Relation relationTo = p.getRelationTo(fPlayer);
                                if(relationTo == Relation.ENEMY || relationTo == Relation.NEUTRAL){
                                    player.damage(event.getDamage());
                                }

                            }else ent.damage(event.getDamage());

                        }
                    }

                }

            }

        }

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public int getMaxLevel() {
        return 0;
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
