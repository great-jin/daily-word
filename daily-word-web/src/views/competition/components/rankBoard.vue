<template>
  <div>
    <el-card class="rank-card">
      <template #header>
        <div class="card-header">
          <span>积分排行榜</span>
        </div>
      </template>

      <el-table
          :data="rankData"
          class="rank-table"
          :row-class-name="tableRowClassName"
      >
        <el-table-column
            prop="rank"
            label="排名"
            align="center"
        />
        <el-table-column
            prop="username"
            label="用户名"
            align="center"
        />
        <el-table-column
            prop="matchCount"
            label="局数"
            align="center"
        />
        <el-table-column
            prop="score"
            label="积分"
            align="center"
        />
      </el-table>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      rankData: [],
    }
  },
  mounted() {
    for (let i = 1; i < 100; i++) {
      this.rankData.push({
        rank: i,
        username: "Test-" + i,
        matchCount: Math.floor(Math.random() * (95 - 70 + 1)) + 70,
        score: Math.floor(Math.random() * (95 - 70 + 1)) + 70
      })
    }
  },
  methods: {
    tableRowClassName({rowIndex}) {
      return rowIndex % 2 === 0 ? "odd-row" : "even-row";
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

.card-header {
  font-size: 18px;
  font-weight: bold;
  text-align: center;
}

.rank-table {
  width: 100%;
  height: calc(100vh - 360px);
  overflow-y: auto;
}

.odd-row {
  background-color: rgba(237, 247, 251, 0.98) !important;
}

.even-row {
  background-color: #ffffff !important;
}
</style>
