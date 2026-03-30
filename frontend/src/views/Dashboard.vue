<template>
  <div class="dashboard">
    <!-- 欢迎区域 -->
    <div class="welcome-section">
      <div class="welcome-text">
        <h1>👋 欢迎回来, {{ userInfo.name }}</h1>
        <p class="current-date">{{ currentDate }}</p>
      </div>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-grid">
      <div class="stat-card cursor-pointer" @click="goToMyInitiated">
        <div class="stat-icon" style="background: #FEF3C7;">
          <el-icon :size="28" color="#F59E0B"><Document /></el-icon>
        </div>
        <div class="stat-content">
          <span class="stat-value">{{ dashboardStats.todoCount }}</span>
          <span class="stat-label">待办事项</span>
        </div>
      </div>

      <div class="stat-card cursor-pointer" @click="goToMinutes">
        <div class="stat-icon" style="background: #DBEAFE;">
          <el-icon :size="28" color="#3B82F6"><Edit /></el-icon>
        </div>
        <div class="stat-content">
          <span class="stat-value">{{ dashboardStats.initiatedCount }}</span>
          <span class="stat-label">我发起的</span>
        </div>
      </div>

      <div class="stat-card cursor-pointer" @click="goToParticipated">
        <div class="stat-icon" style="background: #D1FAE5;">
          <el-icon :size="28" color="#10B981"><User /></el-icon>
        </div>
        <div class="stat-content">
          <span class="stat-value">{{ dashboardStats.participatedCount }}</span>
          <span class="stat-label">我参与的</span>
        </div>
      </div>
    </div>

    <!-- 待办事项列表 -->
    <div class="section">
      <div class="section-header">
        <h2>
          <el-icon><Bell /></el-icon>
          待办事项
        </h2>
        <el-button type="primary" text @click="goToAllTodos">
          查看全部
          <el-icon class="el-icon--right"><ArrowRight /></el-icon>
        </el-button>
      </div>

      <el-card shadow="never" class="todo-list" v-if="todoList.length > 0">
        <div v-for="item in todoList" :key="item.id" class="todo-item">
          <div class="todo-checkbox">
            <el-checkbox v-model="item.completed" @change="handleTodoChange(item)">
              <span :class="{ completed: item.completed }">{{ item.minuteTitle }}</span>
            </el-checkbox>
          </div>
          <div class="todo-meta">
            <span class="todo-assigner">来自 {{ item.assignerName }}</span>
            <span class="todo-time">{{ item.createTime }}</span>
          </div>
          <div class="todo-instruction">{{ item.instruction }}</div>
          <el-button type="primary" size="small" @click="handleTodo(item)">
            处理
          </el-button>
        </div>
      </el-card>

      <el-empty v-else description="暂无待办事项" />
    </div>

    <!-- 快捷入口 -->
    <div class="section">
      <h2 class="section-title">
        <el-icon><Lightning /></el-icon>
        快捷入口
      </h2>
      <div class="quick-actions">
        <div class="quick-action-item" @click="goToCreateMinute">
          <div class="quick-icon" style="background: #FEF3C7;">
            <el-icon :size="24" color="#F59E0B"><Plus /></el-icon>
          </div>
          <span>新建纪要</span>
        </div>

        <div class="quick-action-item" @click="goToTemplates">
          <div class="quick-icon" style="background: #DBEAFE;">
            <el-icon :size="24" color="#3B82F6"><Collection /></el-icon>
          </div>
          <span>模板管理</span>
        </div>

        <div class="quick-action-item" @click="goToProfile">
          <div class="quick-icon" style="background: #D1FAE5;">
            <el-icon :size="24" color="#10B981"><User /></el-icon>
          </div>
          <span>个人中心</span>
        </div>

        <div class="quick-action-item" @click="goToStatistics">
          <div class="quick-icon" style="background: #EDE9FE;">
            <el-icon :size="24" color="#8B5CF6"><DataAnalysis /></el-icon>
          </div>
          <span>统计分析</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import dayjs from 'dayjs'
import {
  Document, Edit, User, Bell, ArrowRight, Lightning,
  Plus, Collection, DataAnalysis
} from '@element-plus/icons-vue'
import { currentUser, dashboardStats as rawDashboardStats, todoList as mockTodoList } from '@/mock/data'

const router = useRouter()

const userInfo = ref(currentUser)

const dashboardStats = ref(rawDashboardStats)

const todoList = ref(mockTodoList.map(t => ({ ...t, completed: false })))

const currentDate = computed(() => {
  return dayjs().format('YYYY年MM月DD日 dddd')
})

const goToMyInitiated = () => router.push('/minutes?filter=initiated')
const goToMinutes = () => router.push('/minutes')
const goToParticipated = () => router.push('/minutes?filter=participated')
const goToAllTodos = () => router.push('/messages?type=todo')
const goToCreateMinute = () => router.push('/minutes/create')
const goToTemplates = () => router.push('/templates')
const goToProfile = () => router.push('/profile')
const goToStatistics = () => router.push('/statistics')

const handleTodo = (item) => {
  router.push(`/minutes/${item.minuteId}`)
}

const handleTodoChange = (item) => {
  // 处理待办完成状态
  console.log('Todo changed:', item)
}
</script>

<style lang="scss" scoped>
.dashboard {
  max-width: 1200px;
  margin: 0 auto;
}

.welcome-section {
  margin-bottom: var(--space-xl);

  h1 {
    font-family: var(--font-code);
    font-size: 28px;
    font-weight: 600;
    color: var(--color-text);
    margin-bottom: var(--space-xs);
  }

  .current-date {
    color: var(--color-text-secondary);
    font-size: 14px;
  }
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: var(--space-lg);
  margin-bottom: var(--space-xl);
}

.stat-card {
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  padding: var(--space-lg);
  display: flex;
  align-items: center;
  gap: var(--space-md);
  box-shadow: var(--shadow-sm);
  transition: all var(--transition-base);

  &:hover {
    box-shadow: var(--shadow-md);
    transform: translateY(-2px);
  }
}

.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: var(--radius-lg);
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-content {
  display: flex;
  flex-direction: column;
}

.stat-value {
  font-family: var(--font-code);
  font-size: 28px;
  font-weight: 700;
  color: var(--color-text);
}

.stat-label {
  font-size: 14px;
  color: var(--color-text-secondary);
}

.section {
  margin-bottom: var(--space-xl);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--space-md);

  h2 {
    font-family: var(--font-code);
    font-size: 18px;
    font-weight: 600;
    color: var(--color-text);
    display: flex;
    align-items: center;
    gap: var(--space-sm);
  }
}

.section-title {
  font-family: var(--font-code);
  font-size: 18px;
  font-weight: 600;
  color: var(--color-text);
  margin-bottom: var(--space-md);
  display: flex;
  align-items: center;
  gap: var(--space-sm);
}

.todo-list {
  :deep(.el-card__body) {
    padding: 0;
  }
}

.todo-item {
  display: grid;
  grid-template-columns: 1fr auto;
  grid-template-rows: auto auto;
  gap: var(--space-xs) var(--space-md);
  padding: var(--space-md) var(--space-lg);
  border-bottom: 1px solid var(--color-border);
  align-items: center;

  &:last-child {
    border-bottom: none;
  }

  &:hover {
    background: var(--color-background);
  }
}

.todo-checkbox {
  :deep(.el-checkbox__label) {
    color: var(--color-text);
  }

  .completed {
    text-decoration: line-through;
    color: var(--color-text-secondary);
  }
}

.todo-meta {
  display: flex;
  align-items: center;
  gap: var(--space-md);
  font-size: 12px;
  color: var(--color-text-secondary);
  grid-row: 1;
  grid-column: 2;
}

.todo-instruction {
  font-size: 13px;
  color: var(--color-text-secondary);
  grid-column: 1 / -1;
  padding-left: 24px;
}

.quick-actions {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: var(--space-md);
}

.quick-action-item {
  background: var(--color-surface);
  border: 2px dashed var(--color-border);
  border-radius: var(--radius-lg);
  padding: var(--space-lg);
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--space-sm);
  cursor: pointer;
  transition: all var(--transition-base);

  &:hover {
    border-color: var(--color-primary);
    background: var(--color-background);
  }

  span {
    font-size: 14px;
    color: var(--color-text);
  }
}

.quick-icon {
  width: 48px;
  height: 48px;
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
}

@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }

  .quick-actions {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>
