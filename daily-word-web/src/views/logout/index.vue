<template>
  <div class="logout-container">
    <el-card class="logout-card">
      <p class="logout-text">您已登出</p>

      <router-link to="/">
        <el-button type="primary" style="margin-right: 14px">回到首页</el-button>
      </router-link>

      <router-link to="/login">
        <el-button type="primary">重新登录</el-button>
      </router-link>
    </el-card>
  </div>
</template>

<style scoped>
.logout-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f7fa;
}

.logout-card {
  text-align: center;
  padding: 30px;
  width: 400px;
}

.logout-text {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 40px;
}
</style>
<script setup lang="ts">
</script>