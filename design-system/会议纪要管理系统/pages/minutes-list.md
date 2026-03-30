# MinutesList - 会议纪要列表页面

> 覆盖规则：此页面的规则覆盖 MASTER.md 中的全局规则

## Page Info
- **Page Name:** 会议纪要列表
- **Route:** `/minutes`
- **Module:** 会议纪要模块
- **Access:** 已登录用户

---

## Layout Structure

```
┌────────────────────────────────────────────────────────────────────────────┐
│  Header: [Logo] 会议纪要管理系统          [搜索框]    [消息🔔] [头像▾]        │
├────────────────────────────────────────────────────────────────────────────┤
│ Sidebar │  Content                                                              │
│         │  ┌──────────────────────────────────────────────────────────────┐   │
│ [首页]  │  │  会议纪要    [+ 新建纪要]                                     │   │
│         │  │                                                              │   │
│ [工作台]│  │  [全部] [草稿] [已发送] [修订中] [已归档]    [筛选▾] [排序▾]  │   │
│         │  ├──────────────────────────────────────────────────────────────┤   │
│ [纪要]  │  │  ┌─────────────────────────────────────────────────────────┐│   │
│ ●Active │  │  │ 📋 {会议标题}                            V1.0  [已发送] ││   │
│         │  │  │ 📅 {日期} | 👤 {创建人} | 💬 {评论数} | 📎 {附件数}     ││   │
│ [模板]  │  │  │ {内容摘要...}                                              ││   │
│ [消息]  │  │  └─────────────────────────────────────────────────────────┘│   │
│ [搜索]  │  │  ┌─────────────────────────────────────────────────────────┐│   │
│ [统计]  │  │  │ 📋 {会议标题}                            V1.1  [修订中] ││   │
│         │  │  │ 📅 {日期} | 👤 {创建人} | 💬 {评论数} | 📎 {附件数}     ││   │
│         │  │  │ {内容摘要...}                                              ││   │
│         │  │  └─────────────────────────────────────────────────────────┘│   │
│         │  ├──────────────────────────────────────────────────────────────┤   │
│         │  │  < 1 2 3 ... 10 >                            共 {n} 条     │   │
│         │  └──────────────────────────────────────────────────────────────┘   │
└────────────────────────────────────────────────────────────────────────────┘
```

---

## Color Usage

| Element | Color | Hex |
|---------|-------|-----|
| 状态标签-草稿 | 灰色 | `#9CA3AF` |
| 状态标签-已发送 | 绿色 | `#10B981` |
| 状态标签-修订中 | 橙色 | `#F97316` |
| 状态标签-已归档 | 蓝色 | `#3B82F6` |
| 卡片背景 | 白色 | `#FFFFFF` |
| 卡片边框 | 浅灰 | `#E2E8F0` |
| 卡片悬浮 | 浅紫 | `#FAF5FF` |
| 正文文字 | 深紫 | `#4C1D95` |
| 次要文字 | 灰色 | `#6B7280` |

---

## Component Specs

### 筛选Tab
- **Component:** Element Plus `el-tabs`
- **Tab Items:** 全部 / 草稿 / 已发送 / 修订中 / 已归档
- **Active Tab:** 下划线 `#7C3AED`，文字 `#7C3AED`
- **Inactive Tab:** 文字 `#6B7280`
- **Gap:** 8px

### 纪要卡片
- **Background:** `#FFFFFF`
- **Border:** 1px solid `#E2E8F0`
- **Border Radius:** 12px
- **Padding:** 20px
- **Margin Bottom:** 16px
- **Hover:**
  - 边框颜色变 `#7C3AED`
  - 阴影变 `var(--shadow-md)`
  - transform: translateY(-2px)
- **Transition:** 200ms

### 状态标签
- **Component:** Element Plus `el-tag`
- **Size:** small
- **Border Radius:** 4px
- **Padding:** 2px 8px

### 版本号
- **Font:** Fira Code
- **Color:** `#6B7280`
- **Size:** 12px

---

## Element Plus Components

| Element | Component | Props |
|---------|-----------|-------|
| 筛选Tab | el-tabs | v-model="activeTab", @tab-change |
| 状态标签 | el-tag | :type="statusType", size="small" |
| 搜索框 | el-input | v-model="keyword", placeholder="搜索纪要...", prefix-icon="Search" |
| 筛选下拉 | el-select | v-model="filterDept" |
| 排序下拉 | el-select | v-model="sortOrder" |
| 新建按钮 | el-button | type="primary", @click="createMinute" |
| 分页 | el-pagination | v-model:current-page, :page-size, :total, @current-change |

---

## Icon Set

使用 **Lucide Icons**

| 用途 | Icon Name |
|------|-----------|
| 会议 | `FileText` |
| 日期 | `Calendar` |
| 评论 | `MessageCircle` |
| 附件 | `Paperclip` |
| 新建 | `Plus` |
| 发送 | `Send` |
| 归档 | `Archive` |

---

## Data Structure

```typescript
interface Minute {
  id: string;
  title: string;
  currentVersion: string;  // V1.0
  status: 'draft' | 'sent' | 'revising' | 'archived';
  meetingDate: string;
  creatorName: string;
  commentCount: number;
  attachmentCount: number;
  summary: string;  // 内容摘要，前100字
}
```

---

## Interaction Rules

| Action | Behavior |
|--------|----------|
| 点击卡片 | 跳转至纪要详情 `/minutes/:id` |
| 点击新建按钮 | 跳转至纪要创建 `/minutes/create` |
| 切换Tab | 筛选列表数据，保留搜索关键词 |
| 点击筛选/排序 | 重新请求列表数据 |
| 分页切换 | 加载对应页数据 |

---

## Accessibility

- [ ] Tab 支持键盘导航 (← →)
- [ ] 卡片有明确的 focus 状态
- [ ] 状态标签有 aria-label
- [ ] 分页支持键盘操作

---

## Responsive Behavior

| Breakpoint | Layout Change |
|------------|---------------|
| < 768px | 隐藏侧边栏(抽屉模式)，卡片单列，Tab可横向滚动 |
| 768px - 1024px | 侧边栏64px，2列卡片 |
| > 1024px | 侧边栏240px，1列卡片(列表模式) |

---

## Loading & Empty States

| State | UI Treatment |
|-------|--------------|
| 加载中 | 骨架屏显示3个卡片 |
| 空数据 | 插图 + "暂无会议纪要" + 新建按钮 |
| 加载失败 | el-alert + 重试按钮 |

---

## Anti-Patterns (禁止使用)

- ❌ Emoji作为图标
- ❌ 卡片hover时布局抖动
- ❌ 长标题不截断
- ❌ 无限滚动无分页
