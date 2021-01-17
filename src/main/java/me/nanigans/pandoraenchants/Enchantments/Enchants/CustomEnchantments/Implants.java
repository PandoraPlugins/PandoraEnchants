package me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchantments;

import me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchant;
import me.nanigans.pandoraenchants.Enchantments.Enchants.EffectObject;
import me.nanigans.pandoraenchants.Util.JsonUtil;
import me.nanigans.pandoraenchants.Util.NBTData;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;


public class Implants extends CustomEnchant implements Listener {
    private final static List<UUID> usersToCheck = new ArrayList<>();
    final Map<String, EffectObject> effectData;
    private static String name;


    public Implants(int id) {
        super(id);
        effectData = convertMapToEffects(convertEffectsToMap(JsonUtil.getData("Enchants.json", "Implants.effects")));
        name = JsonUtil.getData("Enchants.json", "Implants.enchantData.name");
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event){

        if(event.getEntity() instanceof Player){

            final Player player = (Player) event.getEntity();
            if(!usersToCheck.contains(player.getUniqueId())){

                final ItemStack[] armorContents = player.getInventory().getArmorContents();
                for (ItemStack armorContent : armorContents) {
                    if (armorContent != null &&armorContent.getType() != Material.AIR) {
                        final Map<Enchantment, Integer> enchantments = NBTData.getEnchantments(armorContent);
                        if (enchantments != null) {
                            if (enchantments.containsKey(this)) {
                                usersToCheck.add(player.getUniqueId());
                                java.util.Timer timer = new java.util.Timer();
                                final Integer level = enchantments.get(this);
                                final EffectObject timeOut = effectData.get("timeOut");
                                final long delay = timeOut.getValue().longValue();
                                timer.schedule(new Timer(player, level), delay, (long) (delay -
                                                                        (timeOut.isAmpEffect() ? delay * (level/10D) : 0)));
                                return;
                            }
                        }
                    }
                }

            }

        }

    }

    private final class Timer extends TimerTask {

        private final Player player;
        private final int level;
        private final EffectObject healthHeal;
        private final EffectObject hungerHeal;

        public Timer(Player player, int level){
            this.player = player;
            this.level = level;
            healthHeal = effectData.get("healthHeal");
            hungerHeal = effectData.get("hungerHeal");
        }
        @Override
        public void run() {
            try {
                player.setHealth(player.getHealth() + healthHeal.getValue().doubleValue() + (healthHeal.isAmpEffect() ? level : 0));
                player.setFoodLevel((player.getFoodLevel() + hungerHeal.getValue().intValue() + (hungerHeal.isAmpEffect() ? level : 0)));
            }catch(IllegalArgumentException ignored){
                usersToCheck.remove(player.getUniqueId());
                this.cancel();
            }
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getStartLevel() {
        return 0;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ALL;
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
