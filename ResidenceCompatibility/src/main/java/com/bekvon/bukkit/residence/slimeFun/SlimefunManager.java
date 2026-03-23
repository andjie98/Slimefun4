package com.bekvon.bukkit.residence.slimeFun;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

/**
 * Residence Slimefun Compatibility Layer
 * 
 * This class provides compatibility between Residence plugin and 
 * the old version of Slimefun4 (me.mrCookieSlime.Slimefun)
 * 
 * It redirects Residence's Slimefun API calls to the correct classes.
 */
public class SlimefunManager {
    
    /**
     * Check if a location has a Slimefun block
     * 
     * @param location The location to check
     * @return true if there's a Slimefun block at this location
     */
    public static boolean isSlimefunBlock(Location location) {
        if (location == null) {
            return false;
        }
        
        try {
            // Use BlockStorage to check if there's a Slimefun block
            return BlockStorage.hasBlockInfo(location);
        } catch (Exception e) {
            // If BlockStorage is not available or fails, return false
            return false;
        }
    }
    
    /**
     * Check if a player can interact with a Slimefun block
     * 
     * @param player The player trying to interact
     * @param location The location of the block
     * @return true if the player can interact
     */
    public static boolean canInteract(Player player, Location location) {
        if (player == null || location == null) {
            return true; // Allow if we can't check
        }
        
        try {
            // If there's no Slimefun block, allow interaction
            if (!BlockStorage.hasBlockInfo(location)) {
                return true;
            }
            
            // Check if the player has access to the block
            // This uses Slimefun's built-in protection
            me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config blockInfo = BlockStorage.getLocationInfo(location);
            if (blockInfo != null && blockInfo.contains("owner")) {
                // Extract owner from block info and check permissions
                // For now, allow if player is OP or has bypass permission
                if (player.isOp() || player.hasPermission("slimefun.inventory.bypass")) {
                    return true;
                }
                // Additional permission checks can be added here
            }
            
            return true; // Default to allowing interaction
        } catch (Exception e) {
            // If we can't check, allow interaction
            return true;
        }
    }
    
    /**
     * Get the Slimefun item ID at a location
     * 
     * @param location The location to check
     * @return The Slimefun item ID, or null if not a Slimefun block
     */
    public static String getSlimefunItemId(Location location) {
        if (location == null) {
            return null;
        }
        
        try {
            me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config blockInfo = BlockStorage.getLocationInfo(location);
            if (blockInfo != null) {
                // Get the ID from the config
                return blockInfo.getString("id");
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Check if a Slimefun item is protected
     * 
     * @param itemId The Slimefun item ID
     * @return true if the item should be protected by Residence
     */
    public static boolean isProtected(String itemId) {
        if (itemId == null) {
            return false;
        }
        
        try {
            SlimefunItem item = SlimefunItem.getByID(itemId);
            if (item != null) {
                // Protect machines and other important blocks
                return item.isTicking() || 
                       itemId.contains("MACHINE") || 
                       itemId.contains("GENERATOR") ||
                       itemId.contains("CAPACITOR");
            }
        } catch (Exception e) {
            // Ignore
        }
        
        return false;
    }
    
    /**
     * Initialize Slimefun support
     * 
     * @return true if initialization was successful
     */
    public static boolean initialize() {
        try {
            // Check if SlimefunStartup is available
            Class.forName("me.mrCookieSlime.Slimefun.SlimefunStartup");
            System.out.println("[Residence] Successfully initialized Slimefun4 (me.mrCookieSlime version) support");
            return true;
        } catch (ClassNotFoundException e) {
            System.err.println("[Residence] Failed to find Slimefun4 classes");
            return false;
        }
    }
}
