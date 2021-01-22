package me.nanigans.pandoraenchants.Enchantments.Enchants;

import me.nanigans.pandoraenchants.Enchantments.Enchants.CustomEnchantments.*;

public enum Enchantments {

    Swifter(new Swifter(93)),
    Disarmour(new Disarmour(92)),
    Shuffle(new Shuffle(91)),
    Drain(new Drain(90)),
    Blessed(new Blessed(89)),
    Vampire(new Vampire(88)),
    Judgement(new Judgement(87)),
    Lifebloom(new Lifebloom(86)),
    Chubby(new Chubby(85)),
    Purity(new Purity(84)),
    Clarity(new Clarity(83)),
    Pig(new Pig(82)),
    Ice_Aspect(new Ice_Aspect(81)),
    Reaper(new Reaper(80)),
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
