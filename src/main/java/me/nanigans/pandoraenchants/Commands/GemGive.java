package me.nanigans.pandoraenchants.Commands;

import me.nanigans.pandoraenchants.Util.JsonUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class GemGive implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(command.getName().equals("gemenchant")){

            if(sender.hasPermission("Enchants.GiveGem")) {
                if (args.length > 1) {
                    Player player = Bukkit.getPlayerExact(args[0]);
                    if (player != null) {
                        final Map<String, Object> data = (Map<String, Object>) JsonUtil.getData("Gems.json", null);

                        if (data != null && data.containsKey(args[1])) {
                            final Map<String, Object> gem = ((Map<String, Object>) data.get(args[1]));


                        } else {
                            sender.sendMessage(ChatColor.RED + "Please specify a valid gem");
                            return true;
                        }

                    } else {
                        sender.sendMessage(ChatColor.RED + "Please specify a valid player");
                        return true;
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Please specify a player and a gem type");
                    return true;
                }
            }else{
                sender.sendMessage(ChatColor.RED+"Invalid Permissions");
            }

        }

        return false;
    }
}
