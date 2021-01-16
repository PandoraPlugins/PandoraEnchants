package me.nanigans.pandoraenchants;

import me.nanigans.pandoraenchants.Commands.AddEnchantment;
import me.nanigans.pandoraenchants.Commands.GemGive;
import me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchant;
import me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchantments.Implants;
import me.nanigans.pandoraenchants.Util.Glow;
import me.nanigans.pandoraenchants.Util.JsonUtil;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class PandoraEnchants extends JavaPlugin {

    private final List<CustomEnchant> enchantments = new ArrayList<>();

    @Override
    public void onEnable() {
        // Plugin startup logic

        enchantments.add(new Implants(75));
        registerEnchantment(new Glow(72));
        for (CustomEnchant enchantment : enchantments) {
            registerEnchantment(enchantment);
        }
        getCommand("gemenchant").setExecutor(new GemGive());
        getCommand("gemenchant").setTabCompleter(new me.nanigans.pandoraenchants.Commands.Tab.GemGive());
        getCommand("addcustomenchant").setExecutor(new AddEnchantment());
        JsonUtil.makeConfigFile(new File(getDataFolder(), "Gems.json"));
        JsonUtil.makeConfigFile(new File(getDataFolder(), "Enchants.json"));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        deregisterEnchantment(new Glow(72));
        for (CustomEnchant enchantment : enchantments) {
            deregisterEnchantment(enchantment);
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
