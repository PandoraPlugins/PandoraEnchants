package me.nanigans.pandoraenchants;

import me.nanigans.pandoraenchants.Commands.GemGive;
import me.nanigans.pandoraenchants.Util.Glow;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

public final class PandoraEnchants extends JavaPlugin {


    @Override
    public void onEnable() {
        // Plugin startup logic

        registerGlow();
        getCommand("gemenchant").setExecutor(new GemGive());


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerGlow() {
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Glow glow = new Glow(71);
            Enchantment.registerEnchantment(glow);
        }
        catch (IllegalArgumentException ignored){
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}
