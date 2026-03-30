# Search - 搜索结果页面

> 覆盖规则：此页面的规则覆盖 MASTER.md 中的全局规则

## Page Info
- **Page Name:** 搜索结果
- **Route:** `/search`
- **Module:** 搜索模块
- **Access:** 已登录用户

---

## Layout Structure

```
┌────────────────────────────────────────────────────────────────────────────┐
│  Header: [Logo] 会议纪要管理系统    [🔍 搜索框...............]  [消息🔔] [头像▾]│
├────────────────────────────────────────────────────────────────────────────┤
│ Sidebar │  Content                                                              │
│         │  ┌──────────────────────────────────────────────────────────────┐   │
│ [首页]  │  │  搜索结果: 「{关键词}」                        找到 {n} 条结果 │   │
│         │  │                                                              │   │
│ [工作台]│  │  ┌──────────────────────────────────────────────────────────┐│   │
│         │  │  │ 🔍 高级搜索                                    [展开/收起] ││   │
│ [纪要]  │  │  │ ┌──────────────────────────────────────────────────────┐││   │
│ [模板]  │  │  │ │ 日期范围: [2024-01-01] - [2024-01-31]               │││   │
│ [消息]  │  │  │ │ 创建人: [选择用户▾]  部门: [选择部门▾]              │││   │
│ [搜索]  │  │  │ │ 状态: [全部▾]  类型: [全部▾]                        │││   │
│ ●Active │  │  │ └──────────────────────────────────────────────────────┘││   │
│         │  │  │                                              [搜索]   ││   │
│ [统计]  │  │  └──────────────────────────────────────────────────────────┘│   │
│         │  ├──────────────────────────────────────────────────────────────┤   │
│         │  │                                                              │   │
│         │  │  搜索历史: [{标签1}] [{标签2}] [{标签3}]  [🗑️ 清除]          │   │
│         │  │                                                              │   │
│         │  │  ┌──────────────────────────────────────────────────────────┐│   │
│         │  │  │ 📋 {会议标题}                              {日期}        ││   │
│         │  │  │ {内容摘要...} <mark>关键词</mark> 高亮显示...            ││   │
│         │  │  │ 👤 {创建人} | 💬 {评论数} | 📎 {附件数}                 ││   │
│         │  │  └──────────────────────────────────────────────────────────┘│   │
│         │  │                                                              │   │
│         │  │  ┌──────────────────────────────────────────────────────────┐│   │
│         │  │  │ 📋 {会议标题}                              {日期}        ││   │
│         │  │  │ {内容摘要...} <mark>关键词</mark> 高亮显示...            ││   │
│         │  │  │ 👤 {创建人} | 💬 {评论数} | 📎 {附件数}                 ││   │
│         │  │  └──────────────────────────────────────────────────────────┘│   │
│         │  │                                                              │   │
│         │  ├──────────────────────────────────────────────────────────────┤   │
│         │  │  < 1 2 3 ... 10 >                              共 {n} 条     │   │
│         │  └──────────────────────────────────────────────────────────────┘   │
└────────────────────────────────────────────────────────────────────────────┘
```

---

## Color Usage

| Element | Color | Hex |
|---------|-------|-----|
| 搜索框边框 | Primary | `#7C3AED` |
| 关键词高亮 | 黄色背景 | `#FEF08A` |
| 结果卡片背景 | 白色 | `#FFFFFF` |
| 结果卡片边框 | 浅灰 | `#E2E8F0` |
| 高级搜索背景 | 浅灰 | `#F9FAFB` |
| 历史标签背景 | 浅紫 | `#FAF5FF` |
| 正文文字 | 深紫 | `#4C1D95` |
| 次要文字 | 灰色 | `#6B7280` |

---

## Component Specs

### 搜索框
- **Height:** 44px
- **Border:** 2px solid `#7C3AED`
- **Border Radius:** 22px
- **Background:** `#FFFFFF`
- **Focus:** 边框加粗，添加光晕
- **Icon:** 左侧搜索图标

### 高级搜索面板
- **Background:** `#F9FAFB`
- **Border Radius:** 12px
- **Padding:** 20px
- **Margin Bottom:** 24px
- **展开/收起动画:** 200ms

### 结果卡片
- **Background:** `#FFFFFF`
- **Border:** 1px solid `#E2E8F0`
- **Border Radius:** 12px
- **Padding:** 20px
- **Margin Bottom:** 16px
- **Hover:**
  - 边框变 `#7C3AED`
  - 阴影 `var(--shadow-md)`

### 关键词高亮
- **Background:** `#FEF08A`
- **Border Radius:** 2px
- **Padding:** 0 2px

### 历史标签
- **Background:** `#FAF5FF`
- **Border Radius:** 16px
- **Padding:** 6px 12px
- **Hover:** 背景变 `#7C3AED`, 文字变白

---

## Element Plus Components

| Element | Component | Props |
|---------|-----------|-------|
| 搜索框 | el-input | v-model="keyword", prefix-icon="Search", size="large" |
| 高级搜索 | el-collapse | v-model="advancedOpen" |
| 日期范围 | el-date-picker | type="daterange", format="YYYY-MM-DD" |
| 用户选择 | el-select | filterable, remote |
| 部门选择 | el-select | filterable |
| 状态选择 | el-select | options=[全部, 草稿, 已发送, 修订中, 已归档] |
| 分页 | el-pagination | v-model:current-page, :total, :page-size |

---

## Icon Set

使用 **Lucide Icons**

| 用途 | Icon Name |
|------|-----------|
| 搜索 | `Search` |
| 清除 | `X` |
| 删除历史 | `Trash2` |
| 展开 | `ChevronDown` |
| 收起 | `ChevronUp` |

---

## Search Types

| Type | Description |
|------|-------------|
| 全文搜索 | 搜索标题和内容 |
| 标题搜索 | 仅搜索标题 |
| 内容搜索 | 仅搜索正文内容 |

---

## Interaction Rules

| Action | Behavior |
|--------|----------|
| 回车搜索 | 提交搜索表单 |
| 点击搜索按钮 | 提交搜索表单 |
| 点击历史标签 | 使用该关键词搜索 |
| 点击结果卡片 | 跳转至纪要详情 |
| 展开高级搜索 | 显示更多筛选条件 |
| 切换分页 | 加载对应页结果 |

---

## Performance Requirements

| Metric | Requirement |
|--------|-------------|
| 搜索响应时间 | < 2秒 |
| 高亮渲染 | 实时渲染 |
| 分页加载 | < 500ms |

---

## Data Structure

```typescript
interface SearchResult {
  id: string;
  title: string;
  content: string;      // 摘要内容，关键词高亮
  highlightHtml: string; // HTML with <mark> tags
  meetingDate: string;
  creatorName: string;
  status: string;
  commentCount: number;
  attachmentCount: number;
  score: number;         // 相关性得分
}

interface SearchHistory {
  keyword: string;
  searchTime: string;
  resultCount: number;
}
```

---

## Accessibility

- [ ] 搜索框有 label (可用 aria-label)
- [ ] 高级搜索支持键盘操作
- [ ] 高亮内容有说明
- [ ] 搜索结果有总数提示

---

## Responsive Behavior

| Breakpoint | Layout Change |
|------------|---------------|
| < 768px | 搜索框全宽，高级搜索表单项单列 |
| 768px - 1024px | 侧边栏64px，高级搜索2列 |
| > 1024px | 侧边栏240px，高级搜索4列 |

---

## Empty & Loading States

| State | UI Treatment |
|-------|--------------|
| 加载中 | 骨架屏显示5个结果卡片 |
| 无结果 | 插图 + "未找到「{关键词}」相关结果" + 建议 |
| 输入中 | 显示搜索建议下拉 |

---

## Anti-Patterns (禁止使用)

- ❌ 搜索结果无高亮
- ❌ 长搜索无loading状态
- ❌ 无搜索历史管理
- ❌ 搜索不分页导致长滚动
