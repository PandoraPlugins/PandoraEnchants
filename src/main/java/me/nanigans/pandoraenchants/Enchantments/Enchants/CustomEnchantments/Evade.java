package me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchantments;

import me.nanigans.pandoracombattag.Events.CombatLeaveEvent;
import me.nanigans.pandoraenchants.Enchantments.EffectObject;
import me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchant;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Evade extends CustomEnchant implements Listener {
    public Evade(int id) {
        super(id, "Evade");
    }

    @EventHandler
    public void onLeaveCombat(CombatLeaveEvent event){

        final Player player = event.getPlayer();
        final int level = containsEnchant(player.getEquipment().getArmorContents(), this);
        if (level != -1) {

            final EffectObject chance = effectData.get("chance");
            if (calcChance(level, chance.getValue().doubleValue(), chance.isAmpEffect())) {

                final EffectObject lowHealth = effectData.get("lowHealth");
                if(player.getHealth() <= lowHealth.getValue().doubleValue() + (lowHealth.isAmpEffect() ? level-1 : 0)){

                    int duration = (int) (effectData.get("duration").getValue().longValue()/1000*20);
                    final PotionEffect invis = new PotionEffect(PotionEffectType.INVISIBILITY, duration, 1);
                    player.addPotionEffect(invis);
                    soundData.get("onInvis").playSound(player);
                    msgData.get("toPlayer").sendMessage(player);

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
