<template>
  <el-row :gutter="20" class="container">
    <el-col :span="14">
      <div class="room-container">
        <el-row :gutter="12" style="padding: 10px">
          <el-col :span="8">
            <el-card header="当前赛季" shadow="always">
              <div class="card-content">
                <el-icon class="icon"><Medal /></el-icon>
                {{ cardData.season }}
              </div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card header="对战局数" shadow="always">
              <div class="card-content">
                <el-icon class="icon"><GoldMedal/></el-icon>
                胜: {{ cardData.count }}
                &nbsp;&nbsp;
                <el-icon class="icon"><Star /></el-icon>
                负: {{ cardData.count }}
              </div>
            </el-card>
          </el-col>
          <el-col  :span="8">
            <el-card header="我的积分" shadow="always">
              <div class="card-content">
                <el-icon class="icon"><TrophyBase /></el-icon>
                <span>{{ cardData.score }}</span>
              </div>
            </el-card>
          </el-col>
        </el-row>

        <el-row style="margin-top: 20px">
          <el-col :span="24">
            <el-button type="primary" @click="showRank">我的好友</el-button>

            <el-button type="primary" @click="createRoom">创建房间</el-button>

            <FriendDrawer ref="friendDrawer"/>
            <RoomDialog ref="roomDialog"/>
          </el-col>
        </el-row>
      </div>
    </el-col>

    <!-- 排行榜 -->
    <el-col :span="10">
      <div class="rank-container">
        <RankBoard ref="rankBoard"/>
      </div>
    </el-col>
  </el-row>
</template>

<script>
import RankBoard from "./element/rankBoard.vue";
import FriendDrawer from "./element/friendDrawer.vue";
import RoomDialog from "./element/roomDialog.vue";

export default {
  inject: ['reload'],
  components: {
    RankBoard,
    FriendDrawer,
    RoomDialog
  },
  data() {
    return {
      cardData: {
        season: '第 1 赛季',
        score: '1000',
        count: '26'
      }
    }
  },
  methods: {
    showRank() {
      this.$refs.friendDrawer.show();
    },
    createRoom() {
      this.$refs.roomDialog.show();
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
  background: #d9e9f4;
}

.rank-container {
  width: 100%;
  height: 100%;
  border-radius: 25px;
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
