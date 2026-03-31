<template>
  <div class="minutes-editor">
    <!-- 页面头部 -->
    <div class="editor-header">
      <div class="header-left">
        <el-button text @click="handleBack">
          <el-icon class="el-icon--left"><ArrowLeft /></el-icon>
          返回
        </el-button>
        <h1>{{ isEdit ? '编辑纪要' : '新建纪要' }}</h1>
      </div>
      <div class="header-right">
        <div class="save-status" :class="saveStatusClass">
          <el-icon v-if="saveStatus === 'saving'"><Loading /></el-icon>
          <el-icon v-else-if="saveStatus === 'saved'"><Check /></el-icon>
          <el-icon v-else-if="saveStatus === 'error'"><Warning /></el-icon>
          <span>{{ saveStatusText }}</span>
        </div>
      </div>
    </div>

    <!-- 编辑器主体 -->
    <div class="editor-main">
      <el-card shadow="never" class="editor-form">
        <!-- 会议信息区域 -->
        <div class="meeting-info">
          <h3>
            <el-icon><InfoFilled /></el-icon>
            会议信息
          </h3>
          <div class="info-grid">
            <el-form-item label="会议标题" required>
              <el-input v-model="form.title" placeholder="请输入会议标题" />
            </el-form-item>

            <el-form-item label="会议日期">
              <el-date-picker
                v-model="form.meetingDate"
                type="date"
                format="YYYY-MM-DD"
                placeholder="选择日期"
                style="width: 100%;"
              />
            </el-form-item>

            <el-form-item label="会议时间">
              <el-time-picker
                v-model="form.meetingTime"
                format="HH:mm"
                placeholder="选择时间"
                style="width: 100%;"
              />
            </el-form-item>

            <el-form-item label="会议地点">
              <el-input v-model="form.location" placeholder="请输入会议地点" />
            </el-form-item>
          </div>

          <div class="info-row">
            <el-form-item label="主持人">
              <el-select
                v-model="form.hostId"
                placeholder="选择主持人"
                filterable
                remote
                :remote-method="searchUsers"
                style="width: 100%;"
              >
                <el-option
                  v-for="user in userOptions"
                  :key="user.id"
                  :label="user.name"
                  :value="user.id"
                />
              </el-select>
            </el-form-item>

            <el-form-item label="与会者">
              <el-select
                v-model="form.attendees"
                multiple
                placeholder="选择与会者"
                filterable
                remote
                :remote-method="searchUsers"
                style="width: 100%;"
              >
                <el-option
                  v-for="user in userOptions"
                  :key="user.id"
                  :label="user.name + ' (' + user.email + ')'"
                  :value="user.id"
                />
              </el-select>
            </el-form-item>

            <el-form-item label="抄送人">
              <el-select
                v-model="form.ccList"
                multiple
                placeholder="选择抄送人"
                filterable
                remote
                :remote-method="searchUsers"
                style="width: 100%;"
              >
                <el-option
                  v-for="user in userOptions"
                  :key="user.id"
                  :label="user.name"
                  :value="user.id"
                />
              </el-select>
            </el-form-item>
          </div>
        </div>

        <!-- 富文本编辑器 -->
        <div class="editor-content">
          <h3>
            <el-icon><Document /></el-icon>
            会议内容
          </h3>
          <div class="editor-toolbar">
            <el-button-group>
              <el-button size="small" @click="execCommand('bold')">
                <el-icon><EditPen /></el-icon>
              </el-button>
              <el-button size="small" @click="execCommand('italic')">
                <el-icon><Edit /></el-icon>
              </el-button>
              <el-button size="small" @click="execCommand('underline')">
                <el-icon><Delete /></el-icon>
              </el-button>
            </el-button-group>
            <el-divider direction="vertical" />
            <el-button-group>
              <el-button size="small" @click="execCommand('insertUnorderedList')">
                <el-icon><List /></el-icon>
              </el-button>
              <el-button size="small" @click="execCommand('insertOrderedList')">
                <el-icon><List /></el-icon>
              </el-button>
            </el-button-group>
            <el-divider direction="vertical" />
            <el-button size="small" @click="insertTable">
              <el-icon><Grid /></el-icon>
              插入表格
            </el-button>
            <el-button size="small" @click="insertTemplate">
              <el-icon><Collection /></el-icon>
              应用模板
            </el-button>
          </div>
          <div
            ref="editorRef"
            class="rich-editor"
            contenteditable="true"
            @input="handleContentChange"
            @paste="handlePaste"
          ></div>
        </div>

        <!-- 附件区域 -->
        <div class="attachment-section">
          <h3>
            <el-icon><Paperclip /></el-icon>
            附件
          </h3>
          <el-upload
            ref="uploadRef"
            :auto-upload="false"
            :on-change="handleFileChange"
            :file-list="fileList"
            multiple
            drag
          >
            <el-icon class="el-icon--upload"><upload-filled /></el-icon>
            <div class="el-upload__text">
              将文件拖到此处，或<em>点击上传</em>
            </div>
            <template #tip>
              <div class="el-upload__tip">单个文件不超过500MB</div>
            </template>
          </el-upload>

          <div v-if="fileList.length > 0" class="file-list">
            <div v-for="(file, index) in fileList" :key="index" class="file-item">
              <el-icon><Document /></el-icon>
              <span class="file-name">{{ file.name }}</span>
              <span class="file-size">{{ formatFileSize(file.size) }}</span>
              <el-button type="danger" text size="small" @click="removeFile(index)">
                删除
              </el-button>
            </div>
          </div>
        </div>

        <!-- 操作按钮 -->
        <div class="editor-actions">
          <el-button @click="handleBack">取消</el-button>
          <el-button @click="handleSaveDraft">保存草稿</el-button>
          <el-button type="primary" @click="handleSend">
            选择接收人并发送
            <el-icon class="el-icon--right"><ArrowRight /></el-icon>
          </el-button>
        </div>
      </el-card>
    </div>

    <!-- 发送对话框 -->
    <el-dialog v-model="sendDialogVisible" title="选择接收人" width="600px">
      <div class="send-dialog-content">
        <p class="send-tip">确认发送对象后，将生成正式版本并发送邮件通知</p>

        <div class="attendees-section">
          <h4>当前与会者 ({{ form.attendees.length + form.ccList.length }}人)</h4>
          <el-checkbox-group v-model="selectedReceivers">
            <el-checkbox
              v-for="user in allReceivers"
              :key="user.id"
              :label="user.id"
            >
              <div class="receiver-item">
                <el-avatar :size="32" :src="user.avatar">
                  {{ user.name?.charAt(0) }}
                </el-avatar>
                <div class="receiver-info">
                  <span class="receiver-name">{{ user.name }}</span>
                  <span class="receiver-email">{{ user.email }}</span>
                </div>
              </div>
            </el-checkbox>
          </el-checkbox-group>
        </div>

        <div class="email-option">
          <el-checkbox v-model="sendEmail">发送邮件通知</el-checkbox>
          <div v-if="sendEmail" class="email-preview">
            <h4>邮件预览</h4>
            <div class="preview-content">
              <p><strong>主题:</strong> 会议纪要 - {{ form.title }}</p>
              <p><strong>内容:</strong> 请查收会议纪要，在线查看链接...</p>
            </div>
          </div>
        </div>
      </div>

      <template #footer>
        <el-button @click="sendDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmSend" :disabled="selectedReceivers.length === 0">
          确认发送
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, onUnmounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  ArrowLeft, ArrowRight, Check, Warning, Loading,
  InfoFilled, Document, Paperclip, EditPen, List,
  Grid, Collection, UploadFilled, Bell, Delete
} from '@element-plus/icons-vue'
import { minuteDetail, currentUser } from '@/mock/data'

const router = useRouter()
const route = useRoute()

const isEdit = computed(() => !!route.params.id)
const editorRef = ref(null)
const uploadRef = ref(null)

const saveStatus = ref('idle') // idle, saving, saved, error
const saveStatusText = computed(() => {
  switch (saveStatus.value) {
    case 'saving': return '保存中...'
    case 'saved': return '已保存'
    case 'error': return '保存失败'
    default: return ''
  }
})
const saveStatusClass = computed(() => `status-${saveStatus.value}`)

const form = reactive({
  title: '',
  meetingDate: '',
  meetingTime: '',
  location: '',
  hostId: '',
  attendees: [],
  ccList: [],
  content: ''
})

const userOptions = ref([])
const fileList = ref([])
const sendDialogVisible = ref(false)
const selectedReceivers = ref([])
const sendEmail = ref(true)

let autoSaveTimer = null
let contentChanged = false

const allReceivers = computed(() => {
  // 模拟所有可选接收人
  return [
    { id: 'u001', name: '张三', email: 'zhangsan@company.com', avatar: '' },
    { id: 'u002', name: '李四', email: 'lisi@company.com', avatar: '' },
    { id: 'u003', name: '王五', email: 'wangwu@company.com', avatar: '' },
    { id: 'u004', name: '刘总', email: 'liuzong@company.com', avatar: '' }
  ]
})

const execCommand = (command) => {
  document.execCommand(command, false, null)
  editorRef.value?.focus()
}

const insertTable = () => {
  const html = `
    <table style="width:100%; border-collapse:collapse; margin:16px 0;">
      <tr style="background:#f0f0f0;">
        <th style="border:1px solid #ddd; padding:8px;">标题1</th>
        <th style="border:1px solid #ddd; padding:8px;">标题2</th>
      </tr>
      <tr>
        <td style="border:1px solid #ddd; padding:8px;">内容1</td>
        <td style="border:1px solid #ddd; padding:8px;">内容2</td>
      </tr>
    </table>
  `
  document.execCommand('insertHTML', false, html)
}

const insertTemplate = () => {
  ElMessage.info('选择模板功能开发中')
}

const handleContentChange = () => {
  contentChanged = true
  form.content = editorRef.value?.innerHTML || ''
}

const handlePaste = (e) => {
  e.preventDefault()
  const text = e.clipboardData.getData('text/plain')
  document.execCommand('insertText', false, text)
}

const searchUsers = (query) => {
  // 模拟搜索用户
  userOptions.value = [
    { id: 'u001', name: '张三', email: 'zhangsan@company.com' },
    { id: 'u002', name: '李四', email: 'lisi@company.com' }
  ]
}

const handleFileChange = (file, files) => {
  fileList.value = files
}

const removeFile = (index) => {
  fileList.value.splice(index, 1)
}

const formatFileSize = (bytes) => {
  if (bytes < 1024) return bytes + ' B'
  if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + ' KB'
  return (bytes / (1024 * 1024)).toFixed(1) + ' MB'
}

const handleSaveDraft = async () => {
  if (!form.title) {
    ElMessage.warning('请输入会议标题')
    return
  }

  saveStatus.value = 'saving'

  // 模拟保存
  setTimeout(() => {
    saveStatus.value = 'saved'
    ElMessage.success('草稿已保存')
    contentChanged = false

    // 3秒后重置状态
    setTimeout(() => {
      if (saveStatus.value === 'saved') {
        saveStatus.value = 'idle'
      }
    }, 3000)
  }, 1000)
}

const handleSend = () => {
  if (!form.title) {
    ElMessage.warning('请输入会议标题')
    return
  }

  // 初始化接收人列表
  selectedReceivers.value = [...form.attendees, ...form.ccList]
  sendDialogVisible.value = true
}

const confirmSend = async () => {
  try {
    await ElMessageBox.confirm(
      `确定要发送给 ${selectedReceivers.value.length} 位接收人吗？`,
      '确认发送',
      { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }
    )

    // 模拟发送
    ElMessage.success('发送成功！')
    sendDialogVisible.value = false
    router.push('/minutes')
  } catch {
    // 用户取消
  }
}

const handleBack = async () => {
  if (contentChanged) {
    try {
      await ElMessageBox.confirm(
        '有未保存的内容，确定要离开吗？',
        '提示',
        { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }
      )
    } catch {
      return
    }
  }
  router.back()
}

// 自动保存
const startAutoSave = () => {
  autoSaveTimer = setInterval(() => {
    if (contentChanged && form.title) {
      handleSaveDraft()
    }
  }, 30000) // 30秒
}

const stopAutoSave = () => {
  if (autoSaveTimer) {
    clearInterval(autoSaveTimer)
  }
}

onMounted(() => {
  if (isEdit.value) {
    // 加载已有数据
    Object.assign(form, {
      title: minuteDetail.title,
      meetingDate: minuteDetail.meetingDate,
      meetingTime: minuteDetail.meetingTime,
      location: minuteDetail.location,
      hostId: minuteDetail.hostId,
      attendees: minuteDetail.attendees.map(a => a.id),
      ccList: minuteDetail.ccList.map(c => c.id),
      content: minuteDetail.content
    })

    if (editorRef.value) {
      editorRef.value.innerHTML = minuteDetail.content
    }
  }

  startAutoSave()
})

onUnmounted(() => {
  stopAutoSave()
})
</script>

<style lang="scss" scoped>
.minutes-editor {
  max-width: 1200px;
  margin: 0 auto;
}

.editor-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
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

.save-status {
  display: flex;
  align-items: center;
  gap: var(--space-xs);
  font-size: 13px;
  padding: 6px 12px;
  border-radius: var(--radius-full);

  &.status-saving {
    background: #DBEAFE;
    color: #3B82F6;
  }

  &.status-saved {
    background: #D1FAE5;
    color: #10B981;
  }

  &.status-error {
    background: #FEE2E2;
    color: #EF4444;
  }
}

.editor-form {
  :deep(.el-card__body) {
    padding: var(--space-lg);
  }
}

.meeting-info {
  margin-bottom: var(--space-xl);

  h3 {
    font-family: var(--font-code);
    font-size: 16px;
    font-weight: 600;
    color: var(--color-text);
    margin-bottom: var(--space-md);
    display: flex;
    align-items: center;
    gap: var(--space-sm);
  }
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: var(--space-md);
  margin-bottom: var(--space-md);
}

.info-row {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: var(--space-md);
}

.editor-content {
  margin-bottom: var(--space-xl);

  h3 {
    font-family: var(--font-code);
    font-size: 16px;
    font-weight: 600;
    color: var(--color-text);
    margin-bottom: var(--space-md);
    display: flex;
    align-items: center;
    gap: var(--space-sm);
  }
}

.editor-toolbar {
  padding: var(--space-sm);
  background: var(--color-background);
  border: 1px solid var(--color-border);
  border-bottom: none;
  border-radius: var(--radius-md) var(--radius-md) 0 0;
}

.rich-editor {
  min-height: 400px;
  padding: var(--space-lg);
  border: 1px solid var(--color-border);
  border-radius: 0 0 var(--radius-md) var(--radius-md);
  outline: none;
  font-family: var(--font-body);
  font-size: 14px;
  line-height: 1.8;

  &:focus {
    border-color: var(--color-primary);
    box-shadow: 0 0 0 3px rgba(124, 58, 237, 0.1);
  }

  :deep(h2) {
    font-size: 18px;
    font-weight: 600;
    margin: 16px 0 8px;
  }

  :deep(p) {
    margin: 8px 0;
  }

  :deep(ul), :deep(ol) {
    padding-left: 24px;
  }
}

.attachment-section {
  margin-bottom: var(--space-xl);

  h3 {
    font-family: var(--font-code);
    font-size: 16px;
    font-weight: 600;
    color: var(--color-text);
    margin-bottom: var(--space-md);
    display: flex;
    align-items: center;
    gap: var(--space-sm);
  }
}

.file-list {
  margin-top: var(--space-md);
}

.file-item {
  display: flex;
  align-items: center;
  gap: var(--space-sm);
  padding: var(--space-sm) var(--space-md);
  background: var(--color-background);
  border-radius: var(--radius-md);
  margin-bottom: var(--space-xs);

  .file-name {
    flex: 1;
  }

  .file-size {
    color: var(--color-text-secondary);
    font-size: 13px;
  }
}

.editor-actions {
  display: flex;
  justify-content: flex-end;
  gap: var(--space-md);
  padding-top: var(--space-lg);
  border-top: 1px solid var(--color-border);
}

.send-dialog-content {
  .send-tip {
    color: var(--color-text-secondary);
    margin-bottom: var(--space-lg);
  }
}

.attendees-section {
  margin-bottom: var(--space-lg);

  h4 {
    font-size: 14px;
    font-weight: 500;
    margin-bottom: var(--space-md);
  }

  :deep(.el-checkbox) {
    display: flex;
    margin-bottom: var(--space-sm);
  }

  :deep(.el-checkbox__label) {
    width: 100%;
  }
}

.receiver-item {
  display: flex;
  align-items: center;
  gap: var(--space-sm);
  padding: var(--space-xs) 0;
}

.receiver-info {
  display: flex;
  flex-direction: column;

  .receiver-name {
    font-size: 14px;
  }

  .receiver-email {
    font-size: 12px;
    color: var(--color-text-secondary);
  }
}

.email-option {
  border-top: 1px solid var(--color-border);
  padding-top: var(--space-lg);
}

.email-preview {
  margin-top: var(--space-md);
  padding: var(--space-md);
  background: var(--color-background);
  border-radius: var(--radius-md);

  h4 {
    font-size: 13px;
    font-weight: 500;
    margin-bottom: var(--space-sm);
  }

  .preview-content {
    font-size: 13px;
    color: var(--color-text-secondary);
  }
}

@media (max-width: 768px) {
  .info-grid,
  .info-row {
    grid-template-columns: 1fr;
  }
}
</style>
