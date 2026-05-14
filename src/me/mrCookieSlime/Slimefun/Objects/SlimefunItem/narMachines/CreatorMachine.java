package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.narMachines;


import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Misc.compatibles.ProtectionUtils;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineHelper;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public abstract class CreatorMachine extends SlimefunItem {
    public static Map<Block, MachineRecipe> processing = new HashMap();
    public static Map<Block, Integer> progress = new HashMap();
    protected List<MachineRecipe> recipes = new ArrayList();
    private static final int[] codeBorder = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 19, 20, 21, 23, 24, 25, 26};//22去掉
    private static final int[] code = {10, 11, 12, 13, 14, 15, 16};
    private static final int[] border = {27, 30, 31, 32, 35, 36, 39, 41, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53};
    private static final int[] inputSign = {28, 29};
    private static final int[] outputSign = {33, 34};

    public CreatorMachine(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe)   {
        super(category, item, name, recipeType, recipe);

        new BlockMenuPreset(name, getInventoryTitle())
        {
            @Override
            public void init()
            {
                CreatorMachine.this.constructMenu(this);
            }

            @Override
            public void newInstance(BlockMenu menu, Block b) {
                //随机编码值
                if (BlockStorage.getBlockInfo(b, "random-code") == null){
                    Random random = new Random();
                    BlockStorage.addBlockInfo(b, "random-code", String.valueOf(random.nextInt(127)));
                }
                if (BlockStorage.getBlockInfo(b, "last-code") == null){
                    BlockStorage.addBlockInfo(b, "last-code", "0000000");
                }
                if (BlockStorage.getBlockInfo(b, "set-code") == null){
                    BlockStorage.addBlockInfo(b, "set-code", "0000000");
                }
                if (BlockStorage.getBlockInfo(b, "output-item") == null) {
                    BlockStorage.addBlockInfo(b, "output-item", "0");
                }
                try {
                    char[] setCode = BlockStorage.getBlockInfo(b, "set-code").toCharArray();
                    for (int slot : code) {
                        int loc = slot - code[0];
                        //这里是一个7位的二进制数，如果匹配则能够生产出指定物品
                        if (!BlockStorage.hasBlockInfo(b) || setCode[loc] == '0') {
                            menu.replaceExistingItem(slot, new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMGViZTdlNTIxNTE2OWE2OTlhY2M2Y2VmYTdiNzNmZGIxMDhkYjg3YmI2ZGFlMjg0OWZiZTI0NzE0YjI3In19fQ=="),
                                    "&7编码: &a0", "", "&7正确的§e编码组合§7才能产出指定的物质", "§7每当你设置的编码较上一次§e更接近§7正确编码", "§7机器都会发出比较§e悦耳的声音", "&7另外有技巧地设置编码将有效地§e减少生产成本", "§7不过一旦被观测到正确的常数", "§7常数就会被§c重置", "§7这究竟是神的玩笑还是恶魔的诅咒呢", "", "§c> §7使用§d元物质§7进行制作", "§c> §7调整下方的物品修改你想制作的物品", "§6> §b点击此处将编码修改为§e1"));
                            menu.addMenuClickHandler(slot, new MenuClickHandler() {

                                @Override
                                public boolean onClick(Player p, int arg1, ItemStack arg2, ClickAction arg3) {
                                    BlockStorage.addBlockInfo(b, "last-code", String.valueOf(setCode));
                                    setCode[loc] = '1';
                                    BlockStorage.addBlockInfo(b, "set-code", String.valueOf(setCode));
                                    newInstance(menu, b);
                                    return false;
                                }
                            });
                        } else {
                            menu.replaceExistingItem(slot, new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzFiYzJiY2ZiMmJkMzc1OWU2YjFlODZmYzdhNzk1ODVlMTEyN2RkMzU3ZmMyMDI4OTNmOWRlMjQxYmM5ZTUzMCJ9fX0="),
                                    "&7编码: &e1", "", "&7正确的§e编码组合§7才能产出指定的物质", "§7每当你设置的编码较上一次§e更接近§7正确编码", "§7机器都会发出比较§e悦耳的声音", "&7另外有技巧地设置编码将有效地§e减少生产成本", "§7不过一旦被观测到正确的常数", "§7常数就会被§c重置", "§7这究竟是神的玩笑还是恶魔的诅咒呢", "", "§c> §7使用§d元物质§7进行制作", "§c> §7调整下方的物品修改你想制作的物品", "§6> §b点击此处将编码修改为§a0"));
                            menu.addMenuClickHandler(slot, new MenuClickHandler() {

                                @Override
                                public boolean onClick(Player p, int arg1, ItemStack arg2, ClickAction arg3) {
                                    BlockStorage.addBlockInfo(b, "last-code", String.valueOf(setCode));
                                    setCode[loc] = '0';
                                    BlockStorage.addBlockInfo(b, "set-code", String.valueOf(setCode));
                                    newInstance(menu, b);
                                    return false;
                                }
                            });
                        }
                    }
                } catch(Exception x) {
                }
                //修改生产物
                ItemStack resultItem = recipes.get(Integer.valueOf(BlockStorage.getBlockInfo(b, "output-item"))).getOutput()[0];
                menu.replaceExistingItem(22, new CustomItem(resultItem, "§7制作: " + resultItem.getItemMeta().getDisplayName()));
                menu.addMenuClickHandler(22, new MenuClickHandler() {

                    @Override
                    public boolean onClick(Player p, int arg1, ItemStack arg2, ClickAction arg3) {
                        Random random = new Random();
                        BlockStorage.addBlockInfo(b, "random-code", String.valueOf(random.nextInt(127)));
                        int outItem = Integer.valueOf(BlockStorage.getBlockInfo(b, "output-item"));
                        BlockStorage.addBlockInfo(b, "output-item", outItem >= (recipes.size() - 1) ? "0" : String.valueOf(++outItem));
                        newInstance(menu, b);
                        return false;
                    }
                });
            }

            public boolean canOpen(Block b, Player p)
            {
                boolean perm = p.hasPermission("slimefun.inventory.bypass") || CSCoreLib.getLib().getProtectionManager().canAccessChest(p.getUniqueId(), b, true);
                return perm && ProtectionUtils.canAccessItem(p, b);
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow)
            {
                if (flow.equals(ItemTransportFlow.INSERT)) {
                    return CreatorMachine.this.getInputSlots();
                }
                return CreatorMachine.this.getOutputSlots();
            }
        };
        registerBlockHandler(name, new SlimefunBlockHandler()
        {
            @Override
            public void onPlace(Player p, Block b, SlimefunItem item) {}

            @Override
            public boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason)
            {
                BlockMenu inv = BlockStorage.getInventory(b);
                if (inv != null)
                {
                    for (int slot : CreatorMachine.this.getInputSlots()) {
                        if (inv.getItemInSlot(slot) != null) {
                            b.getWorld().dropItemNaturally(b.getLocation(), inv.getItemInSlot(slot));
                        }
                    }
                    for (int slot : CreatorMachine.this.getOutputSlots()) {
                        if (inv.getItemInSlot(slot) != null) {
                            b.getWorld().dropItemNaturally(b.getLocation(), inv.getItemInSlot(slot));
                        }
                    }
                }
                CreatorMachine.progress.remove(b);
                CreatorMachine.processing.remove(b);
                return true;
            }
        });
        registerDefaultRecipes();
    }

    public CreatorMachine(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
        super(category, item, name, recipeType, recipe, recipeOutput);

        new BlockMenuPreset(name, getInventoryTitle())
        {
            @Override
            public void init()
            {
                CreatorMachine.this.constructMenu(this);
            }

            @Override
            public void newInstance(BlockMenu menu, Block b) {}

            @Override
            public boolean canOpen(Block b, Player p)
            {
                boolean perm = p.hasPermission("slimefun.inventory.bypass") || CSCoreLib.getLib().getProtectionManager().canAccessChest(p.getUniqueId(), b, true);
                return perm && ProtectionUtils.canAccessItem(p, b);
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow)
            {
                if (flow.equals(ItemTransportFlow.INSERT)) {
                    return CreatorMachine.this.getInputSlots();
                }
                return CreatorMachine.this.getOutputSlots();
            }
        };
        registerBlockHandler(name, new SlimefunBlockHandler()
        {
            @Override
            public void onPlace(Player p, Block b, SlimefunItem item) {}

            @Override
            public boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason)
            {
                for (int slot : CreatorMachine.this.getInputSlots()) {
                    if (BlockStorage.getInventory(b).getItemInSlot(slot) != null) {
                        b.getWorld().dropItemNaturally(b.getLocation(), BlockStorage.getInventory(b).getItemInSlot(slot));
                    }
                }
                for (int slot : CreatorMachine.this.getOutputSlots()) {
                    if (BlockStorage.getInventory(b).getItemInSlot(slot) != null) {
                        b.getWorld().dropItemNaturally(b.getLocation(), BlockStorage.getInventory(b).getItemInSlot(slot));
                    }
                }
                CreatorMachine.processing.remove(b);
                CreatorMachine.progress.remove(b);
                return true;
            }
        });
        registerDefaultRecipes();
    }

    private void constructMenu(BlockMenuPreset preset){
        for(int i : codeBorder){
            preset.addItem(i, new CustomItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15), " "),
                    new ChestMenu.MenuClickHandler() {
                        @Override
                        public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction) {
                            return false;
                        }
                    });
        }
        for(int i : border){
            preset.addItem(i, new CustomItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7), " "),
                    new ChestMenu.MenuClickHandler() {
                        @Override
                        public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction) {
                            return false;
                        }
                    });
        }
        for(int i : outputSign){
            preset.addItem(i, new CustomItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 11), "§b输出槽"),
                    new ChestMenu.MenuClickHandler() {
                        @Override
                        public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction) {
                            return false;
                        }
                    });
        }
        for(int i : inputSign){
            preset.addItem(i, new CustomItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 4), "§e输入槽"),
                    new ChestMenu.MenuClickHandler() {
                        @Override
                        public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction) {
                            return false;
                        }
                    });
        }
        //进度显示
        preset.addItem(40, new CustomItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15), " "),
                new ChestMenu.MenuClickHandler() {
                    @Override
                    public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction) {
                        return false;
                    }
                });
        //禁止随意将物品放入输出槽
        for (int i : getOutputSlots()) {
            preset.addItem(i, null, new ChestMenu.AdvancedMenuClickHandler() {
                @Override
                public boolean onClick(Player player, int i, ItemStack item, ClickAction action) {
                    return false;
                }

                @Override
                public boolean onClick(InventoryClickEvent event, Player player, int slot, ItemStack item, ClickAction action) {
                    return (item == null) || (item.getType() == null) || (item.getType() == Material.AIR);
                }
            });
        }
        //TODO 按钮
        for (int i : code) {
            preset.addItem(i, new CustomItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7), " "),
                new ChestMenu.MenuClickHandler() {
                    @Override
                    public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction) {
                        return false;
                    }
            });
        }
    }

    public int[] getInputSlots(){
        return new int[]{37, 38};
    }

    public int[] getOutputSlots(){
        return new int[]{42, 43};
    }

    public abstract String getInventoryTitle();

    public abstract ItemStack getProgressBar();

    public abstract void registerDefaultRecipes();

    public abstract int getEnergyConsumption();

    public abstract String getMachineIdentifier();

    public MachineRecipe getProcessing(Block b)
    {
        return processing.get(b);
    }

    public boolean isProcessing(Block b)
    {
        return getProcessing(b) != null;
    }

    public void registerRecipe(MachineRecipe recipe) {
        recipe.setTicks(recipe.getTicks());
        this.recipes.add(recipe);
    }

    public void registerRecipe(int seconds, ItemStack[] input, ItemStack[] output) {
        registerRecipe(new MachineRecipe(seconds, input, output));
    }

    public List<MachineRecipe> getMachineRecipes() {
        return this.recipes;
    }

    private Inventory inject(Block b) {
        int size = BlockStorage.getInventory(b).toInventory().getSize();
        Inventory inv = Bukkit.createInventory(null, size);
        for (int i = 0; i < size; i++) {
            inv.setItem(i, new CustomItem(Material.COMMAND, " &4ALL YOUR PLACEHOLDERS ARE BELONG TO US", 0));
        }
        for (int slot : getOutputSlots()) {
            inv.setItem(slot, BlockStorage.getInventory(b).getItemInSlot(slot));
        }
        return inv;
    }

    protected boolean fits(Block b, ItemStack[] items)
    {
        return inject(b).addItem(items).isEmpty();
    }

    protected void pushMainItems(Block b, ItemStack[] items) {
        if (BlockStorage.getBlockInfo(b, "random-code").equals(String.valueOf(invertBinary(BlockStorage.getBlockInfo(b, "set-code"))))) {
            //产出物品并重新随机随机数
            Random random = new Random();
            BlockStorage.addBlockInfo(b, "random-code", String.valueOf(random.nextInt(127)));
            Inventory inv = inject(b);
            inv.addItem(items);
            for (int slot : getOutputSlots()) {
                BlockStorage.getInventory(b).replaceExistingItem(slot, inv.getItem(slot));
            }
            b.getLocation().getWorld().playSound(b.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        } else {
            int randomCode = Integer.valueOf(BlockStorage.getBlockInfo(b, "random-code"));
            if (Math.abs(randomCode - invertBinary(BlockStorage.getBlockInfo(b, "last-code"))) > Math.abs(randomCode - invertBinary(BlockStorage.getBlockInfo(b, "set-code")))) {
                b.getLocation().getWorld().playSound(b.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
            } else {
                b.getLocation().getWorld().playSound(b.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
            }
        }
    }

    @Override
    public void register(boolean slimefun) {
        addItemHandler(new BlockTicker()
        {
            @Override
            public void tick(Block b, SlimefunItem sf, Config data)
            {
                CreatorMachine.this.tick(b);
            }

            @Override
            public void uniqueTick() {}

            @Override
            public boolean isSynchronized()
            {
                return false;
            }
        });
        super.register(slimefun);
    }

    protected void tick(Block b) {
        ItemMeta im;
        if (isProcessing(b))
        {
            int timeleft = progress.get(b).intValue();
            if (timeleft > 0)
            {
                ItemStack item = getProgressBar().clone();
                item.setDurability(MachineHelper.getDurability(item, timeleft, processing.get(b).getTicks()));
                im = item.getItemMeta();
                im.setDisplayName(" ");
                List<String> lore = new ArrayList();
                lore.add(MachineHelper.getProgress(timeleft, processing.get(b).getTicks()));
                lore.add("");
                lore.add(MachineHelper.getTimeLeft(timeleft / 2));
                im.setLore(lore);
                item.setItemMeta(im);

                BlockStorage.getInventory(b).replaceExistingItem(40, item);
                if (ChargableBlock.isChargable(b))
                {
                    if (ChargableBlock.getCharge(b) < getEnergyConsumption()) {
                        return;
                    }
                    ChargableBlock.addCharge(b, -getEnergyConsumption());
                    progress.put(b, Integer.valueOf(timeleft - 1));
                }
                else
                {
                    progress.put(b, Integer.valueOf(timeleft - 1));
                }
            }
            else
            {
                BlockStorage.getInventory(b).replaceExistingItem(40, new CustomItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)15), " "));
                pushMainItems(b, recipes.get(Integer.valueOf(BlockStorage.getBlockInfo(b, "output-item"))).getOutput());
                progress.remove(b);
                processing.remove(b);
            }
        }
        else
        {
            MachineRecipe r = null;
            Map<Integer, Integer> found = new HashMap();
            for (MachineRecipe recipe : this.recipes)
            {
                for (ItemStack input : recipe.getInput()) {
                    for (int slot : getInputSlots()) {
                        if (SlimefunManager.isItemSimiliar(BlockStorage.getInventory(b).getItemInSlot(slot), input, true))
                        {
                            if (input != null) {
                                found.put(Integer.valueOf(slot), Integer.valueOf(input.getAmount()));
                            }
                            //break;
                        }
                    }
                }
                if (found.size() == recipe.getInput().length)
                {
                    r = recipe;
                    break;
                }
                found.clear();
            }
            if (r != null)
            {
                if (!fits(b, recipes.get(Integer.valueOf(BlockStorage.getBlockInfo(b, "output-item"))).getOutput())) {
                    return;
                }
                for (Map.Entry<Integer, Integer> entry : found.entrySet()) {
                    BlockStorage.getInventory(b).replaceExistingItem(entry.getKey().intValue(), InvUtils.decreaseItem(BlockStorage.getInventory(b).getItemInSlot(entry.getKey().intValue()), entry.getValue().intValue()));
                }
                processing.put(b, r);
                progress.put(b, Integer.valueOf(r.getTicks()));
            }
        }
    }

    private int invertBinary(String binary) {
        char[] chars = binary.toCharArray();
        int result = 0;
        int i = 6;
        for (char c : chars) {
            if (c == '1') {
                result += Math.pow(2, i);
            }
            --i;
        }
        return result;
    }
}
