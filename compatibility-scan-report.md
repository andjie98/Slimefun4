# Slimefun4 1.12.2 兼容性扫描报告

生成时间：2026-04-25

---

## 一、构建配置问题

| 问题 | 当前值 | 目标值 | 严重程度 |
|------|--------|--------|----------|
| Java 编译目标 | Java 11 | Java 8 | **CRITICAL** |
| Paper API 版本 | 1.20+ | 1.12.2 | **CRITICAL** |

**说明**：Java 8 是 1.12.2 服务端最低要求。必须修改 `build.gradle.kts`：

```kotlin
java.toolchain.languageVersion.set(JavaLanguageVersion.of(8))
tasks.compileJava { options.release.set(8) }
```

---

## 二、API 断层分类清单

### 1. BlockData API（1.13+ 独有）— **CRITICAL**

**影响范围**：结构性 API 变化，无法直接映射

| 文件 | 使用点 | 问题描述 |
|------|--------|----------|
| `CompatibilityUtil.java` | `org.bukkit.block.data.BlockData`, `Ageable`, `WallSign` | BlockData 接口在 1.12.2 不存在 |
| `CompatibilityUtil.java` | `blockData.getPlacementMaterial()` | 1.19.4+ 方法，需兼容层 |
| 多处 | `block.getBlockData()` / `block.setBlockData()` | 1.12.2 使用 `block.getState().getData()` (MaterialData) |

**处理策略**：需要建立 `LegacyBlockDataCompat` 类，使用反射或 MaterialData 替代。

---

### 2. NamespacedKey / PersistentDataContainer（1.13+ / 1.14+）— **CRITICAL**

**影响范围**：350+ 处使用

| 文件/模块 | 使用点 | 问题描述 |
|-----------|--------|----------|
| `SlimefunRegistry.java` | `new NamespacedKey(...)` | NamespacedKey 是 1.13+ API |
| `BlockDataService.java` | `PersistentDataContainer`, `PersistentDataHolder` | 1.14+ API |
| `SlimefunUtils.java` | `meta.getPersistentDataContainer()` | 物品数据存储 |
| `LocalizationService.java` | `p.getPersistentDataContainer()` | 玩家语言存储 |
| `ChargeUtils.java` | `container.set(key, type, value)` | 物品充能数据 |
| `TagParser.java` | `Bukkit.getTag(Tag.REGISTRY_*)` | Tag API 是 1.13+ |

**处理策略**：
- NamespacedKey：可模拟实现或使用字符串键替代
- PersistentDataContainer：需使用 NBT API 或配置文件替代存储

---

### 3. Registry API（1.21+）— **HIGH**

| 文件 | 使用点 | 问题描述 |
|------|--------|----------|
| `CompatibilityUtil.java:80` | `Registry.ATTRIBUTE.get(...)` | Registry 是 1.21+ API |
| `SoundEffect.java:234` | `Registry.SOUNDS.get(...)` | 1.21+ API |

**处理策略**：已有 `CompatibilityUtil.getMaxHealth()` 使用版本判断，需扩展。

---

### 4. Particle 粒子效果（1.13+ 大量重命名）— **HIGH**

**影响范围**：43 处

| 状态 | 说明 |
|------|------|
| ✅ 已有兼容层 | `VersionedParticle.java` 处理 1.20.5 重命名 |
| ⚠️ 需扩展 | 1.12.2 粒子 API 与 1.13+ 有根本差异，部分粒子不存在 |

**关键粒子**：
- `PORTAL` - 1.12.2 存在
- `HEART` - 1.12.2 存在
- `CRIMSON_SPORE` - 1.16+ 才有，需降级处理

---

### 5. Sound 声音效果（1.13+ 大量重命名）— **HIGH**

**影响范围**：210 处

| 状态 | 说明 |
|------|------|
| ✅ 已有兼容层 | `VersionedSound.java` 处理部分版本差异 |
| ⚠️ 需扩展 | 1.12.2 声音枚举名称完全不同，需完整映射表 |

**示例**：
- `Sound.ENTITY_ENDERMAN_TELEPORT` → 1.12.2: `Sound.ENDERMAN_TELEPORT`
- `Sound.BLOCK_ANVIL_USE` → 1.12.2: `Sound.ANVIL_USE`

---

### 6. Potion / PotionEffectType（1.13+ / 1.20.5+ 重命名）— **HIGH**

**影响范围**：149 处

| 状态 | 说明 |
|------|------|
| ✅ 已有兼容层 | `VersionedPotionType.java`, `VersionedPotionEffectType.java` |
| ⚠️ API 变化 | `PotionMeta.setBasePotionType()` 是 1.20.5+ API |
| ⚠️ API 变化 | 1.12.2 使用 `PotionData` 类而非 `PotionType` 枚举 |

**关键文件**：
- `FluidPump.java:216` - `meta.setBasePotionType(PotionType.WATER)`
- `SlimefunUtils.java:514-526` - `PotionMeta.hasBasePotionType()` / `getBasePotionType()`
- `AutoBrewer.java` - 大量 PotionType 使用

---

### 7. Material 材质命名（1.13+ 扁平化）— **MEDIUM**

**影响范围**：4 处直接调用，但整个项目依赖正确的 Material 枚举

| 文件 | 使用点 | 问题描述 |
|------|--------|----------|
| `LumberAxe.java:95` | `Material.valueOf("STRIPPED_" + ...)` | STRIPPED_* 是 1.13+ 新材质 |
| `TableSaw.java:104` | `Material.getMaterial(materialName)` | 需版本映射 |
| `CompatibilityUtil.java` | `Material.getMaterial(...)` | 已有部分处理 |

**处理策略**：XSeries 的 `XMaterial` 可以解决大部分材质命名问题。

---

### 8. EntityType 实体类型（1.13+ 重命名）— **MEDIUM**

**影响范围**：3 处

| 文件 | 使用点 | 问题描述 |
|------|--------|----------|
| `FireworkUtils.java:54` | `EntityType.valueOf("FIREWORK")` | 1.12.2: 无 FIREWORK，使用 EntityType 父类 |
| `RecipeType.java:274` | `EntityType.valueOf(mob)` | 需版本映射 |
| `AbstractMonsterSpawner.java:62` | `EntityType.valueOf(...)` | 需版本映射 |

---

### 9. Attribute 属性系统（1.13+ 重命名）— **LOW**

| 状态 | 说明 |
|------|------|
| ✅ 已有兼容层 | `CompatibilityUtil.getMaxHealth()` 已处理 GENERIC_MAX_HEALTH → max_health |

---

## 三、现有兼容层清单

项目已建立以下兼容层，需扩展以支持 1.12.2：

| 文件 | 功能 | 当前支持版本 | 需扩展 |
|------|------|--------------|--------|
| `CompatibilityUtil.java` | BlockData、Attribute、isConnected | 1.16+ → 1.21+ | ✅ 需支持 1.12.2 |
| `VersionedParticle.java` | Particle 重命名 | 1.20.5+ | ✅ 需支持 1.12.2 |
| `VersionedSound.java` | Sound 重命名 | 部分 | ✅ 需完整 1.12.2 映射 |
| `VersionedPotionType.java` | PotionType 重命名 | 1.20.5+ | ✅ 需支持 1.12.2 PotionData |
| `VersionedPotionEffectType.java` | PotionEffectType 重命名 | 1.20.5+ | ✅ 需支持 1.12.2 |
| `MinecraftVersion.java` | 版本判断枚举 | 1.16+ → 26.1 | ✅ 需添加 1.12.2 |
| `SlimefunExtended.java` | 版本判断工具 | 存在 | ✅ 需验证 1.12.2 支持 |

---

## 四、依赖兼容性风险

| 依赖 | 当前版本 | Java 8 兼容风险 |
|------|----------|-----------------|
| dough-api | 未知 | ⚠️ 需检查 |
| HikariCP | 现代版 | ⚠️ 需降级或检查 |
| PostgreSQL JDBC | 现代版 | ⚠️ 需检查 |
| unirest-java | 现代版 | ⚠️ 可能需降级 |
| guizhanlib | 未知 | ⚠️ 需检查 |

---

## 五、处理优先级建议

### 第一优先级（必须解决才能编译）
1. **构建降级**：Java 11 → Java 8，Paper API → 1.12.2 Spigot API
2. **BlockData 替代**：建立 LegacyBlockDataCompat
3. **PersistentDataContainer 替代**：建立替代存储方案

### 第二优先级（解决后可基本运行）
4. **NamespacedKey 模拟**：建立 LegacyNamespacedKey
5. **Material 映射**：引入 XSeries，建立 XSeriesCompat
6. **Sound/Potion/Particle 映射**：扩展现有 Versioned* 类

### 第三优先级（边缘功能降级）
7. **Registry API 替代**：在 1.12.2 下禁用或反射替代
8. **Tag API 替代**：在 1.12.2 下使用硬编码材质列表

---

## 六、下一步建议

1. **确认构建降级可行性**：检查所有依赖是否支持 Java 8
2. **建立 LegacyCompat 模块**：
   - `LegacyBlockDataCompat`
   - `LegacyPersistentDataCompat`
   - `XSeriesCompat`（引入 XSeries）
3. **扩展 MinecraftVersion.java**：添加 `MINECRAFT_1_12_2` 常量
4. **分模块处理**：按优先级逐步替换不兼容 API

---

*报告结束*