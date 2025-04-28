<template>
  <el-drawer
      v-model="visible"
      size="40%"
      style="overflow-y: scroll;"
  >
    <el-card>
      <template #header>
        <span style="font-weight: bold; font-size: 16px">
          <el-tag
              v-if="record.rankType !== '单人挑战'"
              :type="record.score > 0 ? 'success' : 'danger'"
          >
            {{ record.score > 0 ? '对局胜利' : '对局失败' }}
          </el-tag>
        </span>
      </template>

      <el-row>
        <el-col :span="12" class="label-container">
          <span class="label">匹配模式:</span>
          <el-tag type="info">{{ record.rankMode }}</el-tag>
        </el-col>
        <el-col :span="12" class="label-container">
          <span class="label">匹配方式:</span>
          <el-tag type="info">{{ record.rankType }}</el-tag>
        </el-col>
      </el-row>

      <el-row style="margin-top: 20px">
        <el-col :span="12" class="label-container">
          <span class="label">词汇分类:</span>
          <el-tag type="info">{{ record.catalog }}</el-tag>
        </el-col>
        <el-col :span="12" class="label-container">
          <span class="label">单词组数:</span>
          <el-tag type="info">{{ record.wordCount }} / 组</el-tag>
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
            {{ item.userName }}
          </span>
        </div>
      </template>

      <el-row>
        <el-col :span="8">
          <p>正确数: &nbsp;
            <el-tag type="info">{{ item.correctCount }} 题</el-tag>
          </p>
        </el-col>
        <el-col :span="8">
          <p>耗时: &nbsp;
            <el-tag type="info">{{ item.costSecond }}</el-tag>
          </p>
        </el-col>
        <el-col :span="8">
          <p>得分: &nbsp;
            <el-tag type="info">{{ item.score }} 分</el-tag>
          </p>
        </el-col>
      </el-row>
    </el-card>
  </el-drawer>
</template>

<script>
import {getMatchDetail} from "@/api/matchApi";
import {formatSeconds} from "@/util/CommonUtil";

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
      await getMatchDetail(data.matchId).then(res => {
        this.matchDetail = res.data
        this.matchDetail.forEach(it => {
          it.costSecond = formatSeconds(it.costSecond)
        })
      })
    }
  }
}
</script>

<style scoped>
.label-container {
  display: flex;
  align-items: center; /* 垂直居中 */
}

.label {
  display: inline-block;
  width: 80px; /* 统一宽度，确保对齐 */
  text-align: right; /* 文字右对齐 */
  margin-right: 8px; /* 标签间距 */
}
</style>