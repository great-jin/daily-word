<template>
  <div>
    <el-card class="history-card">
      <template #header>
        <div class="history-header">
          <span>匹配记录</span>
        </div>
      </template>

      <el-table
          :data="historyData"
          class="history-table"
          :row-class-name="tableRowStyle"
      >
        <el-table-column
            prop="rankMode"
            label="匹配模式"
            align="center"
            width="120"
            fixed="left"
        >
          <template #default="{ row }">
            <el-tag type="primary">{{ row.rankMode }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
            prop="rankType"
            label="匹配方式"
            align="center"
            width="120"
            fixed="left"
        />
        <el-table-column
            prop="catalog"
            label="词汇"
            align="center"
        >
          <template #default="{ row }">
            <el-tag type="primary">{{ row.catalog }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
            prop="wordCount"
            label="组数"
            align="center"
        />
        <el-table-column
            prop="costSecond"
            label="耗时"
            align="center"
        >
          <template #default="{ row }">
            {{ row.costSecond === null ? '-' : row.costSecond }}
          </template>
        </el-table-column>
        <el-table-column
            prop="score"
            label="得分"
            align="center"
        >
          <template #default="{ row }">
            {{ row.score === null ? '-' : row.score }}
          </template>
        </el-table-column>
        <el-table-column
            prop="result"
            label="胜负"
            align="center"
        >
          <template #default="{ row }">
            <el-tag v-if="row.score === null" type="warning">
              {{ '进行中' }}
            </el-tag>
            <el-tag v-else-if="row.score === 0" type="info">
              {{ '-' }}
            </el-tag>
            <el-tag v-else :type="row.score > 0 ? 'success' : 'danger'">
              {{ row.score > 0 ? '胜利' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
            prop="createTime"
            label="对局时间"
            align="center"
            width="180"
        />
        <el-table-column
            label="操作"
            align="center"
            fixed="right"
        >
          <template #default="{ row }">
            <el-button
                type="primary"
                @click="showDetail(row)"
                :disabled="row.score === null"
                link
            >详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 详情窗口 -->
      <DetailDrawer ref="detailDrawer"/>

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
  </div>
</template>

<script>
import {getRankType} from "@/dict/rankTypeDict";
import {getRankMode} from "@/dict/rankModeDict";
import {listMatchHistory} from "@/api/matchApi";
import DetailDrawer from "./detailDrawer.vue";
import zhCn from "element-plus/es/locale/lang/zh-cn";
import {formatSeconds} from "@/util/commonUtil";

export default {
  components: {
    DetailDrawer,
  },
  data() {
    return {
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
  mounted() {
    this.listTableData()
  },
  methods: {
    async listTableData() {
      const _req = {
        pageNo: this.pagination.pageNo,
        pageSize: this.pagination.pageSize
      }
      await listMatchHistory(_req).then(res => {
        const pageData = res.data
        this.historyData = pageData.list
        this.historyData.forEach(it => {
          it.rankMode = getRankMode(it.rankMode)
          it.rankType = getRankType(it.rankType)
          it.costSecond = formatSeconds(it.costSecond)
          it.createTime = it.createTime == null
              ? '-'
              : it.createTime.replace('T', ' ')
        })
        this.pagination.total = pageData.total
      })
    },
    handlePaging(type, value) {
      if (type === 'size') {
        this.pagination.pageSize = value
      } else if (type === 'page') {
        this.pagination.pageNo = value
      }
      this.listTableData()
    },
    tableRowStyle({rowIndex}) {
      return rowIndex % 2 === 0 ? "odd-row" : "even-row";
    },
    showDetail(data) {
      this.$refs.detailDrawer.show(data);
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

.history-header {
  font-size: 18px;
  font-weight: bold;
  text-align: center;
}

.history-table {
  width: 100%;
  height: calc(100vh - 330px);
  overflow-y: auto;
}

.odd-row {
  background-color: rgba(237, 247, 251, 0.98) !important;
}

.even-row {
  background-color: #ffffff !important;
}
</style>
