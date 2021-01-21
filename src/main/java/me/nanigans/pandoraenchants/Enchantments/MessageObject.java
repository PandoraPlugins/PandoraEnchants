package me.nanigans.pandoraenchants.Enchantments;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;


public class MessageObject {

    private final String message;

    public MessageObject(String message){
        this.message = message;
    }

    /**
     * Sends a message with translated colors
     * @param player the player to send to
     */
    public void sendMessage(LivingEntity player, String... replacers){
        if(message != null){
            String msg = ChatColor.translateAlternateColorCodes('&', message);
            for (String replacer : replacers) {
                final String[] split = replacer.split("~");
                msg = msg.replaceAll("\\{"+split[0]+"}", split[1]);
            }
            player.sendMessage(msg);
        }
    }


    public String getMessages() {
        return message;
    }
}
