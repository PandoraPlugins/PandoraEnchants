package me.nanigans.pandoraenchants;

import me.nanigans.pandoraenchants.Commands.AddEnchantment;
import me.nanigans.pandoraenchants.Commands.GemGive;
import me.nanigans.pandoraenchants.Enchantments.Enchants.Enchantments;
import me.nanigans.pandoraenchants.Events.EventAnalyser;
import me.nanigans.pandoraenchants.Events.PlayerEvents;
import me.nanigans.pandoraenchants.Util.Glow;
import me.nanigans.pandoraenchants.Util.JsonUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;

public final class PandoraEnchants extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        JsonUtil.makeConfigFile(new File(getDataFolder(), "Gems.json"));
        JsonUtil.makeConfigFile(new File(getDataFolder(), "Enchants.json"));

        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD+"REGISTERING ENCHANTMENTS");
        registerEnchantment(new Glow(72));
        for (Enchantments enchantment : Enchantments.values()) {
            registerEnchantment(enchantment.getEnchant());
            getServer().getPluginManager().registerEvents(((Listener) enchantment.getEnchant()), this);
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD+"ENCHANTMENTS REGISTERED");

        getServer().getPluginManager().registerEvents(new EventAnalyser(), this);
        getServer().getPluginManager().registerEvents(new PlayerEvents(), this);

        getCommand("gemenchant").setExecutor(new GemGive());
        getCommand("gemenchant").setTabCompleter(new me.nanigans.pandoraenchants.Commands.Tab.GemGive());
        getCommand("addcustomenchant").setExecutor(new AddEnchantment());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        deregisterEnchantment(new Glow(72));
        for (Enchantments enchantment : Enchantments.values()) {
            deregisterEnchantment(enchantment.getEnchant());
        }

    }

    private void deregisterEnchantment(Enchantment enchant){

        try {

            Field byIdField = Enchantment.class.getDeclaredField("byId");
            Field byNameField = Enchantment.class.getDeclaredField("byName");

            byIdField.setAccessible(true);
            byNameField.setAccessible(true);

            HashMap<Integer, Enchantment> byId = (HashMap<Integer, Enchantment>) byIdField.get(null);
            HashMap<Integer, Enchantment> byName = (HashMap<Integer, Enchantment>) byNameField.get(null);

            byId.remove(enchant.getId());

            if (byName.containsKey(enchant.getName())) {
                byName.remove(enchant.getName());
            }
        } catch (Exception ignored) {
        }

    }

    private void registerEnchantment(Enchantment enchant) {
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Enchantment.registerEnchantment(enchant);
        }
        catch (IllegalArgumentException ignored){
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}
