<template xmlns="http://www.w3.org/1999/html">
  <div class="container">
    <el-row :gutter="20" style="width: 100%; padding: 20px 25px">
      <el-col :span="6">
        <el-card class="box-card" style="float: left">
          <template #header>
            <div class="card-header">
              <span>今日计划</span>
            </div>
          </template>
          <div v-for="o in 2" :key="o" class="text item">{{ 'List item ' + o }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card" style="float: left">
          <template #header>
            <div class="card-header">
              <span>复习计划</span>
            </div>
          </template>
          <div v-for="o in 2" :key="o" class="text item">{{ 'List item ' + o }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card" style="float: left">
          <template #header>
            <div class="card-header">
              <span>历史单词</span>
            </div>
          </template>
          <div v-for="o in 2" :key="o" class="text item">{{ 'List item ' + o }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card" style="float: left">
          <template #header>
            <div class="card-header">
              <span>我的积分</span>
            </div>
          </template>
          <div v-for="o in 2" :key="o" class="text item">{{ 'List item ' + o }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row style="padding: 20px 25px">
      <el-col :span="24">
        <el-button type="primary" @click="planOption('new')">新建计划</el-button>
        <el-button type="primary" @click="planOption('start')">今日任务</el-button>
      </el-col>
    </el-row>

    <word-plan ref="wordPlan"/>
    <word-task ref="wordTask"/>
  </div>
</template>

<script>
import wordPlan from "./wordPlan.vue";
import wordTask from "./wordTask.vue";

export default {
  components: {
    wordPlan,
    wordTask
  },
  data() {
    return {}
  },
  methods: {
    planOption(type) {
      switch (type) {
        case 'new':
          this.$refs.wordPlan.show();
          break
        case 'start':
          this.$refs.wordTask.show();
          break
      }
    }
  }
}
</script>

<style scoped>
.container {
  height: 100%;
  background-color: white;
}

.input-word {
  width: 35px;
}

.box-card {
  width: 100%;
}
</style>