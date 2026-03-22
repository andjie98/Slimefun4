# 命令与权限完全指南

本页面详细介绍 Slimefun4 的所有命令和权限设置。

## 📋 目录

- [基础命令](#基础命令)
- [管理员命令](#管理员命令)
- [研究相关命令](#研究相关命令)
- [物品相关命令](#物品相关命令)
- [权限节点详解](#权限节点详解)
- [配置与优化](#配置与优化)

---

## 🎮 基础命令

### /slimefun (别名：/sf)

主命令，用于访问 Slimefun 的各种功能。

#### 命令格式
```bash
/sf <子命令> [参数]
```

#### 可用子命令

##### 1. /sf guide (或 /sf)
**用途**: 打开 Slimefun 指南
**权限**: `slimefun.command.guide`
**别名**: 无

**使用方法**:
```bash
/sf guide          # 打开指南
/sf                # 同样打开指南
```

**说明**:
- 新手玩家首次加入服务器会自动获得指南（如果启用）
- 可在配置文件中设置默认使用书本界面

##### 2. /sf open_guide
**用途**: 打开指南（无需书本）
**权限**: `slimefun.command.open_guide`
**别名**: 无

**使用方法**:
```bash
/sf open_guide     # 直接打开 GUI 界面
```

**说明**:
- 不需要手持研究指南
- 管理员专属命令
- 方便快速查看配方

##### 3. /sf versions
**用途**: 查看 Slimefun 版本信息
**权限**: `slimefun.command.versions`
**别名**: 无

**使用方法**:
```bash
/sf versions       # 显示版本信息
```

**输出示例**:
```
Slimefun v4.1.15
CS-CoreLib v1.5.18
Server: Paper 1.12.2
Java 版本：1.8.0_xxx
```

##### 4. /sf timings
**用途**: 查看机器运行统计
**权限**: `slimefun.command.timings`
**别名**: 无

**使用方法**:
```bash
/sf timings        # 显示所有机器统计
/sf timings clear  # 清除统计数据
```

**输出信息**:
- 机器运行次数
- 处理物品数量
- 能源消耗统计
- 错误计数

**用途**:
- 诊断机器故障
- 优化生产线
- 监控服务器性能

##### 5. /sf teleporter
**用途**: 管理 GPS 传送网络
**权限**: `slimefun.command.teleporter`
**别名**: 无

**使用方法**:
```bash
/sf teleporter     # 打开传送管理界面
```

**功能**:
- 查看已解锁的传送点
- 设置个人传送点
- 管理传送权限

---

## 🔧 管理员命令

### 物品管理命令

#### /sf cheat
**用途**: 直接获取物品（作弊模式）
**权限**: `slimefun.cheat.items`
**等级**: 管理员

**使用方法**:
```bash
/sf cheat <物品 ID> [数量]
```

**示例**:
```bash
/sf cheat STEEL_INGOT 64        # 获取 64 个钢锭
/sf cheat ELECTRIC_FURNACE 1    # 获取 1 个电动熔炉
```

**物品 ID 查询**:
- 使用 `/sf guide` 查看
- 查看 Wiki 物品列表
- 使用 Tab 补全

**注意事项**:
- ⚠️ 作弊获得的物品可能影响游戏平衡
- ⚠️ 仅在测试或创造模式使用
- ⚠️ 某些物品需要前置研究

#### /sf give
**用途**: 给予玩家物品
**权限**: `slimefun.cheat.items`
**等级**: 管理员

**使用方法**:
```bash
/sf give <玩家> <物品 ID> [数量]
```

**示例**:
```bash
/sf give Steve STEEL_PICKAXE 1    # 给予 Steve 钢镐
/sf give Alex BACKPACK_LARGE 1    # 给予 Alex 大背包
```

### 研究管理命令

#### /sf research
**用途**: 管理玩家研究进度
**权限**: `slimefun.cheat.researches`
**等级**: 管理员

**子命令**:

##### research unlock
解锁玩家的所有研究或指定研究。

```bash
/sf research unlock <玩家> all           # 解锁所有研究
/sf research unlock <玩家> <物品 ID>     # 解锁指定研究
```

**示例**:
```bash
/sf research unlock Steve all            # 解锁 Steve 的所有研究
/sf research unlock Alex STEEL_INGOT     # 解锁 Alex 的钢锭研究
```

##### research reset
重置玩家的研究进度。

```bash
/sf research reset <玩家>                # 重置指定玩家
/sf research reset all                   # 重置所有玩家
```

**警告**:
- ⚠️ 此操作不可逆
- ⚠️ 玩家会失去所有研究进度
- ⚠️ 建议提前备份数据

##### research list
查看玩家的研究进度。

```bash
/sf research list <玩家>                 # 列出研究进度
```

**输出**:
- 已研究物品数量
- 研究完成度百分比
- 当前研究等级

### 统计管理命令

#### /sf stats
**用途**: 查看玩家统计信息
**权限**: `slimefun.stats.others`
**等级**: 管理员

**使用方法**:
```bash
/sf stats <玩家>         # 查看指定玩家统计
/sf stats                # 查看自己的统计
```

**显示信息**:
- 研究完成度
- 制作物品数量
- 使用机器次数
- 游戏时间统计

#### /sf stats clear
**用途**: 清除统计数据
**权限**: `slimefun.stats.others`
**等级**: 管理员

**使用方法**:
```bash
/sf stats clear <玩家>   # 清除指定玩家统计
/sf stats clear all      # 清除所有统计
```

### 世界管理命令

#### /sf world
**用途**: 管理 Slimefun 世界设置
**权限**: `slimefun.command.world`
**等级**: 管理员

**子命令**:

##### world whitelist
设置世界 whitelist。

```bash
/sf world whitelist <世界> enable    # 启用世界
/sf world whitelist <世界> disable   # 禁用世界
```

**说明**:
- 控制 Slimefun 物品在特定世界的使用
- 可单独设置每个物品的启用状态

##### world list
列出所有世界的状态。

```bash
/sf world list                       # 显示世界列表
```

### 备份管理命令

#### /sf backup
**用途**: 管理 Slimefun 备份
**权限**: `slimefun.command.backup`
**等级**: 管理员

**子命令**:

##### backup create
创建手动备份。

```bash
/sf backup create                    # 创建备份
```

**备份内容**:
- 玩家研究数据
- 机器存储数据
- 货物网络数据
- 自定义配置

##### backup restore
恢复备份。

```bash
/sf backup restore <备份文件名>      # 恢复指定备份
```

**警告**:
- ⚠️ 会覆盖当前数据
- ⚠️ 建议先备份当前数据
- ⚠️ 服务器可能需要重启

##### backup list
列出所有备份。

```bash
/sf backup list                      # 显示备份列表
```

### 调试命令

#### /sf debug
**用途**: 调试模式
**权限**: `slimefun.command.debug`
**等级**: 管理员

**使用方法**:
```bash
/sf debug toggle                     # 开关调试模式
/sf debug info                       # 显示调试信息
```

**调试信息**:
- 已加载的物品 ID
- 研究树状态
- 机器运行状态
- 错误日志

**用途**:
- 诊断插件问题
- 开发新物品
- 排查错误

---

## 🔍 研究相关命令

### /sf research (玩家命令)

#### research list
**用途**: 查看自己的研究进度
**权限**: 所有玩家

**使用方法**:
```bash
/sf research list                    # 显示研究进度
```

**输出**:
- 当前研究等级
- 已研究物品数量
- 总研究完成度
- 下一级所需经验

#### research info
**用途**: 查看特定物品的研究信息
**权限**: 所有玩家

**使用方法**:
```bash
/sf research info <物品 ID>          # 查看研究要求
```

**显示信息**:
- 所需研究等级
- 经验值消耗
- 前置研究物品
- 解锁状态

### /sf unlock

#### unlock self
**用途**: 解锁自己的研究
**权限**: 所有玩家（需要足够经验和等级）

**使用方法**:
```bash
/sf unlock <物品 ID>                 # 解锁研究
```

**说明**:
- 自动扣除经验值
- 需要满足等级要求
- 需要前置研究已解锁

#### unlock others
**用途**: 解锁其他玩家的研究
**权限**: `slimefun.cheat.researches`
**等级**: 管理员

**使用方法**:
```bash
/sf unlock <玩家> <物品 ID>          # 为玩家解锁研究
/sf unlock <玩家> all                # 解锁玩家所有研究
```

---

## 📦 物品相关命令

### /sf item

#### item info
**用途**: 查看物品详细信息
**权限**: 所有玩家

**使用方法**:
```bash
/sf item info <物品 ID>              # 查看物品信息
```

**显示信息**:
- 物品名称和描述
- 合成配方
- 研究要求
- 能源消耗（如果是机器）
- YouTube 教程链接
- Wiki 页面链接

#### item give
**用途**: 给予物品（管理员）
**权限**: `slimefun.cheat.items`

**使用方法**:
```bash
/sf item give <玩家> <物品 ID> [数量]
```

#### item cheat
**用途**: 快速获得物品
**权限**: `slimefun.cheat.items`

**使用方法**:
```bash
/sf item cheat <物品 ID> [数量]
```

### /sf recipe

#### recipe show
**用途**: 查看物品合成配方
**权限**: 所有玩家

**使用方法**:
```bash
/sf recipe show <物品 ID>            # 显示配方
```

**显示方式**:
- 图形化配方表
- 材料列表
- 所需机器

#### recipe search
**用途**: 搜索使用特定材料的配方
**权限**: 所有玩家

**使用方法**:
```bash
/sf recipe search <材料 ID>          # 搜索配方
```

**示例**:
```bash
/sf recipe search STEEL_INGOT        # 搜索所有使用钢锭的配方
```

---

## 🔐 权限节点详解

### 基础权限

#### slimefun.command.guide
- **描述**: 允许使用 `/sf guide` 命令
- **默认**: 所有玩家 (true)
- **用途**: 打开 Slimefun 指南

#### slimefun.command.open_guide
- **描述**: 允许使用 `/sf open_guide` 命令
- **默认**: 管理员 (op)
- **用途**: 无需书本打开指南

#### slimefun.command.versions
- **描述**: 允许使用 `/sf versions` 命令
- **默认**: 管理员 (op)
- **用途**: 查看版本信息

#### slimefun.command.teleporter
- **描述**: 允许使用 `/sf teleporter` 命令
- **默认**: 管理员 (op)
- **用途**: 管理传送网络

### 作弊权限

#### slimefun.cheat.items
- **描述**: 允许作弊获得物品
- **默认**: 管理员 (op)
- **权限**: 高
- **相关命令**: 
  - `/sf cheat`
  - `/sf give`
  - `/sf item give`

**使用场景**:
- 服务器测试
- 创造模式
- 奖励玩家
- 应急使用

#### slimefun.cheat.researches
- **描述**: 允许作弊研究
- **默认**: 管理员 (op)
- **权限**: 高
- **相关命令**:
  - `/sf research unlock`
  - `/sf research reset`
  - `/sf unlock others`

**使用场景**:
- 帮助新手玩家
- 重置错误数据
- 测试研究系统

### 统计权限

#### slimefun.stats.others
- **描述**: 允许查看其他玩家统计
- **默认**: 管理员 (op)
- **权限**: 中
- **相关命令**:
  - `/sf stats <玩家>`
  - `/sf stats clear`

**用途**:
- 监控玩家进度
- 服务器统计
- 数据分析

### 特殊权限

#### slimefun.android.bypass
- **描述**: 允许编辑其他玩家的安卓机器人
- **默认**: 管理员 (op)
- **用途**: 管理和维护

**使用场景**:
- 修复故障机器人
- 帮助玩家解决问题
- 服务器管理

#### slimefun.cargo.bypass
- **描述**: 允许编辑其他玩家的货物节点
- **默认**: 管理员 (op)
- **用途**: 货物网络管理

**使用场景**:
- 修复卡住的货物系统
- 优化物流网络
- 清理废弃设备

#### slimefun.inventory.bypass
- **描述**: 允许打开所有 Slimefun 机器
- **默认**: 管理员 (op)
- **用途**: 机器维护

**使用场景**:
- 取出卡住的物品
- 检查机器状态
- 维修设备

### 权限组配置示例

#### 新手玩家权限组
```yaml
newbie:
  permissions:
    slimefun.command.guide: true
    slimefun.command.versions: false
    slimefun.cheat.items: false
    slimefun.cheat.researches: false
```

#### VIP 玩家权限组
```yaml
vip:
  permissions:
    slimefun.command.guide: true
    slimefun.command.open_guide: true
    slimefun.command.versions: true
    slimefun.cheat.items: false
    slimefun.cheat.researches: false
```

#### 管理员权限组
```yaml
admin:
  permissions:
    slimefun.command.guide: true
    slimefun.command.open_guide: true
    slimefun.command.versions: true
    slimefun.cheat.items: true
    slimefun.cheat.researches: true
    slimefun.stats.others: true
    slimefun.android.bypass: true
    slimefun.cargo.bypass: true
    slimefun.inventory.bypass: true
```

---

## ⚙️ 配置与优化

### 命令配置

#### config.yml 命令相关设置

```yaml
# 命令前缀
prefix: '&a&l粘液科技 &7>'

# 是否显示命令帮助
show-command-help: true

# 命令冷却时间
command-cooldowns:
  guide: 0          # 指南命令冷却（秒）
  research: 1       # 研究命令冷却
  item: 0           # 物品命令冷却
```

### 权限管理插件集成

#### LuckPerms 配置示例

```bash
# 创建权限组
/lp creategroup newbie
/lp creategroup vip
/lp creategroup admin

# 设置权限
/lp group newbie permission set slimefun.command.guide true
/lp group vip permission set slimefun.command.open_guide true
/lp group admin permission set slimefun.cheat.items true

# 分配玩家
/lp user Steve parent set newbie
/lp group Alex parent set vip
```

#### PermissionsEx 配置示例

```yaml
groups:
  newbie:
    permissions:
      - slimefun.command.guide
  vip:
    permissions:
      - slimefun.command.guide
      - slimefun.command.open_guide
  admin:
    permissions:
      - slimefun.*
```

### 命令优化建议

#### 1. 限制命令滥用

**设置冷却时间**:
```yaml
command-cooldowns:
  cheat: 60         # 作弊命令 60 秒冷却
  research: 5       # 研究命令 5 秒冷却
```

**使用权限控制**:
- 仅管理员可使用作弊命令
- 普通玩家限制使用频率
- 记录命令使用日志

#### 2. 命令别名优化

**自定义别名**:
```yaml
command-aliases:
  guide:
    - g
    - book
  research:
    - r
    - study
  versions:
    - v
    - ver
```

**示例**:
```bash
/sf g        # 打开指南
/sf r list   # 查看研究进度
/sf v        # 查看版本
```

#### 3. 命令日志

**启用日志**:
```yaml
command-logging:
  enabled: true
  log-cheat-commands: true
  log-research-changes: true
```

**日志内容**:
- 命令使用时间
- 使用者信息
- 命令参数
- 执行结果

---

## 🛠️ 故障排查

### 命令无法使用

#### 问题 1: 命令无响应
**可能原因**:
- 权限不足
- 插件未加载
- 命令拼写错误

**解决方案**:
1. 检查权限设置
2. 确认插件已加载
3. 使用 Tab 补全

#### 问题 2: 权限错误
**可能原因**:
- 权限组配置错误
- 权限插件冲突
- 权限未正确分配

**解决方案**:
1. 使用 `/lp user <玩家> permission check` 检查权限
2. 重新分配权限组
3. 重启权限插件

### 研究命令问题

#### 问题 1: 无法解锁研究
**可能原因**:
- 经验值不足
- 等级不够
- 前置研究未完成

**解决方案**:
1. 收集更多经验
2. 提升研究等级
3. 先完成前置研究

#### 问题 2: 研究数据丢失
**可能原因**:
- 数据文件损坏
- 服务器崩溃
- 插件冲突

**解决方案**:
1. 恢复备份数据
2. 使用 `/sf research reset` 重置
3. 检查服务器日志

---

## 📊 命令速查表

### 玩家常用命令

| 命令 | 权限 | 描述 | 示例 |
|------|------|------|------|
| `/sf` | 所有人 | 打开指南 | `/sf` |
| `/sf research list` | 所有人 | 查看研究进度 | `/sf research list` |
| `/sf unlock <ID>` | 所有人 | 解锁研究 | `/sf unlock STEEL_INGOT` |
| `/sf item info <ID>` | 所有人 | 查看物品信息 | `/sf item info STEEL_PICKAXE` |
| `/sf recipe show <ID>` | 所有人 | 查看配方 | `/sf recipe show STEEL_INGOT` |

### 管理员常用命令

| 命令 | 权限 | 描述 | 示例 |
|------|------|------|------|
| `/sf cheat <ID>` | op | 获得物品 | `/sf cheat STEEL_INGOT 64` |
| `/sf research unlock <玩家> all` | op | 解锁所有研究 | `/sf research unlock Steve all` |
| `/sf research reset <玩家>` | op | 重置研究 | `/sf research reset Steve` |
| `/sf stats <玩家>` | op | 查看统计 | `/sf stats Steve` |
| `/sf timings` | op | 查看机器统计 | `/sf timings` |
| `/sf backup create` | op | 创建备份 | `/sf backup create` |

---

## 🔗 相关链接

- [主页](Home.md) - 返回 Wiki 首页
- [配置指南](#配置与优化) - 详细配置说明
- [GitHub 仓库](https://github.com/andjie98/Slimefun4) - 获取最新版本

---

**最后更新**: 2026-03-22  
**适用版本**: Slimefun4 v4.1.15
