# Slimefun4 中文定制版 - Wiki 文档库

## 📚 Wiki 概览

本目录包含 Slimefun4 中文定制版的完整 Wiki 文档，为玩家和管理员提供全面的使用指南。

## 📁 文档结构

```
wiki/
├── Home.md                    # Wiki 首页 - 项目简介和快速入门
├── Machines.md                # 机器完全指南 - 所有机器设备详解
├── Items.md                   # 物品完全指南 - 所有物品分类和获取
├── Research.md                # 研究系统指南 - 研究等级和解锁机制
├── Commands.md                # 命令与权限 - 所有命令和权限说明
├── _Sidebar.md                # 侧边栏导航 - GitHub Wiki 侧边栏
├── _Footer.md                 # 页脚 - 所有页面底部信息
└── README.md                  # 本文件 - Wiki 使用说明
```

## 📖 文档详情

### 1. [Home.md](Home.md) - Wiki 首页
**内容**:
- 项目简介和特性
- 安装与配置指南
- 游戏内容概览
- 开发指南
- 常见问题解答

**适合**: 新手玩家首次了解插件

### 2. [Machines.md](Machines.md) - 机器完全指南
**内容**:
- 能源生成机器
- 基础加工机器
- 高级制造设备
- 农业与生物机器
- 货物运输系统
- 特殊用途机器
- 机器使用技巧

**适合**: 需要了解机器功能和制作方法的玩家

### 3. [Items.md](Items.md) - 物品完全指南
**内容**:
- 工具与武器分类
- 护甲装备详解
- 背包系统
- 护身符系统
- 喷气背包与飞行装备
- 食物与饮料
- 材料资源
- 特殊物品

**适合**: 需要查询物品属性和获取方式的玩家

### 4. [Research.md](Research.md) - 研究系统指南
**内容**:
- 研究系统简介
- 研究等级详解（1-38 级）
- 研究解锁机制
- 研究物品分类
- 研究技巧与策略
- 常见问题解答

**适合**: 需要规划研究路线的玩家

### 5. [Commands.md](Commands.md) - 命令与权限
**内容**:
- 基础命令说明
- 管理员命令
- 研究相关命令
- 物品相关命令
- 权限节点详解
- 配置与优化

**适合**: 玩家和管理员查询命令用法

### 6. [_Sidebar.md](_Sidebar.md) - 侧边栏导航
**用途**: 
- GitHub Wiki 自动加载的侧边栏
- 提供快速导航链接
- 显示常用内容

**配置**: GitHub Wiki 会自动识别并使用此文件

### 7. [_Footer.md](_Footer.md) - 页脚
**用途**:
- 显示在所有页面底部
- 提供统一的外部链接
- 展示版权信息

## 🎯 使用建议

### 对于新手玩家
1. 首先阅读 [Home.md](Home.md) 了解插件
2. 查看 [Items.md](Items.md) 了解基础物品
3. 参考 [Research.md](Research.md) 规划研究路线
4. 使用 [Commands.md](Commands.md) 学习基础命令

### 对于进阶玩家
1. 深入研究 [Machines.md](Machines.md) 建立自动化
2. 查看 [Items.md](Items.md) 高级物品部分
3. 参考 [Research.md](Research.md) 优化研究顺序
4. 学习高级技巧和策略

### 对于管理员
1. 熟读 [Commands.md](Commands.md) 所有管理命令
2. 配置权限节点
3. 处理玩家问题
4. 维护服务器平衡

## 📊 统计信息

| 指标 | 数值 |
|------|------|
| 总页面数 | 7 页 |
| 主文档页数 | 5 页 |
| 总字数 | 约 30,000 字 |
| 适用版本 | v4.1.15 |
| 最后更新 | 2026-03-22 |

## 🔧 维护说明

### 更新 Wiki
1. 修改对应的 `.md` 文件
2. 提交更改到仓库
3. GitHub Wiki 会自动同步（如配置）

### 添加新页面
1. 在 `wiki/` 目录创建新的 `.md` 文件
2. 在 [_Sidebar.md](_Sidebar.md) 中添加链接
3. 提交到仓库

### 修正错误
1. 直接编辑对应的 `.md` 文件
2. 在提交信息中说明修正内容
3. 推送到仓库

## 📝 编写规范

### Markdown 格式
- 使用 `#` 表示标题（支持 1-6 级）
- 使用 `**粗体**` 强调重点
- 使用 `*斜体*` 表示特殊术语
- 使用 ` ```语言 ``` ` 包裹代码块
- 使用 `-` 创建列表

### 内容组织
- 每个页面以目录开始
- 使用清晰的分节标题
- 提供实际使用示例
- 包含常见问题解答
- 添加相关页面链接

### 链接规范
- 内部链接：`[页面名称](文件名.md)`
- 外部链接：`[链接文字](URL)`
- 锚点链接：`[链接](文件名.md#章节标题)`

## 🌐 GitHub Wiki 集成

### 自动同步
如果配置了 GitHub Actions，Wiki 可以自动同步到 GitHub Wiki：

```yaml
# .github/workflows/wiki.yml
name: Deploy Wiki
on:
  push:
    paths:
      - 'wiki/**'
jobs:
  deploy-wiki:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Deploy Wiki
        uses: Andrew-Chen-wang/github-wiki-action@v4
```

### 手动上传
1. 访问仓库的 Wiki 页面
2. 点击页面右上角的编辑按钮
3. 复制粘贴 `.md` 文件内容
4. 保存页面

## 🤝 贡献指南

### 贡献方式
1. **修正错误** - 发现拼写或事实错误
2. **补充内容** - 添加缺失的信息
3. **改进表达** - 优化文字描述
4. **新增页面** - 添加新的主题

### 提交流程
1. Fork 仓库
2. 创建分支 `git checkout -b wiki/update-name`
3. 修改 Wiki 文件
4. 提交更改 `git commit -m "docs: 更新 XXX 内容"`
5. 推送分支 `git push origin wiki/update-name`
6. 创建 Pull Request

### 贡献者名单
感谢所有为 Wiki 做出贡献的人：
- 初始版本：2026-03-22
- 贡献者列表（待更新）

## 📞 反馈与支持

### 遇到问题？
- 查看 [Home.md](Home.md) 的常见问题部分
- 在 [GitHub Issues](https://github.com/andjie98/Slimefun4/issues) 提问
- 加入社区讨论（如有）

### 发现错误？
- 直接提交 PR 修正
- 或在 Issues 中报告

### 需要新功能文档？
- 在 Issues 中提出需求
- 或自己编写并提交 PR

## 🔗 相关链接

- [主仓库](https://github.com/andjie98/Slimefun4)
- [问题反馈](https://github.com/andjie98/Slimefun4/issues)
- [原版 Wiki](https://github.com/TheBusyBiscuit/Slimefun4/wiki)
- [SpigotMC 讨论页](https://www.spigotmc.org/resources/slimefun.4433/)

## 📜 许可证

本 Wiki 文档采用 [GNU GPLv3](../LICENSE) 许可证发布。

您可以自由：
- 分享 - 复制和分发材料
- 演绎 - 修改和构建原作

条件：
- 署名 - 给出适当的署名
- 相同方式共享 - 使用相同许可证分发

---

**最后更新**: 2026-03-22  
**维护者**: Slimefun4 中文社区  
**版本**: v4.1.15
