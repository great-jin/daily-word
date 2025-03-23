<template>
  <el-dialog
      v-model="visible"
      width="30%"
      title="对局结果"
      @close="close"
      :show-close="false"
  >
    <el-result
        :icon="result.icon"
        :title="result.title"
        :sub-title="result.message"
    >
      <template #extra>
        <router-link to="/competition">
          <el-button type="primary">返回</el-button>
        </router-link>
      </template>
    </el-result>
  </el-dialog>
</template>

<script>
export default {
  data() {
    return {
      visible: false,
      result: {
        icon: 'success',
        title: '您赢得对局',
        message: '耗时 60 秒，获取 5 积分',
      },
    }
  },
  methods: {
    show(data) {
      this.visible = true

    },
    close() {
      this.visible = false
    }
  }
}
</script>

<style scoped>
</style>
