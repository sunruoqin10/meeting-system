# MessageCenter - 消息中心页面

> 覆盖规则：此页面的规则覆盖 MASTER.md 中的全局规则

## Page Info
- **Page Name:** 消息中心
- **Route:** `/messages`
- **Module:** 消息通知模块
- **Access:** 已登录用户

---

## Layout Structure

```
┌────────────────────────────────────────────────────────────────────────────┐
│  Header: [Logo] 会议纪要管理系统          [搜索框]    [消息🔔] [头像▾]        │
├────────────────────────────────────────────────────────────────────────────┤
│ Sidebar │  Content                                                              │
│         │  ┌──────────────────────────────────────────────────────────────┐   │
│ [首页]  │  │  消息中心                              [全部已读] [设置]       │   │
│         │  │                                                              │   │
│ [工作台]│  │  [全部] [待办提醒] [审核通知] [系统公告]   未读: {n}          │   │
│         │  ├──────────────────────────────────────────────────────────────┤   │
│ [纪要]  │  │                                                              │   │
│ [模板]  │  │  ┌──────────────────────────────────────────────────────────┐│   │
│ [消息]  │  │  │ 🔔 待办提醒                              今天 14:30     ││   │
│ ●Active │  │  │                                                          ││   │
│         │  │  │ 📋 会议纪要「{标题}」需要您处理修订任务      [查看]     ││   │
│ [搜索]  │  │  │    发令人: {用户名}                                      ││   │
│ [统计]  │  │  └──────────────────────────────────────────────────────────┘│   │
│         │  │                                                              │   │
│         │  │  ┌──────────────────────────────────────────────────────────┐│   │
│         │  │  │ 📧 审核通知                              今天 10:15     ││   │
│         │  │  │ 🔴 (未读标记)                                               ││   │
│         │  │  │ 📋 您的修订意见已被 {用户名} 审核通过          [查看]    ││   │
│         │  │  └──────────────────────────────────────────────────────────┘│   │
│         │  │                                                              │   │
│         │  │  ┌──────────────────────────────────────────────────────────┐│   │
│         │  │  │ 📢 系统公告                              昨天 09:00     ││   │
│         │  │  │                                                          ││   │
│         │  │  │ 系统将于本周六进行升级维护，预计 downtime 2小时   [查看]  ││   │
│         │  │  └──────────────────────────────────────────────────────────┘│   │
│         │  │                                                              │   │
│         │  └──────────────────────────────────────────────────────────────┘   │
│         │                                                                     │
└────────────────────────────────────────────────────────────────────────────┘
```

---

## Color Usage

| Element | Color | Hex |
|---------|-------|-----|
| 未读标记 | 红色 | `#EF4444` |
| 待办提醒图标 | 橙色 | `#F97316` |
| 审核通知图标 | 蓝色 | `#3B82F6` |
| 系统公告图标 | 紫色 | `#8B5CF6` |
| 消息卡片背景-未读 | 浅紫 | `#FAF5FF` |
| 消息卡片背景-已读 | 白色 | `#FFFFFF` |
| 时间文字 | 灰色 | `#9CA3AF` |
| 正文文字 | 深紫 | `#4C1D95` |
| 次要文字 | 灰色 | `#6B7280` |

---

## Component Specs

### 消息卡片
- **Background (未读):** `#FAF5FF`
- **Background (已读):** `#FFFFFF`
- **Border:** 1px solid `#E2E8F0`
- **Border Radius:** 12px
- **Padding:** 16px 20px
- **Margin Bottom:** 12px
- **Hover:** 边框变 `#7C3AED`
- **Transition:** 200ms

### 未读标记
- **Component:** Element Plus Badge 或红点
- **Size:** 8px 圆形
- **Color:** `#EF4444`
- **Position:** 消息卡片左侧

### 分类Tab
- **Component:** Element Plus `el-tabs`
- **未读计数:** `el-badge` 显示在 tab 右侧

### 全部已读按钮
- **Component:** Element Plus `el-button`
- **Type:** text
- **Color:** `#7C3AED`

---

## Element Plus Components

| Element | Component | Props |
|---------|-----------|-------|
| 分类Tab | el-tabs | v-model="activeTab", @tab-change |
| 消息卡片 | el-card | shadow="hover", :body-style |
| 未读标记 | el-badge | :value="''", :hidden="isRead" |
| 时间 | el-text | size="small", color="#9CA3AF" |
| 查看按钮 | el-button | size="small", type="primary" |
| 全部已读 | el-button | text, @click="markAllRead" |
| 消息设置 | el-button | text, icon="Setting" |

---

## Icon Set

使用 **Lucide Icons**

| 用途 | Icon Name |
|------|-----------|
| 待办提醒 | `ClipboardList` |
| 审核通知 | `CheckCircle` |
| 系统公告 | `Megaphone` |
| 已读 | `Check` |
| 设置 | `Settings` |
| 查看 | `ArrowRight` |

---

## Message Types

| Type | Icon Color | Description |
|------|------------|-------------|
| 待办提醒 | `#F97316` | 任务指派、修订任务等 |
| 审核通知 | `#3B82F6` | 审核通过、驳回等 |
| 系统公告 | `#8B5CF6` | 系统升级、通知等 |
| 抄送通知 | `#6B7280` | 被抄送、被@等 |

---

## Interaction Rules

| Action | Behavior |
|--------|----------|
| 点击消息卡片 | 标记为已读，跳转至关联页面 |
| 点击查看按钮 | 跳转至关联页面 |
| 点击全部已读 | 确认对话框，确认后标记所有消息已读 |
| 切换Tab | 筛选对应类型的消息列表 |
| 滚动到底部 | 加载更多消息 (分页) |

---

## Data Structure

```typescript
interface Message {
  id: string;
  type: 'todo' | 'review' | 'system' | 'cc';
  title: string;
  content: string;
  relatedType: 'minute' | 'task' | 'template' | 'system';
  relatedId: string;
  senderId: string;
  senderName: string;
  isRead: boolean;
  priority: 'urgent' | 'high' | 'medium' | 'low';
  createTime: string;
  readTime: string | null;
}
```

---

## Accessibility

- [ ] 未读状态有 aria-label 提示
- [ ] Tab 支持键盘导航
- [ ] 消息列表支持屏幕阅读器
- [ ] 操作按钮有清晰的焦点状态

---

## Responsive Behavior

| Breakpoint | Layout Change |
|------------|---------------|
| < 768px | 消息卡片全宽，Tab可横向滚动 |
| 768px - 1024px | 侧边栏64px，消息卡片全宽 |
| > 1024px | 侧边栏240px，消息卡片最大宽度800px |

---

## Loading & Empty States

| State | UI Treatment |
|-------|--------------|
| 加载中 | 骨架屏显示3个消息卡片 |
| 空数据 | 插图 + "暂无消息" |
| 全部已读 | "🎉 所有消息已读" |

---

## Anti-Patterns (禁止使用)

- ❌ 未读消息无视觉区分
- ❌ 点击消息不标记已读
- ❌ 删除消息无确认
- ❌ 消息列表无分页导致长滚动
