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
              content="每个用户每个月拥有 10 个邀请码名额"
              placement="top"
          >
            <div style="display: flex; align-items: center;">
              <el-icon style="cursor: pointer;">
                <QuestionFilled />
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
              prop="createTime"
              align="center"
              label="生成时间"
          />
          <el-table-column
              width="160"
              prop="expireTime"
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
              <el-button type="primary" @click="removeCode(row)" link>删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
  </div>
</template>

<script>

export default {
  data() {
    return {
      tableData: []
    }
  },
  methods: {
    generate() {
      if (this.tableData.length >= 10) {
        this.$message.warning('最多生成 10 份邀请码!')
        return
      }

      this.tableData.push({
        code: this.generateCode(),
        status: Math.floor(Math.random() * 3) + 1,
        createTime: '2025-04-02',
        expireTime: '2025-04-09'
      })
    },
    removeCode(data) {
      this.tableData = this.tableData.filter(it => it.code !== data.code)
    },
    generateCode() {
      const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
      let result = '';
      for (let i = 0; i < 8; i++) {
        const randomIndex = Math.floor(Math.random() * chars.length);
        result += chars[randomIndex];
      }
      return result;
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