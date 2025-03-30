<template>
  <el-drawer
      v-model="visible"
      size="30%"
      style="min-height: 400px"
  >
    <template #header>
      <span style="font-weight: bold; font-size: 18px">
        {{ userInfo.userName }}
      </span>
    </template>

    <!-- TODO 查看用户基本信息，赛季对战信息 -->

    <h2>功能开发中，敬请期待！</h2>
  </el-drawer>
</template>

<script>

export default {
  data() {
    return {
      visible: false,
      userInfo: {
        userName: ''
      }
    }
  },
  methods: {
    show(data) {
      this.visible = true
      this.userInfo = data
    }
  }
}
</script>

<style scoped>
</style>