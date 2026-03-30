# MinutesEditor - 会议纪要编辑器页面

> 覆盖规则：此页面的规则覆盖 MASTER.md 中的全局规则

## Page Info
- **Page Name:** 会议纪要编辑器
- **Route:** `/minutes/create` 或 `/minutes/:id/edit`
- **Module:** 会议纪要模块
- **Access:** 已登录用户

---

## Layout Structure

```
┌────────────────────────────────────────────────────────────────────────────┐
│  Header: [← 返回]  {页面标题}                    [自动保存 ✓ 2分钟前]        │
├────────────────────────────────────────────────────────────────────────────┤
│ Sidebar │  Content                                                              │
│         │  ┌──────────────────────────────────────────────────────────────┐   │
│ [首页]  │  │ 会议信息                                                        │   │
│         │  │ ┌────────────┐ ┌────────────┐ ┌────────────┐ ┌────────────┐     │   │
│ [工作台]│  │ │ 📋 标题    │ │ 📅 日期    │ │ ⏰ 时间    │ │ 📍 地点    │     │   │
│         │  │ └────────────┘ └────────────┘ └────────────┘ └────────────┘     │   │
│ [纪要]  │  │                                                              │   │
│ ●Active │  │ 👤 主持人: {姓名}     👥 与会者: {人数} [选择]                 │   │
│         │  ├──────────────────────────────────────────────────────────────┤   │
│ [模板]  │  │ 富文本编辑器 (TinyMCE)                                        │   │
│ [消息]  │  │ ┌──────────────────────────────────────────────────────────┐ │   │
│ [搜索]  │  │ │                                                          │ │   │
│ [统计]  │  │ │  会议背景                                                  │ │   │
│         │  │ │  _______________                                         │ │   │
│         │  │ │                                                          │ │   │
│         │  │ │  讨论事项                                                  │ │   │
│         │  │ │  _______________                                         │ │   │
│         │  │ │                                                          │ │   │
│         │  │ │  决策事项                                                  │ │   │
│         │  │ │  _______________                                         │ │   │
│         │  │ │                                                          │ │   │
│         │  │ └──────────────────────────────────────────────────────────┘ │   │
│         │  ├──────────────────────────────────────────────────────────────┤   │
│         │  │ 附件: [📎 上传附件] 或 [{文件名} x] [预览] [删除]              │   │
│         │  ├──────────────────────────────────────────────────────────────┤   │
│         │  │         [保存草稿]                    [选择接收人并发送 →]     │   │
│         │  └──────────────────────────────────────────────────────────────┘   │
└────────────────────────────────────────────────────────────────────────────┘
```

---

## Color Usage

| Element | Color | Hex |
|---------|-------|-----|
| 编辑器背景 | 白色 | `#FFFFFF` |
| 编辑器边框 | 浅灰 | `#E2E8F0` |
| 工具栏背景 | 浅灰 | `#F9FAFB` |
| 保存提示-成功 | 绿色 | `#10B981` |
| 保存提示-保存中 | 蓝色 | `#3B82F6` |
| 保存提示-失败 | 红色 | `#EF4444` |
| 锁定提示 | 橙色背景 | `#FFF7ED` |
| 正文文字 | 深紫 | `#4C1D95` |

---

## Component Specs

### 会议信息表单
- **Layout:** CSS Grid, `grid-template-columns: repeat(4, 1fr)`
- **Gap:** 16px
- **Margin Bottom:** 24px

### 富文本编辑器 (TinyMCE)
- **Height:** 500px (最小)
- **Border:** 1px solid `#E2E8F0`
- **Border Radius:** 8px
- **Toolbar Background:** `#F9FAFB`
- **Content Padding:** 16px

### 自动保存提示
- **Position:** Header 右侧
- **Font Size:** 12px
- **States:**
  - 保存中: 蓝色圆点 + "保存中..."
  - 已保存: 绿色勾 + "已保存 2分钟前"
  - 保存失败: 红色叹号 + "保存失败 [重试]"

### 编辑锁定提示
- **Component:** Element Plus `el-alert`
- **Type:** warning
- **Content:** "🔒 {用户名} 正在编辑此文档"
- **Closable:** false

---

## Element Plus Components

| Element | Component | Props |
|---------|-----------|-------|
| 返回按钮 | el-button | text, icon="ArrowLeft" |
| 页面标题 | el-text | size="large", weight="bold" |
| 标题输入 | el-input | v-model="form.title", placeholder="请输入会议标题" |
| 日期选择 | el-date-picker | type="date", format="YYYY-MM-DD" |
| 时间选择 | el-time-picker | format="HH:mm" |
| 地点输入 | el-input | v-model="form.location" |
| 主持人选择 | el-select | filterable, remote, :remote-method |
| 与会者选择 | el-select | multiple, filterable, remote |
| 上传附件 | el-upload | drag, :limit="10", accept="*" |
| 保存草稿 | el-button | @click="saveDraft" |
| 发送按钮 | el-button | type="primary", @click="showSendDialog" |

---

## Icon Set

使用 **Lucide Icons**

| 用途 | Icon Name |
|------|-----------|
| 返回 | `ArrowLeft` |
| 保存 | `Save` |
| 发送 | `Send` |
| 上传 | `Upload` |
| 附件 | `Paperclip` |
| 锁定 | `Lock` |

---

## Autosave Behavior

| Rule | Implementation |
|------|----------------|
| 触发间隔 | 30秒 |
| 触发条件 | 内容有变化 |
| 保存状态 | 显示在 Header 右侧 |
| 失败处理 | 显示错误 + 重试按钮 |
| 锁定超时 | 30分钟无操作自动释放 |

---

## Interaction Rules

| Action | Behavior |
|--------|----------|
| 离开页面 | 提示未保存内容将丢失 |
| 点击保存草稿 | 调用保存API，显示保存状态 |
| 点击发送 | 打开选择接收人对话框 |
| 编辑锁定 | 显示锁定提示，禁止编辑 |
| 内容变化 | 重置30秒自动保存计时器 |

---

## Data Structure

```typescript
interface MinuteForm {
  title: string;
  meetingDate: string;
  meetingTime: string;
  location: string;
  hostId: string;
  attendees: string[];  // 用户ID数组
  ccList: string[];     // 抄送人ID数组
  content: string;      // 富文本HTML
  attachments: Attachment[];
}

interface Attachment {
  id: string;
  fileName: string;
  fileSize: number;
  fileType: string;
  url: string;
}
```

---

## Accessibility

- [ ] 所有表单有 label
- [ ] 富文本编辑器支持键盘快捷键
- [ ] 保存状态有 aria-live 提示
- [ ] 对话框 focus trap

---

## Send Dialog

```
┌─────────────────────────────────────────────────────────────┐
│  选择接收人                                            [×]  │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  当前与会者 (3人)                                           │
│  ┌─────────────────────────────────────────────────────────┐│
│  │ ☐ 👤 张三 (zhangsan@company.com)                       ││
│  │ ☑ 👤 李四 (lisi@company.com)          [已选择]        ││
│  │ ☑ 👤 王五 (wangwu@company.com)          [已选择]        ││
│  └─────────────────────────────────────────────────────────┘│
│                                                             │
│  [+ 添加更多接收人]                                         │
│                                                             │
│  邮件预览: [📧 启用邮件通知]                                 │
│                                                             │
│  ┌─────────────────────────────────────────────────────────┐│
│  │ 邮件主题: 会议纪要 - {标题}                              ││
│  │ 邮件内容预览...                                         ││
│  └─────────────────────────────────────────────────────────┘│
│                                                             │
│         [取消]                           [确认发送]         │
└─────────────────────────────────────────────────────────────┘
```

---

## Responsive Behavior

| Breakpoint | Layout Change |
|------------|---------------|
| < 768px | 会议信息表单单列，编辑器高度400px |
| 768px - 1024px | 会议信息2列，编辑器高度450px |
| > 1024px | 会议信息4列，编辑器高度500px |

---

## Anti-Patterns (禁止使用)

- ❌ 自动保存干扰用户输入
- ❌ 保存失败无提示
- ❌ 锁定状态无明确视觉反馈
- ❌ 发送前不确认接收人
