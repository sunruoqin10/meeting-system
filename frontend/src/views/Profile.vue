<template>
  <div class="profile-page">
    <div class="profile-layout">
      <!-- 左侧头像 -->
      <div class="profile-left">
        <el-card shadow="never" class="avatar-card">
          <div class="avatar-wrapper">
            <el-avatar :size="120" :src="userInfo.avatar">
              {{ userInfo.name?.charAt(0) }}
            </el-avatar>
            <el-button class="upload-btn" text @click="handleUploadAvatar">
              <el-icon><Upload /></el-icon>
            </el-button>
          </div>

          <div class="user-info">
            <h2>{{ userInfo.name }}</h2>
            <el-tag :type="getRoleType(userInfo.role)">
              {{ getRoleName(userInfo.role) }}
            </el-tag>
          </div>

          <div class="info-list">
            <div class="info-item">
              <el-icon><Location /></el-icon>
              <span>{{ userInfo.departmentName }}</span>
            </div>
            <div class="info-item">
              <el-icon><Calendar /></el-icon>
              <span>最后登录: {{ userInfo.lastLoginTime }}</span>
            </div>
          </div>
        </el-card>
      </div>

      <!-- 右侧信息 -->
      <div class="profile-right">
        <!-- 基本信息 -->
        <el-card shadow="never" class="info-card">
          <template #header>
            <div class="card-header">
              <span>基本信息</span>
              <el-button type="primary" text @click="toggleEdit">
                <el-icon v-if="!editing"><Edit /></el-icon>
                <el-icon v-else><Check /></el-icon>
                {{ editing ? '完成' : '编辑资料' }}
              </el-button>
            </div>
          </template>

          <el-form :model="form" label-width="100px" :disabled="!editing">
            <el-form-item label="姓名">
              <el-input v-model="form.name" />
            </el-form-item>

            <el-form-item label="邮箱">
              <el-input v-model="form.email" disabled />
            </el-form-item>

            <el-form-item label="手机号">
              <el-input v-model="form.phone" />
            </el-form-item>

            <el-form-item label="部门">
              <el-select v-model="form.departmentId" style="width: 100%;">
                <el-option label="技术研发部" value="d001" />
                <el-option label="市场部" value="d002" />
                <el-option label="销售部" value="d003" />
              </el-select>
            </el-form-item>

            <el-form-item label="角色">
              <el-input :value="getRoleName(userInfo.role)" disabled />
            </el-form-item>
          </el-form>
        </el-card>

        <!-- 安全设置 -->
        <el-card shadow="never" class="info-card">
          <template #header>
            <span>安全设置</span>
          </template>

          <div class="security-list">
            <div class="security-item">
              <div class="security-info">
                <el-icon><Lock /></el-icon>
                <div>
                  <span class="security-title">登录密码</span>
                  <span class="security-desc">定期修改密码可以保护账户安全</span>
                </div>
              </div>
              <el-button @click="showPasswordDialog">修改</el-button>
            </div>

            <div class="security-item">
              <div class="security-info">
                <el-icon><Phone /></el-icon>
                <div>
                  <span class="security-title">绑定手机</span>
                  <span class="security-desc">已绑定: {{ userInfo.phone }}</span>
                </div>
              </div>
              <el-button @click="showPhoneDialog">更换</el-button>
            </div>

            <div class="security-item">
              <div class="security-info">
                <el-icon><Bell /></el-icon>
                <div>
                  <span class="security-title">消息通知</span>
                  <span class="security-desc">接收系统通知和邮件</span>
                </div>
              </div>
              <el-switch v-model="notificationEnabled" />
            </div>
          </div>
        </el-card>
      </div>
    </div>

    <!-- 修改密码对话框 -->
    <el-dialog v-model="passwordDialogVisible" title="修改密码" width="500px">
      <el-form ref="passwordFormRef" :model="passwordForm" :rules="passwordRules" label-width="100px">
        <el-form-item label="当前密码" prop="oldPassword">
          <el-input v-model="passwordForm.oldPassword" type="password" show-password />
        </el-form-item>

        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="passwordForm.newPassword" type="password" show-password />
          <div class="password-tip">
            密码至少8位，包含大小写字母、数字、特殊字符
          </div>
        </el-form-item>

        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="passwordForm.confirmPassword" type="password" show-password />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="passwordDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handlePasswordChange">确认修改</el-button>
      </template>
    </el-dialog>

    <!-- 修改手机对话框 -->
    <el-dialog v-model="phoneDialogVisible" title="更换手机号" width="500px">
      <el-form label-width="100px">
        <el-form-item label="新手机号">
          <el-input v-model="newPhone" placeholder="请输入新手机号" />
        </el-form-item>
        <el-form-item label="验证码">
          <el-input v-model="verifyCode" placeholder="请输入验证码" style="width: 60%;">
            <template #append>
              <el-button :disabled="codeCooldown > 0" @click="sendVerifyCode">
                {{ codeCooldown > 0 ? `${codeCooldown}s` : '获取验证码' }}
              </el-button>
            </template>
          </el-input>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="phoneDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handlePhoneChange">确认更换</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import {
  Upload, Edit, Check, Lock, Phone, Bell, Location, Calendar
} from '@element-plus/icons-vue'
import { currentUser } from '@/mock/data'

const userInfo = ref(currentUser)
const editing = ref(false)
const notificationEnabled = ref(true)
const passwordDialogVisible = ref(false)
const phoneDialogVisible = ref(false)
const codeCooldown = ref(0)

const form = reactive({
  name: '',
  email: '',
  phone: '',
  departmentId: ''
})

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const passwordRules = {
  oldPassword: [{ required: true, message: '请输入当前密码', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 8, message: '密码至少8位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== passwordForm.newPassword) {
          callback(new Error('两次输入密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

const newPhone = ref('')
const verifyCode = ref('')

const getRoleType = (role) => {
  const types = { admin: 'danger', dept_admin: 'warning', user: '' }
  return types[role] || ''
}

const getRoleName = (role) => {
  const names = { admin: '系统管理员', dept_admin: '部门管理员', user: '普通用户' }
  return names[role] || role
}

const toggleEdit = () => {
  if (editing.value) {
    // 保存
    ElMessage.success('资料更新成功')
  }
  editing.value = !editing.value
}

const handleUploadAvatar = () => {
  ElMessage.info('头像上传功能开发中')
}

const showPasswordDialog = () => {
  passwordDialogVisible.value = true
}

const showPhoneDialog = () => {
  phoneDialogVisible.value = true
}

const handlePasswordChange = () => {
  ElMessage.success('密码修改成功，请重新登录')
  passwordDialogVisible.value = false
}

const sendVerifyCode = () => {
  codeCooldown.value = 60
  const timer = setInterval(() => {
    codeCooldown.value--
    if (codeCooldown.value <= 0) {
      clearInterval(timer)
    }
  }, 1000)
  ElMessage.success('验证码已发送')
}

const handlePhoneChange = () => {
  if (!newPhone.value || !verifyCode.value) {
    ElMessage.warning('请填写完整信息')
    return
  }
  ElMessage.success('手机号更换成功')
  phoneDialogVisible.value = false
}

onMounted(() => {
  Object.assign(form, {
    name: userInfo.value.name,
    email: userInfo.value.email,
    phone: userInfo.value.phone,
    departmentId: userInfo.value.departmentId
  })
})
</script>

<style lang="scss" scoped>
.profile-page {
  max-width: 1000px;
  margin: 0 auto;
}

.profile-layout {
  display: grid;
  grid-template-columns: 280px 1fr;
  gap: var(--space-lg);
}

.profile-left {
  .avatar-card {
    :deep(.el-card__body) {
      display: flex;
      flex-direction: column;
      align-items: center;
      padding: var(--space-xl);
    }
  }
}

.avatar-wrapper {
  position: relative;
  margin-bottom: var(--space-md);

  .upload-btn {
    position: absolute;
    bottom: 0;
    right: 0;
    padding: 8px;
    border-radius: 50%;
    background: var(--color-surface);
    border: 1px solid var(--color-border);
  }
}

.user-info {
  text-align: center;
  margin-bottom: var(--space-lg);

  h2 {
    font-size: 18px;
    font-weight: 600;
    margin-bottom: var(--space-sm);
  }
}

.info-list {
  width: 100%;
}

.info-item {
  display: flex;
  align-items: center;
  gap: var(--space-sm);
  padding: var(--space-sm) 0;
  font-size: 14px;
  color: var(--color-text-secondary);
  border-top: 1px solid var(--color-border);

  .el-icon {
    color: var(--color-primary);
  }
}

.profile-right {
  display: flex;
  flex-direction: column;
  gap: var(--space-lg);
}

.info-card {
  :deep(.el-card__header) {
    padding: var(--space-md) var(--space-lg);
    border-bottom: 1px solid var(--color-border);
  }

  :deep(.el-card__body) {
    padding: var(--space-lg);
  }
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.security-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-md);
}

.security-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--space-md);
  background: var(--color-background);
  border-radius: var(--radius-md);
}

.security-info {
  display: flex;
  align-items: center;
  gap: var(--space-md);

  .el-icon {
    font-size: 24px;
    color: var(--color-primary);
  }

  .security-title {
    display: block;
    font-weight: 500;
    margin-bottom: 2px;
  }

  .security-desc {
    font-size: 13px;
    color: var(--color-text-secondary);
  }
}

.password-tip {
  font-size: 12px;
  color: var(--color-text-secondary);
  margin-top: var(--space-xs);
}

@media (max-width: 768px) {
  .profile-layout {
    grid-template-columns: 1fr;
  }

  .profile-left .avatar-card :deep(.el-card__body) {
    flex-direction: row;
    gap: var(--space-lg);

    > * {
      flex: 1;
    }
  }
}
</style>
