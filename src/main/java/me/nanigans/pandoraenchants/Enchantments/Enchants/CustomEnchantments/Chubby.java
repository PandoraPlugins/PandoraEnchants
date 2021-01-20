package me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchantments;

import com.inkzzz.spigot.armorevent.PlayerArmorUnequipEvent;
import me.nanigans.pandoraenchants.Enchantments.EffectObject;
import me.nanigans.pandoraenchants.Enchantments.EnchantmentObject;
import me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchant;
import me.nanigans.pandoraenchants.Util.JsonUtil;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Chubby extends CustomEnchant implements Listener {
    private final EnchantmentObject enchantData;
    private final String name;
    public Chubby(int id) {
        super(id);
        enchantData = new EnchantmentObject(JsonUtil.getData(file, "Chubby"));
        name = enchantData.getName();
    }

    @EventHandler
    public void onArmorRemove(PlayerArmorUnequipEvent event){

        final Player player = event.getPlayer();
        final int level = containsEnchant(player.getInventory().getArmorContents(), this);
        if(level != -1){

            final Map<String, EffectObject> effects = enchantData.getEffects();
            final EffectObject chance = effects.get("chance");
            if(calcChance(level, chance.getValue().doubleValue(), chance.isAmpEffect())){

                final EffectObject healthAmt1 = effects.get("healthAmt");
                final int healthAmt = healthAmt1.getValue().intValue() + (healthAmt1.isAmpEffect() ? level : 0);
                final EffectObject decayRate1 = effects.get("decayRate");
                final long decayRate = decayRate1.getValue().longValue() + (decayRate1.isAmpEffect() ? level*500 : 0);
                final EffectObject decayAmt = effects.get("decayAmt");
                final int decayVal = decayAmt.getValue().intValue();
                player.setMaxHealth(20+healthAmt);
                if(player.getHealth() == 20.0)
                    player.setHealth(player.getMaxHealth());
                final Decay decay = new Decay(decayVal, player);
                new Timer().schedule(decay, decayRate, decayRate);


            }

        }

    }

    private static final class Decay extends TimerTask {

        private final int amount;
        private final Player player;
        public Decay(int amount, Player player){
            this.amount = amount;
            this.player = player;

        }

        @Override
        public void run() {

            player.setMaxHealth(player.getMaxHealth()-amount);
            if(player.getMaxHealth() <= 20)
                this.cancel();

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
    public boolean conflictsWith(Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return false;
    }
}
