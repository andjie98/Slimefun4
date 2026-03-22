# MkDocs Wiki 站点快速启动

## 🚀 5 分钟部署独立 Wiki 站点

### 步骤 1：启用 GitHub Pages

1. 访问 https://github.com/andjie98/Slimefun4/settings/pages
2. 在 **Build and deployment** 部分
3. Source 选择 **GitHub Actions**
4. 保存设置

### 步骤 2：等待自动部署

GitHub Actions 会自动：
- ✅ 安装 MkDocs 和 Material 主题
- ✅ 构建静态网站
- ✅ 部署到 GitHub Pages

访问地址：**https://andjie98.github.io/Slimefun4**

### 步骤 3：查看效果

部署完成后（约 2-3 分钟），访问：
```
https://andjie98.github.io/Slimefun4
```

## 📁 文件结构

```
Slimefun4/
├── docs/                    # 📄 文档内容
│   └── index.md            # 首页
├── mkdocs.yml              # ⚙️ 站点配置
└── .github/workflows/
    └── wiki-site.yml       # 🔄 自动部署
```

## 🎨 特性预览

### 现代化设计
- ✅ Material Design 主题
- ✅ 响应式布局
- ✅ 深色/浅色模式
- ✅ 完美移动端支持

### 强大功能
- ✅ 全文搜索
- ✅ 多级导航
- ✅ 代码高亮
- ✅ 多版本管理
- ✅ 社交分享

## 📝 添加新页面

### 1. 创建文档

在 `docs/` 目录创建 `.md` 文件：

```markdown
# 页面标题

这里是内容...
```

### 2. 添加到导航

编辑 `mkdocs.yml`：

```yaml
nav:
  - 页面名称: filename.md
```

### 3. 提交自动部署

```bash
git add .
git commit -m "docs: 添加新页面"
git push
```

## 🔧 本地开发

### 安装依赖

```bash
pip install mkdocs mkdocs-material pymdown-extensions mike
```

### 本地预览

```bash
mkdocs serve
```

访问：http://127.0.0.1:8000

### 构建发布

```bash
mkdocs build
# 生成文件在 site/ 目录
```

## 🌐 部署选项

### GitHub Pages（默认）
- ✅ 免费
- ✅ 自动部署
- ✅ HTTPS 支持
- ✅ 自定义域名

### 其他平台
- Vercel
- Netlify
- 自己的服务器

## 📊 与 GitHub Wiki 对比

| 特性 | MkDocs 站点 | GitHub Wiki |
|------|-----------|-------------|
| 美观度 | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ |
| 自定义 | ⭐⭐⭐⭐⭐ | ⭐⭐ |
| 搜索 | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ |
| 移动端 | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ |
| 多版本 | ⭐⭐⭐⭐⭐ | ❌ |
| 部署 | 自动 | 手动 |

## 🎯 下一步

### 从 GitHub Wiki 迁移内容

可以将 `wiki/` 目录的内容复制到 `docs/`：

```bash
# 复制主要文档
cp wiki/Home.md docs/index.md
cp wiki/Machines.md docs/machines.md
cp wiki/Items.md docs/items.md
cp wiki/Research.md docs/research.md
cp wiki/Commands.md docs/commands.md
```

### 优化内容格式

MkDocs 支持更多 Markdown 扩展：

```markdown
!!! note "提示"
    这是一个提示框

??? question "常见问题"
    这是可折叠的内容

```python
print("代码高亮")
```
```

## 🆘 故障排查

### 部署失败？

1. 检查 Actions 日志
2. 确认 Python 版本
3. 验证 mkdocs.yml 格式

### 页面不显示？

1. 清除浏览器缓存
2. 检查 gh-pages 分支
3. 等待 5-10 分钟

### 本地预览问题？

```bash
# 重新安装
pip install -U mkdocs mkdocs-material

# 清除缓存
mkdocs build --clean

# 重新启动
mkdocs serve
```

## 📚 学习资源

- [MkDocs 官方文档](https://www.mkdocs.org/)
- [Material 主题](https://squidfunk.github.io/mkdocs-material/)
- [Markdown 语法](https://commonmark.org/help/)

## 🎉 完成！

现在你有一个：
- ✅ 专业的 Wiki 站点
- ✅ 自动部署
- ✅ 美观大方
- ✅ 功能完整

访问：**https://andjie98.github.io/Slimefun4**

---

**最后更新**: 2026-03-22  
**版本**: 1.0
