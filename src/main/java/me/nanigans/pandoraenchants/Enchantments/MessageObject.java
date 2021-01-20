package me.nanigans.pandoraenchants.Enchantments;

import org.bukkit.ChatColor;
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
    public void sendMessage(Player player){
        if(message != null){
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }

    public String getMessages() {
        return message;
    }
}
