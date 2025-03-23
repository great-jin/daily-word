<template>
  <div class="logout-container">
    <el-card class="logout-card">
      <p class="logout-text">您已成功登出</p>

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
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.logout-card {
  text-align: center;
  padding: 30px;
  width: 400px;
  background-color: rgba(224, 240, 247, 0.98);
}

.logout-text {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 40px;
}
</style>
