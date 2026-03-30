# Dashboard - 用户工作台页面

> 覆盖规则：此页面的规则覆盖 MASTER.md 中的全局规则

## Page Info
- **Page Name:** 用户工作台
- **Route:** `/dashboard`
- **Module:** 工作台模块
- **Access:** 已登录用户

---

## Layout Structure

```
┌────────────────────────────────────────────────────────────────────────────┐
│  Header: [Logo] 会议纪要管理系统          [搜索框]    [消息🔔] [头像▾]        │
├────────────────────────────────────────────────────────────────────────────┤
│ Sidebar │                                                                Content │
│         │  ┌──────────────────────────────────────────────────────────────┐ │
│ [首页]  │  │  👋 欢迎回来, {用户名}              [{当前日期}]                │ │
│         │  └──────────────────────────────────────────────────────────────┘ │
│ [工作台]│  ┌────────────────┐ ┌────────────────┐ ┌────────────────┐         │
│ ●Active │  │ 📋 待办事项     │ │ 📝 我发起的    │ │ 👥 我参与的    │         │
│         │  │     {数量}      │ │     {数量}     │ │     {数量}      │         │
│ [纪要]  │  └────────────────┘ └────────────────┘ └────────────────┘         │
│ [模板]  │                                                                │
│ [消息]  │  ┌──────────────────────────────────────────────────────────────┐ │
│ [搜索]  │  │  待办事项                                        [查看全部] │ │
│ [统计]  │  ├──────────────────────────────────────────────────────────────┤ │
│         │  │ ☐ [纪要标题] - 来自 {发令人} - {时间}              [处理]    │ │
│ ─────── │  │ ☐ [纪要标题] - 来自 {发令人} - {时间}              [处理]    │ │
│ [个人]  │  │ ☐ [纪要标题] - 来自 {发令人} - {时间}              [处理]    │ │
│ [设置]  │  └──────────────────────────────────────────────────────────────┘ │
│         │                                                                │
│         │  ┌──────────────────────────────────────────────────────────────┐ │
│         │  │ 快捷入口                                                      │ │
│         │  │ [+ 新建纪要] [📄 模板管理] [👤 个人中心] [📊 统计分析]         │ │
│         │  └──────────────────────────────────────────────────────────────┘ │
└────────────────────────────────────────────────────────────────────────────┘
```

---

## Color Usage

| Element | Color | Hex |
|---------|-------|-----|
| 侧边栏背景 | Primary | `#7C3AED` |
| 侧边栏文字 | 白色 | `#FFFFFF` |
| 侧边栏激活 | 深紫 | `#4C1D95` |
| 统计卡片背景 | 浅紫 | `#FAF5FF` |
| 快捷入口卡片 | 白色 | `#FFFFFF` |
| 主链接文字 | Primary | `#7C3AED` |
| 正文文字 | 深紫 | `#4C1D95` |
| 次要文字 | 灰色 | `#6B7280` |

---

## Component Specs

### 统计卡片 (3列)
- **Layout:** CSS Grid, `grid-template-columns: repeat(3, 1fr)`
- **Gap:** 24px
- **Background:** `#FAF5FF`
- **Border Radius:** 12px
- **Padding:** 24px
- **Shadow:** `var(--shadow-sm)`

### 待办列表卡片
- **Background:** `#FFFFFF`
- **Border Radius:** 12px
- **Padding:** 20px
- **Shadow:** `var(--shadow-md)`
- **Margin Top:** 24px

### 待办项
- **Height:** 56px
- **Border Bottom:** 1px solid `#E2E8F0`
- **Hover:** 背景色 `#F9FAFB`
- **Checkbox:** Element Plus `el-checkbox`
- **处理按钮:** `el-button size="small" type="primary"`

### 快捷入口按钮
- **Layout:** flex, gap 16px
- **Button Style:**
  - Width: 140px
  - Height: 80px
  - Border: 2px dashed `#E2E8F0`
  - Border Radius: 12px
  - Background: `#FFFFFF`
  - Hover: 边框变 `#7C3AED`, 背景 `#FAF5FF`

---

## Element Plus Components

| Element | Component | Props |
|---------|-----------|-------|
| 侧边导航 | el-menu | default-active, collapse |
| 统计数字 | el-statistic | :value="number" |
| 待办列表 | el-table | :data="todoList", border=false |
| 复选框 | el-checkbox | v-model |
| 处理按钮 | el-button | size="small", type="primary" |
| 头像下拉 | el-dropdown | @command="handleCommand" |

---

## Icon Set

使用 **Lucide Icons**

| 用途 | Icon Name |
|------|-----------|
| 首页 | `Home` |
| 会议纪要 | `FileText` |
| 模板管理 | `LayoutTemplate` |
| 消息中心 | `Bell` |
| 搜索 | `Search` |
| 统计分析 | `BarChart3` |
| 个人中心 | `User` |
| 设置 | `Settings` |
| 新建 | `Plus` |
| 待办 | `ClipboardList` |
| 我发起的 | `Send` |
| 我参与的 | `Users` |

---

## Interaction Rules

| Action | Behavior |
|--------|----------|
| 点击待办项 | 跳转至纪要编辑页面，携带 taskId 参数 |
| 点击处理按钮 | 跳转至纪要编辑页面 |
| 点击查看全部 | 跳转至对应的列表页面 |
| 点击新建纪要 | 跳转至纪要创建页面 `/minutes/create` |
| 点击快捷入口 | 跳转至对应功能页面 |
| 侧边栏切换 | 使用 Vue Router 导航 |

---

## Data Binding

### 待办事项列表
```typescript
interface TodoItem {
  id: string;
  minuteId: string;
  minuteTitle: string;
  assignerName: string;
  instruction: string;
  createTime: string;
  status: 'pending' | 'processing';
}
```

### 统计数据
```typescript
interface DashboardStats {
  todoCount: number;      // 待办数量
  initiatedCount: number; // 我发起的数量
  participatedCount: number; // 我参与的数量
}
```

---

## Accessibility

- [ ] 侧边栏支持键盘导航
- [ ] 统计数字有 aria-label
- [ ] 表格支持屏幕阅读器
- [ ] 所有图标有替代文字或 aria-label
- [ ] 颜色对比度 ≥ 4.5:1

---

## Responsive Behavior

| Breakpoint | Layout Change |
|------------|---------------|
| < 768px | 侧边栏折叠为图标模式，统计卡片单列显示 |
| 768px - 1024px | 侧边栏固定 64px，统计卡片 3 列 |
| > 1024px | 侧边栏固定 240px，统计卡片 3 列 |

---

## Loading & Error States

| State | UI Treatment |
|-------|--------------|
| 加载中 | 骨架屏 (skeleton) 显示统计卡片和列表 |
| 空数据 | 空状态插图 + "暂无待办事项" 文字 |
| 加载失败 | el-alert 提示错误 + 重试按钮 |

---

## Anti-Patterns (禁止使用)

- ❌ Emoji作为图标
- ❌ 点击元素无 cursor-pointer
- ❌ 列表无分页导致长滚动
- ❌ 错误状态无提示
