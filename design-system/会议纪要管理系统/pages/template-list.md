# TemplateList - 模板列表页面

> 覆盖规则：此页面的规则覆盖 MASTER.md 中的全局规则

## Page Info
- **Page Name:** 模板列表
- **Route:** `/templates`
- **Module:** 模板管理模块
- **Access:** 已登录用户

---

## Layout Structure

```
┌────────────────────────────────────────────────────────────────────────────┐
│  Header: [Logo] 会议纪要管理系统          [搜索框]    [消息🔔] [头像▾]        │
├────────────────────────────────────────────────────────────────────────────┤
│ Sidebar │  Content                                                              │
│         │  ┌──────────────────────────────────────────────────────────────┐   │
│ [首页]  │  │  模板管理                    [+ 创建模板] [筛选: 全部▾]         │   │
│         │  │                                                              │   │
│ [工作台]│  │  [全部] [预置模板] [我的模板] [团队共享]                      │   │
│         │  ├──────────────────────────────────────────────────────────────┤   │
│ [纪要]  │  │  ┌────────────┐  ┌────────────┐  ┌────────────┐             │   │
│ [模板]  │  │  │  📋        │  │  📋        │  │  📋        │             │   │
│ ●Active │  │  │            │  │            │  │            │             │   │
│         │  │  │ 决议型模板  │  │ 周会型模板  │  │ 自定义模板  │             │   │
│ [消息]  │  │  │ 预置模板    │  │ 预置模板    │  │ 我的模板    │             │   │
│ [搜索]  │  │  │            │  │            │  │            │             │   │
│ [统计]  │  │  │ [使用] [编辑]│  │ [使用] [编辑]│  │ [使用][编辑]│             │   │
│         │  │  └────────────┘  └────────────┘  └────────────┘             │   │
│         │  │                                                              │   │
│         │  │  ┌────────────┐  ┌────────────┐                            │   │
│         │  │  │  📋        │  │  📋        │                            │   │
│         │  │  │            │  │            │                            │   │
│         │  │  │ 讨论型模板  │  │ 项目启动会  │                            │   │
│         │  │  │ 预置模板    │  │ 团队共享    │                            │   │
│         │  │  │            │  │            │                            │   │
│         │  │  │ [使用] [编辑]│  │ [使用][共享]│                            │   │
│         │  │  └────────────┘  └────────────┘                            │   │
│         │  └──────────────────────────────────────────────────────────────┘   │
└────────────────────────────────────────────────────────────────────────────┘
```

---

## Color Usage

| Element | Color | Hex |
|---------|-------|-----|
| 预置标签 | Primary | `#7C3AED` |
| 我的标签 | 橙色 | `#F97316` |
| 共享标签 | 绿色 | `#10B981` |
| 卡片背景 | 白色 | `#FFFFFF` |
| 卡片边框 | 浅灰 | `#E2E8F0` |
| 正文文字 | 深紫 | `#4C1D95` |
| 次要文字 | 灰色 | `#6B7280` |

---

## Component Specs

### 模板卡片
- **Width:** 280px
- **Height:** 200px
- **Background:** `#FFFFFF`
- **Border:** 1px solid `#E2E8F0`
- **Border Radius:** 12px
- **Padding:** 20px
- **Hover:**
  - 边框变 `#7C3AED`
  - transform: translateY(-4px)
  - shadow: `var(--shadow-lg)`
- **Transition:** 200ms

### 模板图标区
- **Height:** 80px
- **Background:** `#FAF5FF`
- **Border Radius:** 8px
- **Icon Size:** 40px
- **Icon Color:** `#7C3AED`

### 模板类型标签
- **Component:** Element Plus `el-tag`
- **Size:** small
- **Position:** 卡片右上角

### 操作按钮组
- **Layout:** flex, gap 8px
- **按钮样式:**
  - 使用: type="primary", size="small"
  - 编辑: text, icon only

---

## Element Plus Components

| Element | Component | Props |
|---------|-----------|-------|
| 筛选Tab | el-tabs | v-model="activeTab" |
| 新建按钮 | el-button | type="primary", icon="Plus" |
| 模板卡片 | el-card | shadow="hover" |
| 类型标签 | el-tag | :type="tagType", size="small" |
| 使用按钮 | el-button | type="primary", size="small" |
| 编辑按钮 | el-button | text, icon="Edit" |
| 删除按钮 | el-button | text, icon="Delete", type="danger" |

---

## Icon Set

使用 **Lucide Icons**

| 用途 | Icon Name |
|------|-----------|
| 决议 | `CheckSquare` |
| 周会 | `Calendar` |
| 讨论 | `MessageSquare` |
| 启动会 | `Rocket` |
| 新建 | `Plus` |
| 编辑 | `Edit` |
| 删除 | `Trash2` |
| 分享 | `Share2` |
| 复制 | `Copy` |

---

## Template Types

| Type | Tag Color | Description |
|------|-----------|-------------|
| 预置模板 | Primary (#7C3AED) | 系统预置，不可删除 |
| 我的模板 | Orange (#F97316) | 用户自定义创建 |
| 团队共享 | Green (#10B981) | 共享给团队/部门 |

---

## Interaction Rules

| Action | Behavior |
|--------|----------|
| 点击模板卡片 | 选中模板，显示预览 |
| 点击使用 | 跳转至纪要创建页，携带templateId |
| 点击编辑 | 跳转至模板编辑页 `/templates/:id/edit` |
| 点击删除 | 弹出确认对话框 |
| 点击新建 | 跳转至模板创建页 `/templates/create` |
| 点击分享 | 弹出分享设置对话框 |

---

## Empty State

```
┌─────────────────────────────────────────────────────────────┐
│                                                             │
│                    📋 (模板插图)                            │
│                                                             │
│               您还没有创建任何模板                           │
│                                                             │
│     创建模板可以将常用的会议结构保存为模板，方便快速创建      │
│                                                             │
│                   [+ 创建模板]                              │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## Data Structure

```typescript
interface Template {
  id: string;
  name: string;
  type: 'preset' | 'custom' | 'shared';
  structure: {
    fields: TemplateField[];
  };
  creatorId: string;
  creatorName: string;
  shareScope: 'private' | 'department' | 'company';
  usageCount: number;
  createTime: string;
  updateTime: string;
}

interface TemplateField {
  id: string;
  name: string;
  type: 'title' | 'content' | 'decision' | 'todo' | 'datetime';
  required: boolean;
  defaultValue?: string;
}
```

---

## Accessibility

- [ ] 模板卡片有清晰的 focus 状态
- [ ] 按钮有 aria-label
- [ ] 空状态有说明文字
- [ ] 键盘可导航

---

## Responsive Behavior

| Breakpoint | Layout Change |
|------------|---------------|
| < 768px | 卡片单列，宽度100%，隐藏侧边栏 |
| 768px - 1024px | 卡片2列，侧边栏64px |
| > 1024px | 卡片3列，侧边栏240px |

---

## Anti-Patterns (禁止使用)

- ❌ 预置模板显示删除按钮
- ❌ 删除操作无确认
- ❌ 长模板名称不截断
- ❌ 模板列表无分页
