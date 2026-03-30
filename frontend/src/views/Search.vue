<template>
  <div class="search-page">
    <!-- 搜索框 -->
    <div class="search-box-wrapper">
      <el-input
        v-model="searchKeyword"
        placeholder="搜索会议纪要..."
        size="large"
        class="search-input"
        :prefix-icon="Search"
        clearable
        @keyup.enter="handleSearch"
      >
        <template #append>
          <el-button @click="handleSearch">搜索</el-button>
        </template>
      </el-input>
    </div>

    <!-- 高级搜索 -->
    <el-collapse v-model="advancedOpen" class="advanced-search">
      <el-collapse-item title="高级搜索" name="advanced">
        <div class="advanced-form">
          <div class="form-row">
            <el-form-item label="日期范围">
              <el-date-picker
                v-model="dateRange"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                format="YYYY-MM-DD"
              />
            </el-form-item>

            <el-form-item label="创建人">
              <el-select v-model="creatorFilter" placeholder="选择创建人" clearable>
                <el-option label="张三" value="u001" />
                <el-option label="李四" value="u002" />
                <el-option label="王五" value="u003" />
              </el-select>
            </el-form-item>

            <el-form-item label="状态">
              <el-select v-model="statusFilter" placeholder="选择状态" clearable>
                <el-option label="草稿" value="draft" />
                <el-option label="已发送" value="sent" />
                <el-option label="修订中" value="revising" />
                <el-option label="已归档" value="archived" />
              </el-select>
            </el-form-item>
          </div>

          <div class="form-actions">
            <el-button @click="resetFilters">重置</el-button>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
          </div>
        </div>
      </el-collapse-item>
    </el-collapse>

    <!-- 搜索历史 -->
    <div class="search-history" v-if="!searchKeyword && searchHistory.length > 0">
      <span class="history-label">搜索历史:</span>
      <el-tag
        v-for="item in searchHistory"
        :key="item.keyword"
        class="history-tag"
        @click="handleHistoryClick(item.keyword)"
      >
        {{ item.keyword }}
      </el-tag>
      <el-button type="primary" text size="small" @click="clearHistory">
        <el-icon><Delete /></el-icon>
        清除
      </el-button>
    </div>

    <!-- 搜索结果 -->
    <div class="search-results" v-if="searchKeyword">
      <div class="results-header">
        <span>找到 <strong>{{ filteredResults.length }}</strong> 条结果</span>
      </div>

      <div class="results-list">
        <el-card
          v-for="result in filteredResults"
          :key="result.id"
          class="result-card"
          shadow="hover"
          @click="goToDetail(result)"
        >
          <div class="result-header">
            <span class="result-title">{{ result.title }}</span>
            <span class="result-date">{{ result.meetingDate }}</span>
          </div>
          <div class="result-content" v-html="result.content"></div>
          <div class="result-meta">
            <span><el-icon><User /></el-icon> {{ result.creatorName }}</span>
            <span><el-icon><ChatDotRound /></el-icon> {{ result.commentCount }}</span>
            <span><el-icon><Paperclip /></el-icon> {{ result.attachmentCount }}</span>
            <el-tag :type="getStatusType(result.status)" size="small">
              {{ getStatusName(result.status) }}
            </el-tag>
          </div>
        </el-card>

        <el-empty v-if="filteredResults.length === 0" description="未找到相关结果" />
      </div>
    </div>

    <!-- 空白状态 -->
    <div class="empty-state" v-if="!searchKeyword && searchHistory.length === 0">
      <el-icon :size="64" color="var(--color-text-secondary)"><Search /></el-icon>
      <p>输入关键词搜索会议纪要</p>
      <p class="tips">支持搜索标题和内容</p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Search, User, ChatDotRound, Paperclip, Delete } from '@element-plus/icons-vue'
import { searchResults as mockResults, searchHistory as mockHistory } from '@/mock/data'

const router = useRouter()
const route = useRoute()

const searchKeyword = ref('')
const advancedOpen = ref('')
const dateRange = ref([])
const creatorFilter = ref('')
const statusFilter = ref('')

const searchResults = ref([...mockResults])
const searchHistory = ref([...mockHistory])

const filteredResults = computed(() => {
  if (!searchKeyword.value) return []

  const keyword = searchKeyword.value.toLowerCase()
  return searchResults.value.filter(r =>
    r.title.toLowerCase().includes(keyword) ||
    r.content.toLowerCase().includes(keyword)
  )
})

const getStatusType = (status) => {
  const types = {
    draft: 'info',
    sent: 'success',
    revising: 'warning',
    archived: ''
  }
  return types[status] || 'info'
}

const getStatusName = (status) => {
  const names = {
    draft: '草稿',
    sent: '已发送',
    revising: '修订中',
    archived: '已归档'
  }
  return names[status] || status
}

const handleSearch = () => {
  if (!searchKeyword.value.trim()) {
    ElMessage.warning('请输入搜索关键词')
    return
  }

  // 添加到搜索历史
  const existing = searchHistory.value.find(h => h.keyword === searchKeyword.value)
  if (!existing) {
    searchHistory.value.unshift({
      keyword: searchKeyword.value,
      searchTime: new Date().toLocaleString()
    })
  }
}

const handleHistoryClick = (keyword) => {
  searchKeyword.value = keyword
  handleSearch()
}

const resetFilters = () => {
  dateRange.value = []
  creatorFilter.value = ''
  statusFilter.value = ''
}

const clearHistory = () => {
  searchHistory.value = []
}

const goToDetail = (result) => {
  router.push(`/minutes/${result.id}`)
}

onMounted(() => {
  if (route.query.keyword) {
    searchKeyword.value = route.query.keyword
    handleSearch()
  }
})
</script>

<style lang="scss" scoped>
.search-page {
  max-width: 900px;
  margin: 0 auto;
}

.search-box-wrapper {
  margin-bottom: var(--space-lg);
}

.search-input {
  :deep(.el-input__wrapper) {
    border-radius: var(--radius-lg) 0 var(--radius-lg) 0;
    padding-left: 20px;
  }

  :deep(.el-input-group__append) {
    border-radius: 0 var(--radius-lg) var(--radius-lg) 0;
    background: var(--color-primary);
    color: white;
    border-color: var(--color-primary);

    .el-button {
      color: white;
    }
  }
}

.advanced-search {
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  margin-bottom: var(--space-lg);
  box-shadow: var(--shadow-sm);

  :deep(.el-collapse-item__header) {
    padding: var(--space-md) var(--space-lg);
    font-weight: 500;
  }

  :deep(.el-collapse-item__content) {
    padding: 0 var(--space-lg) var(--space-lg);
  }
}

.advanced-form {
  .form-row {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: var(--space-md);
  }

  .form-actions {
    display: flex;
    justify-content: flex-end;
    gap: var(--space-sm);
    margin-top: var(--space-md);
  }
}

.search-history {
  display: flex;
  align-items: center;
  gap: var(--space-sm);
  flex-wrap: wrap;
  margin-bottom: var(--space-lg);

  .history-label {
    color: var(--color-text-secondary);
    font-size: 14px;
  }

  .history-tag {
    cursor: pointer;
    padding: 8px 16px;

    &:hover {
      background: var(--color-primary);
      color: white;
    }
  }
}

.results-header {
  margin-bottom: var(--space-md);
  color: var(--color-text-secondary);
  font-size: 14px;

  strong {
    color: var(--color-primary);
  }
}

.results-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-md);
}

.result-card {
  cursor: pointer;
  transition: all var(--transition-base);

  &:hover {
    border-color: var(--color-primary);
    transform: translateY(-2px);
  }
}

.result-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--space-sm);

  .result-title {
    font-weight: 600;
    color: var(--color-text);
  }

  .result-date {
    font-size: 13px;
    color: var(--color-text-secondary);
  }
}

.result-content {
  font-size: 14px;
  color: var(--color-text-secondary);
  line-height: 1.6;
  margin-bottom: var(--space-md);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;

  :deep(mark) {
    background: #FEF08A;
    color: inherit;
    padding: 0 2px;
    border-radius: 2px;
  }
}

.result-meta {
  display: flex;
  align-items: center;
  gap: var(--space-md);
  font-size: 13px;
  color: var(--color-text-secondary);

  span {
    display: flex;
    align-items: center;
    gap: 4px;
  }
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: var(--space-3xl) 0;
  color: var(--color-text-secondary);

  p {
    margin-top: var(--space-md);
  }

  .tips {
    font-size: 14px;
    margin-top: var(--space-xs);
  }
}

@media (max-width: 768px) {
  .advanced-form .form-row {
    grid-template-columns: 1fr;
  }
}
</style>
