package me.nanigans.pandoraenchants.Util;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagInt;
import net.minecraft.server.v1_8_R3.NBTTagList;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NBTData {

    public static ItemStack addEnchantLore(ItemStack item, int enchID, int level){

        final net.minecraft.server.v1_8_R3.ItemStack itemStack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound compound = (itemStack.hasTag() ? itemStack.getTag() : new NBTTagCompound());

        NBTTagList ench = new NBTTagList();
        NBTTagCompound enchant = new NBTTagCompound();
        enchant.set("id", new NBTTagInt(enchID));
        enchant.set("lvl", new NBTTagInt(level));
        ench.add(enchant);
        compound.set("ench", ench);
        itemStack.setTag(compound);
        return CraftItemStack.asBukkitCopy(itemStack);

    }


    public static boolean containsNBT(ItemStack item, String key){

        try {
            net.minecraft.server.v1_8_R3.ItemStack stack = CraftItemStack.asNMSCopy(item);

            if (stack.hasTag()) {
                NBTTagCompound tag = stack.getTag();

                if (tag != null) {

                    return tag.hasKey(key);

                } else return false;
            } else return false;

        }catch(Exception ignored){
            return false;
        }
    }

    public static String getNBT(ItemStack item, String key){

        if(containsNBT(item, key)){

            net.minecraft.server.v1_8_R3.ItemStack stack = CraftItemStack.asNMSCopy(item);

            return stack.getTag().get(key).toString().replaceAll("\"", "");

        }
        return null;
    }

    public static ItemStack removeNBT(ItemStack item, String key){

        if(containsNBT(item, key)){

            net.minecraft.server.v1_8_R3.ItemStack stack = CraftItemStack.asNMSCopy(item);

            stack.getTag().remove(key);
            if(stack.getTag().c().size() <= 0){
                stack.setTag(null);
            }
            item = CraftItemStack.asCraftMirror(stack);

        }
        return item;
    }

    /**
     * Sets item nbt data
     * @param item the item to set it to
     * @param keyValuePair a key value pair in the format like KEY~VAULE
     * @return the new itemstack with set nbt
     */
    public static ItemStack setNBT(ItemStack item, String... keyValuePair){

        net.minecraft.server.v1_8_R3.ItemStack stack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = stack.getTag() != null ? stack.getTag() : new NBTTagCompound();

        for (String s : keyValuePair) {
            String[] nbt = s.split("~");
            tag.setString(nbt[0], nbt[1]);
        }
        stack.setTag((tag));

        item = CraftItemStack.asCraftMirror(stack);

        return item;

    }


    public static Map<String, String> getAllNBT(ItemStack item){

        net.minecraft.server.v1_8_R3.ItemStack stack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = stack.getTag();

        if(tag != null){

            final Set<String> nbtKeys = tag.c();

            Map<String, String> nbtMap = new HashMap<>();

            for (String nbtKey : nbtKeys) {

                String data = getNBT(item, nbtKey);

                if(data != null)
                    nbtMap.put(nbtKey, data);

            }

            return nbtMap;

        }
        return null;

    }
}
