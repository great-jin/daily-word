<template>
  <el-dialog
      v-model="visible"
      title="好友搜索"
      width="24%"
      @close="closeDialog"
  >
    <div style="margin-top: 20px">
      <!-- 搜索用户 -->
      <el-row style="margin-bottom: 20px">
        <el-col :span="24">
          <el-input
              v-model="username"
              placeholder="输入用户名进行搜索"
              style="width: 200px;"
          >
            <template #prefix>
              <el-icon class="el-input__icon">
                <search/>
              </el-icon>
            </template>
          </el-input>
          <el-button
              type="primary"
              style="margin-left: 10px"
              @click="doSearch"
          >
            <el-icon>
              <Search/>
            </el-icon>
          </el-button>
        </el-col>
      </el-row>

      <!-- 用户信息 -->
      <el-row v-if="userInfo !== null">
        <el-col :span="24">
          <el-card shadow="hover">
            <div style="display: flex; justify-content: center; align-items: center;">
              <div style="display: flex; align-items: center; margin-right: 80px;">
                <el-avatar
                    :src="userInfo.avatarUrl"
                    size="medium"
                />
                <span style="margin-left: 10px;">{{ userInfo.username }}</span>
              </div>

              <el-button type="primary" @click="addUser(userInfo.username)" link>添加好友</el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </el-dialog>
</template>

<script>
import {searchUser, sendInvite} from "@/api/userInviteApi";
import {isNameValid} from "@/util/RegexUtil";

export default {
  data() {
    return {
      visible: false,
      username: '',
      userInfo: null
    }
  },
  methods: {
    show() {
      this.visible = true
    },
    closeDialog() {
      this.visible = false
      this.username = ''
      this.userInfo = null
    },
    async doSearch() {
      // 格式校验
      const username = this.username
      if (username === '') {
        this.$message.warning('请先输入用户名')
        return
      }
      if (username.length > 50) {
        this.$message.warning('用户名长度不能超过 50')
        return
      }
      if (!isNameValid(username)) {
        this.$message.warning('用户名只允许包含字母与数字')
        return
      }

      // 查询用户
      await searchUser(username).then(res => {
        if (res.code !== 200) {
          this.$message.warning(res.message)
        } else {
          this.userInfo = res.data
        }
      })
    },
    addUser() {
      sendInvite(this.userInfo.userId).then(res => {
        if (res.code === 200 && res.data) {
          this.$message.success('邀请发送成功')
        }
        this.closeDialog()
      })
    }
  }
}
</script>

<style scoped>
</style>