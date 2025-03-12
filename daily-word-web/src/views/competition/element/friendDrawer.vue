<template>
  <el-drawer
      v-model="visible"
      title="我的好友"
  >
    <el-button
        type="primary"
        style="margin-bottom: 20px; float: left"
    >添加好友
    </el-button>
    <el-button
        type="primary"
        style="margin-bottom: 20px; float: right"
    >刷新列表
    </el-button>

    <el-card class="friend-card">
      <el-table
          :data="friendData"
          class="friend-table"
          :row-class-name="tableRowClassName"
      >
        <el-table-column
            prop="username"
            label="用户名"
            align="center"
        />
        <el-table-column
            prop="status"
            label="状态"
            align="center"
        >
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '在线' : '离线' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
            prop="operate"
            label=""
            fixed="right"
            align="center"
        >
          <template #default>
            <el-button link type="primary">
              邀请
            </el-button>
            <el-button link type="warning">
              删除
            </el-button>
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
      friendData: [],
    }
  },
  mounted() {
    for (let i = 1; i < 100; i++) {
      this.friendData.push(
          {
            username: "Test-" + i,
            status: i % 4 === 0 ? 0 : 1
          })
    }
  },
  methods: {
    show() {
      this.visible = true
    },
    tableRowClassName({rowIndex}) {
      return rowIndex % 2 === 0 ? "odd-row" : "even-row";
    }
  }
}
</script>

<style scoped>
.friend-card {
  width: 100%;
  margin: auto;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.friend-table {
  width: 100%;
  height: calc(100vh - 220px);
  overflow-y: auto;
}

.odd-row {
  background-color: rgba(237, 247, 251, 0.98) !important;
}

.even-row {
  background-color: #ffffff !important;
}
</style>
