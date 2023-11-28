<template>
  <div>
    <header>
      <div class="logo-container">
        <div class="logo"></div>
      </div>
      <h1>Daily Word</h1>
      <h3>
        Welcome to the Daily Word! To the
        <router-link to="/login" style="color: deepskyblue">
          home
        </router-link>
      </h3>
    </header>
  </div>
</template>

<script setup>
</script>

<style scoped>
header {
  width: 100%; /* 宽度占满父元素 */
  height: calc(100vh);
  background-color: #72C1F2; /* 淡蓝色背景 */
  color: white;
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background-image: url('@/asset/bg.png'); /* 替换为你的图片路径 */
  background-size: cover; /* 自适应并覆盖整个 div */
  background-position: center; /* 图片居中 */
}

.logo-container {
  width: 120px;
  height: 120px;
  margin-bottom: 20px;
  background-color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.logo {
  width: 80px;
  height: 80px;
  position: relative;
  overflow: hidden;
}

.logo::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(
      to bottom right,
      transparent 20%,
      #FB9891 60%
  );
  transform: rotate(45deg);
}
</style>