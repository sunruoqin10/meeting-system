-- 会议纪要管理系统数据库表结构
-- 创建数据库
CREATE DATABASE IF NOT EXISTS meetingdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE meetingdb;

-- 1. 用户表
CREATE TABLE users (
    id VARCHAR(32) PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) UNIQUE NOT NULL COMMENT '用户名',
    email VARCHAR(100) UNIQUE NOT NULL COMMENT '邮箱',
    password VARCHAR(255) NOT NULL COMMENT '密码（BCrypt加密）',
    name VARCHAR(50) NOT NULL COMMENT '真实姓名',
    avatar VARCHAR(255) COMMENT '头像URL',
    department_id VARCHAR(32) COMMENT '部门ID',
    role VARCHAR(20) DEFAULT 'user' COMMENT '角色：admin/user',
    phone VARCHAR(20) COMMENT '手机号',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
    last_login_time DATETIME COMMENT '最后登录时间',
    password_expire_date DATE COMMENT '密码过期日期',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_department (department_id),
    INDEX idx_email (email),
    FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 2. 部门表
CREATE TABLE departments (
    id VARCHAR(32) PRIMARY KEY COMMENT '部门ID',
    name VARCHAR(100) UNIQUE NOT NULL COMMENT '部门名称',
    parent_id VARCHAR(32) COMMENT '上级部门ID',
    manager_id VARCHAR(32) COMMENT '部门负责人ID',
    level TINYINT DEFAULT 1 COMMENT '层级',
    sort_order INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_parent (parent_id),
    INDEX idx_manager (manager_id),
    FOREIGN KEY (parent_id) REFERENCES departments(id) ON DELETE CASCADE,
    FOREIGN KEY (manager_id) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- 3. 会议纪要表
CREATE TABLE meeting_minutes (
    id VARCHAR(32) PRIMARY KEY COMMENT '纪要ID',
    title VARCHAR(200) NOT NULL COMMENT '会议标题',
    meeting_date DATE NOT NULL COMMENT '会议日期',
    meeting_time VARCHAR(20) COMMENT '会议时间',
    location VARCHAR(100) COMMENT '会议地点',
    host_id VARCHAR(32) NOT NULL COMMENT '主持人ID',
    status VARCHAR(20) DEFAULT 'draft' COMMENT '状态：draft/sent/revising/archived',
    current_version VARCHAR(20) COMMENT '当前版本号',
    summary TEXT COMMENT '摘要',
    content LONGTEXT COMMENT '会议内容（富文本）',
    creator_id VARCHAR(32) NOT NULL COMMENT '创建人ID',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_creator (creator_id),
    INDEX idx_host (host_id),
    INDEX idx_status (status),
    INDEX idx_meeting_date (meeting_date),
    FOREIGN KEY (creator_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (host_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会议纪要表';

-- 4. 纪要版本表
CREATE TABLE minute_versions (
    id VARCHAR(32) PRIMARY KEY COMMENT '版本ID',
    minute_id VARCHAR(32) NOT NULL COMMENT '纪要ID',
    version VARCHAR(20) NOT NULL COMMENT '版本号（如V1.0）',
    content LONGTEXT COMMENT '版本内容',
    change_log TEXT COMMENT '变更说明',
    creator_id VARCHAR(32) NOT NULL COMMENT '创建人ID',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_minute_version (minute_id, version),
    INDEX idx_minute (minute_id),
    INDEX idx_creator (creator_id),
    FOREIGN KEY (minute_id) REFERENCES meeting_minutes(id) ON DELETE CASCADE,
    FOREIGN KEY (creator_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='纪要版本表';

-- 5. 任务指派表
CREATE TABLE task_assignments (
    id VARCHAR(32) PRIMARY KEY COMMENT '任务ID',
    minute_id VARCHAR(32) NOT NULL COMMENT '纪要ID',
    assignee_id VARCHAR(32) NOT NULL COMMENT '被指派人ID',
    task_content TEXT NOT NULL COMMENT '任务内容',
    deadline DATE COMMENT '截止日期',
    status VARCHAR(20) DEFAULT 'pending' COMMENT '状态：pending/in_progress/completed',
    priority VARCHAR(20) DEFAULT 'normal' COMMENT '优先级：low/normal/high',
    completed_time DATETIME COMMENT '完成时间',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_minute (minute_id),
    INDEX idx_assignee (assignee_id),
    INDEX idx_status (status),
    FOREIGN KEY (minute_id) REFERENCES meeting_minutes(id) ON DELETE CASCADE,
    FOREIGN KEY (assignee_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务指派表';

-- 6. 模板表
CREATE TABLE templates (
    id VARCHAR(32) PRIMARY KEY COMMENT '模板ID',
    name VARCHAR(100) UNIQUE NOT NULL COMMENT '模板名称',
    type VARCHAR(50) NOT NULL COMMENT '模板类型：decision/discussion/weekly/custom',
    content LONGTEXT COMMENT '模板内容（富文本）',
    creator_id VARCHAR(32) NOT NULL COMMENT '创建人ID',
    is_system TINYINT DEFAULT 0 COMMENT '是否系统预置：0-否 1-是',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_type (type),
    INDEX idx_creator (creator_id),
    FOREIGN KEY (creator_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='模板表';

-- 7. 会议参会人员表
CREATE TABLE meeting_attendees (
    id VARCHAR(32) PRIMARY KEY COMMENT '记录ID',
    minute_id VARCHAR(32) NOT NULL COMMENT '纪要ID',
    user_id VARCHAR(32) NOT NULL COMMENT '用户ID',
    role VARCHAR(20) DEFAULT 'attendee' COMMENT '角色：host/attendee/cc',
    attended TINYINT DEFAULT 1 COMMENT '是否出席：0-否 1-是',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_minute_user (minute_id, user_id),
    INDEX idx_minute (minute_id),
    INDEX idx_user (user_id),
    FOREIGN KEY (minute_id) REFERENCES meeting_minutes(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会议参会人员表';

-- 8. 消息表
CREATE TABLE messages (
    id VARCHAR(32) PRIMARY KEY COMMENT '消息ID',
    receiver_id VARCHAR(32) NOT NULL COMMENT '接收人ID',
    type VARCHAR(50) NOT NULL COMMENT '消息类型：todo/review/system/cc',
    title VARCHAR(200) NOT NULL COMMENT '消息标题',
    content TEXT COMMENT '消息内容',
    related_id VARCHAR(32) COMMENT '关联ID（纪要ID、任务ID等）',
    is_read TINYINT DEFAULT 0 COMMENT '是否已读：0-未读 1-已读',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_receiver (receiver_id),
    INDEX idx_is_read (is_read),
    INDEX idx_type (type),
    INDEX idx_created_time (created_time),
    FOREIGN KEY (receiver_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息表';

-- 9. 操作日志表
CREATE TABLE operation_logs (
    id VARCHAR(32) PRIMARY KEY COMMENT '日志ID',
    user_id VARCHAR(32) COMMENT '操作人ID',
    user_name VARCHAR(50) COMMENT '操作人姓名',
    module VARCHAR(50) COMMENT '模块',
    operation VARCHAR(50) COMMENT '操作类型',
    resource_id VARCHAR(32) COMMENT '资源ID',
    resource_type VARCHAR(50) COMMENT '资源类型',
    details TEXT COMMENT '操作详情',
    ip_address VARCHAR(50) COMMENT 'IP地址',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_user (user_id),
    INDEX idx_module (module),
    INDEX idx_created_time (created_time),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- 10. 附件表
CREATE TABLE minute_attachments (
    id VARCHAR(32) PRIMARY KEY COMMENT '附件ID',
    minute_id VARCHAR(32) NOT NULL COMMENT '纪要ID',
    version_id VARCHAR(32) COMMENT '版本ID',
    file_name VARCHAR(255) NOT NULL COMMENT '文件名',
    file_size BIGINT COMMENT '文件大小（字节）',
    file_type VARCHAR(50) COMMENT '文件类型',
    file_path VARCHAR(500) COMMENT '存储路径',
    uploader_id VARCHAR(32) NOT NULL COMMENT '上传人ID',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_minute (minute_id),
    INDEX idx_version (version_id),
    INDEX idx_uploader (uploader_id),
    FOREIGN KEY (minute_id) REFERENCES meeting_minutes(id) ON DELETE CASCADE,
    FOREIGN KEY (version_id) REFERENCES minute_versions(id) ON DELETE CASCADE,
    FOREIGN KEY (uploader_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='附件表';

-- 11. 评论表
CREATE TABLE minute_comments (
    id VARCHAR(32) PRIMARY KEY COMMENT '评论ID',
    minute_id VARCHAR(32) NOT NULL COMMENT '纪要ID',
    user_id VARCHAR(32) NOT NULL COMMENT '评论人ID',
    content TEXT NOT NULL COMMENT '评论内容',
    parent_id VARCHAR(32) COMMENT '父评论ID（回复时使用）',
    level TINYINT DEFAULT 1 COMMENT '层级',
    mentioned_users JSON COMMENT '@的用户列表',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_minute (minute_id),
    INDEX idx_user (user_id),
    INDEX idx_parent (parent_id),
    FOREIGN KEY (minute_id) REFERENCES meeting_minutes(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (parent_id) REFERENCES minute_comments(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- 插入初始部门数据
INSERT INTO departments (id, name, parent_id, manager_id, level, sort_order) VALUES
('d001', '技术部', NULL, NULL, 1, 1),
('d002', '产品部', NULL, NULL, 1, 2),
('d003', '市场部', NULL, NULL, 1, 3),
('d004', '研发一组', 'd001', NULL, 2, 1),
('d005', '研发二组', 'd001', NULL, 2, 2);

-- 插入初始管理员用户
INSERT INTO users (id, username, email, password, name, department_id, role, status, password_expire_date) VALUES
('u001', 'admin', 'admin@company.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '管理员', NULL, 'admin', 1, DATE_ADD(CURDATE(), INTERVAL 90 DAY));
-- 密码是: admin123

-- 插入系统预置模板
INSERT INTO templates (id, name, type, content, creator_id, is_system, status) VALUES
('t001', '决议型模板', 'decision', '<h2>会议决议</h2><p>1. 决议内容...</p><p>2. 责任人：xxx</p>', 'u001', 1, 1),
('t002', '讨论型模板', 'discussion', '<h2>讨论要点</h2><p>1. 讨论主题...</p><p>2. 参与人员：xxx</p>', 'u001', 1, 1),
('t003', '周会型模板', 'weekly', '<h2>周会议程</h2><p>1. 上周工作总结</p><p>2. 本周工作计划</p>', 'u001', 1, 1);
