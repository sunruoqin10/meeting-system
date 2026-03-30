// Mock 数据
import Mock from 'mockjs'

// 生成随机ID
const generateId = () => Math.random().toString(36).substr(2, 9)

// 当前用户
export const currentUser = {
  id: 'u001',
  name: '张三',
  email: 'zhangsan@company.com',
  phone: '13800138000',
  departmentId: 'd001',
  departmentName: '技术研发部',
  role: 'admin',
  avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=zhangsan'
}

// 统计概览
export const dashboardStats = {
  todoCount: 5,
  initiatedCount: 12,
  participatedCount: 28
}

// 待办事项列表
export const todoList = [
  {
    id: 't001',
    minuteId: 'm001',
    minuteTitle: 'Q1产品规划会议',
    assignerName: '李经理',
    instruction: '请补充第三部分的市场分析内容',
    createTime: '2024-01-15 10:30'
  },
  {
    id: 't002',
    minuteId: 'm002',
    minuteTitle: '技术方案评审',
    assignerName: '王总',
    instruction: '审核架构设计文档',
    createTime: '2024-01-15 09:00'
  },
  {
    id: 't003',
    minuteId: 'm003',
    minuteTitle: '项目启动会',
    assignerName: '刘总',
    instruction: '确认项目时间节点',
    createTime: '2024-01-14 16:00'
  }
]

// 会议纪要列表
export const minutesList = [
  {
    id: 'm001',
    title: 'Q1产品规划会议',
    currentVersion: 'V1.2',
    status: 'sent',
    meetingDate: '2024-01-15',
    creatorName: '张三',
    commentCount: 3,
    attachmentCount: 2,
    summary: '讨论了Q1产品规划，确定了核心功能优先级和技术方案...'
  },
  {
    id: 'm002',
    title: '技术方案评审',
    currentVersion: 'V1.0',
    status: 'draft',
    meetingDate: '2024-01-14',
    creatorName: '李四',
    commentCount: 0,
    attachmentCount: 1,
    summary: '对新系统的技术架构进行了详细评审...'
  },
  {
    id: 'm003',
    title: '项目启动会',
    currentVersion: 'V1.3',
    status: 'revising',
    meetingDate: '2024-01-13',
    creatorName: '王五',
    commentCount: 5,
    attachmentCount: 3,
    summary: '启动新项目，明确了项目目标、团队分工和时间节点...'
  },
  {
    id: 'm004',
    title: '周例会',
    currentVersion: 'V1.0',
    status: 'archived',
    meetingDate: '2024-01-10',
    creatorName: '张三',
    commentCount: 2,
    attachmentCount: 0,
    summary: '本周工作总结和下周计划...'
  },
  {
    id: 'm005',
    title: '客户需求对接会',
    currentVersion: 'V1.1',
    status: 'sent',
    meetingDate: '2024-01-12',
    creatorName: '赵六',
    commentCount: 1,
    attachmentCount: 4,
    summary: '与客户对接需求，确认了功能范围和交付时间...'
  }
]

// 会议纪要详情
export const minuteDetail = {
  id: 'm001',
  title: 'Q1产品规划会议',
  currentVersion: 'V1.2',
  status: 'sent',
  meetingDate: '2024-01-15',
  meetingTime: '14:00-16:00',
  location: '会议室A',
  hostId: 'u001',
  hostName: '张三',
  attendees: [
    { id: 'u001', name: '张三', email: 'zhangsan@company.com' },
    { id: 'u002', name: '李四', email: 'lisi@company.com' },
    { id: 'u003', name: '王五', email: 'wangwu@company.com' }
  ],
  ccList: [
    { id: 'u004', name: '刘总', email: 'liuzong@company.com' }
  ],
  content: `
    <h2>一、会议背景</h2>
    <p>本次会议主要讨论Q1季度的产品规划和技术方案。</p>

    <h2>二、讨论事项</h2>
    <ul>
      <li>核心功能优先级排序</li>
      <li>技术方案选型</li>
      <li>团队分工调整</li>
    </ul>

    <h2>三、决策事项</h2>
    <ol>
      <li>确定使用Vue3+Element Plus作为前端技术栈</li>
      <li>后端采用Spring Boot + MyBatis</li>
      <li>数据库使用MySQL 8.0</li>
    </ol>

    <h2>四、待办事项</h2>
    <table style="width:100%; border-collapse: collapse;">
      <tr style="background: #f0f0f0;">
        <th style="border:1px solid #ddd; padding:8px;">事项</th>
        <th style="border:1px solid #ddd; padding:8px;">负责人</th>
        <th style="border:1px solid #ddd; padding:8px;">截止时间</th>
      </tr>
      <tr>
        <td style="border:1px solid #ddd; padding:8px;">完成技术方案文档</td>
        <td style="border:1px solid #ddd; padding:8px;">李四</td>
        <td style="border:1px solid #ddd; padding:8px;">2024-01-20</td>
      </tr>
      <tr>
        <td style="border:1px solid #ddd; padding:8px;">前端项目初始化</td>
        <td style="border:1px solid #ddd; padding:8px;">张三</td>
        <td style="border:1px solid #ddd; padding:8px;">2024-01-22</td>
      </tr>
    </table>
  `,
  attachments: [
    { id: 'a001', fileName: 'Q1产品规划.pptx', fileSize: 2048000, fileType: 'application/vnd.openxmlformats-officedocument.presentationml.presentation' },
    { id: 'a002', fileName: '技术方案.pdf', fileSize: 1024000, fileType: 'application/pdf' }
  ],
  creatorId: 'u001',
  creatorName: '张三',
  createTime: '2024-01-15 16:30',
  sendTime: '2024-01-15 17:00'
}

// 版本历史
export const versionHistory = [
  {
    id: 'v003',
    minuteId: 'm001',
    versionNumber: 'V1.2',
    content: '完整内容...',
    modifierId: 'u001',
    modifierName: '张三',
    modifyTime: '2024-01-15 14:30',
    remark: '合并修订内容',
    isCurrent: true
  },
  {
    id: 'v002',
    minuteId: 'm001',
    versionNumber: 'V1.1',
    content: '上一版本内容...',
    modifierId: 'u002',
    modifierName: '李四',
    modifyTime: '2024-01-14 10:00',
    remark: '添加讨论事项',
    isCurrent: false
  },
  {
    id: 'v001',
    minuteId: 'm001',
    versionNumber: 'V1.0',
    content: '初始版本...',
    modifierId: 'u001',
    modifierName: '张三',
    modifyTime: '2024-01-13 16:00',
    remark: '初始版本',
    isCurrent: false
  }
]

// 模板列表
export const templateList = [
  {
    id: 't001',
    name: '决议型模板',
    type: 'preset',
    icon: 'CheckSquare',
    description: '适用于需要做出明确决策的会议',
    usageCount: 45,
    creatorName: '系统'
  },
  {
    id: 't002',
    name: '周会型模板',
    type: 'preset',
    icon: 'Calendar',
    description: '适用于每周例会',
    usageCount: 120,
    creatorName: '系统'
  },
  {
    id: 't003',
    name: '讨论型模板',
    type: 'preset',
    icon: 'MessageSquare',
    description: '适用于头脑风暴和讨论',
    usageCount: 38,
    creatorName: '系统'
  },
  {
    id: 't004',
    name: '项目启动会模板',
    type: 'shared',
    icon: 'Rocket',
    description: '适用于新项目启动会议',
    usageCount: 15,
    creatorName: '张三'
  },
  {
    id: 't005',
    name: '我的自定义模板',
    type: 'custom',
    icon: 'FileText',
    description: '个人常用的会议模板',
    usageCount: 8,
    creatorName: '李四'
  }
]

// 消息列表
export const messageList = [
  {
    id: 'msg001',
    type: 'todo',
    title: '待办任务提醒',
    content: '会议纪要「Q1产品规划会议」需要您处理修订任务',
    relatedType: 'minute',
    relatedId: 'm001',
    senderName: '系统',
    isRead: false,
    priority: 'high',
    createTime: '2024-01-15 14:30'
  },
  {
    id: 'msg002',
    type: 'review',
    title: '审核通知',
    content: '您的修订意见已被张三审核通过',
    relatedType: 'minute',
    relatedId: 'm002',
    senderName: '张三',
    isRead: false,
    priority: 'medium',
    createTime: '2024-01-15 10:15'
  },
  {
    id: 'msg003',
    type: 'system',
    title: '系统公告',
    content: '系统将于本周六进行升级维护，预计 downtime 2小时',
    relatedType: 'system',
    relatedId: null,
    senderName: '系统',
    isRead: true,
    priority: 'low',
    createTime: '2024-01-14 09:00'
  },
  {
    id: 'msg004',
    type: 'cc',
    title: '抄送通知',
    content: '您被抄送到会议纪要「技术方案评审」',
    relatedType: 'minute',
    relatedId: 'm003',
    senderName: '王五',
    isRead: true,
    priority: 'medium',
    createTime: '2024-01-13 16:00'
  }
]

// 搜索结果
export const searchResults = [
  {
    id: 'm001',
    title: 'Q1产品规划会议',
    content: '...讨论了Q1产品的<mark>技术</mark>方案，确定了核心功能...',
    meetingDate: '2024-01-15',
    creatorName: '张三',
    status: 'sent',
    commentCount: 3,
    attachmentCount: 2,
    score: 0.95
  },
  {
    id: 'm002',
    title: '<mark>技术</span>方案评审',
    content: '对新系统的<mark>技术</mark>架构进行了详细评审...',
    meetingDate: '2024-01-14',
    creatorName: '李四',
    status: 'draft',
    commentCount: 0,
    attachmentCount: 1,
    score: 0.88
  }
]

// 搜索历史
export const searchHistory = [
  { keyword: '技术方案', searchTime: '2024-01-15 14:00' },
  { keyword: '产品规划', searchTime: '2024-01-14 10:00' },
  { keyword: '周会', searchTime: '2024-01-10 09:00' }
]

// 统计数据
export const statisticsData = {
  overview: {
    totalMinutes: 156,
    sentMinutes: 128,
    completedTasks: 89,
    participatedUsers: 45
  },
  trend: [
    { month: '1月', count: 45 },
    { month: '2月', count: 52 },
    { month: '3月', count: 38 },
    { month: '4月', count: 61 },
    { month: '5月', count: 55 },
    { month: '6月', count: 48 }
  ],
  departmentDistribution: [
    { department: '技术研发部', count: 62, percentage: 40 },
    { department: '市场部', count: 46, percentage: 30 },
    { department: '销售部', count: 31, percentage: 20 },
    { department: '行政部', count: 15, percentage: 10 }
  ],
  taskEfficiency: {
    completionRate: 85,
    averageHours: 4.5,
    overdueRate: 8
  },
  topUsers: [
    { rank: 1, userName: '张三', activityScore: 98 },
    { rank: 2, userName: '李四', activityScore: 85 },
    { rank: 3, userName: '王五', activityScore: 76 },
    { rank: 4, userName: '赵六', activityScore: 65 },
    { rank: 5, userName: '刘总', activityScore: 52 }
  ]
}

// 部门树
export const departmentTree = [
  {
    id: 'd001',
    name: '技术研发部',
    parentId: null,
    description: '负责技术研发',
    sort: 1,
    level: 1,
    userCount: 26,
    children: [
      {
        id: 'd002',
        name: '前端组',
        parentId: 'd001',
        description: '前端开发',
        sort: 1,
        level: 2,
        userCount: 12,
        children: []
      },
      {
        id: 'd003',
        name: '后端组',
        parentId: 'd001',
        description: '后端开发',
        sort: 2,
        level: 2,
        userCount: 14,
        children: []
      }
    ]
  },
  {
    id: 'd004',
    name: '市场部',
    parentId: null,
    description: '市场推广',
    sort: 2,
    level: 1,
    userCount: 15,
    children: []
  },
  {
    id: 'd005',
    name: '销售部',
    parentId: null,
    description: '销售团队',
    sort: 3,
    level: 1,
    userCount: 20,
    children: []
  }
]

// 用户列表
export const userList = [
  {
    id: 'u001',
    name: '张三',
    email: 'zhangsan@company.com',
    phone: '13800138001',
    departmentId: 'd001',
    departmentName: '技术研发部',
    role: 'admin',
    status: 'active',
    avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=zhangsan',
    lastLoginTime: '2024-01-15 14:30'
  },
  {
    id: 'u002',
    name: '李四',
    email: 'lisi@company.com',
    phone: '13800138002',
    departmentId: 'd001',
    departmentName: '技术研发部',
    role: 'user',
    status: 'active',
    avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=lisi',
    lastLoginTime: '2024-01-15 13:00'
  },
  {
    id: 'u003',
    name: '王五',
    email: 'wangwu@company.com',
    phone: '13800138003',
    departmentId: 'd002',
    departmentName: '前端组',
    role: 'user',
    status: 'locked',
    avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=wangwu',
    lastLoginTime: '2024-01-14 10:00'
  },
  {
    id: 'u004',
    name: '赵六',
    email: 'zhaoliu@company.com',
    phone: '13800138004',
    departmentId: 'd003',
    departmentName: '后端组',
    role: 'dept_admin',
    status: 'active',
    avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=zhaoliu',
    lastLoginTime: '2024-01-15 09:00'
  }
]

// 操作日志
export const operationLogs = [
  {
    id: 'log001',
    userId: 'u001',
    userName: '张三',
    operationType: 'login',
    operationDetail: '登录成功',
    ipAddress: '192.168.1.100',
    userAgent: 'Chrome/120.0',
    relatedType: null,
    relatedId: null,
    createTime: '2024-01-15 14:30:22'
  },
  {
    id: 'log002',
    userId: 'u002',
    userName: '李四',
    operationType: 'create',
    operationDetail: '创建纪要 #M003',
    ipAddress: '192.168.1.101',
    userAgent: 'Chrome/120.0',
    relatedType: 'minute',
    relatedId: 'm003',
    createTime: '2024-01-15 14:28:15'
  },
  {
    id: 'log003',
    userId: 'u003',
    userName: '王五',
    operationType: 'edit',
    operationDetail: '编辑纪要 #M002',
    ipAddress: '192.168.1.102',
    userAgent: 'Firefox/121.0',
    relatedType: 'minute',
    relatedId: 'm002',
    createTime: '2024-01-15 14:25:00'
  },
  {
    id: 'log004',
    userId: 'u001',
    userName: '张三',
    operationType: 'export',
    operationDetail: '导出PDF #M001',
    ipAddress: '192.168.1.100',
    userAgent: 'Chrome/120.0',
    relatedType: 'minute',
    relatedId: 'm001',
    createTime: '2024-01-15 14:20:30'
  },
  {
    id: 'log005',
    userId: 'u004',
    userName: '赵六',
    operationType: 'assign',
    operationDetail: '指派任务 #T78',
    ipAddress: '192.168.1.103',
    userAgent: 'Safari/17.0',
    relatedType: 'task',
    relatedId: 't078',
    createTime: '2024-01-15 14:15:00'
  }
]

// 评论列表
export const commentList = [
  {
    id: 'c001',
    userId: 'u002',
    userName: '李四',
    userAvatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=lisi',
    content: '技术方案需要补充数据库设计部分 @张三',
    parentId: null,
    mentions: ['u001'],
    createTime: '2024-01-15 15:30',
    replies: [
      {
        id: 'c002',
        userId: 'u001',
        userName: '张三',
        userAvatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=zhangsan',
        content: '好的，我明天补充这部分内容',
        parentId: 'c001',
        mentions: [],
        createTime: '2024-01-15 15:45'
      }
    ]
  },
  {
    id: 'c003',
    userId: 'u003',
    userName: '王五',
    userAvatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=wangwu',
    content: '同意这个方案',
    parentId: null,
    mentions: [],
    createTime: '2024-01-15 14:00'
  }
]

// 部门列表 (表格用)
export const departmentList = [
  { id: 'd001', name: '技术研发部', parentName: '-', userCount: 26, description: '负责技术研发', sort: 1 },
  { id: 'd002', name: '前端组', parentName: '技术研发部', userCount: 12, description: '前端开发', sort: 1 },
  { id: 'd003', name: '后端组', parentName: '技术研发部', userCount: 14, description: '后端开发', sort: 2 },
  { id: 'd004', name: '市场部', parentName: '-', userCount: 15, description: '市场推广', sort: 2 },
  { id: 'd005', name: '销售部', parentName: '-', userCount: 20, description: '销售团队', sort: 3 }
]

// 导出所有mock数据
export default {
  currentUser,
  dashboardStats,
  todoList,
  minutesList,
  minuteDetail,
  versionHistory,
  templateList,
  messageList,
  searchResults,
  searchHistory,
  statisticsData,
  departmentTree,
  userList,
  operationLogs,
  commentList,
  departmentList
}
