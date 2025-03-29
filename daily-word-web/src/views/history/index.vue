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

      <el-config-provider :locale="locale">
        <el-pagination
            layout="prev, pager, next, sizes, total"
            v-model:pageNo-page="pagination.pageNo"
            v-model:page-size="pagination.pageSize"
            :page-sizes="pagination.options"
            :total="pagination.total"
            @size-change="handlePaging('size', $event)"
            @pageNo-change="handlePaging('page', $event)"
            style="margin: 10px 0; float: right"
        />
      </el-config-provider>
    </el-card>
  </el-drawer>
</template>

<script>
import {listMatchHistory} from "@/api/matchApi";
import zhCn from "element-plus/es/locale/lang/zh-cn";

export default {
  data() {
    return {
      visible: false,
      locale: zhCn,
      historyData: [],
      pagination: {
        pageNo: 1,
        pageSize: 10,
        options: [20, 30, 40, 50],
        total: 0
      }
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
      const _req = {
        pageNo: this.pagination.pageNo,
        pageSize: this.pagination.pageSize
      }
      listMatchHistory(_req).then(res => {
        const pageData = res.data
        this.historyData = pageData.list
        this.pagination.total = pageData.total
      })
    },
    handlePaging(type, value) {
      if (type === 'size') {
        this.pagination.pageSize = value
      } else if (type === 'page') {
        this.pagination.pageNo = value
      }
      // 分页重查
      this.listTableData()
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
  height: calc(100vh - 190px);
  overflow-y: auto;
}

.odd-row {
  background-color: rgba(237, 247, 251, 0.98) !important;
}

.even-row {
  background-color: #ffffff !important;
}
</style>
