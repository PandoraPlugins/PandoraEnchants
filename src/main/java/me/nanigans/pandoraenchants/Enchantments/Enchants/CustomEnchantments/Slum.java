package me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchantments;

import me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchant;
import me.nanigans.pandoraenchants.Enchantments.Enchants.EffectObject;
import me.nanigans.pandoraenchants.Util.JsonUtil;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Slum extends CustomEnchant implements Listener {
    private final Map<String, EffectObject> effectData;
    private final String name;
    public Slum(int id) {
        super(id);
        name = JsonUtil.getData("Enchants.json", "Slum.enchantData.name");
        effectData = convertMapToEffects(convertEffectsToMap(JsonUtil.getData("Enchants.json", "Slum.effects")));
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event){

        if(event.getDamager() instanceof LivingEntity && event.getEntity() instanceof LivingEntity){

            final LivingEntity damager = (LivingEntity) event.getDamager();
            final LivingEntity entity = (LivingEntity) event.getEntity();

            if(entity.getEquipment().getItemInHand() != null) {

                final Map<Enchantment, Integer> enchantments = damager.getEquipment().getItemInHand().getEnchantments();
                if (enchantments != null && enchantments.containsKey(this)){

                    final int level = enchantments.get(this);
                    final EffectObject chance = effectData.get("chance");
                    if(ThreadLocalRandom.current().nextDouble(100D) - (chance.isAmpEffect() ? 10D*level : 0) < chance.getValue().doubleValue()){

                        final List<String> clearEffects = (List<String>) effectData.get("clearEffects").getOther();
                        for (String clearEffect : clearEffects) {
                            final PotionEffectType byName = PotionEffectType.getByName(clearEffect);
                            if(byName != null)
                            entity.removePotionEffect(byName);
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
