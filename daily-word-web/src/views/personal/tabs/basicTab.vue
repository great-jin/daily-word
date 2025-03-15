<template>
  <div>
    <el-row>
      <el-col :span="24">
        <el-avatar
            :size="80"
            fit="fill"
            :src="userInfo.url"
            @error="avatarErrorHandler"
        />
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="24">
        <el-descriptions
            :column="1"
            direction="vertical"
            style="padding: 14px 24px"
            border
        >
          <el-descriptions-item label="用户名">
            {{ userInfo.username }}
          </el-descriptions-item>
          <el-descriptions-item label="邮箱">
            {{ userInfo.email }}
          </el-descriptions-item>
          <el-descriptions-item label="注册时间">
            {{ userInfo.registerTime }}
          </el-descriptions-item>
        </el-descriptions>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  data() {
    return {
      userInfo: {
        url: 'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg',
        username: 'admin',
        email: 'ibudai56@163.com',
        registerTime: '2025-03-21',
      }
    }
  },
  methods: {
    avatarErrorHandler() {
      // 默认头像
      this.userInfo.url = 'https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png'
    }
  }
}
</script>

<style>
</style>