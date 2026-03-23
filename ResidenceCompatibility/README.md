# Residence Slimefun Compatibility Plugin

让 Residence 插件支持旧版 Slimefun4 (me.mrCookieSlime 版本) 的兼容层插件。

## 问题背景

Residence 插件在启动时会尝试加载 Slimefun 支持，但它默认查找的是新版 Slimefun 的类：
```
io.github.thebusybiscuit.slimefun4.implementation.Slimefun
```

而旧版 Slimefun4 的包名是：
```
me.mrCookieSlime.Slimefun
```

这导致 Residence 无法找到 Slimefun 类，报错：
```
[Residence] Failed to initialize SlimeFun support
```

## 解决方案

这个兼容层插件提供了 `com.bekvon.bukkit.residence.slimeFun.SlimefunManager` 类，
Residence 会调用这个类来与 Slimefun 交互。

## 安装方法

1. **确保已安装以下插件**：
   - Slimefun4 v4.1.15+
   - CS-CoreLib v1.5.18+
   - Residence 5.x
   - CMILib

2. **将插件放入 plugins 文件夹**：
   ```
   plugins/
   ├── Slimefun v4.1.15.jar
   ├── CS-CoreLib-v1.5.18.jar
   ├── Residence.jar
   ├── CMILib.jar
   └── residence-slimefun-compat-1.0.0.jar  ← 这个插件
   ```

3. **重启服务器**

4. **检查启动日志**，应该看到：
   ```
   [ResidenceSlimefunCompat] Successfully initialized Slimefun4 (me.mrCookieSlime version) support
   [Residence] Successfully initialized Slimefun4 (me.mrCookieSlime version) support
   ```

## 功能

- ✅ 让 Residence 能够识别 Slimefun 方块
- ✅ 支持领地内的 Slimefun 机器保护
- ✅ 消除启动时的 `NoClassDefFoundError` 错误

## 命令

- `/rslimefun reload` - 重新加载兼容层（需要 OP 权限）

## 权限

- `rslimefun.reload` - 允许使用 reload 命令（默认：OP）

## 兼容性

- **Minecraft 版本**: 1.12.2
- **Slimefun**: me.mrCookieSlime 版本 (v4.1.15+)
- **Residence**: 5.x
- **Java**: 8+

## 编译方法

```bash
cd ResidenceCompatibility
mvn clean package
```

编译产物在 `target/residence-slimefun-compat-1.0.0.jar`

## 注意事项

1. 这个插件**必须**在 Slimefun 之后加载（已在 plugin.yml 中设置 `depend: [Slimefun]`）
2. 插件是可选的 - 如果服务器没有安装 Residence，可以不安装此插件
3. 如果使用的是新版 Slimefun (io.github.thebusybiscuit 版本)，不需要此插件

## 许可证

与 Slimefun4 保持一致（GNU GPLv3）
