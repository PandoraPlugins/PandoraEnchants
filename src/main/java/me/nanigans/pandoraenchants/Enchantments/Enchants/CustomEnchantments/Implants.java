package me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchantments;

import me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchant;
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
    private static int foodHeal;
    private static double healthHeal;
    private static long timeOut;
    private static boolean ampHeal;
    private static boolean ampFood;
    private static boolean ampTimeOut;
    private static String name;


    public Implants(int id) {
        super(id);
        final Object data = JsonUtil.getData("Enchants.json", "Implants.effects.hungerHeal.value");
        foodHeal = Integer.parseInt(data.toString());
        healthHeal = JsonUtil.getData("Enchants.json", "Implants.effects.healthHeal.value");
        timeOut = JsonUtil.getData("Enchants.json", "Implants.effects.timeOut.value");
        ampHeal = JsonUtil.getData("Enchants.json", "Implants.effects.healthHeal.ampEffect");
        ampFood = JsonUtil.getData("Enchants.json", "Implants.effects.hungerHeal.ampEffect");
        ampTimeOut = JsonUtil.getData("Enchants.json", "Implants.effects.timeOut.ampEffect");
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
                                timer.schedule(new Timer(player, level), timeOut, (long) (timeOut -
                                                                        (ampTimeOut ? timeOut * (level/10D) : 0)));
                                return;
                            }
                        }
                    }
                }

            }

        }

    }

    private static final class Timer extends TimerTask {

        private final Player player;
        private final int level;
        public Timer(Player player, int level){
            this.player = player;
            this.level = level;
        }
        @Override
        public void run() {
            try {
                player.setHealth(player.getHealth() + healthHeal + (ampHeal ? level : 0));
                player.setFoodLevel((player.getFoodLevel() + foodHeal+(ampFood ? level : 0)));
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
