<template>
  <div class="app-layout">
    <!-- 侧边栏 -->
    <aside class="app-sidebar" :class="{ collapsed: isCollapsed }">
      <div class="sidebar-header">
        <div class="logo" v-show="!isCollapsed">
          <el-icon :size="28" color="white"><Document /></el-icon>
          <span>会议纪要</span>
        </div>
        <el-icon :size="24" color="white" class="collapse-icon" @click="toggleSidebar">
          <Fold v-if="!isCollapsed" />
          <Expand v-else />
        </el-icon>
      </div>

      <el-menu
        :default-active="currentRoute"
        class="sidebar-menu"
        background-color="transparent"
        text-color="rgba(255,255,255,0.8)"
        active-text-color="#ffffff"
        :collapse="isCollapsed"
        :router="true"
      >
        <el-menu-item index="/dashboard">
          <el-icon><HomeFilled /></el-icon>
          <template #title>工作台</template>
        </el-menu-item>

        <el-sub-menu index="minutes">
          <template #title>
            <el-icon><Document /></el-icon>
            <span>会议纪要</span>
          </template>
          <el-menu-item index="/minutes">纪要列表</el-menu-item>
          <el-menu-item index="/minutes/create">新建纪要</el-menu-item>
        </el-sub-menu>

        <el-menu-item index="/templates">
          <el-icon><Collection /></el-icon>
          <template #title>模板管理</template>
        </el-menu-item>

        <el-menu-item index="/messages">
          <el-icon><Bell /></el-icon>
          <template #title>消息中心</template>
        </el-menu-item>

        <el-menu-item index="/search">
          <el-icon><Search /></el-icon>
          <template #title>搜索</template>
        </el-menu-item>

        <el-menu-item index="/statistics">
          <el-icon><DataAnalysis /></el-icon>
          <template #title>统计分析</template>
        </el-menu-item>

        <el-divider v-show="!isCollapsed" style="margin: 12px 16px; border-color: rgba(255,255,255,0.2);" />

        <el-sub-menu index="admin" v-if="isAdmin">
          <template #title>
            <el-icon><Setting /></el-icon>
            <span>系统管理</span>
          </template>
          <el-menu-item index="/admin/departments">部门管理</el-menu-item>
          <el-menu-item index="/admin/users">用户管理</el-menu-item>
          <el-menu-item index="/admin/logs">操作日志</el-menu-item>
        </el-sub-menu>

        <el-menu-item index="/profile">
          <el-icon><User /></el-icon>
          <template #title>个人中心</template>
        </el-menu-item>
      </el-menu>
    </aside>

    <!-- 主内容区 -->
    <main class="app-main">
      <!-- 顶部导航 -->
      <header class="app-header">
        <div class="header-left">
          <el-button text @click="toggleMobileSidebar" class="mobile-menu-btn">
            <el-icon :size="24"><Menu /></el-icon>
          </el-button>
        </div>

        <div class="header-center">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索会议纪要..."
            class="header-search"
            :prefix-icon="Search"
            @keyup.enter="handleGlobalSearch"
          />
        </div>

        <div class="header-right">
          <el-badge :value="unreadCount" :hidden="unreadCount === 0" class="message-badge">
            <el-button text circle @click="$router.push('/messages')">
              <el-icon :size="20"><Bell /></el-icon>
            </el-button>
          </el-badge>

          <el-dropdown @command="handleUserCommand">
            <div class="user-avatar">
              <el-avatar :size="36" :src="userInfo.avatar">
                {{ userInfo.name?.charAt(0) }}
              </el-avatar>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">
                  <el-icon><User /></el-icon>个人中心
                </el-dropdown-item>
                <el-dropdown-item command="settings">
                  <el-icon><Setting /></el-icon>设置
                </el-dropdown-item>
                <el-dropdown-item command="logout" divided>
                  <el-icon><SwitchButton /></el-icon>退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </header>

      <!-- 页面内容 -->
      <div class="app-content">
        <router-view />
      </div>
    </main>

    <!-- 移动端遮罩 -->
    <div
      v-if="showMobileSidebar"
      class="mobile-overlay"
      @click="showMobileSidebar = false"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'
import {
  Document, HomeFilled, Collection, Bell, Search,
  DataAnalysis, Setting, User, Fold, Expand, Menu,
  SwitchButton
} from '@element-plus/icons-vue'
import { currentUser, messageList } from '@/mock/data'

const router = useRouter()
const route = useRoute()

const isCollapsed = ref(false)
const showMobileSidebar = ref(false)
const searchKeyword = ref('')

const userInfo = ref(currentUser)

const isAdmin = computed(() => {
  return userInfo.value?.role === 'admin'
})

const unreadCount = computed(() => {
  return messageList.filter(m => !m.isRead).length
})

const currentRoute = computed(() => route.path)

const toggleSidebar = () => {
  isCollapsed.value = !isCollapsed.value
}

const toggleMobileSidebar = () => {
  showMobileSidebar.value = !showMobileSidebar.value
}

const handleGlobalSearch = () => {
  if (searchKeyword.value.trim()) {
    router.push({ path: '/search', query: { keyword: searchKeyword.value } })
    searchKeyword.value = ''
  }
}

const handleUserCommand = (command) => {
  switch (command) {
    case 'profile':
      router.push('/profile')
      break
    case 'settings':
      ElMessage.info('设置功能开发中')
      break
    case 'logout':
      ElMessageBox.confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        localStorage.removeItem('token')
        localStorage.removeItem('user')
        router.push('/login')
        ElMessage.success('已退出登录')
      }).catch(() => {})
      break
  }
}

onMounted(() => {
  const savedUser = localStorage.getItem('user')
  if (savedUser) {
    userInfo.value = JSON.parse(savedUser)
  }
})
</script>

<style lang="scss" scoped>
.app-layout {
  display: flex;
  min-height: 100vh;
}

.app-sidebar {
  width: 240px;
  background: linear-gradient(180deg, var(--color-primary) 0%, var(--color-primary-dark) 100%);
  color: white;
  position: fixed;
  height: 100vh;
  overflow-y: auto;
  transition: width var(--transition-base);
  z-index: 100;

  &.collapsed {
    width: 64px;

    .sidebar-header .logo span {
      display: none;
    }
  }
}

.sidebar-header {
  padding: var(--space-lg);
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);

  .logo {
    display: flex;
    align-items: center;
    gap: var(--space-sm);
    font-family: var(--font-code);
    font-size: 16px;
    font-weight: 600;
  }

  .collapse-icon {
    cursor: pointer;
    padding: 4px;
    border-radius: var(--radius-sm);
    transition: background var(--transition-fast);

    &:hover {
      background: rgba(255, 255, 255, 0.1);
    }
  }
}

.sidebar-menu {
  border-right: none;

  :deep(.el-menu-item),
  :deep(.el-sub-menu__title) {
    height: 48px;
    line-height: 48px;

    &:hover {
      background: rgba(255, 255, 255, 0.1);
    }
  }

  :deep(.el-menu-item.is-active) {
    background: rgba(255, 255, 255, 0.2);
    border-radius: var(--radius-md);
    margin: 4px 8px;
  }
}

.app-main {
  flex: 1;
  margin-left: 240px;
  min-height: 100vh;
  background: var(--color-background);
  transition: margin-left var(--transition-base);
}

.app-header {
  background: var(--color-surface);
  padding: var(--space-md) var(--space-lg);
  border-bottom: 1px solid var(--color-border);
  display: flex;
  align-items: center;
  gap: var(--space-lg);
  position: sticky;
  top: 0;
  z-index: 50;
}

.header-left {
  display: flex;
  align-items: center;
}

.mobile-menu-btn {
  display: none;
}

.header-center {
  flex: 1;
  max-width: 400px;
}

.header-search {
  :deep(.el-input__wrapper) {
    border-radius: var(--radius-full);
    padding-left: 16px;
  }
}

.header-right {
  display: flex;
  align-items: center;
  gap: var(--space-md);
}

.message-badge {
  :deep(.el-badge__content) {
    top: -2px;
    right: -2px;
  }
}

.user-avatar {
  cursor: pointer;
  padding: 4px;
  border-radius: var(--radius-full);
  transition: background var(--transition-fast);

  &:hover {
    background: var(--color-background);
  }
}

.app-content {
  padding: var(--space-lg);
}

.mobile-overlay {
  display: none;
}

@media (max-width: 768px) {
  .app-sidebar {
    transform: translateX(-100%);

    &.open {
      transform: translateX(0);
    }
  }

  .app-main {
    margin-left: 0;
  }

  .mobile-menu-btn {
    display: flex;
  }

  .header-center {
    display: none;
  }

  .mobile-overlay {
    display: block;
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.5);
    z-index: 99;
  }
}
</style>
