<template>
  <div class="search-container">
    <el-input
        v-model="searchText"
        placeholder="请输入搜索内容"
        className="search-input"
        :style="{ borderRadius: '100px' }"
    />
    <el-button @click="clickOption">翻译</el-button>
  </div>
</template>

<script>
import {translate} from "@/api/wordApi";

export default {
  data() {
    return {
      searchText: ''
    }
  },
  methods: {
    clickOption() {
      translate(this.searchText).then(res => {
        console.log(res)
      })
    }
  }
}
</script>

<style scoped>
.search-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh; /* 让容器占据整个视窗高度 */
}

.search-input {
  width: 300px; /* 或者你想要的宽度 */
}
</style>
