<template>
  <el-drawer
      v-model="visible"
      width="100%"
      style="min-height: 400px"
  >
    <template #header>
      <span style="font-weight: bold; font-size: 18px">申请列表</span>
    </template>
    <!-- TODO 好友申请记录，同意、拒绝 -->

    <h2>功能开发中，敬请期待！</h2>
  </el-drawer>
</template>

<script>

export default {
  data() {
    return {
      visible: false,

    }
  },
  methods: {
    show(data) {
      this.visible = true
    }
  }
}
</script>

<style scoped>
</style>