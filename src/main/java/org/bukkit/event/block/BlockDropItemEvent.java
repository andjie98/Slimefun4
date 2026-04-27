package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import java.util.List;

public class BlockDropItemEvent extends BlockEvent {

    private static final HandlerList handlers = new HandlerList();
    private final List<Item> items;

    public BlockDropItemEvent(Block block, Object blockState, org.bukkit.entity.Player player, List<Item> items) {
        super(block);
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
