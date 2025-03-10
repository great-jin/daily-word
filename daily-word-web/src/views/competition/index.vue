<template>
  <el-row :gutter="20" class="container">
    <el-col :span="12">
      <div class="col-container">
        <el-card class="rank-card">
          <template #header>
            <div class="card-header">
              <span>排行榜</span>
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
                prop="time"
                label="耗时"
                align="center"
            />
            <el-table-column
                prop="score"
                label="分数"
                align="center"
            />
            <el-table-column
                prop="operate"
                label=""
                fixed="right"
                align="center"
            >
              <template #default>
                <el-button link type="primary" size="small">
                  挑战
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </div>
    </el-col>

    <el-col :span="12">
      <div class="col-container">
        <span style="margin-right: 6px">词典: </span>
        <el-select
            v-model="reqParams.catalogue"
            placeholder="Select dictionary"
            style="width: 120px; margin-right: 20px"
        >
          <el-option
              v-for="item in catalogues"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>

        <span style="margin-right: 6px">数量: </span>
        <el-select
            v-model="reqParams.size"
            placeholder="Select batch size"
            style="width: 120px; margin-right: 20px"
        >
          <el-option
              v-for="item in batchOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
        <el-button type="primary" @click="startTask">开始</el-button>

        <AnswerDialog ref="answerDialog"/>
      </div>
    </el-col>
  </el-row>
</template>

<script>
import {CATALOG_ARRAY, RANK_DATA, SIZE_ARRAY} from "./const";
import AnswerDialog from "./answerDialog.vue";
import {getTaskContent} from "@/api/wordApi";

export default {
  components: {
    AnswerDialog
  },
  inject: ['reload'],
  data() {
    return {
      rankData: RANK_DATA,
      catalogues: CATALOG_ARRAY,
      batchOptions: SIZE_ARRAY,
      reqParams: {
        catalogue: CATALOG_ARRAY[0].value,
        size: SIZE_ARRAY[0].value,
        offset: 10
      }
    }
  },
  methods: {
    startTask() {
      getTaskContent(this.reqParams).then(res => {
        this.$refs.answerDialog.show(res.data);
      })
    },
    tableRowClassName({rowIndex}) {
      return rowIndex % 2 === 0 ? "odd-row" : "even-row";
    }
  }
}
</script>

<style>
.container {
  height: 100%;
}

.col-container {
  height: 100%;
  background: #d9e9f4;
}

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
  height: calc(100vh - 300px);
  overflow-y: auto;
}

.odd-row {
  background-color: rgba(237, 247, 251, 0.98) !important;
}

.even-row {
  background-color: #ffffff !important;
}
</style>
