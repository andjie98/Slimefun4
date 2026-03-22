# Slimefun4 Wiki 站点部署指南

本 Wiki 站点使用 **MkDocs** + **Material for MkDocs** 主题构建，可部署到 GitHub Pages。

## 📋 目录结构

```
Slimefun4/
├── docs/                    # 文档源文件
│   ├── index.md            # 首页
│   ├── machines.md         # 机器指南
│   ├── items.md            # 物品指南
│   ├── research.md         # 研究系统
│   ├── commands.md         # 命令参考
│   └── ...
├── mkdocs.yml              # MkDocs 配置文件
└── .github/workflows/
    └── wiki-site.yml       # 自动部署工作流
```

## 🚀 部署方法

### 方法一：GitHub Actions 自动部署（推荐）

#### 1. 启用 GitHub Pages

1. 访问仓库 Settings
2. 找到 **Pages** 部分
3. Source 选择 **GitHub Actions**
4. 保存设置

#### 2. 提交文档

```bash
git add docs/ mkdocs.yml .github/workflows/wiki-site.yml
git commit -m "docs: 添加 Wiki 站点"
git push
```

#### 3. 自动部署

GitHub Actions 会自动：
- 安装 MkDocs 和主题
- 构建静态网站
- 部署到 gh-pages 分支

访问地址：`https://andjie98.github.io/Slimefun4`

### 方法二：本地手动部署

#### 1. 安装依赖

```bash
pip install mkdocs mkdocs-material pymdown-extensions mike
```

#### 2. 本地预览

```bash
mkdocs serve
```

访问：http://127.0.0.1:8000

#### 3. 构建站点

```bash
mkdocs build
```

生成文件在 `site/` 目录

#### 4. 部署到 GitHub Pages

```bash
# 初始化 mike
mike deploy --push main

# 设置默认版本
mike set-default --push main
```

### 方法三：部署到其他平台

#### Vercel
1. 连接 GitHub 仓库
2. 自动检测 MkDocs
3. 一键部署

#### Netlify
1. 连接仓库
2. 构建命令：`mkdocs build`
3. 发布目录：`site/`

## 🎨 主题特性

### Material 主题功能

- ✅ **响应式设计** - 支持手机、平板、电脑
- ✅ **深色模式** - 自动/手动切换
- ✅ **全文搜索** - 快速查找内容
- ✅ **目录导航** - 侧边栏层级结构
- ✅ **代码高亮** - 支持多种语言
- ✅ **多版本** - 支持文档版本管理
- ✅ **社交分享** - 集成社交链接

### 导航结构

```
首页
  ├── 简介
  ├── 安装与配置
  └── 快速入门
游戏内容
  ├── 机器大全
  ├── 物品指南
  └── 研究系统
管理员工具
  ├── 命令参考
  └── 权限配置
开发
  ├── 项目结构
  ├── API 文档
  └── 构建指南
```

## 📝 添加新页面

### 1. 创建文档

在 `docs/` 目录创建 `.md` 文件：

```markdown
# 页面标题

页面内容...
```

### 2. 添加到导航

编辑 `mkdocs.yml`：

```yaml
nav:
  - 页面名称: filename.md
```

### 3. 提交部署

```bash
git add .
git commit -m "docs: 添加新页面"
git push
```

## 🔧 配置选项

### 站点信息

```yaml
site_name: Slimefun4 中文 Wiki
site_description: 官方文档
site_author: Slimefun4 中文社区
site_url: https://andjie98.github.io/Slimefun4
```

### 主题配置

```yaml
theme:
  name: material
  language: zh
  palette:
    primary: green
    accent: green
  features:
    - navigation.tabs
    - search.suggest
```

### 扩展功能

```yaml
markdown_extensions:
  - admonition          # 提示框
  - codehilite          # 代码高亮
  - footnotes           # 脚注
  - toc:
      permalink: true   # 标题链接
```

## 🎯 高级功能

### 版本管理

使用 **mike** 管理多版本：

```bash
# 部署新版本
mike deploy 1.0 v1.0

# 部署最新版
mike deploy main latest

# 设置默认版本
mike set-default main

# 列出所有版本
mike list
```

### 自定义域名

1. 在仓库 Settings → Pages 设置自定义域名
2. 创建 `docs/CNAME` 文件：
   ```
   wiki.yourdomain.com
   ```
3. 配置 DNS CNAME 记录

### SEO 优化

```yaml
extra:
  social:
    - icon: fontawesome/brands/github
      link: https://github.com/...
  analytics:
    provider: google
    property: G-XXXXXXXXXX
```

## 📊 与 GitHub Wiki 对比

| 特性 | MkDocs Wiki 站点 | GitHub Wiki |
|------|-----------------|-------------|
| **自定义程度** | ⭐⭐⭐⭐⭐ 高度自定义 | ⭐⭐ 有限 |
| **主题美观** | ⭐⭐⭐⭐⭐ Material 主题 | ⭐⭐⭐ 基础主题 |
| **搜索功能** | ⭐⭐⭐⭐⭐ 强大搜索 | ⭐⭐⭐ 基础搜索 |
| **响应式** | ⭐⭐⭐⭐⭐ 完美支持 | ⭐⭐⭐ 一般 |
| **多版本** | ⭐⭐⭐⭐⭐ 支持版本 | ⭐ 不支持 |
| **部署灵活** | ⭐⭐⭐⭐⭐ 多平台 | ⭐⭐ 仅 GitHub |
| **SEO 优化** | ⭐⭐⭐⭐⭐ 优秀 | ⭐⭐⭐ 一般 |
| **维护难度** | ⭐⭐⭐ 需要配置 | ⭐⭐⭐⭐⭐ 简单 |

## 🐛 常见问题

### Q1: 部署失败怎么办？

**A**: 检查：
1. GitHub Pages 是否启用
2. 工作流权限是否正确
3. Python 版本是否兼容
4. 查看 Actions 日志详情

### Q2: 如何更新站点？

**A**: 
```bash
# 修改文档后
git add .
git commit -m "docs: 更新内容"
git push

# 自动重新部署
```

### Q3: 本地预览不工作？

**A**:
```bash
# 重新安装依赖
pip install -r requirements.txt

# 清除缓存
mkdocs build --clean

# 重新启动
mkdocs serve
```

### Q4: 如何自定义样式？

**A**:
1. 创建 `docs/stylesheets/` 目录
2. 添加自定义 CSS
3. 在 `mkdocs.yml` 引入：
   ```yaml
   extra_css:
     - stylesheets/extra.css
   ```

## 📦 依赖管理

创建 `requirements.txt`：

```txt
mkdocs==1.5.3
mkdocs-material==9.4.14
pymdown-extensions==10.4
mike==2.0.0
```

安装：
```bash
pip install -r requirements.txt
```

## 🎉 总结

### 优势

✅ **美观专业** - Material 主题，现代化设计  
✅ **功能强大** - 搜索、导航、版本管理  
✅ **易于维护** - Markdown 编写，Git 版本控制  
✅ **灵活部署** - GitHub Pages、Vercel、Netlify  
✅ **完全免费** - 开源工具，无成本  

### 适用场景

- ✅ 项目文档
- ✅ 产品手册
- ✅ 知识库
- ✅ 博客站点
- ✅ 学习笔记

## 🔗 相关资源

- [MkDocs 官方文档](https://www.mkdocs.org/)
- [Material 主题文档](https://squidfunk.github.io/mkdocs-material/)
- [mike 版本管理](https://github.com/jimporter/mike)
- [GitHub Pages 文档](https://pages.github.com/)

---

**最后更新**: 2026-03-22  
**维护者**: Slimefun4 中文社区
