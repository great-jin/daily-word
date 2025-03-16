<template>
  <el-drawer
      v-model="visible"
      title="我的好友"
      size="36%"
      @close="close"
  >
    <el-button
        type="primary"
        @click="addFriend"
        style="margin-bottom: 20px; float: left"
    >
      添加好友
    </el-button>
    <el-button
        type="primary"
        @click="listFriend"
        style="margin-bottom: 20px; float: right"
    >
      刷新状态
    </el-button>

    <el-card class="friend-card">
      <el-table
          :data="friendData"
          class="friend-table"
          :row-class-name="tableRowStyle"
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
                @click="tableOptions('invite', row.userName)"
                link
            >
              邀请
            </el-button>
            <el-popconfirm
                title="确认删除?"
                cancel-button-text="否"
                confirm-button-text="是"
                @confirm="tableOptions('delete', row.userName)"
            >
              <template #reference>
                <el-button type="warning" link>删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </el-drawer>
</template>

<script>
import {list} from "@/api/userFriend"
import {getUId} from "@/util/AuthUtil";

export default {
  data() {
    return {
      visible: false,
      friendData: [],
    }
  },
  methods: {
    show() {
      this.visible = true
      this.listFriend()
    },
    close() {
      this.visible = false
      this.friendData = []
    },
    tableRowStyle({rowIndex}) {
      return rowIndex % 2 === 0 ? "odd-row" : "even-row";
    },
    addFriend() {
      this.$message.success('添加好友')
    },
    listFriend() {
      list(getUId()).then(res => {
        this.friendData = res.data
      })
    },
    tableOptions(type, data) {
      switch (type) {
        case 'invite':
          this.$message.success('邀请: ' + data)
          break
        case 'delete':
          this.$message.warning('删除: ' + data)
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
