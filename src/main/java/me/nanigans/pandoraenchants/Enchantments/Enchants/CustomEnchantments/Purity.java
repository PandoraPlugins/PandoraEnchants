package me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchantments;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import me.nanigans.pandoraenchants.Enchantments.EnchantmentObject;
import me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchant;
import me.nanigans.pandoraenchants.Enchantments.EffectObject;
import me.nanigans.pandoraenchants.Enchantments.MessageObject;
import me.nanigans.pandoraenchants.Enchantments.SoundObject;
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
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Map;

public class Purity extends CustomEnchant implements Listener {
    private final Map<String, EffectObject> effectData;
    private final Map<String, SoundObject> soundData;
    private final Map<String, MessageObject> messageData;
    private final String name;

    public Purity(int id) {
        super(id);
        EnchantmentObject enchantData = new EnchantmentObject(JsonUtil.getData(file, "Purity"));

        name = JsonUtil.getData(file, "Purity.enchantData.name");
        effectData = enchantData.getEffects();
        soundData = enchantData.getSounds();
        messageData = enchantData.getMessages();
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event){

        if(event.getEntity() instanceof Player && event.getDamager() instanceof LivingEntity){

            final Player player = (Player) event.getEntity();

            final int level = containsEnchant(player.getInventory().getArmorContents(), this);
            if(level != -1){

                final EffectObject chance = effectData.get("chance");
                if(calcChance(level, chance.getValue().doubleValue(), chance.isAmpEffect())){
                    final EffectObject radInfo = effectData.get("radius");
                    final float val = radInfo.getValue().floatValue();
                    final double radius = val + (radInfo.isAmpEffect() ? val * level/10 : 0);
                    final List<Entity> nearbyEntities = player.getNearbyEntities(radius, radius, radius);
                    final FPlayer player1 = FPlayers.getInstance().getByPlayer(player);
                    final List<String> effects = (List<String>) effectData.get("clearEffects").getOther();
                    for (Entity nearbyEntity : nearbyEntities) {
                        if(nearbyEntity instanceof Player){

                            final Player p = (Player) nearbyEntity;
                            final FPlayer fPlayer = FPlayers.getInstance().getByPlayer(p);
                            if(fPlayer.getRelationTo(player1).isAlly()){
                                for (String effect : effects) {
                                    final PotionEffectType byName = PotionEffectType.getByName(effect);
                                    if(byName != null && p.hasPotionEffect(byName)){
                                        p.removePotionEffect(byName);
                                    }
                                }
                                soundData.get("soundGive").playSound(p);
                                messageData.get("toPlayersEffected").sendMessage(p);
                            }

                        }
                    }

                    soundData.get("soundGive").playSound(player);
                    messageData.get("toPlayerGiver").sendMessage(player);

                    if(((Boolean) effectData.get("includePlayer").getOther())){
                        for(String effect : effects){
                            final PotionEffectType byName = PotionEffectType.getByName(effect);
                            if(byName != null && player.hasPotionEffect(byName))
                            player.removePotionEffect(byName);
                        }
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
    public boolean conflictsWith(Enchantment other) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return false;
    }
}
