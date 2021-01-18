package me.nanigans.pandoraenchants.Enchantments.Enchants;

import java.util.Map;

public final class EffectObject {

    private final Boolean ampEffect;
    private Number value;
    private Object other;

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

    public Object getOther() {
        return other;
    }
}
