<template>
  <el-row :gutter="14" class="container">
    <el-col :span="14">
      <!-- 基本信息 -->
      <el-row :gutter="12" style="padding-bottom: 10px">
        <el-col :span="8">
          <el-card shadow="always">
            <template #header>
              <div class="card-info-header">当前赛季</div>
            </template>
            <div class="card-content">
              <el-icon class="icon">
                <Medal/>
              </el-icon>
              第 {{ cardData.season }} 赛季
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="always">
            <template #header>
              <div class="card-info-header">对战局数</div>
            </template>
            <div class="card-content">
              <el-icon class="icon">
                <GoldMedal/>
              </el-icon>
              胜: {{ cardData.matchWin }}
              &nbsp;&nbsp;
              <el-icon class="icon">
                <Star/>
              </el-icon>
              负: {{ cardData.matchLost }}
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="always">
            <template #header>
              <div class="card-info-header">我的积分</div>
            </template>
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
                <el-icon>
                  <WindPower/>
                </el-icon>
                &nbsp;
                <el-tooltip
                    class="box-item"
                    effect="dark"
                    :content="item.describe"
                    placement="top-start"
                >
                  {{ item.label }}
                </el-tooltip>
              </div>
            </template>

            <el-button
                type="primary"
                @click="createRoom(item.value)"
                style="height: 60px"
            >
              开始挑战
            </el-button>
          </el-card>
        </el-col>

        <RoomDialog ref="roomDialog"/>
      </el-row>
    </el-col>

    <!-- 匹配记录 -->
    <el-col :span="10">
      <HistoryTable ref="historyTable"/>
    </el-col>
  </el-row>
</template>

<script>
import {CATALOG_ARRAY} from "@/dict/catalogDict";
import {TYPE_OPTIONS} from "@/dict/rankTypeDict";
import RoomDialog from "./roomDialog.vue";
import HistoryTable from "./history/index.vue";
import {getUserRank} from "@/api/rankBoardApi";
import {getToken} from "@/util/AuthUtil";

export default {
  inject: ['reload'],
  components: {
    HistoryTable,
    RoomDialog
  },
  data() {
    return {
      roomType: TYPE_OPTIONS,
      rankTypes: CATALOG_ARRAY,
      activeRankTab: 'CET4',
      cardData: {
        season: 1,
        score: 0,
        matchWin: 0,
        matchLost: 0
      }
    }
  },
  mounted() {
    if (getToken()[0] === '') {
      return
    }

    getUserRank().then(res => {
      this.cardData = res.data
    })
  },
  methods: {
    createRoom(type) {
      this.$refs.roomDialog.show(type);
    }
  }
}
</script>

<style scoped>
.container {
  width: 100%;
  height: 100%;
}

.room-row {
  height: calc(100vh - 330px);
}

.room-card {
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background: #f8eefc;
}

.card-info-header {
  color: #62aaf4;
  font-size: 16px;
  font-weight: bold;
  text-align: center;
}

.card-header {
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
