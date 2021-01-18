package me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchantments;

import me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchant;
import me.nanigans.pandoraenchants.Enchantments.Enchants.EffectObject;
import me.nanigans.pandoraenchants.Enchantments.Enchants.TransformGUI;
import me.nanigans.pandoraenchants.Util.JsonUtil;
import net.minecraft.server.v1_8_R3.ItemFood;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Pig extends CustomEnchant implements Listener {
    private final Map<String, EffectObject> effectData;
    private final String name;
    private final static Map<UUID, Long> consumedTime = new HashMap<>();

    public Pig(int id) {
        super(id);
        effectData = convertMapToEffects(convertEffectsToMap(JsonUtil.getData(file, "Pig.effects")));
        name = JsonUtil.getData("Enchants.json", "Pig.enchantData.name");
    }

    @EventHandler
    public void onEat(PlayerInteractEvent event){

        final ItemStack item = event.getItem();
        if(item != null){

            if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR){

                if(CraftItemStack.asNMSCopy(item).getItem() instanceof ItemFood){
                    final Player player = event.getPlayer();

                    if (player.getFoodLevel() < 20 && containsEnchant(player.getInventory().getArmorContents(), this)) {
                        final int level = getLevelOfEnchant(player.getInventory().getArmorContents(), this);
                        final EffectObject chance = effectData.get("chance");
                        if (ThreadLocalRandom.current().nextDouble(100D) - (chance.isAmpEffect() ? 10D * level : 0) < chance.getValue().doubleValue()) {

                            final long cooldown = effectData.get("cooldown").getValue().longValue();
                            if (consumedTime.containsKey(player.getUniqueId())) {

                                final Long time = consumedTime.get(player.getUniqueId());
                                if (time <= System.currentTimeMillis()) {
                                    consumedTime.put(player.getUniqueId(), System.currentTimeMillis() + cooldown);
                                    feedPlayer(player, item);
                                }

                            } else {
                                consumedTime.put(player.getUniqueId(), System.currentTimeMillis() + cooldown);
                                feedPlayer(player, item);
                            }

                        }
                    }
                }

            }

        }

    }

    private static void feedPlayer(Player player, ItemStack item){

        final net.minecraft.server.v1_8_R3.ItemStack item1 = CraftItemStack.asNMSCopy(item);
        ItemFood itemFood = (ItemFood) item1.getItem();
        final int nutrition = itemFood.getNutrition(item1);
        player.setFoodLevel(player.getFoodLevel()+nutrition);
        player.getInventory().removeItem(item);
        TransformGUI.removeItem(item, player.getInventory());

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
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return false;
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
