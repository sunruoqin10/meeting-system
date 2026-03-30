<template>
  <div class="statistics-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <h1>统计分析</h1>
      <div class="header-actions">
        <el-select v-model="timeRange" style="width: 140px;">
          <el-option label="本月" value="month" />
          <el-option label="本季度" value="quarter" />
          <el-option label="本年" value="year" />
          <el-option label="全部" value="all" />
        </el-select>
        <el-dropdown @command="handleExport">
          <el-button>
            <el-icon class="el-icon--left"><Download /></el-icon>
            导出
          </el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="excel">
                <el-icon><Document /></el-icon>
                导出为 Excel
              </el-dropdown-item>
              <el-dropdown-item command="pdf">
                <el-icon><Document /></el-icon>
                导出为 PDF
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>

    <!-- 统计概览 -->
    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-icon" style="background: #DBEAFE;">
          <el-icon :size="28" color="#3B82F6"><Document /></el-icon>
        </div>
        <div class="stat-content">
          <span class="stat-value">{{ stats.overview.totalMinutes }}</span>
          <span class="stat-label">总纪要数</span>
        </div>
      </div>

      <div class="stat-card">
        <div class="stat-icon" style="background: #D1FAE5;">
          <el-icon :size="28" color="#10B981"><Send /></el-icon>
        </div>
        <div class="stat-content">
          <span class="stat-value">{{ stats.overview.sentMinutes }}</span>
          <span class="stat-label">已发送</span>
        </div>
      </div>

      <div class="stat-card">
        <div class="stat-icon" style="background: #FEF3C7;">
          <el-icon :size="28" color="#F59E0B"><Check /></el-icon>
        </div>
        <div class="stat-content">
          <span class="stat-value">{{ stats.overview.completedTasks }}</span>
          <span class="stat-label">待办完成</span>
        </div>
      </div>

      <div class="stat-card">
        <div class="stat-icon" style="background: #EDE9FE;">
          <el-icon :size="28" color="#8B5CF6"><User /></el-icon>
        </div>
        <div class="stat-content">
          <span class="stat-value">{{ stats.overview.participatedUsers }}</span>
          <span class="stat-label">参与人数</span>
        </div>
      </div>
    </div>

    <!-- 图表区域 -->
    <div class="charts-grid">
      <!-- 趋势图 -->
      <el-card shadow="never" class="chart-card">
        <template #header>
          <span class="chart-title">
            <el-icon><TrendCharts /></el-icon>
            纪要趋势
          </span>
        </template>
        <div ref="trendChartRef" class="chart-container"></div>
      </el-card>

      <!-- 部门分布 -->
      <el-card shadow="never" class="chart-card">
        <template #header>
          <span class="chart-title">
            <el-icon><PieChart /></el-icon>
            部门分布
          </span>
        </template>
        <div ref="pieChartRef" class="chart-container"></div>
      </el-card>

      <!-- 任务效率 -->
      <el-card shadow="never" class="chart-card">
        <template #header>
          <span class="chart-title">
            <el-icon><Timer /></el-icon>
            任务效率
          </span>
        </template>
        <div class="efficiency-content">
          <div class="efficiency-item">
            <div class="efficiency-header">
              <span>完成率</span>
              <span class="efficiency-value">{{ stats.taskEfficiency.completionRate }}%</span>
            </div>
            <el-progress
              :percentage="stats.taskEfficiency.completionRate"
              :stroke-width="12"
              :color="progressColor"
            />
          </div>

          <div class="efficiency-item">
            <div class="efficiency-header">
              <span>平均完成时间</span>
              <span class="efficiency-value">{{ stats.taskEfficiency.averageHours }}h</span>
            </div>
            <el-progress
              :percentage="(stats.taskEfficiency.averageHours / 8) * 100"
              :stroke-width="12"
              :color="'#3B82F6'"
            />
          </div>

          <div class="efficiency-item">
            <div class="efficiency-header">
              <span>逾期率</span>
              <span class="efficiency-value danger">{{ stats.taskEfficiency.overdueRate }}%</span>
            </div>
            <el-progress
              :percentage="stats.taskEfficiency.overdueRate"
              :stroke-width="12"
              :color="'#EF4444'"
            />
          </div>
        </div>
      </el-card>

      <!-- 用户活跃度 -->
      <el-card shadow="never" class="chart-card">
        <template #header>
          <span class="chart-title">
            <el-icon><User /></el-icon>
            用户活跃度 TOP 5
          </span>
        </template>
        <div class="ranking-list">
          <div v-for="user in stats.topUsers" :key="user.rank" class="ranking-item">
            <div class="ranking-rank" :class="'rank-' + user.rank">
              {{ user.rank }}
            </div>
            <div class="ranking-info">
              <span class="ranking-name">{{ user.userName }}</span>
              <el-progress
                :percentage="(user.activityScore / 100) * 100"
                :stroke-width="8"
                :show-text="false"
                :color="getRankColor(user.rank)"
              />
            </div>
            <div class="ranking-score">{{ user.activityScore }}</div>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import {
  Download, Document, Send, Check, User, TrendCharts,
  PieChart, Timer
} from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import { statisticsData } from '@/mock/data'

const timeRange = ref('month')
const trendChartRef = ref(null)
const pieChartRef = ref(null)

const stats = reactive(statisticsData)

const progressColor = computed(() => {
  return stats.taskEfficiency.completionRate >= 80 ? '#10B981' :
         stats.taskEfficiency.completionRate >= 60 ? '#F59E0B' : '#EF4444'
})

const getRankColor = (rank) => {
  const colors = { 1: '#FFD700', 2: '#C0C0C0', 3: '#CD7F32', 4: '#6B7280', 5: '#6B7280' }
  return colors[rank] || '#6B7280'
}

const handleExport = (command) => {
  ElMessage.success(`正在导出 ${command === 'excel' ? 'Excel' : 'PDF'} 文件...`)
}

const initTrendChart = () => {
  if (!trendChartRef.value) return

  const chart = echarts.init(trendChartRef.value)

  const option = {
    tooltip: { trigger: 'axis' },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: {
      type: 'category',
      data: stats.trend.map(t => t.month),
      axisLine: { lineStyle: { color: '#E2E8F0' } },
      axisLabel: { color: '#6B7280' }
    },
    yAxis: {
      type: 'value',
      axisLine: { show: false },
      axisLabel: { color: '#6B7280' },
      splitLine: { lineStyle: { color: '#F3F4F6' } }
    },
    series: [{
      type: 'line',
      data: stats.trend.map(t => t.count),
      smooth: true,
      areaStyle: { color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
        { offset: 0, color: 'rgba(124, 58, 237, 0.3)' },
        { offset: 1, color: 'rgba(124, 58, 237, 0)' }
      ]) },
      lineStyle: { color: '#7C3AED', width: 3 },
      itemStyle: { color: '#7C3AED' }
    }]
  }

  chart.setOption(option)
}

const initPieChart = () => {
  if (!pieChartRef.value) return

  const chart = echarts.init(pieChartRef.value)

  const option = {
    tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
    legend: {
      orient: 'vertical',
      right: '5%',
      top: 'center',
      textStyle: { color: '#6B7280' }
    },
    series: [{
      type: 'pie',
      radius: ['40%', '70%'],
      center: ['35%', '50%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 8,
        borderColor: '#fff',
        borderWidth: 2
      },
      label: { show: false },
      emphasis: {
        label: { show: true, fontSize: 14, fontWeight: 'bold' }
      },
      data: stats.departmentDistribution.map((d, i) => ({
        value: d.count,
        name: d.department,
        itemStyle: { color: ['#8B5CF6', '#3B82F6', '#10B981', '#F97316'][i] }
      }))
    }]
  }

  chart.setOption(option)
}

onMounted(() => {
  setTimeout(() => {
    initTrendChart()
    initPieChart()
  }, 100)
})
</script>

<style lang="scss" scoped>
.statistics-page {
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

  .header-actions {
    display: flex;
    gap: var(--space-sm);
  }
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
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

.charts-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: var(--space-lg);
}

.chart-card {
  :deep(.el-card__header) {
    padding: var(--space-md) var(--space-lg);
    border-bottom: 1px solid var(--color-border);
  }

  :deep(.el-card__body) {
    padding: var(--space-lg);
  }
}

.chart-title {
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: var(--space-sm);

  .el-icon {
    color: var(--color-primary);
  }
}

.chart-container {
  height: 280px;
}

.efficiency-content {
  display: flex;
  flex-direction: column;
  gap: var(--space-lg);
}

.efficiency-item {
  .efficiency-header {
    display: flex;
    justify-content: space-between;
    margin-bottom: var(--space-sm);
    font-size: 14px;
    color: var(--color-text-secondary);
  }

  .efficiency-value {
    font-weight: 600;
    color: var(--color-text);

    &.danger {
      color: var(--color-danger);
    }
  }
}

.ranking-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-md);
}

.ranking-item {
  display: flex;
  align-items: center;
  gap: var(--space-md);
}

.ranking-rank {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 14px;
  color: white;

  &.rank-1 { background: #FFD700; }
  &.rank-2 { background: #C0C0C0; }
  &.rank-3 { background: #CD7F32; }
  &.rank-4, &.rank-5 { background: #6B7280; }
}

.ranking-info {
  flex: 1;

  .ranking-name {
    display: block;
    font-size: 14px;
    margin-bottom: 4px;
  }
}

.ranking-score {
  font-family: var(--font-code);
  font-weight: 600;
  color: var(--color-primary);
}

@media (max-width: 1024px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .charts-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }
}
</style>
