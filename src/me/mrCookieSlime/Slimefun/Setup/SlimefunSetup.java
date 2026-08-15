package me.mrCookieSlime.Slimefun.Setup;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.events.ItemUseEvent;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Misc.PostSlimefunLoadingHandler;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.Objects.MultiBlock;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.EnhancedFurnace;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunBackpack;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunMachine;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.Talisman;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.AutonomousMachineHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.ItemInteractionHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.MultiBlockInteractionHandler;
import me.mrCookieSlime.Slimefun.GPS.Elevator;
import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.api.Backpacks;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.holograms.Projector;

@SuppressWarnings("deprecation")
public class SlimefunSetup {

	public static boolean legacy_ore_washer = false;

	@SuppressWarnings("unchecked")
	public static void setupItems() throws Exception {

		// ── 0. 增强型合成台 (ENHANCED_CRAFTING_TABLE) ──
		new SlimefunMachine(Categories.MACHINES_1, SlimefunItems.ENHANCED_CRAFTING_TABLE, "ENHANCED_CRAFTING_TABLE",
		new ItemStack[] {null, null, null, null, new ItemStack(Material.WORKBENCH), null, null, new ItemStack(Material.DISPENSER), null},
		new ItemStack[0], Material.WORKBENCH)
		.register(false, new MultiBlockInteractionHandler() {

			@Override
			public boolean onInteract(Player p, MultiBlock mb, Block b) {

				SlimefunMachine machine = (SlimefunMachine) SlimefunItem.getByID("ENHANCED_CRAFTING_TABLE");

				if (mb.isMultiBlock(machine)) {
					if (CSCoreLib.getLib().getProtectionManager().canAccessChest(p.getUniqueId(), b, true)) {
						if (Slimefun.hasUnlocked(p, machine.getItem(), true)) {
							Dispenser disp = (Dispenser) b.getRelative(BlockFace.DOWN).getState();

							final Inventory inv = disp.getInventory();
							List<ItemStack[]> inputs = RecipeType.getRecipeInputList(machine);

							for (int i = 0; i < inputs.size(); i++) {
								boolean craft = true;
								for (int j = 0; j < inv.getContents().length; j++) {
									if (!SlimefunManager.isItemSimiliar(inv.getContents()[j], inputs.get(i)[j], true)) {
										if (SlimefunItem.getByItem(inputs.get(i)[j]) instanceof SlimefunBackpack) {
											if (!SlimefunManager.isItemSimiliar(inv.getContents()[j], inputs.get(i)[j], false)) {
												craft = false;
												break;
											}
										}
										else {
											craft = false;
											break;
										}
									}
								}
								
								if (craft) {
									final ItemStack adding = RecipeType.getRecipeOutputList(machine, inputs.get(i)).clone();
									if (Slimefun.hasUnlocked(p, adding, true)) {
										Inventory inv2 = Bukkit.createInventory(null, 9, "test");
										for (int j = 0; j < inv.getContents().length; j++) {
											inv2.setItem(j, inv.getContents()[j] != null ? (inv.getContents()[j].getAmount() > 1 ? new CustomItem(inv.getContents()[j], inv.getContents()[j].getAmount() - 1): null): null);
										}
										if (InvUtils.fits(inv2, adding)) {
											SlimefunItem sfItem = SlimefunItem.getByItem(adding);
											
											if (sfItem instanceof SlimefunBackpack) {
												ItemStack backpack = null;
												
												for (int j = 0; j < 9; j++) {
													if (inv.getContents()[j] != null) {
														if (inv.getContents()[j].getType() != Material.AIR) {
															if (SlimefunItem.getByItem(inv.getContents()[j]) instanceof SlimefunBackpack) {
																backpack = inv.getContents()[j];
																break;
															}
														}
													}
												}
												String id = "";
												int size = ((SlimefunBackpack) sfItem).size;
												
												if (backpack != null) {
													for (String line: backpack.getItemMeta().getLore()) {
														if (line.startsWith(ChatColor.translateAlternateColorCodes('&', "&7ID: ")) && line.contains("#")) {
															id = line.replace(ChatColor.translateAlternateColorCodes('&', "&7ID: "), "");
															Config cfg = new Config(new File("data-storage/Slimefun/Players/" + id.split("#")[0] + ".yml"));
															cfg.setValue("backpacks." + id.split("#")[1] + ".size", size);
															cfg.save();
															break;
														}
													}
												}

												if (id.equals("")) {
													for (int line = 0; line < adding.getItemMeta().getLore().size(); line++) {
														if (adding.getItemMeta().getLore().get(line).equals(ChatColor.translateAlternateColorCodes('&', "&7ID: <ID>"))) {
															ItemMeta im = adding.getItemMeta();
															List<String> lore = im.getLore();
															lore.set(line, lore.get(line).replace("<ID>", Backpacks.createBackpack(p, size)));
															im.setLore(lore);
															adding.setItemMeta(im);
															break;
														}
													}
												}
												else {
													for (int line = 0; line < adding.getItemMeta().getLore().size(); line++) {
														if (adding.getItemMeta().getLore().get(line).equals(ChatColor.translateAlternateColorCodes('&', "&7ID: <ID>"))) {
															ItemMeta im = adding.getItemMeta();
															List<String> lore = im.getLore();
															lore.set(line, lore.get(line).replace("<ID>", id));
															im.setLore(lore);
															adding.setItemMeta(im);
															break;
														}
													}
												}
											}
											

											for (int j = 0; j < 9; j++) {
												if (inv.getContents()[j] != null) {
													if (inv.getContents()[j].getType() != Material.AIR) {
														if (inv.getContents()[j].getType().toString().endsWith("_BUCKET")) inv.setItem(j, new ItemStack(Material.BUCKET));
														else if (inv.getContents()[j].getAmount() > 1) inv.setItem(j, new CustomItem(inv.getContents()[j], inv.getContents()[j].getAmount() - 1));
														else inv.setItem(j, null);
													}
												}
											}
											p.getWorld().playSound(b.getLocation(), Sound.BLOCK_WOOD_BUTTON_CLICK_ON, 1, 1);
											
											inv.addItem(adding);
										}
										else Messages.local.sendTranslation(p, "machines.full-inventory", true);
									}
									return true;
								}
							}
							Messages.local.sendTranslation(p, "machines.pattern-not-found", true);
						}
					}
					return true;
				}
				else return false;
			}
		});

		// ── 1. 便携式合成台 (PORTABLE_CRAFTER) ──
		new SlimefunItem(Categories.PORTABLE, SlimefunItems.PORTABLE_CRAFTER, "PORTABLE_CRAFTER", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.BOOK), new ItemStack(Material.WORKBENCH), null, null, null, null, null, null, null})
		.register(true, new ItemInteractionHandler() {

			@Override
			public boolean onRightClick(ItemUseEvent e, Player p, ItemStack item) {
				if (SlimefunManager.isItemSimiliar(item, SlimefunItems.PORTABLE_CRAFTER, true)) {
					p.openWorkbench(p.getLocation(), true);
					p.getWorld().playSound(p.getLocation(), Sound.BLOCK_WOOD_BUTTON_CLICK_ON, 1, 1);
					return true;
				}
				else return false;
			}
		});

		// ── 2. 末影背包 (ENDER_BACKPACK) ──
		new SlimefunItem(Categories.PORTABLE, SlimefunItems.ENDER_BACKPACK, "ENDER_BACKPACK", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.LEATHER), new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.LEATHER), new ItemStack(Material.ENDER_CHEST), new ItemStack(Material.LEATHER), new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.LEATHER), new ItemStack(Material.ENDER_PEARL)})
		.register(false, new ItemInteractionHandler() {

			@Override
			public boolean onRightClick(ItemUseEvent e, Player p, ItemStack item) {
				if (SlimefunManager.isItemSimiliar(item, SlimefunItems.ENDER_BACKPACK, true)) {
					e.setCancelled(true);
					p.openInventory(p.getEnderChest());
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1, 1);
					return true;
				}
				else return false;
			}
		});

		// ── 2. 斩首剑 (SWORD_OF_BEHEADING) ──
		new SlimefunItem(Categories.WEAPONS, SlimefunItems.SWORD_OF_BEHEADING, "SWORD_OF_BEHEADING", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, new ItemStack(Material.DIAMOND), null, new ItemStack(Material.GHAST_TEAR), new ItemStack(Material.DIAMOND), new ItemStack(Material.GHAST_TEAR), null, new ItemStack(Material.BLAZE_ROD), null}, new String[] {"chance.PLAYER", "chance.SKELETON", "chance.WITHER_SKELETON", "chance.ZOMBIE", "chance.CREEPER"}, new Integer[] {70, 40, 25, 40, 40})
		.register(false);

		// ── 3a. 末影结晶 I/II/III (ENDER_LUMP_1/2/3) — 末影护身符的合成材料 ──
		new SlimefunItem(Categories.LUMPS_AND_MAGIC, SlimefunItems.ENDER_LUMP_1, "ENDER_LUMP_1", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, null, null, null, new ItemStack(Material.EYE_OF_ENDER), null, null, null, null}, new CustomItem(SlimefunItems.ENDER_LUMP_1, 2))
		.register(false);

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, SlimefunItems.ENDER_LUMP_2, "ENDER_LUMP_2", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.ENDER_LUMP_1, SlimefunItems.ENDER_LUMP_1, null, SlimefunItems.ENDER_LUMP_1, SlimefunItems.ENDER_LUMP_1, null, null, null, null})
		.register(false);

		new SlimefunItem(Categories.LUMPS_AND_MAGIC, SlimefunItems.ENDER_LUMP_3, "ENDER_LUMP_3", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {SlimefunItems.ENDER_LUMP_2, SlimefunItems.ENDER_LUMP_2, null, SlimefunItems.ENDER_LUMP_2, SlimefunItems.ENDER_LUMP_2, null, null, null, null})
		.register(false);

		// ── 3b. 普通护身符 (COMMON_TALISMAN) — 各护身符的合成基底 ──
		new SlimefunItem(Categories.LUMPS_AND_MAGIC, SlimefunItems.TALISMAN, "COMMON_TALISMAN", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, new ItemStack(Material.GOLD_INGOT), null, new ItemStack(Material.GOLD_INGOT), new ItemStack(Material.EMERALD), new ItemStack(Material.GOLD_INGOT), null, new ItemStack(Material.GOLD_INGOT), null})
		.register(false);

		// ── 4. 猎手护身符 (HUNTER_TALISMAN) ──
		Talisman hunterTalisman = new Talisman(SlimefunItems.TALISMAN_HUNTER, "HUNTER_TALISMAN",
		new ItemStack[] {new ItemStack(Material.BONE), new ItemStack(Material.STRING), new ItemStack(Material.BONE), new ItemStack(Material.ROTTEN_FLESH), new ItemStack(Material.EMERALD), new ItemStack(Material.ROTTEN_FLESH), new ItemStack(Material.SPIDER_EYE), new ItemStack(Material.BLAZE_POWDER), new ItemStack(Material.SPIDER_EYE)},
		false, false, "hunter", 20);
		hunterTalisman.setRecipeType(RecipeType.ENHANCED_CRAFTING_TABLE);
		hunterTalisman.register(false);

		// ── 4a. 铁砧护身符 (ANVIL_TALISMAN) ──
		Talisman anvilTalisman = new Talisman(SlimefunItems.TALISMAN_ANVIL, "ANVIL_TALISMAN",
		new ItemStack[] {new ItemStack(Material.IRON_INGOT), null, new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT), SlimefunItems.TALISMAN, new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.ANVIL), new ItemStack(Material.IRON_INGOT)},
		true, false, "anvil");
		anvilTalisman.setRecipeType(RecipeType.ENHANCED_CRAFTING_TABLE);
		anvilTalisman.register(false);

		// ── 4b. 矿工护身符 (MINER_TALISMAN) ──
		Talisman minerTalisman = new Talisman(SlimefunItems.TALISMAN_MINER, "MINER_TALISMAN",
		new ItemStack[] {new ItemStack(Material.COAL), null, new ItemStack(Material.COAL), new ItemStack(Material.INK_SACK, 1, (short) 4), SlimefunItems.TALISMAN, new ItemStack(Material.REDSTONE), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.DIAMOND), new ItemStack(Material.IRON_INGOT)},
		false, false, "miner", 20);
		minerTalisman.setRecipeType(RecipeType.ENHANCED_CRAFTING_TABLE);
		minerTalisman.register(false);

		// ── 4c. 岩浆行走者护身符 (LAVA_TALISMAN) ──
		Talisman lavaTalisman = new Talisman(SlimefunItems.TALISMAN_LAVA, "LAVA_TALISMAN",
		new ItemStack[] {null, new ItemStack(Material.BLAZE_ROD), null, new ItemStack(Material.MAGMA_CREAM), SlimefunItems.TALISMAN, new ItemStack(Material.MAGMA_CREAM), null, new ItemStack(Material.LAVA_BUCKET), null},
		true, true, "lava", new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 3600, 4));
		lavaTalisman.setRecipeType(RecipeType.ENHANCED_CRAFTING_TABLE);
		lavaTalisman.register(false);

		// ── 4d. 潜水护身符 (WATER_TALISMAN) ──
		Talisman waterTalisman = new Talisman(SlimefunItems.TALISMAN_WATER, "WATER_TALISMAN",
		new ItemStack[] {null, new ItemStack(Material.FISHING_ROD), null, new ItemStack(Material.WATER_BUCKET), SlimefunItems.TALISMAN, new ItemStack(Material.WATER_BUCKET), null, new ItemStack(Material.WATER_LILY), null},
		true, true, "water", new PotionEffect(PotionEffectType.WATER_BREATHING, 3600, 4));
		waterTalisman.setRecipeType(RecipeType.ENHANCED_CRAFTING_TABLE);
		waterTalisman.register(false);

		// ── 4e. 天使护身符 (ANGEL_TALISMAN) ──
		Talisman angelTalisman = new Talisman(SlimefunItems.TALISMAN_ANGEL, "ANGEL_TALISMAN",
		new ItemStack[] {new ItemStack(Material.FEATHER), null, new ItemStack(Material.FEATHER), new ItemStack(Material.FEATHER), SlimefunItems.TALISMAN, new ItemStack(Material.FEATHER), new ItemStack(Material.FEATHER), null, new ItemStack(Material.FEATHER)},
		false, true, "angel", 75);
		angelTalisman.setRecipeType(RecipeType.ENHANCED_CRAFTING_TABLE);
		angelTalisman.register(false);

		// ── 4f. 火神护身符 (FIRE_TALISMAN) ──
		Talisman fireTalisman = new Talisman(SlimefunItems.TALISMAN_FIRE, "FIRE_TALISMAN",
		new ItemStack[] {null, new ItemStack(Material.MAGMA_CREAM), null, new ItemStack(Material.BLAZE_POWDER), SlimefunItems.TALISMAN, new ItemStack(Material.BLAZE_POWDER), null, new ItemStack(Material.MAGMA_CREAM), null},
		true, true, "fire", new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 3600, 4));
		fireTalisman.setRecipeType(RecipeType.ENHANCED_CRAFTING_TABLE);
		fireTalisman.register(false);

		// ── 4g. 魔术师护身符 (MAGICIAN_TALISMAN) ──
		Talisman magicianTalisman = new Talisman(SlimefunItems.TALISMAN_MAGICIAN, "MAGICIAN_TALISMAN",
		new ItemStack[] {null, new ItemStack(Material.BOOK), null, new ItemStack(Material.INK_SACK, 1, (short) 4), SlimefunItems.TALISMAN, new ItemStack(Material.INK_SACK, 1, (short) 4), null, new ItemStack(Material.ENCHANTMENT_TABLE), null},
		false, false, "magician", 80);
		magicianTalisman.setRecipeType(RecipeType.ENHANCED_CRAFTING_TABLE);
		magicianTalisman.register(false);

		for (Enchantment enchantment: Enchantment.values()) {
			for (int level = 1; level <= enchantment.getMaxLevel(); level++) {
				Slimefun.setItemVariable("MAGICIAN_TALISMAN", "allow-enchantments." + enchantment.getName() + ".level." + level, true);
			}
		}

		// ── 4h. 旅行者护身符 (TRAVELLER_TALISMAN) ──
		Talisman travellerTalisman = new Talisman(SlimefunItems.TALISMAN_TRAVELLER, "TRAVELLER_TALISMAN",
		new ItemStack[] {new ItemStack(Material.FEATHER), null, new ItemStack(Material.FEATHER), SlimefunItems.STAFF_WIND, SlimefunItems.TALISMAN_ANGEL, SlimefunItems.STAFF_WIND, new ItemStack(Material.FEATHER), null, new ItemStack(Material.FEATHER)},
		false, false, "traveller", 60, new PotionEffect(PotionEffectType.SPEED, 3600, 2));
		travellerTalisman.setRecipeType(RecipeType.ENHANCED_CRAFTING_TABLE);
		travellerTalisman.register(false);

		// ── 4i. 战士护身符 (WARRIOR_TALISMAN) ──
		Talisman warriorTalisman = new Talisman(SlimefunItems.TALISMAN_WARRIOR, "WARRIOR_TALISMAN",
		new ItemStack[] {new ItemStack(Material.IRON_BLOCK), null, new ItemStack(Material.IRON_BLOCK), new ItemStack(Material.DIAMOND_SWORD), SlimefunItems.TALISMAN, new ItemStack(Material.DIAMOND_SWORD), new ItemStack(Material.IRON_BLOCK), null, new ItemStack(Material.IRON_BLOCK)},
		true, true, "warrior", new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 3600, 2));
		warriorTalisman.setRecipeType(RecipeType.ENHANCED_CRAFTING_TABLE);
		warriorTalisman.register(false);

		// ── 4j. 骑士护身符 (KNIGHT_TALISMAN) ──
		Talisman knightTalisman = new Talisman(SlimefunItems.TALISMAN_KNIGHT, "KNIGHT_TALISMAN",
		new ItemStack[] {new ItemStack(Material.GOLD_INGOT), null, new ItemStack(Material.GOLD_INGOT), new ItemStack(Material.GOLD_INGOT), SlimefunItems.TALISMAN_WARRIOR, new ItemStack(Material.GOLD_INGOT), new ItemStack(Material.GOLD_INGOT), new ItemStack(Material.IRON_CHESTPLATE), new ItemStack(Material.GOLD_INGOT)},
		"knight", 30, new PotionEffect(PotionEffectType.REGENERATION, 100, 3));
		knightTalisman.setRecipeType(RecipeType.ENHANCED_CRAFTING_TABLE);
		knightTalisman.register(false);

		// ── 4k. 风之护身符 (WHIRLWIND_TALISMAN) ──
		Talisman whirlwindTalisman = new Talisman(SlimefunItems.TALISMAN_WHIRLWIND, "WHIRLWIND_TALISMAN",
		new ItemStack[] {null, new ItemStack(Material.BOW), null, SlimefunItems.STAFF_WIND, SlimefunItems.TALISMAN_TRAVELLER, SlimefunItems.STAFF_WIND, null, new ItemStack(Material.FEATHER), null},
		false, true, "whirlwind", 60);
		whirlwindTalisman.setRecipeType(RecipeType.ENHANCED_CRAFTING_TABLE);
		whirlwindTalisman.register(false);

		// ── 4l. 魔法师护身符 (WIZARD_TALISMAN) ──
		Talisman wizardTalisman = new Talisman(SlimefunItems.TALISMAN_WIZARD, "WIZARD_TALISMAN",
		new ItemStack[] {new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.BOOK), new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.BOOK), SlimefunItems.TALISMAN_MAGICIAN, new ItemStack(Material.BOOK), new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.BOOK), new ItemStack(Material.ENDER_PEARL)},
		false, false, "wizard", 60);
		wizardTalisman.setRecipeType(RecipeType.ENHANCED_CRAFTING_TABLE);
		wizardTalisman.register(false);

		// ── 5. 风杖 (STAFF_ELEMENTAL_WIND) ──
		new SlimefunItem(Categories.MAGIC, SlimefunItems.STAFF_WIND, "STAFF_ELEMENTAL_WIND", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, new ItemStack(Material.GHAST_TEAR), null, new ItemStack(Material.FEATHER), new ItemStack(Material.STICK), new ItemStack(Material.FEATHER), new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.STICK), new ItemStack(Material.ENDER_PEARL)})
		.register(false, new ItemInteractionHandler() {

			@Override
			public boolean onRightClick(ItemUseEvent e, Player p, ItemStack item) {
				if (SlimefunManager.isItemSimiliar(item, SlimefunItems.STAFF_WIND, true)) {
					if (p.getFoodLevel() >= 2) {
						if (p.getInventory().getItemInMainHand().getType() != Material.SHEARS && p.getGameMode() != GameMode.CREATIVE) {
							FoodLevelChangeEvent event = new FoodLevelChangeEvent(p, p.getFoodLevel() - 2);
							Bukkit.getPluginManager().callEvent(event);
							p.setFoodLevel(event.getFoodLevel());
						}
						p.setVelocity(p.getEyeLocation().getDirection().multiply(4));
						p.getWorld().playSound(p.getLocation(), Sound.ENTITY_TNT_PRIMED, 1, 1);
						p.getWorld().playEffect(p.getLocation(), Effect.SMOKE, 1);
						p.setFallDistance(0.0f);
					}
					else {
						Messages.local.sendTranslation(p, "messages.hungry", true);
					}
					return true;
				}
				else return false;
			}
		});

		// ── 4. 镀金背包 (GILDED_BACKPACK) ──
		new SlimefunBackpack(45, Categories.PORTABLE, SlimefunItems.GILDED_BACKPACK, "GILDED_BACKPACK", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.GOLD_INGOT), null, new ItemStack(Material.GOLD_INGOT), new ItemStack(Material.LEATHER), new ItemStack(Material.CHEST), new ItemStack(Material.LEATHER), new ItemStack(Material.GOLD_INGOT), new ItemStack(Material.GOLD_BLOCK), new ItemStack(Material.GOLD_INGOT)})
		.register(false);

		// ── 5. 黑钻炉 (CARBONADO_EDGED_FURNACE) ──
		new EnhancedFurnace(20, 10, 3, SlimefunItems.CARBONADO_EDGED_FURNACE, "CARBONADO_EDGED_FURNACE",
		new ItemStack[] {new ItemStack(Material.DIAMOND), new ItemStack(Material.REDSTONE_BLOCK), new ItemStack(Material.DIAMOND), new ItemStack(Material.LAVA_BUCKET), new ItemStack(Material.FURNACE), new ItemStack(Material.LAVA_BUCKET), new ItemStack(Material.DIAMOND), new ItemStack(Material.NETHER_STAR), new ItemStack(Material.DIAMOND)})
		.register(false);

		// ── 6. 方块放置器 (BLOCK_PLACER) ──
		final String[] blockPlacerBlacklist = Slimefun.getItemValue("BLOCK_PLACER", "unplaceable-blocks") != null ? ((List<String>) Slimefun.getItemValue("BLOCK_PLACER", "unplaceable-blocks")).toArray(new String[((List<String>) Slimefun.getItemValue("BLOCK_PLACER", "unplaceable-blocks")).size()]): new String[] {"STRUCTURE_BLOCK"};

		new SlimefunItem(Categories.MACHINES_1, SlimefunItems.BLOCK_PLACER, "BLOCK_PLACER", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {new ItemStack(Material.GOLD_INGOT), new ItemStack(Material.PISTON_BASE), new ItemStack(Material.GOLD_INGOT), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.DISPENSER), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.GOLD_INGOT), new ItemStack(Material.REDSTONE_BLOCK), new ItemStack(Material.GOLD_INGOT)}, 
		new String[] {"unplaceable-blocks"}, new Object[] {Arrays.asList("STRUCTURE_BLOCK")})
		.register(false, new AutonomousMachineHandler() {

			@Override
			public boolean onBlockDispense(final BlockDispenseEvent e, Block dispenser, final Dispenser d, Block block, Block chest, SlimefunItem machine) {
				if (machine.getID().equalsIgnoreCase("BLOCK_PLACER")) {
					e.setCancelled(true);
					if ((block.getType() == null || block.getType() == Material.AIR) && e.getItem().getType().isBlock()) {
						for(String blockType : blockPlacerBlacklist) {
							if (e.getItem().getType().toString().equals(blockType)) {
								return false;
							}
						}
						byte blockData = e.getItem().getData().getData();
						if (e.getItem().getType() == Material.ANVIL) {
							blockData = (byte) (blockData << 2);
						}
						
						SlimefunItem sfItem = SlimefunItem.getByItem(e.getItem());
						if (sfItem != null) {
							if (!SlimefunItem.blockhandler.containsKey(sfItem.getName())) {
								block.setType(e.getItem().getType());
								block.setData(blockData);
								BlockStorage.store(block, sfItem.getName());
								block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, e.getItem().getType());
								if (d.getInventory().containsAtLeast(e.getItem(), 2)) d.getInventory().removeItem(new CustomItem(e.getItem(), 1));
								else {
									Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunStartup.instance, new BukkitRunnable() {
										@Override
										public void run() {
											d.getInventory().removeItem(e.getItem());
										}
									}, 2L);
								}
							}
						}
						else {
							block.setType(e.getItem().getType());
							block.setData(blockData);
							block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, e.getItem().getType());
							if (d.getInventory().containsAtLeast(e.getItem(), 2)) d.getInventory().removeItem(new CustomItem(e.getItem(), 1));
							else {
								Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunStartup.instance, new BukkitRunnable() {
									@Override
									public void run() {
										d.getInventory().removeItem(e.getItem());
									}
								}, 2L);
							}
						}
					}
					return true;
				}
				else return false;
			}
		});

		// ── 7. 全息显示器 (HOLOGRAM_PROJECTOR) ──
		new SlimefunItem(Categories.TECH, SlimefunItems.HOLOGRAM_PROJECTOR, "HOLOGRAM_PROJECTOR", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, new ItemStack(Material.EYE_OF_ENDER), null, new ItemStack(Material.GOLD_INGOT), new ItemStack(Material.GLOWSTONE), new ItemStack(Material.GOLD_INGOT), null, new ItemStack(Material.GOLD_INGOT), null}, new CustomItem(SlimefunItems.HOLOGRAM_PROJECTOR, 3))
		.register(false, new ItemInteractionHandler() {

			@Override
			public boolean onRightClick(ItemUseEvent e, Player p, ItemStack stack) {
				if (e.getClickedBlock() == null) return false;
				SlimefunItem item = BlockStorage.check(e.getClickedBlock());
				if (item == null || !item.getName().equals("HOLOGRAM_PROJECTOR")) return false;
				e.setCancelled(true);

				if (BlockStorage.getBlockInfo(e.getClickedBlock(), "owner").equals(p.getUniqueId().toString())) {
					Projector.openEditor(p, e.getClickedBlock());
				}

				return true;
			}
		});

		SlimefunItem.registerBlockHandler("HOLOGRAM_PROJECTOR", new SlimefunBlockHandler() {

			@Override
			public void onPlace(Player p, Block b, SlimefunItem item) {
				BlockStorage.addBlockInfo(b, "text", "&b在这里写全息文字内容");
				BlockStorage.addBlockInfo(b, "offset", "-0.5");
				BlockStorage.addBlockInfo(b, "owner", p.getUniqueId().toString());

				Projector.getArmorStand(b);
			}

			@Override
			public boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason) {
				Projector.getArmorStand(b).remove();
				return true;
			}
		});

		// ── 8. 电梯板 (ELEVATOR_PLATE) ──
		new SlimefunItem(Categories.GPS, SlimefunItems.ELEVATOR, "ELEVATOR_PLATE", RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {null, new ItemStack(Material.STONE_PLATE), null, new ItemStack(Material.PISTON_BASE), new ItemStack(Material.REDSTONE_BLOCK), new ItemStack(Material.PISTON_BASE), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT)},
		new CustomItem(SlimefunItems.ELEVATOR, 2))
		.register(false, new ItemInteractionHandler() {

			@Override
			public boolean onRightClick(final ItemUseEvent e, Player p, ItemStack stack) {
				if (e.getClickedBlock() == null) return false;
				SlimefunItem item = BlockStorage.check(e.getClickedBlock());
				if (item == null) return false;
				if (!item.getName().equals("ELEVATOR_PLATE")) return false;

				if (BlockStorage.getBlockInfo(e.getClickedBlock(), "owner").equals(p.getUniqueId().toString())) Elevator.openEditor(p, e.getClickedBlock());
				return true;
			}
		});

		SlimefunItem.registerBlockHandler("ELEVATOR_PLATE", new SlimefunBlockHandler() {

			@Override
			public void onPlace(Player p, Block b, SlimefunItem item) {
				BlockStorage.addBlockInfo(b, "floor", "&r楼层 #0");
				BlockStorage.addBlockInfo(b, "owner", p.getUniqueId().toString());
			}

			@Override
			public boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason) {
				return true;
			}
		});

	}

	public static void registerPostHandler(PostSlimefunLoadingHandler handler) {
		MiscSetup.post_handlers.add(handler);
	}

}
