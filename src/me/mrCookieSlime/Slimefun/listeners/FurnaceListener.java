package me.mrCookieSlime.Slimefun.listeners;

import java.util.HashSet;
import java.util.Set;

import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.EnhancedFurnace;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class FurnaceListener implements Listener {

	private final SlimefunStartup plugin;
	private final Set<Location> activeFurnaces = new HashSet<Location>();
	
	public FurnaceListener(SlimefunStartup plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBurn(FurnaceBurnEvent e) {
		EnhancedFurnace furnace = getEnhancedFurnace(e.getBlock());
		if (furnace != null) {
			long burnTime = (long) e.getBurnTime() * furnace.getFuelEfficiency();
			e.setBurnTime((int) Math.min(Integer.MAX_VALUE, burnTime));
			startAccelerating(e.getBlock(), furnace);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onSmelt(FurnaceSmeltEvent e) {
		EnhancedFurnace furnace = getEnhancedFurnace(e.getBlock());
		if (furnace != null) {
			startAccelerating(e.getBlock(), furnace);

			ItemStack output = e.getResult().clone();
			int outputAmount = output.getAmount() * furnace.getOutput();
			ItemStack currentOutput = ((Furnace) e.getBlock().getState()).getInventory().getResult();
			int availableSpace = output.getMaxStackSize() - (currentOutput == null ? 0 : currentOutput.getAmount());

			if (outputAmount > availableSpace) {
				e.setCancelled(true);
				return;
			}

			output.setAmount(outputAmount);
			e.setResult(output);
		}
	}

	private EnhancedFurnace getEnhancedFurnace(Block block) {
		SlimefunItem item = BlockStorage.check(block);
		return item instanceof EnhancedFurnace ? (EnhancedFurnace) item : null;
	}

	private void startAccelerating(final Block block, final EnhancedFurnace enhancedFurnace) {
		final Location location = block.getLocation();
		if (!activeFurnaces.add(location)) return;

		new BukkitRunnable() {

			private int idleTicks;

			@Override
			public void run() {
				if (!block.getChunk().isLoaded() || getEnhancedFurnace(block) != enhancedFurnace || !(block.getState() instanceof Furnace)) {
					stop();
					return;
				}

				Furnace furnace = (Furnace) block.getState();
				short cookTime = furnace.getCookTime();
				if (furnace.getBurnTime() == 0 && cookTime == 0) {
					idleTicks++;
					if (idleTicks >= 20) stop();
					return;
				}

				idleTicks = 0;
				if (furnace.getInventory().getSmelting() != null && cookTime >= 0 && cookTime < 199) {
					furnace.setCookTime((short) Math.min(199, cookTime + enhancedFurnace.getSpeed() - 1));
					furnace.update(true, false);
				}
			}

			private void stop() {
				activeFurnaces.remove(location);
				cancel();
			}
		}.runTaskTimer(plugin, 1L, 1L);
	}

}
