<template>
  <el-row :gutter="14" class="container">
    <el-col :span="14" class="board-col">
      <div class="title-header">
        <el-tooltip
            class="box-item"
            effect="dark"
            content="仅展示前 50 名用户"
            placement="bottom"
        >
          <span>积分排行榜</span>
        </el-tooltip>
      </div>

      <el-tabs
          v-model="activeRankTab"
          type="card"
      >
        <el-tab-pane
            v-for="(item) in rankTypes"
            :label="item.label"
            :name="item.value"
        >
          <RankBoard v-if="activeRankTab === item.value" :rank-type="item.value"/>
        </el-tab-pane>
      </el-tabs>
    </el-col>

    <el-col :span="10">
      <FriendTable ref="friendTable"/>
    </el-col>
  </el-row>
</template>

<script>
import {CATALOG_ARRAY} from "@/dict/catalogDict";
import {TYPE_OPTIONS} from "@/dict/rankTypeDict";
import RankBoard from "./board/index.vue";
import FriendTable from "./friend/index.vue";
import {getUserRank} from "@/api/rankBoardApi";

export default {
  inject: ['reload'],
  components: {
    RankBoard,
    FriendTable
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
    getUserRank().then(res => {
      this.cardData = res.data
    })
  },
  methods: {}
}
</script>

<style scoped>
.container {
  width: 100%;
  height: 100%;
  padding-left: 10px;
}

.title-header {
  font-size: 18px;
  font-weight: bold;
  text-align: center;
  margin: 10px 0;
}

.board-col {
  padding: 10px;
  border-radius: 10px;
  background: white;
}

::v-deep(.el-tabs__item) {
  font-weight: bold;
}
</style>
