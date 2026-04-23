# Slimefun 4 跨版本兼容方案（最低兼容到 Minecraft 1.12.2）

## 背景

当前项目基于 Slimefun 4，已有 1.16+ 兼容实现。目标是让该项目最低兼容至 Minecraft 1.12.2，同时继续支持 1.16+。

`XSeries` 适合用于解决 Minecraft 版本之间的枚举/常量名字差异，但它不能替代所有 Bukkit API 版本兼容工作。方案的核心思想是：

- 使用 `XSeries` 统一处理 `Material`、`Sound`、`Enchantment`、`PotionType` 等常量差异
- 保留并扩展已有的版本兼容层（反射、事件兼容、API 方法签名分支）
- 将构建目标降低到 Java 8，确保 1.12.2 可以加载

---

## 方案一：单一Jar兼容包（推荐）

### 1. 构建目标调整

最重要的一步是让插件可以在 Java 8 环境下编译：

- `build.gradle.kts` 里修改为 Java 8 toolchain
- `tasks.compileJava.options.release.set(8)`

这意味着整个项目必须使用 Java 8 语法，并且所有依赖也必须兼容 Java 8。

> 这一步是最低兼容 1.12.2 的前提。否则即使兼容代码齐全，插件也会因为 Java 版本不匹配而无法加载。

### 2. 添加 `XSeries` 依赖

在 `build.gradle.kts` 中加入：

```kotlin
implementation("com.cryptomorin:XSeries:9.2.3")
```

如果使用 ShadowJar 打包，需要一并 relocate：

```kotlin
relocate("com.cryptomorin.xseries", "io.github.thebusybiscuit.slimefun4.libraries.xseries")
```

### 3. 引入兼容层类

新增辅助兼容类，例如：`XSeriesCompat`。

它负责从名称解析到当前服务器版本可用的 Bukkit 枚举对象，并且加入必要的回退逻辑。

### 4. 改造常量解析点

优先改造以下类型的逻辑：

- `Material.valueOf(...)` / `Material.getMaterial(...)`
- `Sound.valueOf(...)`
- `Enchantment.getByName(...)`
- `PotionType.getByName(...)`
- `Particle.valueOf(...)`
- `EntityType.valueOf(...)`
- `DyeColor`、`TreeSpecies`、`EquipmentSlot` 等版本敏感常量

改造后统一走 `XSeriesCompat`，并在必要时保留 `fallback`。

### 5. 保留并扩展现有版本兼容层

当前项目已有如下兼容层：

- `city.norain.slimefun4.compatibillty.VersionedEvent`
- `city.norain.slimefun4.SlimefunExtended.getServerVerDetail(...)`
- `io.github.thebusybiscuit.slimefun4.api.MinecraftVersion` 版本判断
- `MinecraftVersionUtil`

这些兼容层必须继续存在，并额外补齐 1.13 以前的逻辑分支。

### 6. 重点改造区域

建议优先处理：

- `src/main/java/**/utils/compatibility/**`
- `VersionedPotionType.java`
- `VersionedItemFlag.java`
- `VersionedEnchantment.java`
- `FireworkUtils.java`
- `JsonUtils.java`
- 各种 item/recipe 注册代码
- GUI/Inventory 事件处理代码

### 7. 版本分支策略

按范围划分兼容分支：

- `1.12.2` / `1.13` 之前：旧 Material/API 语义
- `1.13` - `1.15`：物料命名变动后期
- `1.16+`：当前主线兼容

`XSeries` 处理名称兼容，现有兼容层处理 API 变化。

---

## 方案二：双版本构建（若单包代价过高）

如果单一 Jar 的 Java 8 兼容代价过大，可采用双版本构建：

- `slimefun-legacy`：支持 1.12.2，Java 8，包含 legacy 兼容代码
- `slimefun-modern`：支持 1.16+，Java 21，继续当前现代实现

优点：每个版本可以单独优化，缺点：维护成本更高。

---

## 关键实现细节

### A. `XSeriesCompat` 样板

```java
public final class XSeriesCompat {

    public static Material parseMaterial(String name) {
        return XMaterial.matchXMaterial(name)
            .map(XMaterial::parseMaterial)
            .orElse(Material.STONE);
    }

    public static Sound parseSound(String name) {
        return XSound.matchXSound(name)
            .map(XSound::parseSound)
            .orElse(null);
    }

    public static Enchantment parseEnchantment(String name) {
        return XEnchantment.matchXEnchantment(name)
            .map(XEnchantment::parseEnchantment)
            .orElse(null);
    }

    // 其他辅助方法：Particle、EntityType、PotionType 等
}
```

### B. `Material` 解析回退

```java
XMaterial xMaterial = XMaterial.matchXMaterial(name).orElse(null);
if (xMaterial != null) {
    return xMaterial.parseMaterial();
}
return Material.getMaterial(name);
```

### C. 1.12.2 专用修正

`1.12.2` 下常见问题：

- `Material` 名称和参数不一致
- `Sound` 枚举名大量不同
- `InventoryClickEvent.getClickedInventory()` 方法签名变化
- `BlockExplodeEvent` 构造器差异
- `PotionType` / `Enchantment` 名称差异
- `DyeColor` 与 颜色命名

这类问题必须在兼容层里显式处理。

---

## 具体开发任务清单

1. 修改 `build.gradle.kts`：Java 8 编译目标 + XSeries 依赖 + Shadow relocate
2. 新增 `src/main/java/city/norain/slimefun4/compatibility/XSeriesCompat.java`
3. 审计并替换项目中所有直接使用版本敏感常量的代码
4. 扩展 `VersionedEvent`、`SlimefunExtended`、`MinecraftVersionUtil`，补齐 1.12/1.13 判断
5. 编写 1.12.2 / 1.16+ 的单元测试和加载测试
6. 在实际 1.12.2、1.15、1.16 环境中逐步验证
7. 处理 `plugin.yml`、`dependencies`、`softdepend` 兼容性

---

## 重要风险点

- `Java 8` 编译目标会限制当前项目语法与依赖
- 1.12.2 中 `Bukkit` API 变化仍然很多，不能仅靠 `XSeries`
- `XSeries` 支持范围取决于版本号，部分新 MC 语义可能需要额外补丁
- 如果当前依赖库不兼容 Java 8，则需要替换或删减

---

## 最终建议

- 推荐优先实现“单一兼容 Jar + Java 8 + XSeries”方案
- 如果评估发现 Java 8 兼容成本过高，再考虑“legacy / modern 双构建”方案
- `XSeries` 用于“枚举常量兼容”，现有版本兼容层继续处理“API/方法差异”

该方案已经适配你要求的最低兼容范围：`1.12.2` 至 `1.16+`。
