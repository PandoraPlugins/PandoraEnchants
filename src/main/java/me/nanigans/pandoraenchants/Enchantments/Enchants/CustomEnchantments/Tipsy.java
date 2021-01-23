package me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchantments;

import me.nanigans.pandoraenchants.Enchantments.EffectObject;
import me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchant;
import me.nanigans.pandoraenchants.Util.JsonUtil;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Tipsy extends CustomEnchant implements Listener {
    public Tipsy(int id) {
        super(id, JsonUtil.getData(file, "Tipsy"));
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event){

        if(event.getEntity() instanceof LivingEntity && event.getDamager() instanceof LivingEntity){

            final LivingEntity ent = (LivingEntity) event.getEntity();
            final int level = containsEnchant(ent.getEquipment().getArmorContents(), this);
            if(level != -1){

                final EffectObject chance = effectData.get("chance");
                if (calcChance(level, chance.getValue().doubleValue(), chance.isAmpEffect())) {

                    final EffectObject duration = effectData.get("duration");
                    final int length = (int) (duration.getValue().longValue() / 1000 * 20 + (duration.isAmpEffect() ? level*10 : 0));
                    final EffectObject strengthAmp = effectData.get("strengthAmp");
                    final PotionEffect strengthEffect = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, length, strengthAmp.getValue().intValue() + (strengthAmp.isAmpEffect() ? level : 0));
                    final EffectObject fatigueAmp = effectData.get("fatigueAmp");
                    final PotionEffect fatigueEffect = new PotionEffect(PotionEffectType.SLOW_DIGGING, length, fatigueAmp.getValue().intValue() + (fatigueAmp.isAmpEffect() ? level : 0));

                    ent.addPotionEffect(strengthEffect);
                    ent.addPotionEffect(fatigueEffect);
                    soundData.get("onReceive").playSound(ent);
                    msgData.get("toReceiver").sendMessage(ent);

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
