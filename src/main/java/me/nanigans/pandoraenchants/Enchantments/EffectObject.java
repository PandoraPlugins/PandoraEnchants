package me.nanigans.pandoraenchants.Enchantments;

import java.util.Map;

public final class EffectObject {

    private final Boolean ampEffect;
    private Number value;
    private Object other;
    private SoundObject sound;

    public EffectObject(Map<String, Object> effect){

        this.ampEffect = ((Boolean) effect.get("ampEffect"));
        if(effect.get("value") instanceof Number)
        this.value = ((Number) effect.get("value"));
        else this.other = effect.get("value");

    }

    public Boolean isAmpEffect() {
        return ampEffect;
    }

    public Number getValue() {
        return value;
    }

    /**
     * Gets the object that is in the 'value' section of the map. Use this if it is not a number
     * @return the object found in 'value'
     */
    public Object getOther() {
        return other;
    }

}
