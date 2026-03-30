<template>
  <div class="version-history">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <el-button text @click="goBack">
          <el-icon class="el-icon--left"><ArrowLeft /></el-icon>
          返回
        </el-button>
        <h1>版本历史 - {{ minuteTitle }}</h1>
      </div>
    </div>

    <!-- 版本列表 -->
    <div class="version-section">
      <div class="version-header">
        <h2>版本列表</h2>
      </div>

      <div class="version-timeline">
        <div
          v-for="(version, index) in versionList"
          :key="version.id"
          class="version-item"
          :class="{ current: version.isCurrent }"
        >
          <div class="timeline-connector" v-if="index > 0"></div>
          <div class="timeline-node">
            <div class="node-dot" :class="{ current: version.isCurrent }"></div>
          </div>

          <el-card shadow="never" class="version-card">
            <div class="version-card-header">
              <div class="version-info">
                <el-tag v-if="version.isCurrent" type="primary" effect="dark">
                  {{ version.versionNumber }} (当前)
                </el-tag>
                <el-tag v-else type="info">
                  {{ version.versionNumber }}
                </el-tag>
                <span class="version-time">{{ version.modifyTime }}</span>
              </div>
              <div class="version-modifier">
                <el-icon><User /></el-icon>
                {{ version.modifierName }}
              </div>
            </div>

            <div class="version-remark">
              <el-icon><InfoFilled /></el-icon>
              {{ version.remark }}
            </div>

            <div class="version-actions">
              <el-button type="primary" text size="small" @click="handleView(version)">
                <el-icon><Eye /></el-icon>
                查看
              </el-button>
              <el-button type="primary" text size="small" @click="handleCompare(version)">
                <el-icon><Connection /></el-icon>
                对比
              </el-button>
              <el-button
                v-if="!version.isCurrent"
                type="warning"
                text
                size="small"
                @click="handleRollback(version)"
              >
                <el-icon><RefreshLeft /></el-icon>
                回滚
              </el-button>
            </div>
          </el-card>
        </div>
      </div>
    </div>

    <!-- 版本对比视图 -->
    <el-dialog
      v-model="compareDialogVisible"
      title="版本对比"
      width="900px"
      :fullscreen="compareFullscreen"
    >
      <div class="compare-toolbar">
        <div class="compare-versions">
          <el-select v-model="compareFrom" placeholder="选择版本">
            <el-option
              v-for="v in versionList"
              :key="v.id"
              :label="v.versionNumber"
              :value="v.id"
            />
          </el-select>
          <el-icon><Right /></el-icon>
          <el-select v-model="compareTo" placeholder="选择版本">
            <el-option
              v-for="v in versionList"
              :key="v.id"
              :label="v.versionNumber"
              :value="v.id"
            />
          </el-select>
        </div>
        <el-button text @click="compareFullscreen = !compareFullscreen">
          <el-icon v-if="compareFullscreen"><Close /></el-icon>
          <el-icon v-else><FullScreen /></el-icon>
        </el-button>
      </div>

      <div class="compare-content" v-if="compareFrom && compareTo">
        <div class="compare-panel">
          <div class="panel-header">
            <span>{{ getVersionNumber(compareFrom) }}</span>
          </div>
          <div class="panel-content" v-html="getCompareDiff('from')"></div>
        </div>
        <div class="compare-panel">
          <div class="panel-header target">
            <span>{{ getVersionNumber(compareTo) }}</span>
          </div>
          <div class="panel-content" v-html="getCompareDiff('to')"></div>
        </div>
      </div>

      <div v-else class="compare-placeholder">
        <el-icon :size="48"><Connection /></el-icon>
        <p>请选择两个版本进行对比</p>
      </div>
    </el-dialog>

    <!-- 版本查看弹窗 -->
    <el-dialog v-model="viewDialogVisible" :title="'查看版本 ' + viewingVersion?.versionNumber" width="800px">
      <div class="version-view-content" v-html="viewingVersion?.content"></div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  ArrowLeft, Eye, Connection, RefreshLeft, Right,
  InfoFilled, User, Close, FullScreen
} from '@element-plus/icons-vue'
import { versionHistory as mockVersionHistory, minuteDetail } from '@/mock/data'

const router = useRouter()
const route = useRoute()

const minuteTitle = ref(minuteDetail.title)
const versionList = ref([...mockVersionHistory])

const compareDialogVisible = ref(false)
const compareFullscreen = ref(false)
const compareFrom = ref('')
const compareTo = ref('')
const viewDialogVisible = ref(false)
const viewingVersion = ref(null)

const getVersionNumber = (id) => {
  const version = versionList.value.find(v => v.id === id)
  return version?.versionNumber || ''
}

const getCompareDiff = (type) => {
  // 模拟差异对比内容
  const fromContent = `
    <h2>一、会议背景</h2>
    <p>本次会议主要讨论Q1季度的产品规划和技术方案。</p>
    <h2>二、讨论事项</h2>
    <ul>
      <li>核心功能优先级排序</li>
      <li><span class="diff-delete">旧的技术方案</span></li>
      <li><span class="diff-insert">新的技术方案选型</span></li>
      <li>团队分工调整</li>
    </ul>
  `

  const toContent = `
    <h2>一、会议背景</h2>
    <p>本次会议主要讨论Q1季度的产品规划和技术方案。</p>
    <h2>二、讨论事项</h2>
    <ul>
      <li>核心功能优先级排序</li>
      <li><span class="diff-insert">新的技术方案选型</span></li>
      <li>团队分工调整</li>
    </ul>
  `

  return type === 'from' ? fromContent : toContent
}

const goBack = () => router.back()

const handleView = (version) => {
  viewingVersion.value = version
  viewDialogVisible.value = true
}

const handleCompare = (version) => {
  // 自动选择当前版本和选中版本进行对比
  const currentVersion = versionList.value.find(v => v.isCurrent)
  if (currentVersion) {
    compareFrom.value = currentVersion.id
    compareTo.value = version.id
  }
  compareDialogVisible.value = true
}

const handleRollback = async (version) => {
  try {
    await ElMessageBox.confirm(
      `确定要回滚到版本 ${version.versionNumber} 吗？回滚将创建新版本，内容与 ${version.versionNumber} 相同。`,
      '确认回滚',
      {
        confirmButtonText: '确认回滚',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    // 模拟回滚操作
    ElMessage.success('回滚成功，已创建新版本')
    router.push(`/minutes/${route.params.id}`)
  } catch {
    // 取消
  }
}

onMounted(() => {
  // 加载版本数据
})
</script>

<style lang="scss" scoped>
.version-history {
  max-width: 900px;
  margin: 0 auto;
}

.page-header {
  margin-bottom: var(--space-lg);

  .header-left {
    display: flex;
    align-items: center;
    gap: var(--space-md);

    h1 {
      font-family: var(--font-code);
      font-size: 20px;
      font-weight: 600;
      color: var(--color-text);
    }
  }
}

.version-section {
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  padding: var(--space-lg);
  box-shadow: var(--shadow-sm);
}

.version-header {
  margin-bottom: var(--space-lg);

  h2 {
    font-family: var(--font-code);
    font-size: 16px;
    font-weight: 600;
    color: var(--color-text);
  }
}

.version-timeline {
  position: relative;
}

.version-item {
  position: relative;
  padding-left: 40px;
  padding-bottom: var(--space-lg);

  &:last-child {
    padding-bottom: 0;
  }

  &.current {
    .node-dot {
      background: var(--color-primary);
      border-color: var(--color-primary);
    }
  }
}

.timeline-connector {
  position: absolute;
  left: 7px;
  top: -20px;
  width: 2px;
  height: 20px;
  background: var(--color-border);
}

.timeline-node {
  position: absolute;
  left: 0;
  top: 4px;
}

.node-dot {
  width: 16px;
  height: 16px;
  border-radius: 50%;
  background: var(--color-surface);
  border: 2px solid var(--color-border);
  transition: all var(--transition-fast);

  &.current {
    box-shadow: 0 0 0 4px rgba(124, 58, 237, 0.2);
  }
}

.version-card {
  :deep(.el-card__body) {
    padding: var(--space-md);
  }
}

.version-card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--space-sm);
}

.version-info {
  display: flex;
  align-items: center;
  gap: var(--space-md);

  .version-time {
    font-size: 13px;
    color: var(--color-text-secondary);
  }
}

.version-modifier {
  display: flex;
  align-items: center;
  gap: var(--space-xs);
  font-size: 13px;
  color: var(--color-text-secondary);
}

.version-remark {
  display: flex;
  align-items: center;
  gap: var(--space-xs);
  font-size: 13px;
  color: var(--color-text-secondary);
  margin-bottom: var(--space-md);
  padding: var(--space-sm);
  background: var(--color-background);
  border-radius: var(--radius-sm);
}

.version-actions {
  display: flex;
  gap: var(--space-sm);
}

.compare-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--space-lg);
  padding: var(--space-md);
  background: var(--color-background);
  border-radius: var(--radius-md);

  .compare-versions {
    display: flex;
    align-items: center;
    gap: var(--space-md);
  }
}

.compare-content {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: var(--space-md);
  min-height: 400px;
}

.compare-panel {
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  overflow: hidden;

  .panel-header {
    padding: var(--space-sm) var(--space-md);
    background: var(--color-background);
    font-weight: 600;
    font-family: var(--font-code);
    border-bottom: 1px solid var(--color-border);

    &.target {
      background: rgba(124, 58, 237, 0.1);
      color: var(--color-primary);
    }
  }

  .panel-content {
    padding: var(--space-md);
    font-size: 14px;
    line-height: 1.8;
    max-height: 500px;
    overflow-y: auto;

    :deep(.diff-insert) {
      background: #D1FAE5;
      color: #166534;
      padding: 2px 4px;
      border-radius: 2px;
    }

    :deep(.diff-delete) {
      background: #FEE2E2;
      color: #991B1B;
      text-decoration: line-through;
      padding: 2px 4px;
      border-radius: 2px;
    }
  }
}

.compare-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 400px;
  color: var(--color-text-secondary);

  .el-icon {
    margin-bottom: var(--space-md);
  }
}

.version-view-content {
  padding: var(--space-md);
  font-size: 14px;
  line-height: 1.8;
  max-height: 500px;
  overflow-y: auto;
}

@media (max-width: 768px) {
  .compare-content {
    grid-template-columns: 1fr;
  }
}
</style>
