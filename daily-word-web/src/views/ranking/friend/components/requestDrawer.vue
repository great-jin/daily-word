<template>
  <el-drawer
      v-model="visible"
      size="30%"
      style="min-height: 400px"
  >
    <template #header>
      <span style="font-weight: bold; font-size: 18px">申请列表</span>
    </template>

    <el-card shadow="hover">
      <el-button-group style="margin-bottom: 20px">
        <el-button @click="clickTypes('toMe')">待处理申请</el-button>
        <el-button @click="clickTypes('fromMe')">我的申请</el-button>
      </el-button-group>

      <div
          v-for="user in userList"
          :key="user.username"
          style="margin-bottom: 10px;"
      >
        <el-card shadow="hover">
          <el-row style="display: flex; justify-content: center; align-items: center;">
            <el-col :span="8">
              <div style="display: flex; align-items: center;">
                <el-avatar
                    :src="user.avatarUrl"
                    size="medium"
                />
                <span style="margin-left: 10px;">{{ user.username }}</span>
              </div>
            </el-col>

            <el-col :span="7">
              <span>{{ user.dateInterval }}</span>
            </el-col>

            <el-col :span="7">
              <!-- 我的待处理 -->
              <template v-if="type==='toMe'">
                <el-button
                    type="primary"
                    @click="clickOption('ok', user)"
                    link
                >同意
                </el-button>
                <el-button
                    type="danger"
                    @click="clickOption('reject', user)"
                    link
                >拒绝
                </el-button>
              </template>

              <!-- 我的申请 -->
              <template v-else>
                <el-tag v-if="user.processStatus === 2" type='danger'>
                  {{ '已拒绝' }}
                </el-tag>
                <el-tag v-else :type="user.processStatus === 0 ? 'warning' : 'primary'">
                  {{ user.processStatus === 0 ? '待同意' : '已同意' }}
                </el-tag>
              </template>
            </el-col>
          </el-row>
        </el-card>
      </div>
    </el-card>
  </el-drawer>
</template>

<script>

import {handleInvite, listRequests} from "@/api/userInviteApi";

export default {
  data() {
    return {
      visible: false,
      type: 'toMe',
      userList: []
    }
  },
  methods: {
    show() {
      this.visible = true

      this.clickTypes('toMe')
    },
    clickTypes(type) {
      this.type = type
      this.userList = []

      listRequests(type).then(res => {
        if (res.code === 200) {
          this.userList = res.data
        }
      })
    },
    clickOption(type, data) {
      let status = 0
      switch (type) {
        case 'ok':
          status = 1
          break
        case 'reject':
          status = 2
          break
      }
      const params = {
        userId: data.userId,
        status: status
      }
      handleInvite(params).then(res => {
        if (res.code === 200 && res.data) {
          this.$message.success('操作成功')
        }
        this.clickTypes(this.type)
      })
    }
  }
}
</script>

<style scoped>
</style>