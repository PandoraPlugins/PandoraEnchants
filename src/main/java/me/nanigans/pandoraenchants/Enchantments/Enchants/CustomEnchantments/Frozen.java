package me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchantments;

import me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchant;
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
import static me.nanigans.pandoraenchants.Util.JsonUtil.getFromMap;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Frozen extends CustomEnchant implements Listener {
    private final Map<String, Object> slowness;
    private final Map<String, Object> duration;
    private final Map<String, Object> chance;

    public Frozen(int id) {
        super(id);

        final Map<String, Map<String, Object>> effects = convertEffectsToMap(JsonUtil.getData(file, "Frozen.effects"));
        slowness = effects.get("slowness");
        duration = effects.get("duration");
        chance = effects.get("chance");
    }

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event){

        final Entity damager = event.getDamager();
        if (damager instanceof LivingEntity) {
            final LivingEntity livingEntity = (LivingEntity) damager;
            if (event.getEntity() instanceof LivingEntity) {
                final LivingEntity entity = (LivingEntity) event.getEntity();

                final ItemStack[] armorContents = entity.getEquipment().getArmorContents();

                if (armorContents != null && containsEnchant(armorContents, this)) {
                    final int levelOfEnchant = getLevelOfEnchant(armorContents, this);
                    if(ThreadLocalRandom.current().nextDouble(100) - (getFromMap(chance, "ampEffect") ? levelOfEnchant*10 : 0)
                            <= (double) getFromMap(chance, "value")){
                        final long value = (long) getFromMap(duration, "value")/1000*20;

                        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int) ((int) value +
                                                        (getFromMap(duration, "ampEffect") ? value * (levelOfEnchant/10D) : 0)),
                                ((Long) getFromMap(slowness, "value")).intValue() * (getFromMap(slowness, "ampEffect") ? levelOfEnchant : 1)));
                    }

                }

            }
        }

    }

    @Override
    public String getName() {
        return "Frozen";
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
