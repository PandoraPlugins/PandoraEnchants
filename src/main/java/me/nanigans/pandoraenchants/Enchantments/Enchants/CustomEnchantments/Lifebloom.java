package me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchantments;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.struct.Relation;
import me.nanigans.pandoraenchants.Enchantments.EffectObject;
import me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchant;
import me.nanigans.pandoraenchants.Util.JsonUtil;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Lifebloom extends CustomEnchant implements Listener {
    private final FPlayers instance = FPlayers.getInstance();

    public Lifebloom(int id) {
        super(id, "Lifebloom");
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDeath(PlayerDeathEvent event){

        final Player player = event.getEntity();
        final int level = containsEnchant(player.getInventory().getArmorContents(), this);
        if(level != -1){

            final EffectObject chance = effectData.get("chance");
            if (calcChance(level, chance.getValue().doubleValue(), chance.isAmpEffect())) {
                final FPlayer byPlayer = instance.getByPlayer(player);
                final EffectObject radInfo = effectData.get("radius");
                final float val = radInfo.getValue().floatValue();
                final double radius = val + (radInfo.isAmpEffect() ? val * level/5 : 0);
                final List<Entity> nearbyEntities = player.getNearbyEntities(radius, radius, radius);
                for (Entity nearbyEntity : nearbyEntities) {
                    if(nearbyEntity instanceof Player){

                        final Player nearbyPlayer = (Player) nearbyEntity;
                        final Relation relationTo = instance.getByPlayer(nearbyPlayer).getRelationTo(byPlayer);
                        if (relationTo == Relation.ALLY || relationTo == Relation.MEMBER) {
                            final double health = nearbyPlayer.getHealth();
                            final EffectObject healAmt = effectData.get("healAmt");
                            final double amt = healAmt.getValue().doubleValue();
                            final Boolean ampEffect = healAmt.isAmpEffect();
                            nearbyPlayer.setHealth(Math.min(player.getMaxHealth(), health+(amt*(ampEffect ? level : 1))));
                            soundData.get("onReceive").playSound(nearbyPlayer);
                            msgData.get("toReceiver").sendMessage(nearbyPlayer, "player~"+player.getName());

                        }

                    }
                }
                msgData.get("toGiver").sendMessage(player, "playerAmt~"+ (int) nearbyEntities.stream().filter(i -> i instanceof Player).count());

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
