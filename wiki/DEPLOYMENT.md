# GitHub Wiki 部署说明

本文件说明如何将 `wiki/` 目录中的文档部署到 GitHub Wiki。

## 📋 部署方法

### 方法一：使用 GitHub Actions（推荐）

#### 1. 创建工作流文件

创建 `.github/workflows/wiki-deploy.yml`:

```yaml
name: Deploy Wiki

on:
  push:
    branches:
      - main
    paths:
      - 'wiki/**'

jobs:
  deploy-wiki:
    runs-on: ubuntu-latest
    
    steps:
      - name: Checkout Repository
        uses: actions/checkout@v3
      
      - name: Push Wiki to GitHub
        uses: Andrew-Chen-wang/github-wiki-action@v4
        with:
          wiki_dir: wiki
          git_user: ${{ github.actor }}
          git_email: ${{ github.actor }}@users.noreply.github.com
```

#### 2. 启用 GitHub Wiki

1. 访问仓库 Settings
2. 找到 "Features" 部分
3. 勾选 "Wikis" 选项
4. Wiki 功能已启用

#### 3. 自动部署

现在每次推送 `wiki/` 目录的更改都会自动部署到 GitHub Wiki。

### 方法二：手动部署

#### 1. 克隆 Wiki 仓库

```bash
# Wiki 是一个独立的 git 仓库
git clone https://github.com/andjie98/Slimefun4.wiki.git
cd Slimefun4.wiki
```

#### 2. 复制 Wiki 文件

```bash
# 从主仓库复制所有 wiki 文件
cp /path/to/Slimefun4/wiki/*.md .
```

#### 3. 提交并推送

```bash
git add .
git commit -m "docs: 部署完整 Wiki 文档"
git push origin master
```

### 方法三：使用 Git 子模块

#### 1. 在主仓库中配置

```bash
# 在主仓库根目录创建 .gitmodules 文件
cat > .gitmodules << EOF
[submodule "wiki"]
    path = wiki
    url = https://github.com/andjie98/Slimefun4.wiki.git
EOF

# 初始化子模块
git submodule update --init --recursive
```

#### 2. 更新 Wiki 内容

```bash
cd wiki
# 编辑文件
git add .
git commit -m "docs: 更新 Wiki 内容"
git push
cd ..
git add wiki
git commit -m "更新子模块"
git push
```

## 📁 文件结构说明

### 主仓库 (Slimefun4)

```
Slimefun4/
├── wiki/                      # Wiki 文档源文件
│   ├── Home.md               # 首页
│   ├── Machines.md           # 机器指南
│   ├── Items.md              # 物品指南
│   ├── Research.md           # 研究系统
│   ├── Commands.md           # 命令权限
│   ├── _Sidebar.md           # 侧边栏
│   ├── _Footer.md            # 页脚
│   └── README.md             # Wiki 说明
└── .github/
    └── workflows/
        └── wiki-deploy.yml   # 自动部署工作流
```

### GitHub Wiki 仓库

```
Slimefun4.wiki/
├── Home.md                   # 自动显示为首页
├── Machines.md              # 机器指南页面
├── Items.md                 # 物品指南页面
├── Research.md              # 研究系统页面
├── Commands.md              # 命令权限页面
├── _Sidebar.md              # 侧边栏（自动识别）
└── _Footer.md               # 页脚（自动识别）
```

## 🔧 配置选项

### GitHub Wiki 设置

1. **访问权限**
   - 公开仓库：所有人可见
   - 私有仓库：仅协作者可见

2. **编辑权限**
   - 默认：所有人可编辑（公开仓库）
   - 建议：仅协作者可编辑

3. **设置方法**
   - Settings → Features → Wikis
   - 配置相关选项

### 自定义域名

如需为 Wiki 设置自定义域名：

1. 在 GitHub Pages 设置
2. 添加 CNAME 记录
3. 配置 DNS

## 📊 部署检查清单

部署前确认：

- [ ] 所有 `.md` 文件已创建
- [ ] 文件命名正确（无中文空格）
- [ ] 内部链接测试通过
- [ ] 图片链接正确（如有）
- [ ] _Sidebar.md 包含所有页面链接
- [ ] _Footer.md 配置正确
- [ ] README.md 说明完整

部署后验证：

- [ ] 所有页面可正常访问
- [ ] 导航链接工作正常
- [ ] 侧边栏显示正确
- [ ] 页脚显示正确
- [ ] 移动端显示正常
- [ ] 搜索功能可用

## 🎨 Wiki 主题

GitHub Wiki 使用默认主题，支持：

- 清晰的标题层级
- Markdown 表格
- 代码高亮
- 表情符号
- 任务列表

## 📝 最佳实践

### 1. 内容组织

- 保持页面简洁（每页 2000-5000 字）
- 使用清晰的目录结构
- 提供内部链接导航
- 添加相关页面引用

### 2. 更新维护

- 定期更新过时内容
- 修正发现的错误
- 添加新的功能文档
- 收集用户反馈

### 3. 版本管理

- 在页脚标注适用版本
- 重大更新时创建版本分支
- 保留历史版本文档

## 🔗 相关链接

- [GitHub Wiki 官方文档](https://docs.github.com/en/communities/documenting-your-project-with-wikis)
- [GitHub Actions 文档](https://docs.github.com/en/actions)
- [Markdown 语法指南](https://guides.github.com/features/mastering-markdown/)

## 💡 常见问题

### Q: Wiki 和 README 有什么区别？

**A**: 
- README 显示在仓库首页
- Wiki 是多页面的完整文档系统
- Wiki 支持搜索和分类

### Q: 如何启用 Wiki？

**A**:
1. Settings → Features
2. 勾选 "Wikis"
3. 保存设置

### Q: Wiki 可以版本控制吗？

**A**: 
- 可以，Wiki 是独立的 git 仓库
- 支持所有 git 操作
- 有完整的提交历史

### Q: 如何禁止他人编辑 Wiki？

**A**:
- 私有仓库：默认仅协作者可编辑
- 公开仓库：无法完全禁止，但可回滚更改

---

**部署指南版本**: 1.0  
**最后更新**: 2026-03-22  
**适用**: Slimefun4 v4.1.15
