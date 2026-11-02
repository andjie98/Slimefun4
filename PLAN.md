# PLAN — Slimefun4 精简版

> 本文锁定**版本**与**平台**两个基线，再据此排后续工作。
> 基线未变的项不需要再讨论；要改，先改本文的决策表。

- 编写日期：2026-11-02
- 代码基线：分支 `slim-version`，HEAD `64be7a4`（2026-09-29）
- 上游基线：Slimefun4 **v4.1.15**

---

## 1. 决策：版本与平台（锁定）

| 维度 | 决定 | 依据 | 不允许出现 |
|---|---|---|---|
| 上游基线 | Slimefun **v4.1.15** | 当前代码、pom、README 均已按此版本精简，替换基线等于重做 | 混入 4.2+/master 的 API |
| 目标 Minecraft 版本 | **1.12.2** | 现网收益最大、社区存档与物品 data value 体系完整；1.13+ 的扁平化会推翻全部配方与物品 ID | 1.13+ 扁平化 Material、`BlockData`、PDC |
| 目标服务端平台 | **Paper 1.12.2**（构建面用 Spigot 1.12.2 API） | Paper 向下兼容 Spigot 插件，且提供 `sendActionBar(String)` 等少量增强；只用 Spigot API 可保证两端都能跑 | 依赖 Paper 独有 API 而不加 `softdepend` 判空；Folia 调度器 |
| 编译期依赖版本 | **spigot-api 1.12.2-R0.1-SNAPSHOT**（`provided`） | 现状是 `bukkit:1.12-R0.1-SNAPSHOT`，比目标低两个小版本，存在方法缺失的编译期假象 | `bukkit:1.12`、`1.12.1` 等非 1.12.2 版本 |
| Java 字节码 | **Java 8（major version 52）**，每个 class 都要满足 | 1.12.2 服务端跑在 Java 8 语义上；major 55+ 直接加载失败 | 用 `source/target 1.8` 代替 `release` |
| 编译环境 | **JDK 11 + `maven.compiler.release=8`** | `--release 8` 同时限制字节码版本**和** API 集合，能在编译期拦住 `List.of` 这类 JDK 9+ 方法 | JDK 17/25 直编（本机 PATH 默认 JDK 25，会静默放过 JDK 9+ API） |
| 插件版本号 | 沿用 `4.1.15`，如需区分精简版用后缀 `4.1.15-slim.<n>` | plugin.yml 用 `${project.version}` 过滤；改名要先确认无代码读版本号做比较 | 与上游同号却内容不同、无后缀 |

**一句话结论：Paper 1.12.2 / Spigot 1.12.2 API / Java 8 字节码 / JDK 11 编译 / 上游基线 v4.1.15。**

---

## 2. 现状盘点（实测，非推断）

| 项 | 实测结果 |
|---|---|
| 构建 | `target/Slimefun v4.1.15.jar`，905,544 字节，590 个 class，**全部 major 52** ✅ |
| 主类 | `me.mrCookieSlime.Slimefun.SlimefunStartup`（plugin.yml `main` 正确） |
| plugin.yml | 无 `api-version`（1.12.2 正确）；`softdepend: [CS-CoreLib, ClearLag, WorldEdit, PlotSquared, CoreProtect, Residence]` |
| 硬依赖 | **CS-CoreLib v1.5.18**——虽写在 `softdepend`，实际功能必需，缺失时大量特性失效 |
| 研究系统 | `config.yml` 已设 `enable-researching: false` ✅ |
| 本机 JDK | 11.0.32.1、8u502、17.0.20.1、25.0.3.9 均在 `C:\Program Files\Microsoft\` |
| 构建工具 | Maven 3.9.11，**默认 `java`/`mvn` 跑的是 JDK 25** ⚠️ |
| 服务端环境 | **本机没有 `D:\server\1.12.2`**（技能记录 2026-09-24 存在，现在不存在）→ 当前**无法做运行期验证** ⚠️ |

### 已确认的问题

1. **pom 用 `source/target 1.8`**：能出 major 52，但不拦 JDK 9+ API。JDK 25 编译下 `List.of(...)` 会通过编译，然后服务端抛 `NoSuchMethodError`。
2. **编译版本低于目标版本**：`bukkit:1.12-R0.1-SNAPSHOT`，目标运行在 1.12.2。
3. **第三方 jar 只做了部分隔离**：`.gitignore` 只忽略 `lib/\[领地\]Residence5.1.6.2.jar`，而 **`lib/Residence.jar` 仍在版本库里**（1.2 MB，同属付费插件）；`lib/` 下另有 6 个第三方 jar 被跟踪。
4. **README 与实现有漂移**：正文写"11 个核心物品"，表格实为 15 行（含 3 种护身符分类行）；版本/平台一节仍写"Bukkit/Spigot 1.12+"。
5. **布局非标准**：`pom.xml` 把 `src` 设为 `sourceDirectory`（无 `src/main/java`、`src/main/resources`），官方校验脚本 `validate_plugin_layout.py` 会误报 `missing plugin.yml`。这是上游 4.1.x 的老布局，**保留**，但校验时要加说明。

---

## 3. 工作项

### P0 — 版本与平台落地（必做，先做）

| # | 任务 | 验收标准 |
|---|---|---|
| P0-1 | pom 依赖改 `org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT`，`provided`；同步删除 `dependencyManagement` 里的 `bukkit:1.12` | `mvn dependency:tree` 中无 `org.bukkit:bukkit` |
| P0-2 | `maven.compiler.source/target` → `maven.compiler.release=8`，并在 `maven-compiler-plugin` 里显式写 `<release>8</release>` | 用 **JDK 25** 构建也失败于 JDK 9+ API，用 JDK 11 构建成功 |
| P0-3 | 固定构建环境：`$env:JAVA_HOME='C:\Program Files\Microsoft\jdk-11.0.32.1+1'` 后构建 | 构建日志 Java version 显示 11 |
| P0-4 | 版本号决定落地（默认保持 `4.1.15`，需要区分则 `4.1.15-slim.1`） | `plugin.yml` 与 jar 文件名一致 |

**P0 完成判据（一条命令跑完）：**

```powershell
$env:JAVA_HOME = 'C:\Program Files\Microsoft\jdk-11.0.32.1+1'
mvn -B clean package -DskipTests
# 590 个 class 全部 major 52；jar 出现在 target/Slimefun v4.1.15.jar
```

### P1 — 构建可复现

| # | 任务 | 验收标准 |
|---|---|---|
| P1-1 | `lib/` 下 8 个 `system` scope 依赖写进 README 的"编译前置清单"（文件名 + 版本 + 用途） | 新机器照文档能直接 `mvn package` 成功 |
| P1-2 | 若后续要上 CI：把 `lib/` 之外的可解析依赖（`CS-CoreLib` 走 jitpack）改为仓库依赖 | CI 构建无需手工放 jar |

### P2 — 运行期验证（当前被环境阻塞）

| # | 任务 | 验收标准 |
|---|---|---|
| P2-1 | 搭建隔离服务端：Paper 1.12.2 **build 1620**，独立目录，端口 `25599`，`online-mode=false`，`level-type=FLAT`。**不得使用生产服目录** | 服务端能起，日志无 `Error occurred while enabling` |
| P2-2 | 冒烟清单（逐项记录 `[OK]`/失败，**不要用 `✓`/`✗`**，Windows 控制台是 GBK） | 见下方清单 |

**冒烟清单：**

1. 启动无 `Error occurred while enabling Slimefun`，无 `NoClassDefFoundError`
2. `/sf guide` 能拿到指南书并在 GUI 内翻页（物品全部为 1.12.2 名称，无空槽）
3. 增强型合成台：工作台 + 发射器结构识别成功，能合成便携式合成台
4. 黑钻炉：熔炼速度 20x、燃料 10x、产出 3x
5. 电梯板：同 X/Z 上下传送
6. 护身符：普通护身符生效；末影护身符放进末影箱生效且不占背包
7. 斩首剑、风杖、方块放置器、全息显示器、镀金背包各验证一次

> 阻塞项：本机 `D:\server\1.12.2` 不存在。**P2 需要先确认服务端来源**（本地新建 / 由 Chief 或 QA 提供），在此之前 P0、P1 可以独立推进。

### P3 — 交付

| # | 任务 | 验收标准 |
|---|---|---|
| P3-1 | README「兼容性」节改为：服务端 Paper 1.12.2（兼容 Spigot 1.12.2）、Java 8 运行时、构建用 JDK 11 | 与本文决策表一致 |
| P3-2 | 修正物品数量表述，与表格行数对齐 | 数字一致 |
| P3-3 | 发布前自查付费 jar：`git check-ignore -v lib/Residence.jar` 必须命中；当前**未命中** | 跟踪列表中无私货 |
| P3-4 | 归档 jar + SHA256 + 冒烟记录 | 可回溯 |

---

## 4. 风险与处置

| 风险 | 影响 | 处置 |
|---|---|---|
| 用 JDK 25 直接构建 | 编译通过、运行期 `NoSuchMethodError` | P0-2 用 `release=8` 硬拦；构建脚本里显式设 `JAVA_HOME` |
| 无运行期验证环境 | "编译通过"被当成"能用" | P2 必须做，未验不收 |
| 付费插件 `lib/Residence.jar` 在库中 | 公开发布即侵权，强推也删不掉 | P3-3 先加 `.gitignore`，必要时改写历史 |
| 精简掉机器后旧存档方块无交互 | 玩家资产观感受损 | 已在 README 声明；升级前提示玩家拆除机器 |
| 上游 1.12.2 已停更 | 无安全修复 | 接受，本项目定位是稳定精简版而非追新 |

---

## 5. 本次不改的东西

- 不引入 XSeries 等跨版本抽象层（决策已定 1.12.2 单版本，抽象层只增加调试成本）
- 不做 1.13+ 兼容分支
- 不改 `src` 作为源码根的非标准布局
- 不动研究系统（已禁用）
