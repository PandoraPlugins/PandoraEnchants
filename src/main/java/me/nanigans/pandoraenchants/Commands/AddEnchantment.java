package me.nanigans.pandoraenchants.Commands;

import me.nanigans.pandoraenchants.Enchantments.Enchants.TransformGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddEnchantment implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equals("addcustomenchant")) {


            if (sender.hasPermission("Enchants.OpenGUI")) {

                if (sender instanceof Player) {
                    openGUI(((Player) sender));
                }else{
                    if(args.length > 0){

                        final Player player = Bukkit.getPlayerExact(args[0]);
                        if(player != null){
                            openGUI(player);
                        }else{
                            sender.sendMessage(ChatColor.RED+"Please specify a valid player");
                            return true;
                        }

                    }else{
                        sender.sendMessage(ChatColor.RED+"Please specify a player");
                        return true;
                    }
                }

            } else {
                sender.sendMessage(ChatColor.RED + "Invalid Permissions");
                return true;
            }

        }

        return false;
    }

    private void openGUI(Player player) {

        final TransformGUI transformGUI = new TransformGUI(player);


    }

}
