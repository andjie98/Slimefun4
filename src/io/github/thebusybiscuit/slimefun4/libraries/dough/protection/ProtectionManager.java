package io.github.thebusybiscuit.slimefun4.libraries.dough.protection;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class ProtectionManager {
    
    private final List<ProtectionModule> modules = new ArrayList<>();
    
    public void registerModule(ProtectionModule module) {
        if (!modules.contains(module) && module.isEnabled()) {
            modules.add(module);
            module.register();
        }
    }
    
    public void unregisterModule(ProtectionModule module) {
        if (modules.contains(module)) {
            modules.remove(module);
            module.unregister();
        }
    }
    
    public boolean hasPermission(Player player, Location location, ProtectionModule.ProtectionAction action) {
        for (ProtectionModule module : modules) {
            if (!module.hasPermission(player, location, action)) {
                return false;
            }
        }
        return true;
    }
    
    public boolean hasPermission(OfflinePlayer player, Location location, ProtectionModule.ProtectionAction action) {
        for (ProtectionModule module : modules) {
            if (!module.hasPermission(player, location, action)) {
                return false;
            }
        }
        return true;
    }
    
    public List<ProtectionModule> getModules() {
        return new ArrayList<>(modules);
    }
    
    public boolean isModuleRegistered(String name) {
        for (ProtectionModule module : modules) {
            if (module.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}