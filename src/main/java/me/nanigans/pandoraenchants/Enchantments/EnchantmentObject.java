package me.nanigans.pandoraenchants.Enchantments;

import me.nanigans.pandoraenchants.Util.JsonUtil;
import org.bukkit.enchantments.EnchantmentTarget;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchant.convertMapToEffects;

public class EnchantmentObject {
    private final Map<String, EffectObject> effects;
    private final Map<String, SoundObject> sounds;
    private final Map<String, MessageObject> messages;
    private final String name;
    private final int maxLevel;
    private final List<EnchantmentTarget> restrictArmor;

    public EnchantmentObject(Map<String, Object> enchantment){

        effects = convertMapToEffects(((Map<String, Map<String, Object>>) enchantment.get("effects")));

        sounds = getSounds((Map<String, Object>) enchantment.get("sounds"));
        messages = getMessages((Map<String, Object>) enchantment.get("messages"));
        name = JsonUtil.getFromMap(enchantment, "enchantData.name");
        maxLevel = JsonUtil.getFromMap(enchantment, "enchantData.maxLevel");

        restrictArmor = ((List<String>) JsonUtil.getFromMap(enchantment, "allowEnchanted")).stream()
                .map(EnchantmentTarget::valueOf).collect(Collectors.toList());


    }

    private static Map<String, SoundObject> getSounds(Map<String, Object> sounds){

        final Map<String, SoundObject> soundObj = new HashMap<>();
        if(sounds != null)
        sounds.forEach((i, j) -> soundObj.put(i, new SoundObject(((Map<String, Object>) j))));

        return soundObj;
    }

    private static Map<String, MessageObject> getMessages(Map<String, Object> messages){

        final Map<String, MessageObject> msgObj = new HashMap<>();
        if(messages != null)
        messages.forEach((i, j) -> msgObj.put(i, new MessageObject(j.toString())));

        return msgObj;

    }

    public List<EnchantmentTarget> getRestrictArmor() {
        return restrictArmor;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public String getName() {
        return name;
    }

    public Map<String, EffectObject> getEffects() {
        return effects;
    }

    public Map<String, SoundObject> getSounds() {
        return sounds;
    }

    public Map<String, MessageObject> getMessages() {
        return messages;
    }
}
