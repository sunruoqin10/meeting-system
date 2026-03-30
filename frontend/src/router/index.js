import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/',
    component: () => import('@/views/Layout.vue'),
    redirect: '/dashboard',
    meta: { requiresAuth: true },
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue')
      },
      {
        path: 'minutes',
        name: 'MinutesList',
        component: () => import('@/views/MinutesList.vue')
      },
      {
        path: 'minutes/create',
        name: 'MinutesCreate',
        component: () => import('@/views/MinutesEditor.vue')
      },
      {
        path: 'minutes/:id',
        name: 'MinutesDetail',
        component: () => import('@/views/MinutesDetail.vue')
      },
      {
        path: 'minutes/:id/edit',
        name: 'MinutesEdit',
        component: () => import('@/views/MinutesEditor.vue')
      },
      {
        path: 'minutes/:id/versions',
        name: 'VersionHistory',
        component: () => import('@/views/VersionHistory.vue')
      },
      {
        path: 'templates',
        name: 'TemplateList',
        component: () => import('@/views/TemplateList.vue')
      },
      {
        path: 'messages',
        name: 'MessageCenter',
        component: () => import('@/views/MessageCenter.vue')
      },
      {
        path: 'search',
        name: 'Search',
        component: () => import('@/views/Search.vue')
      },
      {
        path: 'statistics',
        name: 'Statistics',
        component: () => import('@/views/Statistics.vue')
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/Profile.vue')
      },
      {
        path: 'admin/departments',
        name: 'DepartmentManage',
        component: () => import('@/views/DepartmentManage.vue')
      },
      {
        path: 'admin/users',
        name: 'UserManage',
        component: () => import('@/views/UserManage.vue')
      },
      {
        path: 'admin/logs',
        name: 'OperationLogs',
        component: () => import('@/views/OperationLogs.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')

  if (to.meta.requiresAuth !== false && !token) {
    next('/login')
  } else if (to.path === '/login' && token) {
    next('/dashboard')
  } else {
    next()
  }
})

export default router
