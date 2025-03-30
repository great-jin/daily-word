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
        <el-button type="primary" @click="close">返回</el-button>
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
        title: '提交成功',
        message: '对局结果计算中，可在匹配记录页查看结果',
      },
    }
  },
  methods: {
    show() {
      this.visible = true
    },
    close() {
      this.visible = false
      this.$router.push('/competition');
    }
  }
}
</script>

<style scoped>
</style>
