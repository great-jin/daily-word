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

      <SearchDialog ref="searchDialog"/>
      <DetailDialog ref="detailDialog"/>
      <RequestDrawer ref="requestDrawer"/>

      <el-table
          :data="friendData"
          class="friend-table"
          :row-class-name="tableRowStyle"
          @row-click="selectRow"
      >
        <el-table-column label="用户名" align="center">
          <template #default="{ row }">
            <div style="display: flex; align-items: center; justify-content: center;">
              <el-avatar
                  :size="30"
                  :src="row.avatarUrl"
                  fit="cover"
                  style="margin-right: 10px;"
              />
              <span>{{ row.userName }}</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column
            prop="online"
            label="状态"
            align="center"
        >
          <template #default="{ row }">
            <el-tag :type="row.online ? 'success' : 'info'">
              {{ row.online ? '在线' : '离线' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
            prop="lastOnline"
            label="最近在线"
            align="center"
        />
        <el-table-column
            prop="operate"
            label=""
            fixed="right"
            align="center"
        >
          <template #default="{ row }">
            <el-popconfirm
                width="200"
                title="确认删除此好友吗?"
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
import {deleteById, listFriends} from "@/api/userFriendApi"
import DetailDialog from "./components/detailDialog.vue";
import RequestDrawer from "./components/requestDrawer.vue";
import SearchDialog from "./components/searchDialog.vue";

export default {
  components: {
    SearchDialog,
    DetailDialog,
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
    listTableData() {
      listFriends().then(res => {
        this.friendData = res.data
      })
    },
    tableRowStyle({rowIndex}) {
      return rowIndex % 2 === 0 ? "odd-row" : "even-row";
    },
    addFriend() {
      this.$refs.searchDialog.show()
    },
    requestList() {
      this.$refs.requestDrawer.show()
    },
    selectRow(row, column, event) {
      if (column.property === "operate") {
        // 点击按钮时，不触发 selectRow
        return
      }

      this.$refs.detailDialog.show(row)
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
