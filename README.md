# Slimefun 4 (粘液科技)

[![Build and Release](https://github.com/andjie98/Slimefun4/actions/workflows/release.yml/badge.svg)](https://github.com/andjie98/Slimefun4/actions/workflows/release.yml)
[![GitHub release](https://img.shields.io/github/v/release/andjie98/Slimefun4?include_prereleases)](https://github.com/andjie98/Slimefun4/releases)
[![License](https://img.shields.io/github/license/andjie98/Slimefun4)](LICENSE)

这是 Slimefun 4 的中文定制版本，针对 Minecraft 1.12.2 进行优化和修复。

## 下载

最新版本可在 [Releases](https://github.com/andjie98/Slimefun4/releases) 页面下载。

## 修复内容

- 修复世界卸载时 `NullPointerException` 刷屏问题
- 修复 `BlockStorage.getTickingLocations` 返回 null 的问题

## 原版说明

Slimefun 4 是对革命性插件 [Slimefun](http://dev.bukkit.org/bukkit-plugins/slimefun/) 的第四次重大更新。

本项目基于 [GNU GPLv3](LICENSE) 许可证开源。

### 开发构建

原版开发构建可在以下地址获取：
https://thebusybiscuit.github.io/builds/TheBusyBiscuit/Slimefun4/master/

### Wiki

详细使用说明请参考原版 Wiki：
https://github.com/TheBusyBiscuit/Slimefun4/wiki

## 依赖

- CS-CoreLib
- Spigot/Paper 1.12.2

## 构建

```bash
mvn clean package
```

构建产物位于 `target/` 目录。
