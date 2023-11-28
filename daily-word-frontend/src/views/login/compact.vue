<template>
  <el-dialog
      v-model="dialogVisible"
      width="30%"
      title="用户协议"
      style="--el-dialog-padding-primary: 15px;"
      :center="true"
  >
    <div class="user-agreement-content">
      <p>欢迎使用我们的服务。</p>
      <p>1. 本服务的所有权和运作权归每日一词所有。</p>
      <!-- 其他协议内容 -->
    </div>
  </el-dialog>
</template>

<script>
export default {
  data() {
    return {
      dialogVisible: false
    }
  },
  methods: {
    show() {
      this.dialogVisible = true
    },
    clickOption(type) {
      if (type === 'ok') {

      } else {
        this.dialogVisible = false
      }
    }
  }
}
</script>

<style scoped>
.dialog-footer {
   display: flex;
   justify-content: center;
   align-items: flex-end;
   height: 100%;
 }

.button-container {
  margin-bottom: 20px; /* 调整底部间距 */
}
</style>
