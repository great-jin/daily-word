<template>
  <div>
    <el-row>
      <el-col :span="24">
        <el-avatar
            :size="80"
            fit="fill"
            :src="userInfo.avatar"
            @error="avatarErrorHandler"
        />
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="24">
        <el-descriptions
            :column="1"
            style="padding: 14px 200px"
            border
        >
          <el-descriptions-item label="用&nbsp;户&nbsp;名">
            {{ userInfo.userName }}
          </el-descriptions-item>
          <el-descriptions-item label="电子邮箱">
            {{ userInfo.email }}
          </el-descriptions-item>
          <el-descriptions-item label="注册时间">
            {{ userInfo.registerTime.substring(0, 10) }}
          </el-descriptions-item>
        </el-descriptions>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {getDetails} from "@/api/userDetails";

export default {
  data() {
    return {
      userInfo: {
        avatar: '',
        userName: null,
        email: null,
        registerTime: '',
      }
    }
  },
  mounted() {
    getDetails().then(res => {
      this.userInfo = res.data
    })
  },
  methods: {
    avatarErrorHandler() {
      this.userInfo.avatar = 'https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png'
    }
  }
}
</script>

<style>
</style>