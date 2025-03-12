<template>
  <el-row :gutter="20" class="container">
    <el-col :span="14">
      <div class="room-container">
        <el-col :span="24">
          <el-button type="primary" @click="showRank">我的好友</el-button>

          <el-button type="primary" @click="createRoom">创建房间</el-button>

          <FriendDrawer ref="friendDrawer"/>
          <RoomDialog ref="roomDialog"/>
        </el-col>
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
    return {}
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
  padding: 20px 0;
  background: #d9e9f4;
}

.rank-container {
  width: 100%;
  height: 100%;
  background: #d9e9f4;
}
</style>
