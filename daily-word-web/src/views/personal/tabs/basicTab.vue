<template>
  <div>
    <el-row>
      <el-col :span="24">
        <el-avatar
            :size="80"
            fit="fill"
            :src="avatar"
            @error="avatarErrorHandler"
        />
      </el-col>
    </el-row>

    <el-row style="margin-top: 20px">
      <el-col :span="24">
        <el-row
            :gutter="20"
            v-for="item in details"
            style="margin: 16px 0"
        >
          <el-col :span="12" style="text-align: right">
            <span>{{ item.label }}:</span>
          </el-col>
          <el-col :span="12" style="text-align: left">
            <span style="color: #7d7c7c">{{ item.value }}</span>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {getDetails} from "@/api/userDetailsApi";

export default {
  data() {
    return {
      avatar: '',
      details: [
        {
          label: '用户名',
          value: ''
        },
        {
          label: '电子邮箱',
          value: ''
        },
        {
          label: '注册时间',
          value: ''
        }
      ]
    }
  },
  mounted() {
    getDetails().then(res => {
      const userInfo = res.data
      this.avatar = userInfo.avatar
      this.details = [
        {
          label: '用户名',
          value: userInfo.userName
        },
        {
          label: '电子邮箱',
          value: userInfo.email
        },
        {
          label: '注册时间',
          value: userInfo.registerTime.substring(0, 10)
        }
      ]
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