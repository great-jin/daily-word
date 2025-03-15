<template>
  <div class="container">
    <el-tabs
        tab-position="left"
        class="tab-container"
    >
      <el-tab-pane label="基本信息">
        <BasicTab/>
      </el-tab-pane>
      <el-tab-pane label="隐私设置">
        <PrivateTab/>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import BasicTab from "./tabs/basicTab.vue";
import PrivateTab from "./tabs/privateTab.vue";

export default {
  components: {
    BasicTab,
    PrivateTab
  }
}
</script>

<style scoped>
.container {
  height: 100%;
  border-radius: 30px;
  background-color: rgba(223, 243, 255, 0.98);
}

.tab-container {
  height: calc(100vh - 300px);
  padding: 60px 240px;
}
</style>