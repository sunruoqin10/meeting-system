<template>
  <div class="user-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <h1>用户管理</h1>
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
          新增用户
        </el-button>
      </div>
    </div>

    <!-- 筛选 -->
    <div class="filter-bar">
      <el-input
        v-model="searchKeyword"
        placeholder="搜索用户..."
        class="search-input"
        :prefix-icon="Search"
        clearable
      />

      <el-select v-model="deptFilter" placeholder="筛选部门" clearable style="width: 140px;">
        <el-option label="技术研发部" value="d001" />
        <el-option label="市场部" value="d002" />
        <el-option label="销售部" value="d003" />
      </el-select>

      <el-select v-model="roleFilter" placeholder="筛选角色" clearable style="width: 140px;">
        <el-option label="普通用户" value="user" />
        <el-option label="部门管理员" value="dept_admin" />
        <el-option label="系统管理员" value="admin" />
      </el-select>

      <el-select v-model="statusFilter" placeholder="筛选状态" clearable style="width: 120px;">
        <el-option label="正常" value="active" />
        <el-option label="锁定" value="locked" />
        <el-option label="禁用" value="disabled" />
      </el-select>
    </div>

    <!-- 用户表格 -->
    <el-card shadow="never" class="table-card">
      <el-table :data="filteredList" border stripe v-loading="loading">
        <el-table-column label="用户" width="220">
          <template #default="{ row }">
            <div class="user-cell">
              <el-avatar :size="36" :src="row.avatar">
                {{ row.name?.charAt(0) }}
              </el-avatar>
              <div class="user-info">
                <span class="user-name">{{ row.name }}</span>
                <span class="user-email">{{ row.email }}</span>
              </div>
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="departmentName" label="部门" />

        <el-table-column label="角色" width="120">
          <template #default="{ row }">
            <el-tag :type="getRoleType(row.role)" size="small">
              {{ getRoleName(row.role) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)" size="small">
              {{ getStatusName(row.status) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="lastLoginTime" label="最后登录" width="180" />

        <el-table-column label="操作" width="200" align="center">
          <template #default="{ row }">
            <el-button type="primary" text size="small" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-dropdown @command="(cmd) => handleMoreCommand(cmd, row)">
              <el-button type="primary" text size="small">
                更多
                <el-icon class="el-icon--right"><ArrowDown /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="resetPassword">
                    <el-icon><Key /></el-icon>
                    重置密码
                  </el-dropdown-item>
                  <el-dropdown-item
                    :command="row.status === 'locked' ? 'unlock' : 'lock'"
                  >
                    <el-icon><Lock v-if="row.status !== 'locked'" /><Unlock v-else /></el-icon>
                    {{ row.status === 'locked' ? '解锁' : '锁定' }}
                  </el-dropdown-item>
                  <el-dropdown-item
                    command="delete"
                    divided
                    :disabled="row.role === 'admin'"
                  >
                    <el-icon><Delete /></el-icon>
                    删除
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
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

    <!-- 创建/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑用户' : '新增用户'"
      width="600px"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入姓名" />
        </el-form-item>

        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>

        <el-form-item label="手机号">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>

        <el-form-item label="部门" prop="departmentId">
          <el-select v-model="form.departmentId" placeholder="选择部门" style="width: 100%;">
            <el-option label="技术研发部" value="d001" />
            <el-option label="市场部" value="d002" />
            <el-option label="销售部" value="d003" />
          </el-select>
        </el-form-item>

        <el-form-item label="角色" prop="role">
          <el-radio-group v-model="form.role">
            <el-radio label="user">普通用户</el-radio>
            <el-radio label="dept_admin">部门管理员</el-radio>
            <el-radio label="admin">系统管理员</el-radio>
          </el-radio-group>
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
import { ref, computed, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Search, Plus, Upload, Download, ArrowDown,
  Key, Lock, Unlock, Delete
} from '@element-plus/icons-vue'
import { userList as mockUserList } from '@/mock/data'

const loading = ref(false)
const searchKeyword = ref('')
const deptFilter = ref('')
const roleFilter = ref('')
const statusFilter = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const dialogVisible = ref(false)
const isEdit = ref(false)

const userList = ref([...mockUserList])

const form = ref({
  id: '',
  name: '',
  email: '',
  phone: '',
  departmentId: '',
  role: 'user'
})

const rules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入有效的邮箱', trigger: 'blur' }
  ],
  departmentId: [{ required: true, message: '请选择部门', trigger: 'change' }]
}

const filteredList = computed(() => {
  let result = [...userList.value]

  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase()
    result = result.filter(u =>
      u.name.toLowerCase().includes(keyword) ||
      u.email.toLowerCase().includes(keyword)
    )
  }

  if (deptFilter.value) {
    result = result.filter(u => u.departmentId === deptFilter.value)
  }

  if (roleFilter.value) {
    result = result.filter(u => u.role === roleFilter.value)
  }

  if (statusFilter.value) {
    result = result.filter(u => u.status === statusFilter.value)
  }

  total.value = result.length
  return result
})

const getRoleType = (role) => {
  const types = { admin: 'danger', dept_admin: 'warning', user: '' }
  return types[role] || ''
}

const getRoleName = (role) => {
  const names = { admin: '系统管理员', dept_admin: '部门管理员', user: '普通用户' }
  return names[role] || role
}

const getStatusType = (status) => {
  const types = { active: 'success', locked: 'warning', disabled: 'info' }
  return types[status] || ''
}

const getStatusName = (status) => {
  const names = { active: '正常', locked: '锁定', disabled: '禁用' }
  return names[status] || status
}

const showCreateDialog = () => {
  isEdit.value = false
  form.value = { id: '', name: '', email: '', phone: '', departmentId: '', role: 'user' }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  form.value = { ...row }
  dialogVisible.value = true
}

const handleSubmit = () => {
  ElMessage.success(isEdit.value ? '编辑成功' : '创建成功')
  dialogVisible.value = false
}

const handleMoreCommand = async (command, row) => {
  switch (command) {
    case 'resetPassword':
      ElMessage.success(`密码已重置，发送至 ${row.email}`)
      break
    case 'lock':
      await ElMessageBox.confirm(`确定要锁定用户「${row.name}」吗？`, '锁定确认')
      row.status = 'locked'
      ElMessage.success('用户已锁定')
      break
    case 'unlock':
      row.status = 'active'
      ElMessage.success('用户已解锁')
      break
    case 'delete':
      try {
        await ElMessageBox.confirm(
          `确定要删除用户「${row.name}」吗？此操作不可恢复。`,
          '删除确认',
          { confirmButtonText: '删除', cancelButtonText: '取消', type: 'warning' }
        )
        const index = userList.value.findIndex(u => u.id === row.id)
        if (index > -1) userList.value.splice(index, 1)
        ElMessage.success('删除成功')
      } catch {
        // 取消
      }
      break
  }
}

const handleImport = () => ElMessage.info('导入功能开发中')
const handleExport = () => ElMessage.info('导出功能开发中')
</script>

<style lang="scss" scoped>
.user-manage {
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

  .search-input {
    width: 250px;
  }
}

.table-card {
  :deep(.el-card__body) {
    padding: 0;
  }
}

.user-cell {
  display: flex;
  align-items: center;
  gap: var(--space-sm);

  .user-info {
    display: flex;
    flex-direction: column;

    .user-name {
      font-weight: 500;
    }

    .user-email {
      font-size: 12px;
      color: var(--color-text-secondary);
    }
  }
}

.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  padding: var(--space-md);
  border-top: 1px solid var(--color-border);
}
</style>
