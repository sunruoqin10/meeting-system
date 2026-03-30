# Tasks

## 阶段一：项目基础设施搭建

- [ ] Task 1.1: 创建Spring Boot后端项目骨架，配置pom.xml依赖（Spring Boot、MyBatis、Spring Security、JWT、Spring Mail等）
  - [ ] SubTask 1.1.1: 创建Maven项目结构，配置parent pom
  - [ ] SubTask 1.1.2: 添加核心依赖（spring-boot-starter-web、mybatis-spring-boot-starter、spring-boot-starter-security等）
  - [ ] SubTask 1.1.3: 配置application.yml数据库连接、日志等基础配置

- [ ] Task 1.2: 创建Vue3+Element Plus前端项目骨架
  - [ ] SubTask 1.2.1: 使用Vite创建Vue3项目
  - [ ] SubTask 1.2.2: 安装Element Plus、ECharts等依赖
  - [ ] SubTask 1.2.3: 配置项目目录结构和基础路由

- [ ] Task 1.3: 设计并创建MySQL数据库表结构（不含外键约束）
  - [ ] SubTask 1.3.1: 创建Users表（用户表）
  - [ ] SubTask 1.3.2: 创建Departments表（部门表）
  - [ ] SubTask 1.3.3: 创建MeetingMinutes表（会议纪要主表）
  - [ ] SubTask 1.3.4: 创建MinuteVersions表（纪要版本表）
  - [ ] SubTask 1.3.5: 创建TaskAssignments表（任务指派表）
  - [ ] SubTask 1.3.6: 创建Templates表（模板表）
  - [ ] SubTask 1.3.7: 创建MeetingAttendees表（与会者关联表）
  - [ ] SubTask 1.3.8: 创建Messages表（消息表）
  - [ ] SubTask 1.3.9: 创建OperationLogs表（操作日志表）
  - [ ] SubTask 1.3.10: 创建MinuteAttachments表（附件表）
  - [ ] SubTask 1.3.11: 创建MinuteComments表（评论表）

## 阶段二：用户管理和认证

- [ ] Task 2.1: 实现用户注册功能
  - [ ] SubTask 2.1.1: 后端创建User实体类和Mapper
  - [ ] SubTask 2.1.2: 后端实现注册接口，校验邮箱唯一性，BCrypt加密密码
  - [ ] SubTask 2.1.3: 前端创建注册页面表单

- [ ] Task 2.2: 实现用户登录功能
  - [ ] SubTask 2.2.1: 后端实现JWT Token生成和验证
  - [ ] SubTask 2.2.2: 后端实现登录接口，验证密码，更新最后登录时间
  - [ ] SubTask 2.2.3: 前端实现登录页面，保存Token到localStorage

- [ ] Task 2.3: 实现用户信息管理
  - [ ] SubTask 2.3.1: 后端实现查看和修改个人信息接口
  - [ ] SubTask 2.3.2: 前端实现个人中心页面

- [ ] Task 2.4: 实现部门管理
  - [ ] SubTask 2.4.1: 后端实现部门CRUD接口（应用层校验外键和层级深度）
  - [ ] SubTask 2.4.2: 前端实现部门管理页面

- [ ] Task 2.5: 实现权限控制
  - [ ] SubTask 2.5.1: 后端配置Spring Security，定义角色（普通用户/管理员/部门管理员）
  - [ ] SubTask 2.5.2: 后端实现基于角色的访问控制
  - [ ] SubTask 2.5.3: 前端实现基于角色的菜单和按钮显示控制

## 阶段三：会议纪要核心功能

- [ ] Task 3.1: 实现会议纪要创建与编辑
  - [ ] SubTask 3.1.1: 后端实现纪要创建接口
  - [ ] SubTask 3.1.2: 后端实现富文本内容保存接口
  - [ ] SubTask 3.1.3: 后端实现自动草稿保存（30秒间隔）
  - [ ] SubTask 3.1.4: 后端实现编辑锁定机制（应用层实现）
  - [ ] SubTask 3.1.5: 前端集成TinyMCE富文本编辑器
  - [ ] SubTask 3.1.6: 前端实现草稿自动保存提示

- [ ] Task 3.2: 实现模板管理
  - [ ] SubTask 3.2.1: 后端实现预置模板初始化（决议型、讨论型、周会型）
  - [ ] SubTask 3.2.2: 后端实现自定义模板CRUD（校验模板名称唯一性）
  - [ ] SubTask 3.2.3: 后端实现另存为模板功能
  - [ ] SubTask 3.2.4: 前端实现模板选择和模板管理页面

- [ ] Task 3.3: 实现会议纪要发送
  - [ ] SubTask 3.3.1: 后端实现纪要发送接口，创建V1.0版本
  - [ ] SubTask 3.3.2: 后端实现邮件生成和发送（Spring Mail + SMTP）
  - [ ] SubTask 3.3.3: 后端实现发送状态通知
  - [ ] SubTask 3.3.4: 前端实现发送确认页面，选择与会者

- [ ] Task 3.4: 实现版本管理
  - [ ] SubTask 3.4.1: 后端实现版本创建接口（版本号格式校验）
  - [ ] SubTask 3.4.2: 后端实现历史版本查询
  - [ ] SubTask 3.4.3: 后端实现版本差异对比
  - [ ] SubTask 3.4.4: 后端实现版本回滚
  - [ ] SubTask 3.4.5: 前端实现版本历史查看和对比界面

## 阶段四：协作功能

- [ ] Task 4.1: 实现协作指派
  - [ ] SubTask 4.1.1: 后端实现指派任务创建（校验任务数量≤5）
  - [ ] SubTask 4.1.2: 后端实现工作台待办事项通知
  - [ ] SubTask 4.1.3: 后端实现任务处理和修订模式
  - [ ] SubTask 4.1.4: 后端实现审核与合并功能
  - [ ] SubTask 4.1.5: 前端实现指派任务界面和工作台

- [ ] Task 4.2: 实现消息通知
  - [ ] SubTask 4.2.1: 后端实现站内消息创建和查询
  - [ ] SubTask 4.2.2: 后端实现消息分类和已读状态
  - [ ] SubTask 4.2.3: 前端实现消息中心页面

- [ ] Task 4.3: 实现评论/讨论
  - [ ] SubTask 4.3.1: 后端实现评论创建（校验评论深度≤3）
  - [ ] SubTask 4.3.2: 后端实现评论回复功能
  - [ ] SubTask 4.3.3: 后端实现评论通知和@功能
  - [ ] SubTask 4.3.4: 后端实现评论删除权限校验
  - [ ] SubTask 4.3.5: 前端实现评论功能界面

## 阶段五：辅助功能

- [ ] Task 5.1: 实现搜索功能
  - [ ] SubTask 5.1.1: 后端实现全文搜索（Elasticsearch或MySQL全文索引）
  - [ ] SubTask 5.1.2: 后端实现高级搜索（多条件组合）
  - [ ] SubTask 5.1.3: 后端实现搜索历史记录
  - [ ] SubTask 5.1.4: 前端实现搜索结果高亮显示

- [ ] Task 5.2: 实现导出功能
  - [ ] SubTask 5.2.1: 后端实现Word导出（Apache POI）
  - [ ] SubTask 5.2.2: 后端实现PDF导出（iText）
  - [ ] SubTask 5.2.3: 后端实现批量导出ZIP
  - [ ] SubTask 5.2.4: 前端实现导出功能界面

- [ ] Task 5.3: 实现附件管理
  - [ ] SubTask 5.3.1: 后端实现附件上传（校验文件大小≤500MB和类型）
  - [ ] SubTask 5.3.2: 后端实现附件下载
  - [ ] SubTask 5.3.3: 后端实现在线预览（图片、PDF、Office）
  - [ ] SubTask 5.3.4: 前端实现附件管理界面

- [ ] Task 5.4: 实现统计分析
  - [ ] SubTask 5.4.1: 后端实现使用统计查询
  - [ ] SubTask 5.4.2: 后端实现用户活跃度统计
  - [ ] SubTask 5.4.3: 后端实现部门分析统计
  - [ ] SubTask 5.4.4: 后端实现任务效率统计
  - [ ] SubTask 5.4.5: 前端实现ECharts图表展示

## 阶段六：工作台和用户界面

- [ ] Task 6.1: 实现用户工作台
  - [ ] SubTask 6.1.1: 后端实现待办事项查询
  - [ ] SubTask 6.1.2: 后端实现我发起的查询
  - [ ] SubTask 6.1.3: 后端实现我参与的查询
  - [ ] SubTask 6.1.4: 前端实现工作台主页面

- [ ] Task 6.2: 实现整体前端界面
  - [ ] SubTask 6.2.1: 前端实现整体布局（Header、Sidebar、Content）
  - [ ] SubTask 6.2.2: 前端实现路由配置和权限路由
  - [ ] SubTask 6.2.3: 前端实现响应式布局适配

## 阶段七：安全和其他功能

- [ ] Task 7.1: 实现安全功能
  - [ ] SubTask 7.1.1: 后端实现密码策略校验（8位、90天强制修改）
  - [ ] SubTask 7.1.2: 后端实现防SQL注入（参数化查询）
  - [ ] SubTask 7.1.3: 后端实现防XSS攻击（输入转义）
  - [ ] SubTask 7.1.4: 后端实现防CSRF攻击（Token验证）
  - [ ] SubTask 7.1.5: 后端实现会话管理（30分钟超时）
  - [ ] SubTask 7.1.6: 后端实现审计日志（SLF4J + Logback）

- [ ] Task 7.2: 实现操作日志
  - [ ] SubTask 7.2.1: 后端实现操作日志记录（AOP）
  - [ ] SubTask 7.2.2: 后端实现日志查询

- [ ] Task 7.3: 性能优化
  - [ ] SubTask 7.3.1: 后端实现数据库连接池优化
  - [ ] SubTask 7.3.2: 后端实现常用查询缓存（可选择性使用Caffeine等）
  - [ ] SubTask 7.3.3: 前端实现列表分页和懒加载

## 阶段八：测试和部署

- [ ] Task 8.1: 单元测试和集成测试
  - [ ] SubTask 8.1.1: 后端编写Service层单元测试
  - [ ] SubTask 8.1.2: 后端编写Controller层集成测试
  - [ ] SubTask 8.1.3: 前端编写组件测试

- [ ] Task 8.2: 部署配置
  - [ ] SubTask 8.2.1: 后端打包配置（JAR包）
  - [ ] SubTask 8.2.2: 前端打包构建（生产环境构建）
  - [ ] SubTask 8.2.3: Nginx配置（前端静态资源代理）

---

# Task Dependencies
- Task 1.1 必须在Task 1.2之前完成（后端先于前端）
- Task 1.3 必须在Task 2.1之前完成（数据库先于业务）
- Task 2.1、2.2 必须在Task 2.5之前完成（登录后才能做权限控制）
- Task 2.5 必须在Task 3.1之前完成（权限控制是基础）
- Task 3.1 必须在Task 3.2、3.3、3.4之前完成（基础功能先完成）
- Task 3.3 必须在Task 4.1之前完成（发送后才能指派）
- Task 5.1 必须在Task 5.4之前完成（搜索是统计的基础）
- Task 8.1 必须在Task 8.2之前完成（测试先于部署）