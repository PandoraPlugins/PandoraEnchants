package me.nanigans.pandoraenchants.Enchantments;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Map;

public class SoundObject {

    private final Sound sound;
    private final float volume;
    private final float pitch;
    private final boolean toPlayerOnly;

    public SoundObject(Map<String, Object> soundData){
        if(soundData == null) {
            sound = null;
            volume = 0;
            pitch = 0;
            toPlayerOnly = false;
            return;
        }
        sound = Sound.valueOf(soundData.get("soundName").toString());
        volume = Float.parseFloat(soundData.get("volume").toString());
        pitch = Float.parseFloat(soundData.get("pitch").toString());
        toPlayerOnly = Boolean.parseBoolean(soundData.get("toPlayerOnly").toString());

    }

    /**
     * Plays a sound if it is not null.
     * @param player the player to play the sound for
     */
    public void playSound(Player player){
        if(sound != null){
            if(toPlayerOnly)
                player.playSound(player.getLocation(), sound, volume, pitch);
            else player.getWorld().playSound(player.getLocation(), sound, volume, pitch);
        }
    }

    public boolean isToPlayerOnly() {
        return toPlayerOnly;
    }

    public float getPitch() {
        return pitch;
    }

    public Sound getSound() {
        return sound;
    }

    public float getVolume() {
        return volume;
    }
}
