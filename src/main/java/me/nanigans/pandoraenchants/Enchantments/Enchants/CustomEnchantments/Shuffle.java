package me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchantments;

import me.nanigans.pandoraenchants.Enchantments.EffectObject;
import me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchant;
import me.nanigans.pandoraenchants.Util.JsonUtil;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Shuffle extends CustomEnchant implements Listener {
    public Shuffle(int id) {
        super(id, "Shuffle");
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event){

        if(event.getEntity() instanceof LivingEntity && event.getDamager() instanceof Player){

            final LivingEntity shuffler = (LivingEntity) event.getEntity();

            final int level = containsEnchant(shuffler.getEquipment().getArmorContents(), this);
            if(level != -1){

                final EffectObject chance = effectData.get("chance");
                if (calcChance(level, chance.getValue().doubleValue(), chance.isAmpEffect())) {
                    final Player shuffled = (Player) event.getDamager();

                    final PlayerInventory inv = shuffled.getInventory();
                    final List<ItemStack> items = new ArrayList<>(9);
                    for (int i = 0; i < 9; i++) {
                        final ItemStack item = inv.getItem(i);
                        items.add(item);
                    }
                    Collections.shuffle(items);
                    for (int i = 0; i < items.size(); i++) {
                        inv.setItem(i, items.get(i));
                    }

                    soundData.get("onReceive").playSound(shuffled);
                    soundData.get("onGive").playSound(shuffler);
                    msgData.get("toGiver").sendMessage(shuffler, "player~"+shuffled.getName());
                    msgData.get("toReceiver").sendMessage(shuffled, "player~"+shuffler.getName());

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
