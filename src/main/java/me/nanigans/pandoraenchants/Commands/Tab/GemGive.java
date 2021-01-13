package me.nanigans.pandoraenchants.Commands.Tab;

import me.nanigans.pandoraenchants.Util.JsonUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GemGive implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(command.getName().equals("gemenchant")){

            if(sender.hasPermission("Enchants.GiveGem")){

                if(args.length == 2){
                    return ((Map<String, Object>) JsonUtil.getData("Gems.json", null)).keySet().stream()
                            .filter(i -> !i.startsWith("_")).filter(i -> i.startsWith(args[1])).collect(Collectors.toList());
                }

            }

        }
        return null;
    }
}
