<template>
  <el-dialog
      v-model="visible"
      width="40%"
  >
    <template #header>
      <div style="display: flex; justify-content: center; align-items: center;">
        <el-avatar
            :size="40"
            fit="fill"
            :src="userInfo.avatarUrl"
            style="margin-right: 14px;"
        />
        <span style="font-weight: bold; font-size: 18px; line-height: 1;">
          {{ userInfo.userName }}
        </span>
      </div>
    </template>

    <el-row :gutter="12" style="margin: 10px">
      <el-col :span="12">
        <el-card shadow="always">
          <template #header>
            <div class="card-info-header">对战局数</div>
          </template>
          <div class="card-content">
            <el-icon class="icon">
              <GoldMedal/>
            </el-icon>
            胜: {{ rankInfo.matchWin }}
            &nbsp;&nbsp;&nbsp;&nbsp;
            <el-icon class="icon">
              <Star/>
            </el-icon>
            负: {{ rankInfo.matchLost }}
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="always">
          <template #header>
            <div class="card-info-header">赛季积分</div>
          </template>
          <div class="card-content">
            <el-icon class="icon">
              <TrophyBase/>
            </el-icon>
            <span>{{ rankInfo.score }}</span>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </el-dialog>
</template>

<script>
import {getRankByUid} from "@/api/rankBoardApi";

export default {
  data() {
    return {
      visible: false,
      userInfo: {
        avatarUrl: '',
        userName: ''
      },
      rankInfo: {
        score: 0,
        matchWin: 0,
        matchLost: 0
      }
    }
  },
  methods: {
    async show(data) {
      this.visible = true
      this.userInfo = data
      await getRankByUid(data.userId).then(res => {
        this.rankInfo = res.data
      })
    }
  }
}
</script>

<style scoped>
.card-info-header {
  color: #62aaf4;
  font-size: 16px;
  font-weight: bold;
  text-align: center;
}

.card-content {
  display: flex;
  align-items: center;
  justify-content: center;
}

.icon {
  font-size: 20px;
  margin-right: 4px;
}
</style>