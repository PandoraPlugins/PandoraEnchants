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

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Clarity extends CustomEnchant implements Listener {
    private final Map<String, EffectObject> effectData;
    private final String name;
    public Clarity(int id) {
        super(id);
        effectData = convertMapToEffects(convertEffectsToMap(JsonUtil.getData(file, "Clarity.effects")));
        name = JsonUtil.getData(file, "Clarity.enchantData.name");
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {

        if (event.getEntity() instanceof LivingEntity) {

            final LivingEntity entity = (LivingEntity) event.getEntity();
            final ItemStack[] armorContents = entity.getEquipment().getArmorContents();
            if (containsEnchant(armorContents, this)) {
                final EffectObject chance = effectData.get("chance");
                final int level = getLevelOfEnchant(armorContents, this);
                if (ThreadLocalRandom.current().nextDouble(100D) - (chance.isAmpEffect() ? 10D * level : 0) < chance.getValue().doubleValue()) {
                    entity.removePotionEffect(PotionEffectType.BLINDNESS);
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
