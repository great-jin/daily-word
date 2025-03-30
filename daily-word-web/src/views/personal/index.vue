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
      <el-tab-pane label="实&nbsp;&nbsp;验&nbsp;&nbsp;室">
        <LabTab/>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import BasicTab from "./tabs/basicTab.vue";
import PrivateTab from "./tabs/privateTab.vue";
import LabTab from "@/views/personal/tabs/labTab.vue";

export default {
  components: {
    LabTab,
    BasicTab,
    PrivateTab
  }
}
</script>

<style scoped>
.container {
  height: 100%;
  border-radius: 20px;
  background-color: rgb(255, 255, 255);
}

.tab-container {
  height: calc(100vh - 300px);
  padding: 60px 240px;
}
</style>