package me.mrCookieSlime.Slimefun.listeners;

import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.Slimefun;

import org.bukkit.Material;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ArmorListener implements Listener {
	
	public ArmorListener(SlimefunStartup plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority=EventPriority.MONITOR)
	public void onDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player && !e.isCancelled()) {
			Player p = (Player) e.getEntity();
			for (ItemStack armor: p.getInventory().getArmorContents()) {
				if (armor != null && SlimefunItem.getByItem(armor) != null) {
					if (SlimefunItem.getByItem(armor).isItem(SlimefunItems.ENDER_BOOTS) && Slimefun.hasUnlocked(p, SlimefunItems.ENDER_BOOTS, true)) {
						if (e instanceof EntityDamageByEntityEvent) {
							if (((EntityDamageByEntityEvent) e).getDamager() instanceof EnderPearl) e.setCancelled(true);
						}
					}
					else if (SlimefunItem.getByItem(armor).isItem(SlimefunItems.SLIME_BOOTS) && Slimefun.hasUnlocked(p, SlimefunItems.SLIME_BOOTS, true)) {
						if (e.getCause() == DamageCause.FALL) e.setCancelled(true);
					}
					else if (SlimefunItem.getByItem(armor).isItem(SlimefunItems.SLIME_BOOTS_STEEL) && Slimefun.hasUnlocked(p, SlimefunItems.SLIME_BOOTS_STEEL, true)) {
						if (e.getCause() == DamageCause.FALL) e.setCancelled(true);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onTrample(PlayerInteractEvent e) {
		if (e.getAction() == Action.PHYSICAL && e.getClickedBlock().getType() == Material.SOIL) {
			if (SlimefunManager.isItemSimiliar(e.getPlayer().getInventory().getBoots(), SlimefunItem.getItem("FARMER_SHOES"), true)) e.setCancelled(true);
		}
	}
}
