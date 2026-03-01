package io.github.thebusybiscuit.slimefun4.libraries.dough.protection;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public interface ProtectionModule {
    
    String getName();
    
    boolean hasPermission(Player player, Location location, ProtectionAction action);
    
    boolean hasPermission(OfflinePlayer player, Location location, ProtectionAction action);
    
    boolean isEnabled();
    
    void register();
    
    void unregister();
    
    enum ProtectionAction {
        BREAK_BLOCK,
        PLACE_BLOCK,
        INTERACT_BLOCK,
        INTERACT_ENTITY,
        ACCESS_INVENTORY
    }
}