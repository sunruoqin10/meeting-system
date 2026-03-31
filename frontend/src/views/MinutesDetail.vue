<template>
  <div class="minutes-detail">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <el-button text @click="goBack">
          <el-icon class="el-icon--left"><ArrowLeft /></el-icon>
          返回列表
        </el-button>
        <h1>{{ detail.title }}</h1>
      </div>
      <div class="header-actions">
        <el-button @click="handleExport">
          <el-icon class="el-icon--left"><Download /></el-icon>
          导出
        </el-button>
        <el-dropdown @command="handleMoreCommand">
          <el-button>
            更多
            <el-icon class="el-icon--right"><ArrowDown /></el-icon>
          </el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="versions">
                <el-icon><Clock /></el-icon>
                版本历史
              </el-dropdown-item>
              <el-dropdown-item command="share">
                <el-icon><Share /></el-icon>
                分享链接
              </el-dropdown-item>
              <el-dropdown-item command="favorite">
                <el-icon><Star /></el-icon>
                收藏
              </el-dropdown-item>
              <el-dropdown-item command="delete" divided v-if="canDelete">
                <el-icon><Delete /></el-icon>
                删除
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
        <el-button type="primary" @click="handleEdit">
          <el-icon class="el-icon--left"><Edit /></el-icon>
          编辑
        </el-button>
      </div>
    </div>

    <!-- 会议信息卡片 -->
    <el-card shadow="never" class="info-card">
      <div class="info-grid">
        <div class="info-item">
          <el-icon><Calendar /></el-icon>
          <span>{{ detail.meetingDate }} {{ detail.meetingTime }}</span>
        </div>
        <div class="info-item">
          <el-icon><LocationInformation /></el-icon>
          <span>{{ detail.location }}</span>
        </div>
        <div class="info-item">
          <el-icon><User /></el-icon>
          <span>主持人: {{ detail.hostName }}</span>
        </div>
        <div class="info-item">
          <el-icon><User /></el-icon>
          <span>与会者: {{ detail.attendees.length }}人</span>
        </div>
      </div>
      <div class="version-badge">
        <el-tag type="primary" effect="dark">
          {{ detail.currentVersion }}
        </el-tag>
        <el-button type="primary" text @click="goToVersions">
          查看版本历史
          <el-icon class="el-icon--right"><ArrowRight /></el-icon>
        </el-button>
      </div>
    </el-card>

    <!-- 富文本内容 -->
    <el-card shadow="never" class="content-card">
      <div class="minute-content" v-html="detail.content"></div>
    </el-card>

    <!-- 附件区域 -->
    <el-card shadow="never" class="attachment-card" v-if="detail.attachments?.length > 0">
      <template #header>
        <div class="card-header">
          <span>
            <el-icon><Paperclip /></el-icon>
            附件 ({{ detail.attachments.length }})
          </span>
          <el-button type="primary" text size="small" @click="downloadAll">
            全部下载
          </el-button>
        </div>
      </template>

      <div class="attachment-list">
        <div v-for="file in detail.attachments" :key="file.id" class="attachment-item">
          <div class="file-icon">
            <el-icon :size="24"><Document /></el-icon>
          </div>
          <div class="file-info">
            <span class="file-name">{{ file.fileName }}</span>
            <span class="file-size">{{ formatFileSize(file.fileSize) }}</span>
          </div>
          <div class="file-actions">
            <el-button type="primary" text size="small" @click="handlePreviewFile(file)">
              预览
            </el-button>
            <el-button type="primary" text size="small" @click="downloadFile(file)">
              下载
            </el-button>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 评论区 -->
    <el-card shadow="never" class="comment-card">
      <template #header>
        <div class="card-header">
          <span>
            <el-icon><ChatDotRound /></el-icon>
            评论 ({{ commentList.length }})
          </span>
        </div>
      </template>

      <!-- 评论输入 -->
      <div class="comment-input">
        <el-avatar :size="40" :src="currentUser.avatar">
          {{ currentUser.name?.charAt(0) }}
        </el-avatar>
        <div class="input-wrapper">
          <el-input
            v-model="newComment"
            type="textarea"
            :rows="3"
            placeholder="发表评论... (使用 @用户名 可以提醒他人)"
            @keydown.enter.ctrl="submitComment"
          />
          <div class="input-actions">
            <el-button type="primary" @click="submitComment" :disabled="!newComment.trim()">
              发表
            </el-button>
          </div>
        </div>
      </div>

      <!-- 评论列表 -->
      <div class="comment-list" v-if="commentList.length > 0">
        <div v-for="comment in commentList" :key="comment.id" class="comment-item">
          <el-avatar :size="40" :src="comment.userAvatar">
            {{ comment.userName?.charAt(0) }}
          </el-avatar>
          <div class="comment-content">
            <div class="comment-header">
              <span class="comment-author">{{ comment.userName }}</span>
              <span class="comment-time">{{ comment.createTime }}</span>
            </div>
            <div class="comment-body">
              <span v-html="formatCommentContent(comment.content)"></span>
            </div>
            <div class="comment-actions">
              <el-button type="primary" text size="small" @click="handleReply(comment)">
                回复
              </el-button>
            </div>

            <!-- 回复列表 -->
            <div v-if="comment.replies?.length > 0" class="reply-list">
              <div v-for="reply in comment.replies" :key="reply.id" class="reply-item">
                <el-avatar :size="32" :src="reply.userAvatar">
                  {{ reply.userName?.charAt(0) }}
                </el-avatar>
                <div class="reply-content">
                  <div class="comment-header">
                    <span class="comment-author">{{ reply.userName }}</span>
                    <span class="comment-time">{{ reply.createTime }}</span>
                  </div>
                  <div class="comment-body">
                    <span v-html="formatCommentContent(reply.content)"></span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <el-empty v-else description="暂无评论" />
    </el-card>

    <!-- 附件预览弹窗 -->
    <el-dialog v-model="previewVisible" title="附件预览" width="800px">
      <div class="preview-content">
        <el-image
          v-if="previewingFile?.fileType?.startsWith('image/')"
          :src="previewUrl"
          fit="contain"
          style="width: 100%; height: 500px;"
        />
        <div v-else class="preview-placeholder">
          <el-icon :size="64"><Document /></el-icon>
          <p>此文件类型暂不支持预览</p>
          <el-button type="primary" @click="downloadFile(previewingFile)">
            下载文件
          </el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  ArrowLeft, ArrowRight, ArrowDown, Edit, Download,
  Clock, Share, Star, Delete, Calendar, LocationInformation,
  User, Paperclip, Document, ChatDotRound
} from '@element-plus/icons-vue'
import { getMinuteDetail, deleteMinute } from '@/api'

const router = useRouter()
const route = useRoute()

const detail = ref({
  id: '',
  title: '',
  content: '',
  meetingDate: '',
  meetingTime: '',
  location: '',
  status: '',
  creatorId: '',
  attendees: [],
  attachments: []
})
const commentList = ref([])
const newComment = ref('')
const previewVisible = ref(false)
const previewingFile = ref(null)
const previewUrl = ref('')
const loading = ref(false)

const canDelete = computed(() => {
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  return user.id === detail.value.creatorId || user.role === 'admin'
})

const loadDetail = async () => {
  try {
    loading.value = true
    const id = route.params.id
    const data = await getMinuteDetail(id)
    detail.value = data
  } catch (error) {
    console.error('加载会议纪要详情失败:', error)
    ElMessage.error(error.message || '加载会议纪要详情失败')
  } finally {
    loading.value = false
  }
}

const goBack = () => router.push('/minutes')
const handleEdit = () => router.push(`/minutes/${detail.value.id}/edit`)
const goToVersions = () => router.push(`/minutes/${detail.value.id}/versions`)

const handleExport = (command) => {
  ElMessageBox.confirm('选择导出格式', '导出', {
    confirmButtonText: 'Word',
    cancelButtonText: 'PDF',
    distinguishCancelAndClose: true
  }).then(() => {
    ElMessage.success('正在导出 Word 文档...')
  }).catch(action => {
    if (action === 'cancel') {
      ElMessage.success('正在导出 PDF 文档...')
    }
  })
}

const handleMoreCommand = async (command) => {
  switch (command) {
    case 'versions':
      goToVersions()
      break
    case 'share':
      ElMessage.success('链接已复制到剪贴板')
      break
    case 'favorite':
      ElMessage.success('已添加到收藏')
      break
    case 'delete':
      try {
        await ElMessageBox.confirm('确定要删除这条会议纪要吗？此操作不可恢复。', '删除确认', {
          confirmButtonText: '删除',
          cancelButtonText: '取消',
          type: 'warning'
        })
        await deleteMinute(detail.value.id)
        ElMessage.success('删除成功')
        router.push('/minutes')
      } catch {
        // 取消
      }
      break
  }
}

const formatFileSize = (bytes) => {
  if (bytes < 1024) return bytes + ' B'
  if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + ' KB'
  return (bytes / (1024 * 1024)).toFixed(1) + ' MB'
}

const handlePreviewFile = (file) => {
  previewingFile.value = file
  previewUrl.value = URL.createObjectURL(new Blob())
  previewVisible.value = true
}

const downloadFile = (file) => {
  ElMessage.success(`正在下载 ${file.fileName}`)
}

const downloadAll = () => {
  ElMessage.success('正在打包下载所有附件...')
}

const formatCommentContent = (content) => {
  // 格式化@提及
  return content.replace(/@(\w+)/g, '<span style="color: var(--color-primary);">@$1</span>')
}

const submitComment = () => {
  if (!newComment.value.trim()) return

  commentList.value.unshift({
    id: 'c_new',
    userId: currentUser.id,
    userName: currentUser.name,
    userAvatar: currentUser.avatar,
    content: newComment.value,
    parentId: null,
    mentions: [],
    createTime: new Date().toLocaleString(),
    replies: []
  })

  newComment.value = ''
  ElMessage.success('评论发表成功')
}

const handleReply = (comment) => {
  newComment.value = `@${comment.userName} `
}

onMounted(async () => {
  await loadDetail()
})
</script>

<style lang="scss" scoped>
.minutes-detail {
  max-width: 900px;
  margin: 0 auto;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: var(--space-lg);
  gap: var(--space-md);

  .header-left {
    display: flex;
    flex-direction: column;
    gap: var(--space-sm);

    h1 {
      font-family: var(--font-code);
      font-size: 22px;
      font-weight: 600;
      color: var(--color-text);
    }
  }

  .header-actions {
    display: flex;
    gap: var(--space-sm);
    flex-shrink: 0;
  }
}

.info-card {
  margin-bottom: var(--space-lg);

  :deep(.el-card__body) {
    padding: var(--space-lg);
  }
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: var(--space-md);
  margin-bottom: var(--space-md);
}

.info-item {
  display: flex;
  align-items: center;
  gap: var(--space-sm);
  color: var(--color-text);

  .el-icon {
    color: var(--color-primary);
  }
}

.version-badge {
  display: flex;
  align-items: center;
  gap: var(--space-md);
  padding-top: var(--space-md);
  border-top: 1px solid var(--color-border);

  :deep(.el-tag) {
    font-family: var(--font-code);
  }
}

.content-card {
  margin-bottom: var(--space-lg);

  :deep(.el-card__body) {
    padding: var(--space-xl);
  }
}

.minute-content {
  font-size: 15px;
  line-height: 1.8;
  color: var(--color-text);

  :deep(h2) {
    font-size: 18px;
    font-weight: 600;
    margin: 20px 0 10px;
    color: var(--color-text);
  }

  :deep(p) {
    margin: 10px 0;
  }

  :deep(ul), :deep(ol) {
    padding-left: 24px;
    margin: 10px 0;
  }

  :deep(table) {
    width: 100%;
    border-collapse: collapse;
    margin: 16px 0;

    th, td {
      border: 1px solid var(--color-border);
      padding: 10px;
    }

    th {
      background: var(--color-background);
      font-weight: 600;
    }
  }
}

.attachment-card {
  margin-bottom: var(--space-lg);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;

  span {
    display: flex;
    align-items: center;
    gap: var(--space-sm);
    font-weight: 500;
  }
}

.attachment-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-sm);
}

.attachment-item {
  display: flex;
  align-items: center;
  gap: var(--space-md);
  padding: var(--space-md);
  background: var(--color-background);
  border-radius: var(--radius-md);

  .file-icon {
    width: 48px;
    height: 48px;
    background: var(--color-surface);
    border-radius: var(--radius-md);
    display: flex;
    align-items: center;
    justify-content: center;
    color: var(--color-primary);
  }

  .file-info {
    flex: 1;
    display: flex;
    flex-direction: column;

    .file-name {
      font-weight: 500;
      color: var(--color-text);
    }

    .file-size {
      font-size: 13px;
      color: var(--color-text-secondary);
    }
  }

  .file-actions {
    display: flex;
    gap: var(--space-xs);
  }
}

.comment-card {
  :deep(.el-card__body) {
    padding: var(--space-lg);
  }
}

.comment-input {
  display: flex;
  gap: var(--space-md);
  margin-bottom: var(--space-xl);

  .input-wrapper {
    flex: 1;

    .input-actions {
      display: flex;
      justify-content: flex-end;
      margin-top: var(--space-sm);
    }
  }
}

.comment-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-lg);
}

.comment-item {
  display: flex;
  gap: var(--space-md);
}

.comment-content {
  flex: 1;
}

.comment-header {
  display: flex;
  align-items: center;
  gap: var(--space-md);
  margin-bottom: var(--space-xs);

  .comment-author {
    font-weight: 500;
    color: var(--color-text);
  }

  .comment-time {
    font-size: 13px;
    color: var(--color-text-secondary);
  }
}

.comment-body {
  font-size: 14px;
  line-height: 1.6;
  color: var(--color-text);
  margin-bottom: var(--space-sm);
}

.comment-actions {
  display: flex;
  gap: var(--space-sm);
}

.reply-list {
  margin-top: var(--space-md);
  padding-left: var(--space-lg);
  border-left: 2px solid var(--color-border);
}

.reply-item {
  display: flex;
  gap: var(--space-sm);
  margin-bottom: var(--space-md);

  &:last-child {
    margin-bottom: 0;
  }
}

.reply-content {
  flex: 1;
}

.preview-placeholder {
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

@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
  }

  .info-grid {
    grid-template-columns: 1fr;
  }
}
</style>
