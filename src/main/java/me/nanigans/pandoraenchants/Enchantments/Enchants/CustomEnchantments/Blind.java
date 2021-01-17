package me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchantments;

import me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchant;
import me.nanigans.pandoraenchants.Enchantments.Enchants.EffectObject;
import me.nanigans.pandoraenchants.Util.JsonUtil;
import me.nanigans.pandoraenchants.Util.NBTData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Blind extends CustomEnchant implements Listener {
    Map<String, EffectObject> effectData;
    public Blind(int id) {
        super(id);
        effectData = convertMapToEffects(convertEffectsToMap(JsonUtil.getData("Effects.json", "Blind.effects")));
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event){

        if(event.getEntity() instanceof LivingEntity && event.getDamager() instanceof LivingEntity){

            final LivingEntity entity = ((LivingEntity) event.getEntity());
            final LivingEntity damager = ((LivingEntity) event.getDamager());

            final ItemStack itemInHand = damager.getEquipment().getItemInHand();
            if(itemInHand != null){

                final Map<Enchantment, Integer> enchantments = itemInHand.getEnchantments();
                if(enchantments.containsKey(this)){
                    final Integer level = enchantments.get(this);

                    final EffectObject chance = effectData.get("chance");
                    if(ThreadLocalRandom.current().nextDouble(100D) - (chance.isAmpEffect() ? level*10D : 0) < chance.getValue().doubleValue()){

                        final EffectObject duration = effectData.get("duration");
                        final long length = duration.getValue().longValue() / 1000 * 20;
                        final PotionEffect effect = new PotionEffect(PotionEffectType.BLINDNESS, (int) (length + (duration.isAmpEffect() ? length*(level/10) : 0)),
                                level);
                        entity.addPotionEffect(effect);

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
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return false;
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
