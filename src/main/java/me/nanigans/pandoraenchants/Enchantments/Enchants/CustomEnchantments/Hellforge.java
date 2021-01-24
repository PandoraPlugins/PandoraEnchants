package me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchantments;

import me.nanigans.pandoraenchants.Enchantments.EffectObject;
import me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchant;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class Hellforge extends CustomEnchant implements Listener {
    public Hellforge(int id) {
        super(id, "Hellforge");
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){

        final int level = containsEnchant(event.getPlayer().getEquipment().getArmorContents(), this);
        if(level != -1){

            final EffectObject chance = effectData.get("chance");
            if(calcChance(level, chance.getValue().doubleValue(), chance.isAmpEffect())){

                final Player player = event.getPlayer();
                final EffectObject repairAmt = effectData.get("repairAmt");
                final short value = (short) (repairAmt.getValue().shortValue() + (repairAmt.isAmpEffect() ? level*2 : 0));
                final ItemStack[] armorContents = player.getEquipment().getArmorContents();
                final ItemStack armorContent = armorContents[(int) (Math.random() * armorContents.length)];
                armorContent.setDurability((short) (armorContent.getDurability()+value));

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
