# Statistics - 统计分析页面

> 覆盖规则：此页面的规则覆盖 MASTER.md 中的全局规则

## Page Info
- **Page Name:** 统计分析
- **Route:** `/statistics`
- **Module:** 统计分析模块
- **Access:** 已登录用户 (管理员可见完整数据)

---

## Layout Structure

```
┌────────────────────────────────────────────────────────────────────────────┐
│  Header: [Logo] 会议纪要管理系统          [搜索框]    [消息🔔] [头像▾]        │
├────────────────────────────────────────────────────────────────────────────┤
│ Sidebar │  Content                                                              │
│         │  ┌──────────────────────────────────────────────────────────────┐   │
│ [首页]  │  │  统计分析                          [时间范围: 本月▾] [导出▾]  │   │
│         │  │                                                              │   │
│ [工作台]│  ├──────────────────────────────────────────────────────────────┤   │
│         │  │ ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐              │   │
│ [纪要]  │  │ │ 📋     │ │ 📝     │ │ ✅     │ │ 👥     │              │   │
│ [模板]  │  │ │  总纪要  │ │  已发送  │ │  待办完成 │ │  参与人数 │              │   │
│ [消息]  │  │ │   {n}   │ │   {n}   │ │   {n}   │ │   {n}   │              │   │
│ [搜索]  │  │ └─────────┘ └─────────┘ └─────────┘ └─────────┘              │   │
│ [统计]  │  ├──────────────────────────────────────────────────────────────┤   │
│ ●Active │  │                                                              │   │
│         │  │  ┌────────────────────────┐  ┌────────────────────────┐      │   │
│ [个人]  │  │  │  纪要趋势              │  │  部门分布 (饼图)        │      │   │
│ [设置]  │  │  │  ┌──────────────────┐  │  │  ┌──────────────────┐  │      │   │
│         │  │  │  │    📈 折线图      │  │  │  │      🥧          │  │      │   │
│         │  │  │  │   1月 2月 3月 ...│  │  │  │  研发 40%        │  │      │   │
│         │  │  │  └──────────────────┘  │  │  │  市场 30%        │  │      │   │
│         │  │  └────────────────────────┘  └────────────────────────┘      │   │
│         │  │                                                              │   │
│         │  │  ┌────────────────────────┐  ┌────────────────────────┐      │   │
│         │  │  │  任务效率              │  │  用户活跃度 TOP 5       │      │   │
│         │  │  │  ┌──────────────────┐  │  │  ┌──────────────────┐  │      │   │
│         │  │  │  │  ████████░░ 80%   │  │  │  │ 1. 张三   ████  │  │      │   │
│         │  │  │  │  完成率: {n}%     │  │  │  │ 2. 李四   ███   │  │      │   │
│         │  │  │  │  平均: {n}小时    │  │  │  │ 3. 王五   ██    │  │      │   │
│         │  │  │  └──────────────────┘  │  │  └──────────────────┘  │      │   │
│         │  │  └────────────────────────┘  └────────────────────────┘      │   │
│         │  │                                                              │   │
│         │  └──────────────────────────────────────────────────────────────┘   │
└────────────────────────────────────────────────────────────────────────────┘
```

---

## Color Usage (Charts)

| Element | Color | Hex |
|---------|-------|-----|
| 折线图主色 | Primary | `#7C3AED` |
| 折线图填充 | Primary 20% | `#7C3AED33` |
| 饼图-研发部 | 紫色 | `#8B5CF6` |
| 饼图-市场部 | 蓝色 | `#3B82F6` |
| 饼图-销售部 | 绿色 | `#10B981` |
| 饼图-行政部 | 橙色 | `#F97316` |
| 柱状图 | Primary | `#7C3AED` |
| 进度条背景 | 浅灰 | `#E2E8F0` |
| 进度条填充 | 绿色 | `#10B981` |
| 正文文字 | 深紫 | `#4C1D95` |
| 次要文字 | 灰色 | `#6B7280` |

---

## Component Specs

### 统计卡片 (4列)
- **Layout:** CSS Grid, `grid-template-columns: repeat(4, 1fr)`
- **Gap:** 24px
- **Background:** `#FAF5FF`
- **Border Radius:** 12px
- **Padding:** 24px
- **Icon Size:** 32px
- **Number Size:** 28px, Fira Code, bold

### 图表容器
- **Background:** `#FFFFFF`
- **Border:** 1px solid `#E2E8F0`
- **Border Radius:** 12px
- **Padding:** 24px
- **Height:** 320px
- **Shadow:** `var(--shadow-sm)`

### 图表区域 (2x2 Grid)
- **Layout:** CSS Grid, `grid-template-columns: repeat(2, 1fr)`
- **Gap:** 24px

---

## Element Plus Components

| Element | Component | Props |
|---------|-----------|-------|
| 时间范围选择 | el-select | v-model="timeRange", options |
| 导出按钮 | el-dropdown | @command="handleExport" |
| 统计卡片 | el-statistic | :value="number" |
| 图表 | echarts | :option, : autoresize |
| 进度条 | el-progress | :percentage, :stroke-width |
| 排名列表 | el-table | :data="topUsers", border=false |

---

## Chart Library

**ECharts** (已在需求中指定)

| Chart Type | Use Case | Library |
|------------|----------|---------|
| 折线图 | 纪要趋势 (时间序列) | ECharts line |
| 饼图 | 部门分布 (占比) | ECharts pie |
| 柱状图 | 用户活跃度 (排名) | ECharts bar |
| 环形图 | 任务完成率 (进度) | ECharts gauge |

---

## Icon Set

使用 **Lucide Icons**

| 用途 | Icon Name |
|------|-----------|
| 总纪要 | `FileText` |
| 已发送 | `Send` |
| 待办完成 | `CheckSquare` |
| 参与人数 | `Users` |
| 导出 | `Download` |
| 趋势 | `TrendingUp` |
| 部门 | `Building` |
| 任务 | `ClipboardList` |
| 活跃度 | `Activity` |

---

## Chart Options

### 折线图 (趋势)
```javascript
{
  xAxis: { type: 'category', data: ['1月', '2月', '3月', ...] },
  yAxis: { type: 'value' },
  series: [{
    type: 'line',
    data: [120, 132, 101, ...],
    areaStyle: { color: '#7C3AED33' },
    lineStyle: { color: '#7C3AED' },
    itemStyle: { color: '#7C3AED' }
  }]
}
```

### 饼图 (部门分布)
```javascript
{
  series: [{
    type: 'pie',
    radius: ['40%', '70%'],
    data: [
      { value: 40, name: '研发部' },
      { value: 30, name: '市场部' },
      { value: 20, name: '销售部' },
      { value: 10, name: '行政部' }
    ]
  }]
}
```

---

## Interaction Rules

| Action | Behavior |
|--------|----------|
| 切换时间范围 | 刷新所有图表数据 |
| 点击导出 | 显示导出选项 (Excel/PDF) |
| 鼠标悬停图表 | 显示详细数据 tooltip |
| 点击图例 | 切换显示/隐藏数据系列 |

---

## Data Structure

```typescript
interface StatisticsData {
  overview: {
    totalMinutes: number;
    sentMinutes: number;
    completedTasks: number;
    participatedUsers: number;
  };
  trend: {
    month: string;
    count: number;
  }[];
  departmentDistribution: {
    department: string;
    count: number;
    percentage: number;
  }[];
  taskEfficiency: {
    completionRate: number;
    averageHours: number;
    overdueRate: number;
  };
  topUsers: {
    rank: number;
    userName: string;
    activityScore: number;
  }[];
}
```

---

## Accessibility

- [ ] 图表有 aria-label 描述
- [ ] 数据表格作为图表备选
- [ ] 颜色有图案区分 (colorblind)
- [ ] 支持键盘导航

---

## Responsive Behavior

| Breakpoint | Layout Change |
|------------|---------------|
| < 768px | 统计卡片单列，图表单列堆叠 |
| 768px - 1024px | 统计卡片2列，图表2列 |
| > 1024px | 统计卡片4列，图表2x2网格 |

---

## Loading State

| State | UI Treatment |
|-------|--------------|
| 加载中 | 骨架屏显示4个统计卡片 + 4个图表容器 |
| 加载失败 | el-alert 错误提示 + 重试按钮 |

---

## Anti-Patterns (禁止使用)

- ❌ 图表无 loading 状态
- ❌ 图表颜色对比度不足
- ❌ 饼图超过6个分类
- ❌ 数据更新无过渡动画
