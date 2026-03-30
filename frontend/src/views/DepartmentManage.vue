<template>
  <div class="department-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <h1>部门管理</h1>
      <div class="header-actions">
        <el-button @click="handleImport">
          <el-icon class="el-icon--left"><Upload /></el-icon>
          导入
        </el-button>
        <el-button @click="handleExport">
          <el-icon class="el-icon--left"><Download /></el-icon>
          导出
        </el-button>
        <el-button type="primary" @click="showCreateDialog">
          <el-icon class="el-icon--left"><Plus /></el-icon>
          新增部门
        </el-button>
      </div>
    </div>

    <!-- 搜索 -->
    <el-input
      v-model="searchKeyword"
      placeholder="搜索部门..."
      class="search-input"
      :prefix-icon="Search"
      clearable
    />

    <!-- 视图切换 -->
    <div class="view-toggle">
      <el-radio-group v-model="viewMode">
        <el-radio-button label="tree">
          <el-icon><Grid /></el-icon>
          树形视图
        </el-radio-button>
        <el-radio-button label="table">
          <el-icon><List /></el-icon>
          表格视图
        </el-radio-button>
      </el-radio-group>
    </div>

    <!-- 树形视图 -->
    <div v-if="viewMode === 'tree'" class="tree-view">
      <el-tree
        :data="departmentTree"
        :props="treeProps"
        node-key="id"
        default-expand-all
        :expand-on-click-node="false"
      >
        <template #default="{ node, data }">
          <div class="tree-node">
            <div class="node-content">
              <el-icon><Office /></el-icon>
              <span class="node-label">{{ data.name }}</span>
              <el-tag size="small" type="info">{{ data.userCount }}人</el-tag>
            </div>
            <div class="node-actions">
              <el-button type="primary" text size="small" @click="handleEdit(data)">
                编辑
              </el-button>
              <el-button type="primary" text size="small" @click="handleAddChild(data)">
                添加下级
              </el-button>
              <el-button
                v-if="data.userCount === 0"
                type="danger"
                text
                size="small"
                @click="handleDelete(data)"
              >
                删除
              </el-button>
            </div>
          </div>
        </template>
      </el-tree>
    </div>

    <!-- 表格视图 -->
    <el-card v-else shadow="never" class="table-card">
      <el-table :data="filteredList" border stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="部门名称" />
        <el-table-column prop="parentName" label="上级部门" />
        <el-table-column prop="userCount" label="人数" width="100" align="center" />
        <el-table-column prop="description" label="描述" />
        <el-table-column prop="sort" label="排序" width="80" align="center" />
        <el-table-column label="操作" width="200" align="center">
          <template #default="{ row }">
            <el-button type="primary" text size="small" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button type="danger" text size="small" @click="handleDelete(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 创建/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑部门' : '新增部门'"
      width="500px"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="部门名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入部门名称" />
        </el-form-item>

        <el-form-item label="上级部门" prop="parentId">
          <el-cascader
            v-model="form.parentId"
            :options="cascaderOptions"
            :props="{ checkStrictly: true, value: 'id', label: 'name' }"
            placeholder="选择上级部门（留空为顶级）"
            clearable
            style="width: 100%;"
          />
        </el-form-item>

        <el-form-item label="部门描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="3"
            placeholder="请输入部门描述"
          />
        </el-form-item>

        <el-form-item label="排序">
          <el-input-number v-model="form.sort" :min="0" :max="999" />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Search, Plus, Upload, Download, Grid, List, Office
} from '@element-plus/icons-vue'
import { departmentTree as mockTree, departmentList as mockList } from '@/mock/data'

const searchKeyword = ref('')
const viewMode = ref('tree')
const dialogVisible = ref(false)
const isEdit = ref(false)

const departmentTree = ref([...mockTree])
const departmentList = ref([...mockList])

const treeProps = {
  children: 'children',
  label: 'name'
}

const form = ref({
  id: '',
  name: '',
  parentId: '',
  description: '',
  sort: 0
})

const rules = {
  name: [{ required: true, message: '请输入部门名称', trigger: 'blur' }]
}

const cascaderOptions = computed(() => {
  const convert = (nodes) => nodes.map(node => ({
    id: node.id,
    name: node.name,
    children: node.children ? convert(node.children) : []
  }))
  return convert(departmentTree.value)
})

const filteredList = computed(() => {
  if (!searchKeyword.value) return departmentList.value
  const keyword = searchKeyword.value.toLowerCase()
  return departmentList.value.filter(d =>
    d.name.toLowerCase().includes(keyword) ||
    d.description.toLowerCase().includes(keyword)
  )
})

const showCreateDialog = () => {
  isEdit.value = false
  form.value = { id: '', name: '', parentId: '', description: '', sort: 0 }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  form.value = { ...row }
  dialogVisible.value = true
}

const handleAddChild = (parent) => {
  isEdit.value = false
  form.value = { id: '', name: '', parentId: parent.id, description: '', sort: 0 }
  dialogVisible.value = true
}

const handleDelete = async (row) => {
  if (row.userCount > 0) {
    ElMessage.warning('该部门下有用户，无法删除')
    return
  }

  try {
    await ElMessageBox.confirm(
      `确定要删除部门「${row.name}」吗？`,
      '删除确认',
      { confirmButtonText: '删除', cancelButtonText: '取消', type: 'warning' }
    )
    ElMessage.success('删除成功')
  } catch {
    // 取消
  }
}

const handleSubmit = () => {
  if (!form.value.name) {
    ElMessage.warning('请输入部门名称')
    return
  }

  ElMessage.success(isEdit.value ? '编辑成功' : '创建成功')
  dialogVisible.value = false
}

const handleImport = () => ElMessage.info('导入功能开发中')
const handleExport = () => ElMessage.info('导出功能开发中')
</script>

<style lang="scss" scoped>
.department-manage {
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

.search-input {
  max-width: 300px;
  margin-bottom: var(--space-md);
}

.view-toggle {
  margin-bottom: var(--space-lg);
}

.tree-view {
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  padding: var(--space-lg);
  box-shadow: var(--shadow-sm);
}

.tree-node {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  padding: var(--space-xs) 0;

  .node-content {
    display: flex;
    align-items: center;
    gap: var(--space-sm);

    .el-icon {
      color: var(--color-primary);
    }

    .node-label {
      font-weight: 500;
    }
  }

  .node-actions {
    display: flex;
    gap: var(--space-xs);
  }
}

.table-card {
  :deep(.el-card__body) {
    padding: 0;
  }
}

:deep(.el-tree-node__content) {
  padding: var(--space-sm) var(--space-md);
}
</style>
