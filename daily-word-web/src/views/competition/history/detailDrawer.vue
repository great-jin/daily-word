<template>
  <el-drawer
      v-model="visible"
      size="30%"
  >
    <!-- TODO 对战匹配信息，对手，结果 -->

    <h2>功能开发中，敬请期待！</h2>
  </el-drawer>
</template>

<script>

export default {
  data() {
    return {
      visible: false
    }
  },
  methods: {
    show() {
      this.visible = true
    }
  }
}
</script>

<style scoped>
</style>