package me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchantments;

import me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchant;

public enum Enchantments {

    Frozen(new Implants(76)),
    Implants(new Implants(75));

    private final CustomEnchant enchant;

    Enchantments(CustomEnchant implants) {
        this.enchant = implants;
    }

    public CustomEnchant getEnchant() {
        return enchant;
    }
}
