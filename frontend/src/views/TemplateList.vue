<template>
  <div class="template-list">
    <!-- 页面头部 -->
    <div class="page-header">
      <h1>模板管理</h1>
      <div class="header-actions">
        <el-button type="primary" @click="goToCreate">
          <el-icon class="el-icon--left"><Plus /></el-icon>
          创建模板
        </el-button>
      </div>
    </div>

    <!-- 筛选标签 -->
    <div class="filter-tabs">
      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane label="全部" name="all" />
        <el-tab-pane label="预置模板" name="preset" />
        <el-tab-pane label="我的模板" name="custom" />
        <el-tab-pane label="团队共享" name="shared" />
      </el-tabs>
    </div>

    <!-- 模板列表 -->
    <div class="templates-grid">
      <div
        v-for="template in filteredList"
        :key="template.id"
        class="template-card"
        @click="handleUseTemplate(template)"
      >
        <div class="card-header">
          <div class="template-icon">
            <el-icon :size="40" color="var(--color-primary)">
              <component :is="template.icon" />
            </el-icon>
          </div>
          <el-tag
            :type="getTemplateTagType(template.type)"
            size="small"
            class="type-tag"
          >
            {{ getTemplateTypeName(template.type) }}
          </el-tag>
        </div>

        <div class="card-body">
          <h3 class="template-name">{{ template.name }}</h3>
          <p class="template-desc">{{ template.description }}</p>
          <div class="template-meta">
            <span>
              <el-icon><User /></el-icon>
              {{ template.creatorName }}
            </span>
            <span>
              <el-icon><Document /></el-icon>
              已使用 {{ template.usageCount }} 次
            </span>
          </div>
        </div>

        <div class="card-actions">
          <el-button type="primary" size="small" @click.stop="handleUseTemplate(template)">
            使用
          </el-button>
          <el-button
            v-if="template.type !== 'preset'"
            type="info"
            text
            size="small"
            @click.stop="handleEdit(template)"
          >
            编辑
          </el-button>
          <el-dropdown v-if="template.type !== 'preset'" @command="(cmd) => handleMoreCommand(cmd, template)">
            <el-button type="info" text size="small" @click.stop>
              <el-icon><More /></el-icon>
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="copy">
                  <el-icon><DocumentCopy /></el-icon>
                  复制
                </el-dropdown-item>
                <el-dropdown-item command="share">
                  <el-icon><Share /></el-icon>
                  分享
                </el-dropdown-item>
                <el-dropdown-item command="delete" divided>
                  <el-icon><Delete /></el-icon>
                  删除
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>

      <el-empty v-if="filteredList.length === 0" description="暂无模板" />
    </div>

    <!-- 创建模板对话框 -->
    <el-dialog v-model="createDialogVisible" title="创建模板" width="600px">
      <el-form ref="formRef" :model="templateForm" :rules="rules" label-width="80px">
        <el-form-item label="模板名称" prop="name">
          <el-input v-model="templateForm.name" placeholder="请输入模板名称" />
        </el-form-item>

        <el-form-item label="模板描述" prop="description">
          <el-input
            v-model="templateForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入模板描述"
          />
        </el-form-item>

        <el-form-item label="模板类型">
          <el-radio-group v-model="templateForm.type">
            <el-radio label="custom">我的模板</el-radio>
            <el-radio label="shared">团队共享</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item v-if="templateForm.type === 'shared'" label="共享范围">
          <el-select v-model="templateForm.shareScope">
            <el-option label="本部门" value="department" />
            <el-option label="全公司" value="company" />
          </el-select>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCreateSubmit">创建</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Plus, User, Document, More, DocumentCopy, Share, Delete,
  Check, Calendar, ChatDotRound, Promotion, Files
} from '@element-plus/icons-vue'
import { templateList as mockTemplateList } from '@/mock/data'

const router = useRouter()

const activeTab = ref('all')
const templateList = ref([...mockTemplateList])
const createDialogVisible = ref(false)

const templateForm = ref({
  name: '',
  description: '',
  type: 'custom',
  shareScope: 'department'
})

const rules = {
  name: [{ required: true, message: '请输入模板名称', trigger: 'blur' }],
  description: [{ required: true, message: '请输入模板描述', trigger: 'blur' }]
}

const filteredList = computed(() => {
  if (activeTab.value === 'all') {
    return templateList.value
  }
  return templateList.value.filter(t => t.type === activeTab.value)
})

const getTemplateTagType = (type) => {
  const types = {
    preset: '',
    custom: 'warning',
    shared: 'success'
  }
  return types[type] || ''
}

const getTemplateTypeName = (type) => {
  const names = {
    preset: '预置模板',
    custom: '我的模板',
    shared: '团队共享'
  }
  return names[type] || type
}

const goToCreate = () => {
  createDialogVisible.value = true
}

const handleTabChange = () => {
  // 重新筛选
}

const handleUseTemplate = (template) => {
  router.push({ path: '/minutes/create', query: { templateId: template.id } })
  ElMessage.success(`已选择模板「${template.name}」`)
}

const handleEdit = (template) => {
  ElMessage.info('模板编辑功能开发中')
}

const handleMoreCommand = async (command, template) => {
  switch (command) {
    case 'copy':
      ElMessage.success('模板已复制')
      break
    case 'share':
      ElMessage.success('分享链接已复制')
      break
    case 'delete':
      try {
        await ElMessageBox.confirm(
          `确定要删除模板「${template.name}」吗？`,
          '删除确认',
          { confirmButtonText: '删除', cancelButtonText: '取消', type: 'warning' }
        )
        const index = templateList.value.findIndex(t => t.id === template.id)
        if (index > -1) {
          templateList.value.splice(index, 1)
        }
        ElMessage.success('删除成功')
      } catch {
        // 取消
      }
      break
  }
}

const handleCreateSubmit = () => {
  if (!templateForm.value.name) {
    ElMessage.warning('请输入模板名称')
    return
  }

  ElMessage.success('模板创建成功')
  createDialogVisible.value = false
  templateForm.value = {
    name: '',
    description: '',
    type: 'custom',
    shareScope: 'department'
  }
}
</script>

<style lang="scss" scoped>
.template-list {
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
  background: var(--color-surface);
  padding: var(--space-md);
  border-radius: var(--radius-lg);
  margin-bottom: var(--space-lg);
  box-shadow: var(--shadow-sm);
}

.templates-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: var(--space-lg);
}

.template-card {
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  padding: var(--space-lg);
  cursor: pointer;
  transition: all var(--transition-base);

  &:hover {
    border-color: var(--color-primary);
    box-shadow: var(--shadow-md);
    transform: translateY(-4px);
  }
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: var(--space-md);
}

.template-icon {
  width: 64px;
  height: 64px;
  background: var(--color-background);
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
}

.type-tag {
  font-size: 12px;
}

.card-body {
  margin-bottom: var(--space-md);
}

.template-name {
  font-size: 16px;
  font-weight: 600;
  color: var(--color-text);
  margin-bottom: var(--space-sm);
}

.template-desc {
  font-size: 13px;
  color: var(--color-text-secondary);
  line-height: 1.5;
  margin-bottom: var(--space-md);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.template-meta {
  display: flex;
  gap: var(--space-md);
  font-size: 12px;
  color: var(--color-text-secondary);

  span {
    display: flex;
    align-items: center;
    gap: 4px;
  }
}

.card-actions {
  display: flex;
  gap: var(--space-sm);
  padding-top: var(--space-md);
  border-top: 1px solid var(--color-border);
}
</style>
