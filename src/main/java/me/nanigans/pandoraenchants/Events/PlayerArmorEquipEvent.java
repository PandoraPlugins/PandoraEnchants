package me.nanigans.pandoraenchants.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public final class PlayerArmorEquipEvent extends Event implements Cancellable {
    private static final HandlerList HANDLER_LIST;
    private final Player player;
    private final ItemStack itemStack;
    private boolean isCancelled = false;

    public PlayerArmorEquipEvent(final Player player, final ItemStack itemStack) {
        super();
        this.player = player;
        this.itemStack = itemStack;
    }

    public Player getPlayer() {
        return this.player;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public HandlerList getHandlers() {
        return PlayerArmorEquipEvent.HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return PlayerArmorEquipEvent.HANDLER_LIST;
    }

    static {
        HANDLER_LIST = new HandlerList();
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = true;
    }
}