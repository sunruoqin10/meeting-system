<template>
  <div class="operation-logs">
    <!-- 页面头部 -->
    <div class="page-header">
      <h1>操作日志</h1>
      <div class="header-actions">
        <el-button @click="handleRefresh">
          <el-icon class="el-icon--left"><Refresh /></el-icon>
          刷新
        </el-button>
        <el-button @click="handleExport">
          <el-icon class="el-icon--left"><Download /></el-icon>
          导出日志
        </el-button>
      </div>
    </div>

    <!-- 筛选 -->
    <div class="filter-bar">
      <el-select v-model="userFilter" placeholder="操作人" clearable style="width: 140px;">
        <el-option label="张三" value="u001" />
        <el-option label="李四" value="u002" />
        <el-option label="王五" value="u003" />
      </el-select>

      <el-select v-model="typeFilter" placeholder="操作类型" clearable style="width: 140px;">
        <el-option label="登录" value="login" />
        <el-option label="创建" value="create" />
        <el-option label="编辑" value="edit" />
        <el-option label="删除" value="delete" />
        <el-option label="导出" value="export" />
        <el-option label="指派" value="assign" />
      </el-select>

      <el-date-picker
        v-model="dateRange"
        type="datetimerange"
        range-separator="至"
        start-placeholder="开始时间"
        end-placeholder="结束时间"
        format="YYYY-MM-DD HH:mm"
        value-format="YYYY-MM-DD HH:mm:ss"
        style="width: 360px;"
      />
    </div>

    <!-- 日志表格 -->
    <el-card shadow="never" class="table-card">
      <el-table :data="filteredList" border stripe v-loading="loading">
        <el-table-column prop="createTime" label="时间" width="180" />

        <el-table-column label="操作人" width="120">
          <template #default="{ row }">
            <div class="user-cell">
              <el-avatar :size="28" :src="getUserAvatar(row.userId)">
                {{ row.userName?.charAt(0) }}
              </el-avatar>
              <span>{{ row.userName }}</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="类型" width="120">
          <template #default="{ row }">
            <el-tag :color="getTypeColor(row.operationType)" size="small" effect="dark">
              {{ getTypeName(row.operationType) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="详情">
          <template #default="{ row }">
            <el-tooltip :content="row.operationDetail" placement="top">
              <span class="detail-text">{{ row.operationDetail }}</span>
            </el-tooltip>
          </template>
        </el-table-column>

        <el-table-column label="IP地址" width="150">
          <template #default="{ row }">
            <span class="ip-text">
              <el-icon><Location /></el-icon>
              {{ row.ipAddress }}
            </span>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="currentPage"
          :page-size="pageSize"
          :total="total"
          layout="total, prev, pager, next"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { Refresh, Download, Location } from '@element-plus/icons-vue'
import { operationLogs as mockLogs, userList } from '@/mock/data'

const loading = ref(false)
const userFilter = ref('')
const typeFilter = ref('')
const dateRange = ref([])
const currentPage = ref(1)
const pageSize = ref(20)
const total = ref(0)

const logList = ref([...mockLogs])

const typeColors = {
  login: '#3B82F6',
  logout: '#6B7280',
  create: '#10B981',
  edit: '#F97316',
  delete: '#EF4444',
  export: '#8B5CF6',
  assign: '#06B6D4'
}

const typeNames = {
  login: '登录',
  logout: '登出',
  create: '创建',
  edit: '编辑',
  delete: '删除',
  export: '导出',
  assign: '指派'
}

const filteredList = computed(() => {
  let result = [...logList.value]

  if (userFilter.value) {
    result = result.filter(log => log.userId === userFilter.value)
  }

  if (typeFilter.value) {
    result = result.filter(log => log.operationType === typeFilter.value)
  }

  if (dateRange.value && dateRange.value.length === 2) {
    const [start, end] = dateRange.value
    result = result.filter(log => {
      const logTime = new Date(log.createTime)
      return logTime >= start && logTime <= end
    })
  }

  total.value = result.length
  return result
})

const getUserAvatar = (userId) => {
  const user = userList.find(u => u.id === userId)
  return user?.avatar || ''
}

const getTypeColor = (type) => typeColors[type] || '#6B7280'
const getTypeName = (type) => typeNames[type] || type

const handleRefresh = () => {
  loading.value = true
  setTimeout(() => {
    loading.value = false
    ElMessage.success('刷新成功')
  }, 500)
}

const handleExport = () => {
  ElMessage.success('正在导出日志文件...')
}
</script>

<style lang="scss" scoped>
.operation-logs {
  max-width: 1400px;
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

.filter-bar {
  display: flex;
  gap: var(--space-md);
  margin-bottom: var(--space-lg);
  flex-wrap: wrap;
}

.table-card {
  :deep(.el-card__body) {
    padding: 0;
  }
}

.user-cell {
  display: flex;
  align-items: center;
  gap: var(--space-xs);

  span {
    font-size: 13px;
  }
}

.detail-text {
  display: inline-block;
  max-width: 300px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  font-size: 13px;
}

.ip-text {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  color: var(--color-text-secondary);
  font-family: var(--font-code);
}

.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  padding: var(--space-md);
  border-top: 1px solid var(--color-border);
}
</style>
