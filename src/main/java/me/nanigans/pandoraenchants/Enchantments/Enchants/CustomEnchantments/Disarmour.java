package me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchantments;

import me.nanigans.pandoraenchants.Enchantments.EffectObject;
import me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchant;
import me.nanigans.pandoraenchants.Util.JsonUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Disarmour extends CustomEnchant implements Listener {
    public Disarmour(int id) {
        super(id, JsonUtil.getData(file, "Disarmour"));
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event){

        if(event.getEntity() instanceof LivingEntity && event.getDamager() instanceof Player){

            final LivingEntity remover = (LivingEntity) event.getEntity();
            final int level = containsEnchant(remover.getEquipment().getArmorContents(), this);
            if(level != -1){

                final EffectObject chance = effectData.get("chance");
                if (calcChance(level, chance.getValue().doubleValue(), chance.isAmpEffect())) {

                    final EffectObject numberRemoved = effectData.get("numberRemoved");
                    final int numRemove = numberRemoved.getValue().intValue();
                    final int amount = numberRemoved.isAmpEffect() ? (int) Math.min(4, Math.random() * level) : numRemove;

                    final Player removeFrom = (Player) event.getDamager();
                    final ItemStack[] armorContents = removeFrom.getEquipment().getArmorContents();
                    final List<ItemStack> items = new ArrayList<>();
                    final int size = removeFrom.getInventory().all((Material) null).size();
                    final int removeAmt = Math.min(size, amount);
                    for (int i = 0; i < removeAmt; i++) {
                        final int indx = (int) (Math.random() * armorContents.length);
                        final ItemStack armorContent = armorContents[indx];
                        armorContents[indx] = null;
                        items.add(armorContent);
                    }
                    for (ItemStack item : items) {
                        removeFrom.getInventory().addItem(item);
                    }

                    soundData.get("onRemove").playSound(removeFrom);
                    msgData.get("toRemover").sendMessage(remover, "amount~"+removeAmt, "player~"+removeFrom.getName());
                    msgData.get("toRemoved").sendMessage(removeFrom, "amount~"+removeAmt, "player~"+remover.getName());

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
