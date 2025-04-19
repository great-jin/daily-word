<template>
  <div class="container">
    <el-tabs
        v-model="activeTab"
        tab-position="left"
        class="tab-container"
        @tab-click="changeTab"
    >
      <el-tab-pane label="基本信息" name="info">
        <BasicTab v-if="activeTab === 'info'"/>
      </el-tab-pane>
      <el-tab-pane label="隐私设置" name="private">
        <PrivateTab v-if="activeTab === 'private'"/>
      </el-tab-pane>
      <el-tab-pane label="实&nbsp;&nbsp;验&nbsp;&nbsp;室" name="lab">
        <LabTab v-if="activeTab === 'lab'"/>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import BasicTab from "./tabs/basicTab.vue";
import PrivateTab from "./tabs/privacyTab.vue";
import LabTab from "@/views/personal/tabs/labTab.vue";

export default {
  components: {
    LabTab,
    BasicTab,
    PrivateTab
  },
  data() {
    return {
      activeTab: 'info'
    }
  },
  methods: {
    changeTab(tab) {
      this.activeTab = tab.paneName
    }
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