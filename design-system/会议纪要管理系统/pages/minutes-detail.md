# MinutesDetail - 会议纪要详情页面

> 覆盖规则：此页面的规则覆盖 MASTER.md 中的全局规则

## Page Info
- **Page Name:** 会议纪要详情
- **Route:** `/minutes/:id`
- **Module:** 会议纪要模块
- **Access:** 已登录用户

---

## Layout Structure

```
┌────────────────────────────────────────────────────────────────────────────┐
│  Header: [← 返回列表]  {会议标题}                    [编辑] [导出▾] [更多▾]   │
├────────────────────────────────────────────────────────────────────────────┤
│ Sidebar │  Content                                                              │
│         │  ┌──────────────────────────────────────────────────────────────┐   │
│ [首页]  │  │ 会议信息卡片                                                    │   │
│         │  │ 📅 {日期} {时间} | 📍 {地点} | 👤 主持人: {姓名}              │   │
│ [工作台]│  │ 👥 与会者: {人数}人  | 发送时间: {时间}                       │   │
│         │  ├──────────────────────────────────────────────────────────────┤   │
│ [纪要]  │  │ 版本: V1.0 [查看版本历史]                                      │   │
│ ●Active │  ├──────────────────────────────────────────────────────────────┤   │
│         │  │                                                              │   │
│ [模板]  │  │ 富文本内容展示区                                              │   │
│ [消息]  │  │                                                              │   │
│ [搜索]  │  │ {渲染后的HTML内容}                                            │   │
│ [统计]  │  │                                                              │   │
│         │  │                                                              │   │
│         │  ├──────────────────────────────────────────────────────────────┤   │
│         │  │ 附件 (2)                                       [+ 下载全部]   │   │
│         │  │ 📎 {文件名1}.pdf (2.3MB)                           [预览][下载]│
│         │  │ 📎 {文件名2}.docx (1.1MB)                           [预览][下载]│
│         │  ├──────────────────────────────────────────────────────────────┤   │
│         │  │ 评论区                                                        │   │
│         │  │ ┌─────────────────────────────────────────────────────────┐   │ │
│         │  │ │ 👤 {用户名} · {时间}                                    │   │ │
│         │  │ │ 评论内容 @{被@用户}                                     │   │ │
│         │  │ │ [回复]                                                   │   │ │
│         │  │ └─────────────────────────────────────────────────────────┘   │ │
│         │  │ ┌─────────────────────────────────────────────────────────┐   │ │
│         │  │ │ 💬 发表评论...                                           │   │ │
│         │  │ └─────────────────────────────────────────────────────────┘   │ │
│         │  │                                     [发表评论]              │   │
│         │  └──────────────────────────────────────────────────────────────┘   │
└────────────────────────────────────────────────────────────────────────────┘
```

---

## Color Usage

| Element | Color | Hex |
|---------|-------|-----|
| 详情卡片背景 | 白色 | `#FFFFFF` |
| 信息标签背景 | 浅紫 | `#FAF5FF` |
| 版本徽章 | Primary | `#7C3AED` |
| 附件图标 | 灰色 | `#6B7280` |
| 评论边框 | 浅灰 | `#E2E8F0` |
| @提及文字 | Primary | `#7C3AED` |
| 正文文字 | 深紫 | `#4C1D95` |
| 次要文字 | 灰色 | `#6B7280` |

---

## Component Specs

### 会议信息卡片
- **Background:** `#FAF5FF`
- **Border Radius:** 12px
- **Padding:** 20px
- **Margin Bottom:** 24px
- **Layout:** flex, gap 24px, flex-wrap

### 版本徽章
- **Component:** Element Plus `el-tag`
- **Type:** custom (背景 `#7C3AED`, 文字白色)
- **Size:** small
- **Border Radius:** 4px

### 附件项
- **Layout:** flex, justify-content: space-between
- **Padding:** 12px 16px
- **Background:** `#FFFFFF`
- **Border:** 1px solid `#E2E8F0`
- **Border Radius:** 8px
- **Margin Bottom:** 8px
- **Hover:** 背景 `#F9FAFB`

### 评论项
- **Padding:** 16px
- **Border Bottom:** 1px solid `#E2E8F0`
- **Avatar:** 40px 圆形
- **用户名:** Fira Sans 14px 600
- **时间:** Fira Sans 12px 400 `#6B7280`

---

## Element Plus Components

| Element | Component | Props |
|---------|-----------|-------|
| 返回按钮 | el-button | text, icon="ArrowLeft" |
| 编辑按钮 | el-button | type="primary", @click="editMinute" |
| 导出下拉 | el-dropdown | @command="handleExport" |
| 更多下拉 | el-dropdown | @command="handleMore" |
| 版本标签 | el-tag | type="primary", size="small" |
| 附件列表 | el-table | :data="attachments", border=false |
| 附件预览 | el-image | :src="url", :preview-src-list |
| 评论输入 | el-input | type="textarea", :rows="3", v-model="commentText" |
|发表评论 | el-button | type="primary", @click="submitComment" |

---

## Icon Set

使用 **Lucide Icons**

| 用途 | Icon Name |
|------|-----------|
| 返回 | `ArrowLeft` |
| 编辑 | `Edit` |
| 导出 | `Download` |
| 更多 | `MoreVertical` |
| 日期 | `Calendar` |
| 时间 | `Clock` |
| 地点 | `MapPin` |
| 主持人 | `User` |
| 与会者 | `Users` |
| 附件 | `Paperclip` |
| 评论 | `MessageCircle` |
| 回复 | `Reply` |
| 下载 | `Download` |
| 预览 | `Eye` |

---

## Interaction Rules

| Action | Behavior |
|--------|----------|
| 点击编辑 | 跳转至编辑页面 `/minutes/:id/edit` |
| 点击导出 | 显示导出选项菜单 (Word/PDF/批量) |
| 点击预览附件 | 打开附件预览弹窗 |
| 点击下载附件 | 触发文件下载 |
| 提交评论 | 调用评论API，刷新评论区 |
| 点击回复 | 显示回复输入框 |
| @用户 | 触发用户选择器 |

---

## Export Menu

```
┌─────────────────┐
│ 📄 导出为 Word  │
│ 📕 导出为 PDF   │
│ ─────────────── │
│ 📦 批量导出     │
│ 📊 导出对比报告 │
└─────────────────┘
```

---

## More Menu

```
┌─────────────────────┐
│ 📋 版本历史         │
│ 🔄 分享链接         │
│ ───────────────     │
│ ⭐ 收藏             │
│ 🗑️ 删除             │ (仅发起人/管理员)
└─────────────────────┘
```

---

## Data Structure

```typescript
interface MinuteDetail {
  id: string;
  title: string;
  currentVersion: string;
  status: string;
  meetingDate: string;
  meetingTime: string;
  location: string;
  hostId: string;
  hostName: string;
  attendees: Attendee[];
  ccList: string[];
  content: string;  // HTML
  attachments: Attachment[];
  creatorId: string;
  creatorName: string;
  createTime: string;
  sendTime: string;
}

interface Comment {
  id: string;
  userId: string;
  userName: string;
  userAvatar: string;
  content: string;
  parentId: string | null;
  mentions: string[];
  createTime: string;
  replies?: Comment[];
}
```

---

## Accessibility

- [ ] 返回按钮有清晰的焦点状态
- [ ] 导出菜单支持键盘操作
- [ ] 评论区的 @ 提及有提示
- [ ] 附件下载有 aria-label

---

## Responsive Behavior

| Breakpoint | Layout Change |
|------------|---------------|
| < 768px | 隐藏侧边栏，信息卡片单列，评论区全宽 |
| 768px - 1024px | 侧边栏64px，附件列表2列 |
| > 1024px | 侧边栏240px，附件列表单列 |

---

## Anti-Patterns (禁止使用)

- ❌ 附件上传限制不提示
- ❌ 评论提交无loading状态
- ❌ 删除操作无确认对话框
- ❌ 长内容页面无滚动锚点
