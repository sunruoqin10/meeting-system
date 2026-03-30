# Login - 登录页面

> 覆盖规则：此页面的规则覆盖 MASTER.md 中的全局规则

## Page Info
- **Page Name:** 登录页面
- **Route:** `/login`
- **Module:** 用户认证

---

## Layout Structure

```
┌─────────────────────────────────────────────────────────────┐
│                    [顶部留空 - 背景色填充]                     │
│                                                             │
│    ┌─────────────────────────────────────────────────┐      │
│    │                                                  │      │
│    │              🏢 会议纪要管理系统                   │      │
│    │                                                  │      │
│    │         ┌────────────────────────┐              │      │
│    │         │  邮箱地址              │              │      │
│    │         └────────────────────────┘              │      │
│    │         ┌────────────────────────┐              │      │
│    │         │  密码                  │              │      │
│    │         └────────────────────────┘              │      │
│    │                                                  │      │
│    │         ┌────────────────────────┐              │      │
│    │         │      登 录            │              │      │
│    │         └────────────────────────┘              │      │
│    │                                                  │      │
│    │         [注册账号]   [忘记密码?]                 │      │
│    │                                                  │      │
│    └─────────────────────────────────────────────────┘      │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## Color Usage

| Element | Color | Hex |
|---------|-------|-----|
| 页面背景 | Primary渐变 | `#7C3AED` → `#A78BFA` |
| 卡片背景 | 白色 | `#FFFFFF` |
| 主按钮背景 | CTA | `#F97316` |
| 链接文字 | Primary | `#7C3AED` |
| 正文文字 | 深紫 | `#4C1D95` |
| 次要文字 | 灰色 | `#6B7280` |

---

## Typography

| Element | Font | Size | Weight |
|---------|------|------|--------|
| 系统标题 | Fira Code | 28px | 700 |
| 表单标签 | Fira Sans | 14px | 500 |
| 输入文字 | Fira Sans | 16px | 400 |
| 按钮文字 | Fira Sans | 16px | 600 |
| 链接文字 | Fira Sans | 14px | 400 |

---

## Component Specs

### 登录卡片
- **Width:** 420px
- **Padding:** 48px
- **Border Radius:** 16px
- **Background:** `#FFFFFF`
- **Shadow:** `var(--shadow-xl)`
- **Margin:** 居中显示

### 输入框
- **Height:** 48px
- **Border:** 1px solid `#E2E8F0`
- **Border Radius:** 8px
- **Focus:** 边框变 `#7C3AED`，添加 3px 光晕
- **Margin Bottom:** 20px

### 主按钮
- **Height:** 48px
- **Background:** `#F97316`
- **Border Radius:** 8px
- **Hover:** `opacity: 0.9`，`translateY(-1px)`
- **Transition:** 200ms

### 链接
- **Color:** `#7C3AED`
- **Hover:** 下划线
- **Gap:** 24px

---

## Element Plus Components

| Element | Component | Props |
|---------|-----------|-------|
| 邮箱输入 | el-input | prefix-icon="Message" |
| 密码输入 | el-input | prefix-icon="Lock", show-password |
| 登录按钮 | el-button | type="primary", native-type="submit" |
| 链接文字 | el-link | underline="never" |

---

## Icon Set

使用 **Lucide Icons** 或 **Heroicons**

| 用途 | Icon Name |
|------|-----------|
| 邮箱 | `Mail` 或 `Message` |
| 密码 | `Lock` |
| 用户 | `User` |
| 显示密码 | `Eye` |

---

## Interaction Rules

| Rule | Behavior |
|------|----------|
| 登录按钮点击 | 禁用按钮，显示 loading spinner，调用登录API |
| 回车提交 | 表单支持回车键提交 |
| 表单验证 | 邮箱格式校验，密码非空校验 |
| 错误提示 | 输入框下方红色文字提示 |
| 登录成功 | 跳转至工作台 `/dashboard` |

---

## Accessibility

- [ ] 所有输入框有对应的 label
- [ ] 按钮有清晰的 focus 状态
- [ ] 错误状态有 aria-describedby 关联
- [ ] 支持键盘导航（Tab顺序正确）
- [ ] 颜色对比度 ≥ 4.5:1

---

## Anti-Patterns (禁止使用)

- ❌ Emoji作为图标
- ❌ 登录按钮在加载时无反馈
- ❌ 错误信息不明确
- ❌ 表单无验证直接提交

---

## Responsive Behavior

| Breakpoint | Layout Change |
|------------|---------------|
| < 480px | 卡片宽度 100% - 32px，padding 缩小至 24px |
| 480px - 768px | 卡片宽度 420px，保持默认padding |
| > 768px | 完全居中显示 |
