package com.bekvon.bukkit.residence.slimeFun;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Residence Slimefun Compatibility Plugin
 * 
 * This plugin provides compatibility between Residence and old Slimefun4
 * by redirecting API calls to the correct classes.
 */
public class CompatibilityPlugin extends JavaPlugin {
    
    private static CompatibilityPlugin instance;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // Initialize Slimefun support
        boolean success = SlimefunManager.initialize();
        
        if (success) {
            getLogger().info("Successfully loaded Residence Slimefun compatibility layer!");
            getLogger().info("Residence can now interact with Slimefun4 blocks.");
        } else {
            getLogger().severe("Failed to initialize Slimefun support!");
            getLogger().severe("Please make sure Slimefun4 is installed and loaded.");
        }
    }
    
    @Override
    public void onDisable() {
        instance = null;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("rslimefun")) {
            if (!sender.hasPermission("rslimefun.reload")) {
                sender.sendMessage("§cYou don't have permission to do this!");
                return true;
            }
            
            if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                boolean success = SlimefunManager.initialize();
                if (success) {
                    sender.sendMessage("§aResidence Slimefun compatibility reloaded!");
                } else {
                    sender.sendMessage("§cFailed to reload Residence Slimefun compatibility!");
                }
                return true;
            }
            
            sender.sendMessage("§6Residence Slimefun Compatibility §7- §eHelp");
            sender.sendMessage("§7/rslimefun reload §8- §7Reload compatibility layer");
            return true;
        }
        
        return false;
    }
    
    public static CompatibilityPlugin getInstance() {
        return instance;
    }
}
