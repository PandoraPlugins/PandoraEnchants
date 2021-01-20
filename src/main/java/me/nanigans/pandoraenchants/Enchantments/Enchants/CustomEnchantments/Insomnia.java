package me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchantments;

import me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchant;
import me.nanigans.pandoraenchants.Enchantments.EffectObject;
import me.nanigans.pandoraenchants.Util.JsonUtil;
import org.bukkit.Material;
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

public class Insomnia extends CustomEnchant implements Listener {

    private final Map<String, EffectObject> effectData;
    private final String name;

    public Insomnia(int id) {
        super(id);
        effectData = convertMapToEffects(convertEffectsToMap(JsonUtil.getData(file, "Insomnia.effects")));
        name = JsonUtil.getData(file, "Insomnia.enchantData.name");
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event){

        if(event.getEntity() instanceof LivingEntity && event.getDamager() instanceof LivingEntity){

            final LivingEntity entity = ((LivingEntity) event.getEntity());
            final LivingEntity damager = ((LivingEntity) event.getDamager());

            final ItemStack itemInHand = damager.getEquipment().getItemInHand();
            if(itemInHand != null && itemInHand.getType() != Material.AIR){

                final Map<Enchantment, Integer> enchantments = itemInHand.getEnchantments();
                if(enchantments.containsKey(this)){
                    final Integer level = enchantments.get(this);

                    final EffectObject chance = effectData.get("chance");
                    if(ThreadLocalRandom.current().nextDouble(100D) - (chance.isAmpEffect() ? level*10D : 0) < chance.getValue().doubleValue()){

                        final EffectObject duration = effectData.get("duration");
                        final long length = duration.getValue().longValue() / 1000 * 20;
                        final EffectObject blindness = effectData.get("potionEffect");
                        final int duration1 = (int) (length + (duration.isAmpEffect() ? length * (level / 10) : 0));
                        final int amplifier = blindness.getValue().intValue() * (blindness.isAmpEffect() ? level : 1);
                        final PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, duration1,
                                amplifier);
                        final PotionEffect slowDig = new PotionEffect(PotionEffectType.SLOW_DIGGING, duration1,
                                amplifier);
                        final PotionEffect confusion = new PotionEffect(PotionEffectType.CONFUSION, duration1,
                                amplifier);
                        entity.addPotionEffect(slow);
                        entity.addPotionEffect(slowDig);
                        entity.addPotionEffect(confusion);


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
