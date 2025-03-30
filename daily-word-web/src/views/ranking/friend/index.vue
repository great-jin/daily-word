<template>
  <div>
    <el-card class="friend-card">
      <span style="font-weight: bold; font-size: 18px">我的好友</span>

      <el-button
          type="primary"
          @click="addFriend"
          style="margin-bottom: 20px; float: left"
      >
        添加好友
      </el-button>
      <el-button
          type="primary"
          @click="requestList"
          style="margin-bottom: 20px; float: right"
      >
        我的申请
      </el-button>

      <DetailDrawer ref="detailDrawer"/>
      <RequestDrawer ref="requestDrawer"/>

      <el-table
          :data="friendData"
          class="friend-table"
          :row-class-name="tableRowStyle"
          @row-click="selectRow"
      >
        <el-table-column
            prop="userName"
            label="用户名"
            align="center"
        />
        <el-table-column
            prop="online"
            label="状态"
            align="center"
        >
          <template #default="{ row }">
            <el-tag :type="row.online ? 'success' : 'danger'">
              {{ row.online ? '在线' : '离线' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
            prop="operate"
            label=""
            fixed="right"
            align="center"
        >
          <template #default="{ row }">
            <el-button
                type="primary"
                @click="tableOptions('invite', row)"
                link
            >
              邀请
            </el-button>
            <el-popconfirm
                title="确认删除?"
                cancel-button-text="否"
                confirm-button-text="是"
                @confirm="tableOptions('delete', row)"
            >
              <template #reference>
                <el-button type="warning" link>删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import {deleteById, list} from "@/api/userFriendApi"
import DetailDrawer from "./components/detailDrawer.vue";
import RequestDrawer from "./components/requestDrawer.vue";

export default {
  components: {
    DetailDrawer,
    RequestDrawer
  },
  data() {
    return {
      visible: false,
      friendData: [],
    }
  },
  mounted() {
    this.listTableData()
  },
  methods: {
    close() {
      this.visible = false
      this.friendData = []
    },
    listTableData() {
      list().then(res => {
        this.friendData = res.data
      })
    },
    tableRowStyle({rowIndex}) {
      return rowIndex % 2 === 0 ? "odd-row" : "even-row";
    },
    addFriend() {
      this.$message.info('功能开发中，敬请期待！')
    },
    requestList() {
      this.$refs.requestDrawer.show(null)
    },
    selectRow(row, column, event) {
      this.$refs.detailDrawer.show(row)
    },
    tableOptions(type, row) {
      switch (type) {
        case 'invite':
          this.$message.info('功能开发中，敬请期待！')
          break
        case 'delete':
          deleteById(row.userId).then(res => {
            if (res.data) {
              this.$message.success('删除成功')
              this.listTableData()
            } else {
              this.$message.error('删除失败')
            }
          })
          break
      }
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
  height: calc(100vh - 294px);
  overflow-y: auto;
}

.odd-row {
  background-color: rgba(237, 247, 251, 0.98) !important;
}

.even-row {
  background-color: #ffffff !important;
}
</style>
