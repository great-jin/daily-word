<template>
  <el-drawer
      v-model="visible"
      size="30%"
      style="overflow-y: scroll;"
  >
    <el-card>
      <template #header>
        <span style="font-weight: bold; font-size: 18px">对局详情</span>
      </template>

      <el-row>
        <el-col :span="12">
          词汇分类:&nbsp;
          <el-tag type="success">{{ record.catalog }}</el-tag>
        </el-col>
        <el-col :span="12">
          单词组数:&nbsp;
          <el-tag type="success">{{ record.wordCount }}</el-tag>
        </el-col>
      </el-row>
      <el-row style="margin-top: 20px">
        <el-col :span="12">
          匹配模式:&nbsp;
          <el-tag type="success">{{ record.rankMode }}</el-tag>
        </el-col>
        <el-col :span="12">
          匹配方式:&nbsp;
          <el-tag type="success">{{ record.rankType }}</el-tag>
        </el-col>
      </el-row>
    </el-card>

    <el-card
        v-for="item in matchDetail"
        style="margin-top: 14px"
    >
      <template #header>
        <div class="card-header">
          <span style="font-weight: bold">
            用户名: &nbsp;
            <el-tag type="primary">{{ item.userName }}</el-tag>
          </span>
        </div>
      </template>

      <el-row>
        <el-col :span="8">
          <p>正确数: &nbsp;
            <el-tag type="success">{{ item.correctCount }}</el-tag>
          </p>
        </el-col>
        <el-col :span="8">
          <p>耗时: &nbsp;
            <el-tag type="success">{{ item.costSecond }}</el-tag>
          </p>
        </el-col>
        <el-col :span="8">
          <p>积分: &nbsp;
            <el-tag type="success">{{ item.score }}</el-tag>
          </p>
        </el-col>
      </el-row>
    </el-card>
  </el-drawer>
</template>

<script>
import {getMatchDetail} from "@/api/matchApi";

export default {
  data() {
    return {
      visible: false,
      matchDetail: [],
      record: null
    }
  },
  methods: {
    async show(data) {
      this.visible = true
      this.record = data
      await getMatchDetail(data.groupId).then(res => {
        this.matchDetail = res.data
      })
    }
  }
}
</script>

<style scoped>
</style>