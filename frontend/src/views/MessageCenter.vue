<template>
  <div class="message-center">
    <!-- 页面头部 -->
    <div class="page-header">
      <h1>消息中心</h1>
      <div class="header-actions">
        <el-button type="primary" text @click="handleMarkAllRead" :disabled="unreadCount === 0">
          <el-icon class="el-icon--left"><Check /></el-icon>
          全部已读
        </el-button>
        <el-button type="primary" text>
          <el-icon class="el-icon--left"><Setting /></el-icon>
          设置
        </el-button>
      </div>
    </div>

    <!-- 筛选标签 -->
    <div class="filter-tabs">
      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane name="all">
          <template #label>
            <span>全部</span>
            <el-badge :value="totalUnread" :hidden="totalUnread === 0" />
          </template>
        </el-tab-pane>
        <el-tab-pane label="待办提醒" name="todo">
          <template #label>
            <span>待办提醒</span>
            <el-badge :value="todoCount" :hidden="todoCount === 0" />
          </template>
        </el-tab-pane>
        <el-tab-pane label="审核通知" name="review" />
        <el-tab-pane label="系统公告" name="system" />
      </el-tabs>
    </div>

    <!-- 消息列表 -->
    <div class="message-list" v-loading="loading">
      <el-card
        v-for="message in filteredList"
        :key="message.id"
        class="message-card"
        :class="{ unread: !message.isRead }"
        shadow="never"
        @click="handleReadMessage(message)"
      >
        <div class="message-main">
          <div class="message-icon" :class="'type-' + message.type">
            <el-icon :size="20">
              <component :is="getMessageIcon(message.type)" />
            </el-icon>
          </div>

          <div class="message-content">
            <div class="message-header">
              <span class="message-title">{{ message.title }}</span>
              <span class="message-time">{{ formatTime(message.createTime) }}</span>
            </div>
            <div class="message-body">
              {{ message.content }}
            </div>
            <div class="message-meta">
              <span class="message-sender">
                <el-icon><User /></el-icon>
                {{ message.senderName }}
              </span>
              <el-tag v-if="message.priority === 'high'" type="danger" size="small">
                紧急
              </el-tag>
            </div>
          </div>

          <div v-if="!message.isRead" class="unread-dot"></div>
        </div>

        <div class="message-actions">
          <el-button type="primary" text size="small" @click.stop="handleViewRelated(message)">
            查看详情
          </el-button>
          <el-button
            v-if="!message.isRead"
            type="success"
            text
            size="small"
            @click.stop="handleMarkRead(message)"
          >
            标为已读
          </el-button>
        </div>
      </el-card>

      <el-empty v-if="!loading && filteredList.length === 0" description="暂无消息" />
    </div>

    <!-- 分页 -->
    <div class="pagination-wrapper" v-if="total > pageSize">
      <el-pagination
        v-model:current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        layout="prev, pager, next"
        @current-change="handlePageChange"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  Check, Setting, Bell, ClipboardList, CheckCircle,
  Megaphone, ChatDotRound, User
} from '@element-plus/icons-vue'
import { messageList as mockMessageList } from '@/mock/data'

const router = useRouter()

const loading = ref(false)
const activeTab = ref('all')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const messageList = ref([...mockMessageList])

const filteredList = computed(() => {
  if (activeTab.value === 'all') {
    return messageList.value
  }
  return messageList.value.filter(m => m.type === activeTab.value)
})

const totalUnread = computed(() => messageList.value.filter(m => !m.isRead).length)
const todoCount = computed(() => {
  return messageList.value.filter(m => m.type === 'todo' && !m.isRead).length
})
const unreadCount = computed(() => totalUnread.value)

const getMessageIcon = (type) => {
  const icons = {
    todo: 'ClipboardList',
    review: 'CheckCircle',
    system: 'Megaphone',
    cc: 'ChatDotRound'
  }
  return icons[type] || 'Bell'
}

const formatTime = (time) => {
  const now = new Date()
  const msgTime = new Date(time)
  const diff = now - msgTime

  if (diff < 60000) return '刚刚'
  if (diff < 3600000) return Math.floor(diff / 60000) + '分钟前'
  if (diff < 86400000) return Math.floor(diff / 3600000) + '小时前'

  return time
}

const handleTabChange = () => {
  currentPage.value = 1
}

const handlePageChange = (page) => {
  currentPage.value = page
}

const handleReadMessage = (message) => {
  if (!message.isRead) {
    message.isRead = true
  }

  // 跳转到相关页面
  if (message.relatedType === 'minute' && message.relatedId) {
    router.push(`/minutes/${message.relatedId}`)
  }
}

const handleViewRelated = (message) => {
  handleReadMessage(message)
}

const handleMarkRead = (message) => {
  message.isRead = true
  ElMessage.success('已标为已读')
}

const handleMarkAllRead = async () => {
  messageList.value.forEach(m => {
    m.isRead = true
  })
  ElMessage.success('全部已标为已读')
}

onMounted(() => {
  total.value = filteredList.value.length
})
</script>

<style lang="scss" scoped>
.message-center {
  max-width: 900px;
  margin: 0 auto;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--space-lg);

  h1 {
    font-family: var(--font-code);
    font-size: 24px;
    font-weight: 600;
    color: var(--color-text);
  }

  .header-actions {
    display: flex;
    gap: var(--space-sm);
  }
}

.filter-tabs {
  background: var(--color-surface);
  padding: var(--space-md);
  border-radius: var(--radius-lg);
  margin-bottom: var(--space-lg);
  box-shadow: var(--shadow-sm);

  :deep(.el-tabs) {
    .el-tabs__header {
      margin-bottom: 0;
    }

    .el-tabs__item {
      color: var(--color-text-secondary);

      &.is-active {
        color: var(--color-primary);
      }
    }
  }
}

.message-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-md);
}

.message-card {
  cursor: pointer;
  transition: all var(--transition-base);

  &:hover {
    border-color: var(--color-primary);
  }

  &.unread {
    background: var(--color-background);
    border-left: 3px solid var(--color-primary);
  }

  :deep(.el-card__body) {
    padding: var(--space-md) var(--space-lg);
  }
}

.message-main {
  display: flex;
  align-items: flex-start;
  gap: var(--space-md);
  position: relative;
}

.message-icon {
  width: 44px;
  height: 44px;
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;

  &.type-todo {
    background: #FEF3C7;
    color: #F59E0B;
  }

  &.type-review {
    background: #DBEAFE;
    color: #3B82F6;
  }

  &.type-system {
    background: #EDE9FE;
    color: #8B5CF6;
  }

  &.type-cc {
    background: #F3F4F6;
    color: #6B7280;
  }
}

.message-content {
  flex: 1;
  min-width: 0;
}

.message-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--space-xs);

  .message-title {
    font-weight: 600;
    color: var(--color-text);
  }

  .message-time {
    font-size: 13px;
    color: var(--color-text-secondary);
  }
}

.message-body {
  font-size: 14px;
  color: var(--color-text-secondary);
  line-height: 1.5;
  margin-bottom: var(--space-sm);
}

.message-meta {
  display: flex;
  align-items: center;
  gap: var(--space-md);

  .message-sender {
    display: flex;
    align-items: center;
    gap: 4px;
    font-size: 13px;
    color: var(--color-text-secondary);
  }
}

.unread-dot {
  width: 8px;
  height: 8px;
  background: var(--color-danger);
  border-radius: 50%;
  flex-shrink: 0;
}

.message-actions {
  display: flex;
  gap: var(--space-sm);
  margin-top: var(--space-md);
  padding-top: var(--space-md);
  border-top: 1px solid var(--color-border);
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: var(--space-xl);
}
</style>
