<template>
  <div class="minutes-list">
    <!-- 页面头部 -->
    <div class="page-header">
      <h1>会议纪要</h1>
      <el-button type="primary" @click="goToCreate">
        <el-icon class="el-icon--left"><Plus /></el-icon>
        新建纪要
      </el-button>
    </div>

    <!-- 筛选标签 -->
    <div class="filter-tabs">
      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane label="全部" name="all" />
        <el-tab-pane label="草稿" name="draft" />
        <el-tab-pane label="已发送" name="sent" />
        <el-tab-pane label="修订中" name="revising" />
        <el-tab-pane label="已归档" name="archived" />
      </el-tabs>

      <div class="filter-actions">
        <el-select v-model="filterDept" placeholder="筛选部门" clearable style="width: 140px;">
          <el-option label="技术研发部" value="tech" />
          <el-option label="市场部" value="market" />
          <el-option label="销售部" value="sales" />
        </el-select>

        <el-select v-model="sortOrder" placeholder="排序" style="width: 120px;">
          <el-option label="最新创建" value="create_desc" />
          <el-option label="最早创建" value="create_asc" />
          <el-option label="最新会议" value="meeting_desc" />
          <el-option label="最早会议" value="meeting_asc" />
        </el-select>
      </div>
    </div>

    <!-- 搜索结果提示 -->
    <div v-if="searchKeyword" class="search-tip">
      搜索关键词: <strong>"{{ searchKeyword }}"</strong>
      <el-button type="primary" text @click="clearSearch">清除</el-button>
    </div>

    <!-- 纪要列表 -->
    <div class="minutes-grid" v-loading="loading">
      <el-card
        v-for="item in filteredList"
        :key="item.id"
        class="minute-card"
        shadow="hover"
        @click="goToDetail(item)"
      >
        <div class="card-header">
          <div class="card-title">
            <el-icon class="card-icon"><Document /></el-icon>
            <span class="title-text">{{ item.title }}</span>
          </div>
          <el-tag
            :class="'status-' + item.status"
            size="small"
          >
            {{ statusText[item.status] }}
          </el-tag>
        </div>

        <div class="card-meta">
          <span><el-icon><Calendar /></el-icon> {{ item.meetingDate }}</span>
          <span><el-icon><User /></el-icon> {{ item.creatorName }}</span>
          <span><el-icon><ChatDotRound /></el-icon> {{ item.commentCount }}</span>
          <span><el-icon><Paperclip /></el-icon> {{ item.attachmentCount }}</span>
        </div>

        <div class="card-summary">
          {{ item.summary }}
        </div>

        <div class="card-footer">
          <span class="version">版本 {{ item.currentVersion }}</span>
          <el-button type="primary" text size="small" @click.stop="handleEdit(item)">
            编辑
          </el-button>
        </div>
      </el-card>

      <el-empty v-if="!loading && filteredList.length === 0" description="暂无会议纪要">
        <el-button type="primary" @click="goToCreate">新建纪要</el-button>
      </el-empty>
    </div>

    <!-- 分页 -->
    <div class="pagination-wrapper" v-if="total > pageSize">
      <el-pagination
        v-model:current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        layout="prev, pager, next, jumper"
        @current-change="handlePageChange"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  Document, Calendar, User, ChatDotRound, Paperclip, Plus
} from '@element-plus/icons-vue'
import { getMinutesList } from '@/api'

const router = useRouter()
const route = useRoute()

const loading = ref(false)
const activeTab = ref('all')
const filterDept = ref('')
const sortOrder = ref('create_desc')
const searchKeyword = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const statusText = {
  draft: '草稿',
  sent: '已发送',
  revising: '修订中',
  archived: '已归档'
}

const minutesList = ref([])

const filteredList = computed(() => {
  let result = [...minutesList.value]

  if (activeTab.value !== 'all') {
    result = result.filter(m => m.status === activeTab.value)
  }

  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase()
    result = result.filter(m =>
      (m.title && m.title.toLowerCase().includes(keyword)) ||
      (m.summary && m.summary.toLowerCase().includes(keyword))
    )
  }

  result.sort((a, b) => {
    if (sortOrder.value === 'create_desc') {
      const dateA = new Date(a.createdTime || '2000-01-01')
      const dateB = new Date(b.createdTime || '2000-01-01')
      return dateB - dateA
    } else if (sortOrder.value === 'create_asc') {
      const dateA = new Date(a.createdTime || '2000-01-01')
      const dateB = new Date(b.createdTime || '2000-01-01')
      return dateA - dateB
    } else if (sortOrder.value === 'meeting_desc') {
      const dateA = new Date(a.meetingDate || '2000-01-01')
      const dateB = new Date(b.meetingDate || '2000-01-01')
      return dateB - dateA
    } else if (sortOrder.value === 'meeting_asc') {
      const dateA = new Date(a.meetingDate || '2000-01-01')
      const dateB = new Date(b.meetingDate || '2000-01-01')
      return dateA - dateB
    }
    return 0
  })

  total.value = result.length
  return result
})

const loadMinutes = async () => {
  try {
    loading.value = true
    const params = {}
    if (activeTab.value !== 'all') {
      params.status = activeTab.value
    }
    if (searchKeyword.value) {
      params.keyword = searchKeyword.value
    }
    const data = await getMinutesList(params)
    minutesList.value = data
  } catch (error) {
    console.error('加载会议纪要失败:', error)
    ElMessage.error(error.message || '加载会议纪要失败')
  } finally {
    loading.value = false
  }
}

const goToCreate = () => router.push('/minutes/create')
const goToDetail = (item) => router.push(`/minutes/${item.id}`)
const handleEdit = (item) => router.push(`/minutes/${item.id}/edit`)

const handleTabChange = () => {
  currentPage.value = 1
  loadMinutes()
}

const handlePageChange = (page) => {
  currentPage.value = page
}

const clearSearch = () => {
  searchKeyword.value = ''
  loadMinutes()
}

onMounted(async () => {
  if (route.query.keyword) {
    searchKeyword.value = route.query.keyword
  }
  await loadMinutes()
})

watch(() => route.query.keyword, (val) => {
  if (val) {
    searchKeyword.value = val
    loadMinutes()
  }
})

watch([activeTab, sortOrder], () => {
  loadMinutes()
})
</script>

<style lang="scss" scoped>
.minutes-list {
  max-width: 1200px;
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
}

.filter-tabs {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--space-lg);
  background: var(--color-surface);
  padding: var(--space-md);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);

  :deep(.el-tabs) {
    .el-tabs__header {
      margin-bottom: 0;
    }

    .el-tabs__nav-wrap::after {
      display: none;
    }

    .el-tabs__item {
      color: var(--color-text-secondary);

      &.is-active {
        color: var(--color-primary);
      }
    }

    .el-tabs__active-bar {
      background-color: var(--color-primary);
    }
  }
}

.filter-actions {
  display: flex;
  gap: var(--space-sm);
}

.search-tip {
  background: var(--color-background);
  padding: var(--space-md);
  border-radius: var(--radius-md);
  margin-bottom: var(--space-md);
  display: flex;
  align-items: center;
  gap: var(--space-sm);
  color: var(--color-text-secondary);
}

.minutes-grid {
  display: grid;
  gap: var(--space-md);
}

.minute-card {
  cursor: pointer;
  transition: all var(--transition-base);

  &:hover {
    transform: translateY(-2px);
    border-color: var(--color-primary);
  }

  :deep(.el-card__body) {
    padding: var(--space-lg);
  }
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: var(--space-md);
}

.card-title {
  display: flex;
  align-items: center;
  gap: var(--space-sm);
  flex: 1;
  min-width: 0;

  .card-icon {
    color: var(--color-primary);
    flex-shrink: 0;
  }

  .title-text {
    font-weight: 600;
    color: var(--color-text);
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
}

.card-meta {
  display: flex;
  gap: var(--space-lg);
  margin-bottom: var(--space-md);
  font-size: 13px;
  color: var(--color-text-secondary);

  span {
    display: flex;
    align-items: center;
    gap: 4px;
  }
}

.card-summary {
  font-size: 14px;
  color: var(--color-text-secondary);
  line-height: 1.6;
  margin-bottom: var(--space-md);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: var(--space-md);
  border-top: 1px solid var(--color-border);

  .version {
    font-family: var(--font-code);
    font-size: 12px;
    color: var(--color-text-secondary);
  }
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: var(--space-xl);
}

@media (max-width: 768px) {
  .filter-tabs {
    flex-direction: column;
    gap: var(--space-md);
    align-items: stretch;

    :deep(.el-tabs) {
      .el-tabs__header {
        width: 100%;
      }
    }
  }

  .filter-actions {
    justify-content: space-between;
  }
}
</style>
