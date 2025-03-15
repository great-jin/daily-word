<template>
  <el-row :gutter="20" class="container">
    <el-col :span="14">
      <!-- 基本信息 -->
      <el-row :gutter="12" style="padding: 0 10px 10px 10px">
        <el-col :span="8">
          <el-card header="当前赛季" shadow="always">
            <div class="card-content">
              <el-icon class="icon">
                <Medal/>
              </el-icon>
              {{ cardData.season }}
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card header="对战局数" shadow="always">
            <div class="card-content">
              <el-icon class="icon">
                <GoldMedal/>
              </el-icon>
              胜: {{ cardData.winNum }}
              &nbsp;&nbsp;
              <el-icon class="icon">
                <Star/>
              </el-icon>
              负: {{ cardData.lostNum }}
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card header="我的积分" shadow="always">
            <div class="card-content">
              <el-icon class="icon">
                <TrophyBase/>
              </el-icon>
              <span>{{ cardData.score }}</span>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 创建房间 -->
      <el-row :gutter="12" class="room-row">
        <el-col
            :span="6"
            v-for="(item) in roomType"
        >
          <el-card shadow="always" class="room-card">
            <template #header>
              <div class="card-header">
                <el-icon><WindPower /></el-icon>
                {{ item.label }}
              </div>
            </template>
            <el-button
                type="primary"
                @click="createRoom(item.value)"
                style="height: 60px"
            >
              开始匹配
            </el-button>
          </el-card>
        </el-col>

        <RoomDialog ref="roomDialog"/>
      </el-row>
    </el-col>

    <!-- 排行榜 -->
    <el-col :span="10">
      <el-tabs
          v-model="activeRankTab"
          type="card"
      >
        <el-tab-pane
            v-for="(item) in rankTypes"
            :label="item.label"
            :name="item.value"
        >
          <RankBoard ref="rankBoard"/>
        </el-tab-pane>
      </el-tabs>
    </el-col>
  </el-row>
</template>

<script>
import RankBoard from "./element/rankBoard.vue";
import RoomDialog from "./element/roomDialog.vue";
import {CATALOG_ARRAY, ROOM_ARRAY} from "@/views/competition/const";

export default {
  inject: ['reload'],
  components: {
    RankBoard,
    RoomDialog
  },
  data() {
    return {
      roomType: ROOM_ARRAY,
      rankTypes: CATALOG_ARRAY,
      activeRankTab: 'CET4',
      cardData: {
        season: '第 1 赛季',
        score: '1000',
        winNum: 30,
        lostNum: 12
      }
    }
  },
  methods: {
    createRoom(type) {
      this.$refs.roomDialog.show(type);
    }
  }
}
</script>

<style>
.container {
  width: 100%;
  height: 100%;
}

.room-container {
  width: 100%;
  height: 100%;
  border-radius: 25px;
}

.room-row {
  padding: 10px;
  height: calc(100vh - 320px);
}

.room-card {
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background: #f8eefc;
}

.card-header {
  font-size: 18px;
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
