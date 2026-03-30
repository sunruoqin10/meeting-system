# 会议纪要管理系统 - 前端

基于 Vue3 + Element Plus + ECharts 的会议纪要管理系统前端项目。

## 技术栈

- **框架**: Vue 3 (Composition API)
- **路由**: Vue Router 4
- **状态管理**: Pinia
- **UI组件**: Element Plus
- **图表**: ECharts
- **构建工具**: Vite
- **样式**: SCSS

## 项目结构

```
frontend/
├── src/
│   ├── assets/
│   │   └── styles/
│   │       └── main.scss        # 全局样式
│   ├── mock/
│   │   └── data.js             # Mock 数据
│   ├── router/
│   │   └── index.js             # 路由配置
│   ├── views/                   # 页面组件
│   │   ├── Login.vue            # 登录页
│   │   ├── Layout.vue           # 布局组件
│   │   ├── Dashboard.vue        # 工作台
│   │   ├── MinutesList.vue      # 纪要列表
│   │   ├── MinutesEditor.vue    # 纪要编辑器
│   │   ├── MinutesDetail.vue    # 纪要详情
│   │   ├── VersionHistory.vue   # 版本历史
│   │   ├── TemplateList.vue     # 模板管理
│   │   ├── MessageCenter.vue    # 消息中心
│   │   ├── Search.vue           # 搜索页
│   │   ├── Statistics.vue       # 统计分析
│   │   ├── Profile.vue          # 个人中心
│   │   ├── DepartmentManage.vue  # 部门管理
│   │   ├── UserManage.vue       # 用户管理
│   │   └── OperationLogs.vue    # 操作日志
│   ├── App.vue
│   └── main.js
├── public/
│   └── favicon.svg
├── index.html
├── package.json
├── vite.config.js
└── README.md
```

## 功能模块

### 已实现页面

1. **登录页面** - 用户登录表单
2. **工作台** - 统计概览、待办事项、快捷入口
3. **会议纪要列表** - 筛选、搜索、分页
4. **会议纪要编辑器** - 富文本编辑、自动保存、附件上传
5. **会议纪要详情** - 内容展示、评论、附件预览
6. **版本历史** - 版本列表、对比、回滚
7. **模板管理** - 模板列表、创建、分享
8. **消息中心** - 消息分类、已读/未读
9. **搜索页** - 全文搜索、高级筛选
10. **统计分析** - 图表展示、数据导出
11. **个人中心** - 信息编辑、密码修改
12. **部门管理** - 树形/表格视图、CRUD
13. **用户管理** - 用户列表、角色管理
14. **操作日志** - 日志查询、导出

### 设计规范

遵循 `../design-system/会议纪要管理系统/` 中的UI设计规范：

- **色彩系统**: Primary `#7C3AED`, CTA `#F97316`
- **字体**: Fira Code (标题) + Fira Sans (正文)
- **阴影**: Soft UI 风格
- **图标**: Lucide Icons / Element Plus Icons

## 快速开始

```bash
# 安装依赖
npm install

# 开发模式
npm run dev

# 构建生产版本
npm run build

# 预览生产版本
npm run preview
```

## Mock 数据

所有页面使用 `src/mock/data.js` 中的模拟数据，无需后端即可运行完整功能。

## 注意事项

- 登录后会自动跳转到工作台
- 所有操作使用 mock 数据，无真实后端交互
- 统计数据使用 ECharts 渲染
- 样式使用 SCSS，遵循设计规范中的变量定义
