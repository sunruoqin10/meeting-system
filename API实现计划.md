# 会议系统 API 实现计划

## 高优先级（核心功能）

### 1. MeetingMinuteController - 会议纪要管理 ⭐
- 列表查询（分页、筛选）
- 创建会议纪要
- 编辑会议纪要
- 删除会议纪要
- 详情查询
- 状态管理（草稿、修订中、已发送、已归档）

### 2. DepartmentController - 部门管理 ⭐
- 部门列表查询
- 创建部门
- 编辑部门
- 删除部门
- 部门树形结构

### 3. UserController - 用户管理 ⭐
- 用户列表查询（分页、筛选）
- 创建用户
- 编辑用户
- 删除用户
- 重置密码
- 锁定/解锁用户
- 更新用户状态

## 中优先级（重要功能）

### 4. TemplateController - 模板管理
- 模板列表
- 创建模板
- 编辑模板
- 删除模板
- 复制模板创建纪要

### 5. MessageController - 消息中心
- 消息列表
- 标记已读
- 删除消息
- 发送消息

### 6. VersionController - 版本历史
- 版本列表
- 创建版本
- 恢复到指定版本
- 版本对比

### 7. TaskAssignmentController - 任务分配
- 任务列表
- 创建任务
- 更新任务状态
- 删除任务

## 低优先级（辅助功能）

### 8. MinuteCommentController - 评论管理
- 评论列表
- 添加评论
- 删除评论

### 9. MinuteAttachmentController - 附件管理
- 附件列表
- 上传附件
- 删除附件
- 下载附件

### 10. MeetingAttendeeController - 参会人员管理
- 参会人员列表
- 添加参会人员
- 删除参会人员

### 11. OperationLogController - 操作日志
- 日志列表（带筛选）
- 日志查询

### 12. StatisticsController - 统计数据
- 总体统计数据
- 趋势数据
- 部门分布
- 任务效率

### 13. SearchController - 搜索功能
- 全文搜索
- 高级搜索（时间、部门、人员等）

## 实现顺序

1. 第一批：MeetingMinuteController、DepartmentController、UserController
2. 第二批：TemplateController、MessageController
3. 第三批：VersionController、TaskAssignmentController
4. 第四批：评论、附件、参会人员、日志、统计、搜索

## 进度跟踪

- [x] AuthController - 认证功能（登录、获取用户信息、更新用户信息、修改密码）
- [ ] MeetingMinuteController - 进行中
- [ ] DepartmentController
- [ ] UserController
- [ ] TemplateController
- [ ] MessageController
- [ ] VersionController
- [ ] TaskAssignmentController
- [ ] MinuteCommentController
- [ ] MinuteAttachmentController
- [ ] MeetingAttendeeController
- [ ] OperationLogController
- [ ] StatisticsController
- [ ] SearchController
