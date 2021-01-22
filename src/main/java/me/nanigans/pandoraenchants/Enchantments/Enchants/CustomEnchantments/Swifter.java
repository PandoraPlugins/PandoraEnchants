package me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchantments;

import me.nanigans.pandoraenchants.Enchantments.EffectObject;
import me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchant;
import me.nanigans.pandoraenchants.Util.JsonUtil;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Swifter extends CustomEnchant implements Listener {
    public Swifter(int id) {
        super(id, JsonUtil.getData(file, "Swifter"));
    }

    @EventHandler
    public void onHit(EntityDamageEvent event){

        if(event.getEntity() instanceof LivingEntity){

            final LivingEntity entity = (LivingEntity) event.getEntity();

            final int level = containsEnchant(entity.getEquipment().getArmorContents(), this);
            if (level != -1) {

                final EffectObject chance = effectData.get("chance");
                if (calcChance(level, chance.getValue().doubleValue(), chance.isAmpEffect())) {
                    final EffectObject duration = effectData.get("duration");
                    final int length = (int) (duration.getValue().longValue() / 1000 * 20);
                    final EffectObject amplifier = effectData.get("amplifier");
                    final PotionEffect potionEffect = new PotionEffect(PotionEffectType.SPEED, length + (duration.isAmpEffect() ? level * 100 : 0),
                            amplifier.getValue().intValue() + (amplifier.isAmpEffect() ? level : 0));
                    entity.addPotionEffect(potionEffect);

                    soundData.get("onReceive").playSound(entity);
                    msgData.get("toReceiver").sendMessage(entity);

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
