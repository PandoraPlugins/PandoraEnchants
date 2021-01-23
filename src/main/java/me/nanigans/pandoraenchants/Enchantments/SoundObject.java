package me.nanigans.pandoraenchants.Enchantments;

import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
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
        final Object sound = soundData.get("sound");
        if(sound != null) {
            this.sound = Sound.valueOf(sound.toString());
            volume = Float.parseFloat(soundData.get("volume").toString());
            pitch = Float.parseFloat(soundData.get("pitch").toString());
            final Object toPlayerOnly = soundData.get("toPlayerOnly");
            if(toPlayerOnly != null)
            this.toPlayerOnly = Boolean.parseBoolean(toPlayerOnly.toString());
            else this.toPlayerOnly = false;
        }else{
            this.sound = null;
            this.volume = 1.0F;
            this.pitch = 1.0F;
            this.toPlayerOnly = false;
        }

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
    /**
     * Convenience method for living entities. Casts it to player
     * Plays a sound if it is not null.
     * @param entity the player to play the sound for
     */
    public void playSound(LivingEntity entity){
        if(sound != null && entity instanceof Player){
            Player player = ((Player) entity);

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
