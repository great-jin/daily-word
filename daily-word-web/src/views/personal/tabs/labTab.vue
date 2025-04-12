<template>
  <div>
    <el-row justify="center">
      <el-col :span="24">
        <div style="display: flex; justify-content: center; align-items: center;">
          <el-button
              type="primary"
              @click="generate"
              style="margin-right: 14px"
          >
            生成邀请码
          </el-button>

          <el-tooltip
              effect="dark"
              content="每个用户拥有 6 个邀请码名额"
              placement="top"
          >
            <div style="display: flex; align-items: center;">
              <el-icon style="cursor: pointer;">
                <QuestionFilled/>
              </el-icon>
            </div>
          </el-tooltip>
        </div>
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="24">
        <el-table
            :data="tableData"
            class="code-table"
        >
          <el-table-column
              prop="index"
              align="center"
              label="序号"
          >
            <template #default="scope">
              {{ scope.$index + 1 }}
            </template>
          </el-table-column>
          <el-table-column
              width="160"
              prop="code"
              align="center"
              label="邀请码"
          />
          <el-table-column
              prop="status"
              align="center"
              label="激活状态"
          >
            <template #default="{ row }">
              <el-tag type="primary" v-if="row.status === 1">未激活</el-tag>
              <el-tag type="info" v-else-if="row.status === 2">已激活</el-tag>
              <el-tag type="warning" v-else>已过期</el-tag>
            </template>
          </el-table-column>
          <el-table-column
              width="160"
              prop="generateDate"
              align="center"
              label="生成时间"
          />
          <el-table-column
              width="160"
              prop="expireDate"
              align="center"
              label="过期时间"
          />
          <el-table-column
              label="操作"
              align="center"
              width="140"
              fixed="right"
          >
            <template #default="{ row }">
              <el-button
                  type="primary"
                  @click="reActive(row.id)"
                  :disabled="row.status !== 3"
                  link
              >激活
              </el-button>
              <el-button type="primary" @click="removeCode(row.id)" link>删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
  </div>
</template>

<script>

import {
  deleteCode,
  generateCode,
  listInviteCode,
  reactiveCode
} from "@/api/inviteCodeApi";

export default {
  data() {
    return {
      tableData: []
    }
  },
  mounted() {
    this.listTable()
  },
  methods: {
    listTable() {
      listInviteCode().then(res => {
        this.tableData = res.data
      })
    },
    async generate() {
      if (this.tableData.length >= 6) {
        this.$message.warning('最多生成 6 份邀请码!')
        return
      }

      await generateCode().then(() => {
        // 重新刷新
        this.listTable()
      })
    },
    async reActive(data) {
      await reactiveCode(data).then(res => {
        if (res.code === 200 && res.data) {
          this.$message.success('激活成功')
          // 重新刷新
          this.listTable()
        }
      })
    },
    async removeCode(data) {
      await deleteCode(data).then(res => {
        if (res.code === 200 && res.data) {
          this.$message.success('删除成功')
          // 重新刷新
          this.listTable()
        }
      })
    }
  }
}
</script>

<style scoped>
.code-table {
  width: 70%;
  max-height: calc(100vh - 400px);
  margin: 40px auto 0 auto;
  overflow-y: scroll;
}
</style>