# Slimefun4 中文定制版 Wiki

欢迎来到 **Slimefun4 中文定制版** 的官方 Wiki！本插件是 Slimefun 4 的中文定制版本，专为 Minecraft 1.12.2 优化和修复。

## 📖 目录

- [简介](#简介)
- [核心特性](#核心特性)
- [安装与配置](#安装与配置)
- [游戏内容](#游戏内容)
- [开发指南](#开发指南)
- [常见问题](#常见问题)

---

## 🎯 简介

**Slimefun4** 是一个革命性的 Minecraft 插件，它将你的服务器变成一个功能齐全的模组包，而无需安装任何客户端模组！本中文定制版本在原版基础上进行了关键性修复和本地化优化。

### 版本信息
- **当前版本**: 4.1.15
- **适用版本**: Minecraft 1.12.2
- **许可证**: GNU GPLv3
- **开发状态**: 维护中

### 独有修复
- ✅ 修复世界卸载时 `NullPointerException` 刷屏问题
- ✅ 修复 `BlockStorage.getTickingLocations` 返回 null 导致的崩溃
- ✅ 完整的中文本地化支持

---

## ✨ 核心特性

### 🔧 自动化机器体系
- **能源生成**: 各种发电机和能源生产设备
- **材料加工**: 矿石处理、合金冶炼、宝石加工
- **农业自动化**: 自动种植、收获、动物养殖
- **物品运输**: 智能货物节点和分拣系统

### ⚡ 能量网络
- 完整的能源存储和传输系统
- 能量网络监控和管理
- 多种能源生成方式
- 智能能耗分配

### 🤖 安卓机器人系统
- 可编程的机器人助手
- 多种机器人类型
- 脚本系统支持
- 自动化任务执行

### 🌍 GPS 定位系统
- 传送网络建设
- 电梯系统
- 坐标定位和管理

### 💎 地质资源系统
- 自定义矿物生成
- 石油开采
- 冰矿资源
- 资源勘探设备

### 🎒 丰富物品系统
- **工具与武器**: 高科技工具和武器
- **护甲装备**: 特殊功能护甲
- **背包系统**: 多种容量存储背包
- **护身符**: 各种增益效果的护身符
- **喷气背包**: 飞行装备
- **灵魂绑定**: 灵魂绑定物品系统

---

## 📥 安装与配置

### 系统要求
- **服务端**: Spigot/Paper 1.12.2
- **Java**: Java 8 或更高版本
- **内存**: 建议 2GB+ 专用内存

### 依赖插件
#### 必需依赖
- **CS-CoreLib v1.5.18** - 核心框架库

#### 可选依赖
- **WorldEdit 6.1** - 地形编辑支持
- **CoreProtect 2.14.4** - 行为日志与回滚
- **Residence 5.1.6+** - 领地管理
- **PlotSquared 3.5.0** - 地皮系统集成
- **ClearLag** - 性能优化
- **EmeraldEnchants 2.0** - 翡翠附魔

### 安装步骤

1. **下载插件**
   - 从 [Releases](https://github.com/andjie98/Slimefun4/releases) 下载最新版本
   - 下载 CS-CoreLib v1.5.18

2. **安装文件**
   ```
   server/
   └── plugins/
       ├── Slimefun v4.1.15.jar
       └── CS-CoreLib v1.5.18.jar
   ```

3. **启动服务器**
   - 首次运行会自动生成配置文件
   - 检查控制台是否有错误信息

4. **配置优化**
   - 编辑 `plugins/Slimefun/config.yml`
   - 根据服务器需求调整参数

### 配置说明

#### config.yml 关键选项

```yaml
options:
  auto-update: false              # 自动更新（建议关闭）
  armor-update-interval: 10       # 护甲效果更新间隔（tick）
  give-guide-on-first-join: true  # 首次加入赠送指导手册
  print-out-loading: false        # 打印加载信息
  enable-armor-effects: true      # 启用护甲效果
  prefix: '&a&l粘液科技 &7>'      # 消息前缀
  auto-save-delay-in-minutes: 10  # 自动保存间隔（分钟）
  
guide:
  default-view-book: false        # 默认使用书本界面
  
items:
  talismans: true                 # 启用护身符
  coolers: true                   # 启用冷却器
  backpacks: true                 # 启用背包
  
research-ranks:                   # 研究等级列表
  - Chicken
  - Cow
  - ...
```

---

## 🎮 游戏内容

### 研究系统

#### 研究等级
从新手到大师的完整研究等级体系：
- 新手阶段：Chicken → Student
- 学徒阶段：Apprentice → Magician  
- 法师阶段：Wizard → Master
- 终极阶段：The chosen One

#### 研究解锁
- 使用研究指南查看可研究物品
- 消耗经验等级解锁研究
- 研究成功后可获得烟花奖励
- 创造模式可跳过研究（可配置）

### 机器分类

#### 基础加工机器
- **电动熔炉**: 高效矿物冶炼
- **电动铸锭厂**: 金属锭加工
- **电动粉末清洗机**: 矿物粉末提纯
- **粉碎机**: 矿石粉碎
- **压缩机**: 材料压缩

#### 高级制造设备
- **自动化合成室**: 自动合成物品
- **压力舱**: 高压材料处理
- **炼油厂**: 石油精炼
- **采油机**: 石油开采
- **冷冻机**: 液体固化

#### 农业与生物
- **自动喂食器**: 动物自动喂养
- **动物生长加速器**: 加速动物成长
- **作物生长加速器**: 加速作物生长
- **堆肥机**: 有机物堆肥
- **经验收集器**: 收集经验值

#### 货物运输系统
- **货物输入节点**: 自动输入物品
- **货物输出节点**: 自动输出物品
- **高级货物输出节点**: 智能输出控制
- **货物合成节点**: 自动合成

#### 能源设备
- **反应堆接口**: 核反应堆控制
- **充电台**: 物品充电
- **各种发电机**: 多种能源生产

### 特殊物品

#### 护身符系统
- 幸运护身符：增加幸运值
- 防护护身符：提供伤害减免
- 魔法护身符：增强魔法效果
- 末影护身符：特殊传送能力

#### 背包系统
- 小型背包：27 格存储
- 中型背包：36 格存储  
- 大型背包：45 格存储
- 灵魂绑定背包：死亡不掉落

#### 工具与武器
- 多功能工具：集成多种功能
- 电动工具：高效耐用
- 灵魂绑定工具：绑定玩家
- 特殊镐子：开采特殊矿物

#### 喷气背包与飞行装备
- 基础喷气背包：短途飞行
- 高级喷气背包：长距离飞行
- 喷气靴：短距离滑翔

### Android 机器人

#### 机器人类型
- **基础型**: 简单任务执行
- **程序员型**: 可运行复杂脚本
- **战斗型**: 自动战斗
- **工作型**: 自动化工作

#### 脚本系统
- 可视化脚本编程
- 条件判断和循环
- 物品筛选和排序
- 下载和分享脚本

---

## 🛠️ 开发指南

### 构建项目

#### 环境要求
- **JDK**: 8 或更高版本
- **Maven**: 3.6+
- **Git**: 最新版本

#### 构建步骤

```bash
# 克隆仓库
git clone https://github.com/andjie98/Slimefun4.git

# 进入项目目录
cd Slimefun4

# 清理并构建
mvn clean package

# 构建产物位置
# target/Slimefun v4.1.15.jar
```

### 项目结构

```
Slimefun4/
├── src/                          # 源代码目录
│   ├── me/mrCookieSlime/Slimefun/
│   │   ├── AncientAltar/         # 古代祭坛系统
│   │   ├── Android/              # 安卓机器人系统
│   │   ├── Commands/             # 命令处理
│   │   ├── Events/               # 事件监听
│   │   ├── GEO/                  # 地质资源系统
│   │   ├── GPS/                  # GPS 网络系统
│   │   ├── Objects/              # 核心对象定义
│   │   │   ├── SlimefunItem/     # 物品定义
│   │   │   └── tasks/            # 任务系统
│   │   ├── api/                  # API 接口
│   │   ├── listeners/            # 事件监听器
│   │   └── Setup/                # 初始化设置
│   ├── io/github/thebusybiscuit/ # Dough 库
│   ├── config.yml                # 配置文件
│   └── plugin.yml                # 插件描述
├── lib/                          # 依赖库
├── tests/                        # 测试代码
└── pom.xml                       # Maven 配置
```

### 添加新物品

#### 基础示例

```java
public class MyCustomItem extends SlimefunItem {
    public MyCustomItem() {
        super(
            category,           // 物品分类
            itemStack,          // 物品展示
            recipeType,         // 合成类型
            recipe              // 合成配方
        );
        this.register();        // 注册物品
    }
}
```

#### 机器物品示例

```java
public class MyMachine extends SlimefunMachine {
    public MyMachine() {
        super(
            category,
            itemStack,
            recipeType,
            recipe,
            output,
            energyConsumption,    // 能耗
            processingSpeed       // 处理速度
        );
        this.register();
    }
}
```

### API 使用

#### 获取物品信息

```java
// 通过 ID 获取物品
SlimefunItem item = SlimefunItem.getById("MY_ITEM_ID");

// 通过 ItemStack 获取物品
SlimefunItem item = SlimefunItem.getByItem(itemStack);

// 检查是否已解锁
boolean unlocked = Slimefun.hasUnlocked(player, item, true);
```

#### 研究系统

```java
// 创建研究
Research research = new Research(
    100,                    // 研究 ID
    "My Research",          // 研究名称
    3                       // 研究等级
);

// 添加物品
research.addItems(myItem1, myItem2);

// 注册研究
research.register();
```

### 事件监听

#### 自定义事件

```java
@EventHandler
public void onResearchUnlock(ResearchUnlockEvent event) {
    Player player = event.getPlayer();
    Research research = event.getResearch();
    
    // 处理研究解锁事件
    Bukkit.broadcastMessage(
        player.getName() + " 解锁了 " + research.getName()
    );
}
```

---

## ❓ 常见问题

### Q1: 服务器启动时报错怎么办？

**A**: 检查以下几点：
1. 确认已安装 CS-CoreLib v1.5.18
2. 检查服务端版本是否为 1.12.2
3. 查看日志文件 `logs/latest.log` 获取详细错误信息
4. 确保没有其他插件冲突

### Q2: 如何关闭自动更新？

**A**: 在 `config.yml` 中设置：
```yaml
options:
  auto-update: false
```

### Q3: 物品在某个世界无法使用？

**A**: 检查世界 whitelist 配置：
```yaml
# 检查 worlds.yml 或 whitelist 配置
# 确保该世界启用了相应物品
```

### Q4: 如何备份玩家数据？

**A**: Slimefun 会自动保存数据：
- 数据文件位置：`plugins/Slimefun/data-storage/`
- 自动保存间隔：10 分钟（可配置）
- 手动备份整个 `data-storage` 文件夹

### Q5: 机器不工作怎么办？

**A**: 排查步骤：
1. 检查是否有足够能源供应
2. 确认输入物品正确
3. 检查输出槽位是否为空
4. 查看控制台是否有错误信息
5. 使用 `/sf timings` 检查机器状态

### Q6: 如何重置玩家研究进度？

**A**: 使用管理员命令：
```bash
/sf research reset <player>
```

### Q7: 安卓机器人不执行脚本？

**A**: 检查：
1. 脚本是否正确编写
2. 机器人是否有足够电量
3. 脚本文件是否在正确目录
4. 使用 `/sf android` 命令检查状态

### Q8: 世界加载时出现 NullPointerException？

**A**: 此问题已在中文定制版修复：
- 升级到最新版本（4.1.15+）
- 如仍有问题，检查是否有其他插件冲突

---

## 🔗 相关链接

- [GitHub 仓库](https://github.com/andjie98/Slimefun4)
- [问题反馈](https://github.com/andjie98/Slimefun4/issues)
- [原版 Wiki](https://github.com/TheBusyBiscuit/Slimefun4/wiki)
- [SpigotMC 讨论页](https://www.spigotmc.org/resources/slimefun.4433/)

---

## 📝 贡献指南

欢迎为 Slimefun4 中文定制版做出贡献！

### 贡献方式
- 🐛 报告 Bug
- 💡 提出新功能建议
- 📝 改进文档
- 🔧 提交代码修复

### 开发流程
1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

---

## 📜 许可证

本项目采用 **GNU GPLv3** 许可证开源。

- 您可以自由使用、修改和分发
- 任何衍生作品必须采用相同许可证
- 详见 [LICENSE](LICENSE) 文件

---

## 👥 致谢

- **TheBusyBiscuit** - Slimefun4 原作者
- **Slimefun 社区** - 所有贡献者
- **中文定制版团队** - 本地化和修复工作

---

**最后更新**: 2026-03-22  
**Wiki 版本**: 1.0  
**适用版本**: Slimefun4 v4.1.15
