package me.nanigans.pandoraenchants.Enchantments.Enchants;

import me.nanigans.pandoraenchants.PandoraEnchants;
import org.bukkit.enchantments.Enchantment;

public abstract class CustomEnchant extends Enchantment{
    protected PandoraEnchants plugin;
    public CustomEnchant(int id) {
        super(id);
        plugin = PandoraEnchants.getPlugin(PandoraEnchants.class);
    }


}
