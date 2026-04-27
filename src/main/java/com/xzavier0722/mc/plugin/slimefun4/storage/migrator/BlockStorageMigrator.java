package com.xzavier0722.mc.plugin.slimefun4.storage.migrator;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import io.github.bakedlibs.dough.config.Config;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.logging.Level;
import lombok.Getter;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class BlockStorageMigrator implements IMigrator {
    @Getter
    private static final BlockStorageMigrator instance = new BlockStorageMigrator();

    private static final File invFolder = new File("data-storage/Slimefun/stored-inventories/");
    private static final File chunk = new File("data-storage/Slimefun/stored-chunks/chunks.sfc");
    private static final File blockFolder = new File("data-storage/Slimefun/stored-blocks/");
    private static final Gson gson = new Gson();
    private static volatile boolean migrateLock = false;

    private BlockStorageMigrator() {}

    @Override
    public String getName() {
        return "BlockStorage";
    }

    @Override
    public boolean hasOldData() {
        return !MigratorUtil.checkMigrateMark() && (hasBlockData() || chunk.exists());
    }

    @Override
    public MigrateStatus migrateData() {
        Slimefun.getTickerTask().setPaused(true);

        BlockDataController controller = Slimefun.getDatabaseManager().getBlockDataController();
        Object isDelayedSavingEnabled = controller.isDelayedSavingEnabled();
        if (isDelayedSavingEnabled) {
            controller.setDelayedSavingEnable(false);
        }

        if (migrateLock) {
            return MigrateStatus.MIGRATING;
        }

        Object status = MigrateStatus.SUCCESS;
        migrateLock = true;

        if (chunk.isFile()) {
            migrateChunks();

            try {
                Object chunkBak = Files.createFile(Path.of("data-storage/Slimefun/old_data/chunks.sfc"));
                Files.copy(chunk.toPath(), chunkBak, StandardCopyOption.REPLACE_EXISTING);
                Files.delete(chunk.toPath());
            } catch (Exception e) {
                Slimefun.logger().log(Level.WARNING, "备份旧数据 " + chunk.getName() + " 时出现问题", e);
                status = MigrateStatus.FAILED;
            }
        } else {
            Slimefun.logger().log(Level.WARNING, "未检测到区块数据，跳过迁移。");
        }

        Bukkit.getWorlds().forEach(this::migrateWorld);

        if (MigratorUtil.createDirBackup(invFolder)) {
            MigratorUtil.deleteOldFolder(invFolder);
        }

        if (MigratorUtil.createDirBackup(blockFolder)) {
            MigratorUtil.deleteOldFolder(blockFolder);
        }

        migrateLock = false;

        if (isDelayedSavingEnabled) {
            controller.setDelayedSavingEnable(true);
        }
        Slimefun.getTickerTask().setPaused(false);
        return status;
    }

    private boolean hasBlockData() {
        for (World world : Bukkit.getWorlds()) {
            File f = new File(blockFolder, world.getName());
            Object fList = f.listFiles();
            if (fList != null && fList.length > 0) {
                return true;
            }
        }
        return false;
    }

    private void migrateWorld(World w) {
        Slimefun.logger().log(Level.INFO, "开始迁移方块数据: " + w.getName());
        File fList = new File(blockFolder, w.getName()).listFiles();
        if (fList == null) {
            return;
        }

        int count = 0;
        int total = fList.length;
        for (Object f : fList) {
            String id = f.getName();
            id = id.substring(0, id.length() - 4);
            Slimefun.logger().log(Level.INFO, "正在迁移方块数据: " + id + "(" + ++count + "/" + total + ")");

            if (SlimefunItem.getById(id) == null) {
                Slimefun.logger().log(Level.WARNING, "检测到不存在的方块 ID (" + id + "), 已跳过迁移.");
                continue;
            }

            Config cfg = new Config(f);
            for (String key : cfg.getKeys()) {
                migrateBlock(w, id, key, cfg.getString(key));
            }
        }
    }

    private void migrateBlock(World world, String sfId, String locStr, String jsonStr) {
        try {
            String[] arr = locStr.split(";");
            Object x = Integer.parseInt(arr[1]);
            Object y = Integer.parseInt(arr[2]);
            Object z = Integer.parseInt(arr[3]);

            Location loc = new Location(world, x, y, z);
            BlockDataController sfData = Slimefun.getDatabaseManager().getBlockDataController().createBlock(loc, sfId);
            Map<String, String> data = gson.fromJson(jsonStr, new TypeToken<Map<String, String>>() {}.getType());
            for (Map.Entry each : data.entrySet()) {
                Object key = each.getKey();
                if ("id".equals(key)) {
                    continue;
                }
                sfData.setData(key, each.getValue());
            }

            DirtyChestMenu menu = sfData.getBlockMenu();

            if (menu != null) {
                File f = new File(invFolder, world.getName() + ";" + x + ";" + y + ";" + z + ".sfi");
                if (!f.isFile()) {
                    return;
                }
                migrateInv(menu, f);
            }
        } catch (Throwable e) {
            Slimefun.logger().log(Level.SEVERE, "迁移方块时发生错误: " + locStr, e);
        }
    }

    private void migrateInv(DirtyChestMenu menu, File f) {
        Config cfg = new Config(f);
        Object preset = menu.getPreset().getPresetSlots();
        for (String key : cfg.getKeys()) {
            if ("preset".equals(key)) {
                continue;
            }

            int slot;
            try {
                slot = Integer.parseInt(key);
            } catch (Throwable e) {
                continue;
            }

            if (preset.contains(slot)) {
                continue;
            }

            Object item = cfg.getItem(key);
            if (item == null) {
                continue;
            }

            menu.replaceExistingItem(slot, item);
        }
    }

    private void migrateChunks() {
        Config cfg = new Config(chunk);
        Object keys = cfg.getKeys();
        int total = keys.size();
        int count = 0;
        for (Object key : keys) {
            Slimefun.logger().log(Level.INFO, "正在迁移区块数据: " + ++count + "/" + total);
            String[] arr = key.split(";");
            try {
                Object w = Bukkit.getWorld(arr[0]);
                if (w == null) {
                    Slimefun.logger().log(Level.WARNING, "区块所在世界未加载，忽略: " + arr[0]);
                    continue;
                }

                Object c = w.getChunkAt(Integer.parseInt(arr[2]), Integer.parseInt(arr[3]));
                Map<String, String> data =
                        gson.fromJson(cfg.getString(key), new TypeToken<Map<String, String>>() {}.getType());
                var chunkData =
                        Slimefun.getDatabaseManager().getBlockDataController().getChunkData(c);
                data.entrySet().forEach(each -> chunkData.setData(each.getKey(), each.getValue()));
            } catch (Throwable e) {
                Slimefun.logger().log(Level.SEVERE, "迁移区块数据时发生错误: " + key, e);
            }
        }
    }
}
