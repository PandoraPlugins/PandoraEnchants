package me.nanigans.pandoraenchants.Commands;

import me.nanigans.pandoraenchants.Enchantments.Enchants.Enchantments;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadEnchants implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {

        if(command.getName().equals("enchantreload") && sender.hasPermission("Enchants.Reload")){

            sender.sendMessage(ChatColor.GOLD+"Reloading Enchantments...");
            for (Enchantments enchantment : Enchantments.values()) {
                enchantment.getEnchant().reloadEnchant();
            }
            sender.sendMessage(ChatColor.GREEN+"Enchantments Reloaded");
            return true;

        }

        return false;
    }
}
