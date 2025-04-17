<template>
  <div>
    <el-card class="rank-card">
      <el-table
          :data="rankData"
          class="rank-table"
          :row-class-name="tableRowClassName"
      >
        <el-table-column label="用户" align="center">
          <template #default="{ row }">
            <div style="display: flex; align-items: center; justify-content: center;">
              <el-avatar
                  :size="30"
                  :src="row.avatarUrl"
                  fit="cover"
                  style="margin-right: 8px;"
              />
              <span>{{ row.userName }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column
            prop="index"
            label="排名"
            align="center"
        >
          <template #default="{ row }">
            <el-tag type="primary">
              {{ row.index }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
            prop="matchCount"
            label="局数"
            align="center"
        />
        <el-table-column
            prop="score"
            label="积分"
            align="center"
        >
          <template #default="{ row }">
            <div style="display: flex; align-items: center; justify-content: center;">
              <el-icon style="margin-right: 4px;">
                <StarFilled />
              </el-icon>
              <span>{{ row.score }}</span>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import {listRankBoard} from "@/api/rankBoardApi"

export default {
  props: {
    rankType: String
  },
  data() {
    return {
      activeType: this.rankType,
      rankData: []
    }
  },
  mounted() {
    this.listTable()
  },
  methods: {
    listTable() {
      listRankBoard(this.activeType).then(res => {
        this.rankData = res.data
      })
    },
    tableRowClassName({rowIndex}) {
      return rowIndex % 2 === 0 ? 'odd-row' : 'even-row'
    }
  }
}
</script>

<style scoped>
.rank-card {
  width: 100%;
  height: 100%;
  margin: auto;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.rank-table {
  width: 100%;
  height: calc(100vh - 380px);
  overflow-y: auto;
}

.odd-row {
  background-color: rgba(237, 247, 251, 0.98) !important;
}

.even-row {
  background-color: #ffffff !important;
}
</style>
