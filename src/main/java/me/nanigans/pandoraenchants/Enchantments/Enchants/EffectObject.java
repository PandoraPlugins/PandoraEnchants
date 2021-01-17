package me.nanigans.pandoraenchants.Enchantments.Enchants;

import java.util.Map;

public final class EffectObject {

    private final Boolean ampEffect;
    private final Number value;

    public EffectObject(Map<String, Object> effect){

        this.ampEffect = ((Boolean) effect.get("ampEffect"));
        this.value = ((Number) effect.get("value"));

    }

    public Boolean isAmpEffect() {
        return ampEffect;
    }

    public Number getValue() {
        return value;
    }
}
