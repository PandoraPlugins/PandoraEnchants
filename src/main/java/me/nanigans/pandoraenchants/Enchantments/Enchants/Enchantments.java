package me.nanigans.pandoraenchants.Enchantments.Enchants;

import me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchantments.*;

public enum Enchantments {

    Slum(new Slum(79)),
    Insomnia(new Insomnia(78)),
    Blind(new Blind(77)),
    Frozen(new Frozen(76)),
    Implants(new Implants(75));

    private final CustomEnchant enchant;

    Enchantments(CustomEnchant implants) {
        this.enchant = implants;
    }

    public static CustomEnchant getById(int id){
        for (Enchantments value : Enchantments.values()) {
            if(value.getEnchant().getId() == id)
                return value.getEnchant();
        }
        return null;
    }

    public CustomEnchant getEnchant() {
        return enchant;
    }
}
