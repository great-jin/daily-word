<template>
  <el-drawer
      v-model="visible"
      size="50%"
      direction="ltr"
      @close="close"
  >
    <template #header>
      <span style="font-weight: bold; font-size: 18px">匹配记录</span>
    </template>

    <el-card class="history-card">
      <el-table
          :data="historyData"
          class="history-table"
          :row-class-name="tableRowStyle"
      >
        <el-table-column
            prop="rankMode"
            label="匹配方式"
            align="center"
        />
        <el-table-column
            prop="roomMode"
            label="匹配模式"
            align="center"
        />
        <el-table-column
            prop="catalogue"
            label="词汇"
            align="center"
        >
          <template #default="{ row }">
            <el-tag type="primary">{{ row.catalogue }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
            prop="wordCount"
            label="组数"
            align="center"
        />
        <el-table-column
            prop="time"
            label="耗时"
            align="center"
        />
        <el-table-column
            prop="score"
            label="得分"
            align="center"
        />
        <el-table-column
            prop="result"
            label="胜负"
            align="center"
        >
          <template #default="{ row }">
            <el-tag :type="row.result ? 'success' : 'danger'">
              {{ row.result ? '胜利' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </el-drawer>
</template>

<script>
export default {
  data() {
    return {
      visible: false,
      historyData: [],
    }
  },
  methods: {
    show() {
      this.visible = true

      this.listTableData()
    },
    close() {
      this.visible = false
      this.historyData = []
    },
    listTableData() {
      for (let i = 0; i < 40; i++) {
        this.historyData.push({
          rankMode: '随机匹配',
          roomMode: '双人对战',
          catalogue: '四级词汇',
          wordCount: '30个/组',
          time: '60s',
          score: 5,
          result: true,
        })
      }
    },
    tableRowStyle({rowIndex}) {
      return rowIndex % 2 === 0 ? "odd-row" : "even-row";
    }
  }
}
</script>

<style scoped>
.history-card {
  width: 100%;
  margin: auto;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.history-table {
  width: 100%;
  height: calc(100vh - 180px);
  overflow-y: auto;
}

.odd-row {
  background-color: rgba(237, 247, 251, 0.98) !important;
}

.even-row {
  background-color: #ffffff !important;
}
</style>
