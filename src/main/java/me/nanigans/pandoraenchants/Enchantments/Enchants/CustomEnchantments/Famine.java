package me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchantments;

import me.nanigans.pandoraenchants.Enchantments.EffectObject;
import me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchant;
import me.nanigans.pandoraenchants.Util.JsonUtil;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;

public class Famine extends CustomEnchant implements Listener {
    public Famine(int id) {
        super(id, "Famine");
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event){

        if(event.getEntity() instanceof HumanEntity && event.getDamager() instanceof LivingEntity){

            final HumanEntity receiver = (HumanEntity) event.getEntity();
            final LivingEntity damager = (LivingEntity) event.getDamager();

            final ItemStack itemInHand = damager.getEquipment().getItemInHand();
            final Map<Enchantment, Integer> enchantments = itemInHand.getEnchantments();
            if (enchantments.containsKey(this)) {
                final Integer level = enchantments.get(this);

                final EffectObject chance = effectData.get("chance");
                if (calcChance(level, chance.getValue().doubleValue(), chance.isAmpEffect())) {

                    final EffectObject duration = effectData.get("duration");
                    final int length = (int) (duration.getValue().longValue() / 1000 * 20);
                    final EffectObject amplifier = effectData.get("amplifier");
                    final PotionEffect potionEffect = new PotionEffect(PotionEffectType.HUNGER, length + (duration.isAmpEffect() ? level * 100 : 0),
                            amplifier.getValue().intValue() + (amplifier.isAmpEffect() ? level : 0));
                    receiver.addPotionEffect(potionEffect);

                    soundData.get("onReceive").playSound(receiver);
                    msgData.get("toReceiver").sendMessage(receiver, "player~"+damager.getName());
                    msgData.get("toGiver").sendMessage(damager, "player~"+receiver.getName());

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
