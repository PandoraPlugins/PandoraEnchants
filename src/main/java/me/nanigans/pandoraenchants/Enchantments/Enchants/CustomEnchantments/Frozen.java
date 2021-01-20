package me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchantments;

import me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchant;
import me.nanigans.pandoraenchants.Enchantments.EffectObject;
import me.nanigans.pandoraenchants.Util.JsonUtil;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Frozen extends CustomEnchant implements Listener {
    final Map<String, EffectObject> effectData;

    private final String name;

    public Frozen(int id) {
        super(id);

        final Map<String, Map<String, Object>> effects = convertEffectsToMap(JsonUtil.getData(file, "Frozen.effects"));
        effectData = convertMapToEffects(effects);
        name = JsonUtil.getData(file, "Frozen.enchantData.name");
    }

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event){

        final Entity damager = event.getDamager();
        if (damager instanceof LivingEntity) {
            final LivingEntity livingEntity = (LivingEntity) damager;
            if (event.getEntity() instanceof LivingEntity) {
                final LivingEntity entity = (LivingEntity) event.getEntity();

                final ItemStack[] armorContents = entity.getEquipment().getArmorContents();

                final int level = containsEnchant(armorContents, this);
                if(level != -1){
                    final EffectObject chance = effectData.get("chance");

                    if(ThreadLocalRandom.current().nextDouble(100) - (chance.isAmpEffect() ? level*10 : 0)
                            <= chance.getValue().doubleValue()){
                        final EffectObject duration = effectData.get("duration");
                        final long value = duration.getValue().longValue()/1000*20;
                        final EffectObject slowness = effectData.get("slowness");

                        final PotionEffect effect = new PotionEffect(PotionEffectType.SLOW, (int) ((int) value +
                                (duration.isAmpEffect() ? value * (level / 10D) : 0)),
                                (slowness.getValue().intValue() * (slowness.isAmpEffect() ? level : 1)));
                        livingEntity.addPotionEffect(effect);
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
        return 5;
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
