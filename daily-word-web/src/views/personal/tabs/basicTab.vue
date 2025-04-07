<template>
  <div>
    <el-row style="margin-bottom: 14px">
      <el-col :span="24" style="display: flex; justify-content: center;">
        <el-upload
            v-model:file-list="fileList"
            class="avatar-uploader"
            :show-file-list="false"
            accept="image/*"
            :http-request="customUpload"
        >
          <div class="avatar-wrapper">
            <el-avatar
                :size="80"
                fit="fill"
                :src="detailForm.avatar"
            />
            <div class="avatar-overlay">修改头像</div>
          </div>
        </el-upload>
      </el-col>
    </el-row>


    <el-row style="padding: 10px 100px 10px 200px">
      <el-col :span="16">
        <el-form
            ref="regForm"
            :model="detailForm"
            label-width="100px"
            :disabled="disableForm"
        >
          <el-form-item label="用&nbsp;户&nbsp;名:" prop="userName">
            <el-input
                v-model="detailForm.userName"
            />
          </el-form-item>
          <el-form-item label="注册时间:" prop="registerTime">
            <el-input
                v-model="detailForm.registerTime"
                disabled
            />
          </el-form-item>
        </el-form>
      </el-col>

      <el-col :span="8" style="float: right">
        <el-button
            type="primary"
            @click="saveModify"
            :disabled="disableForm"
        >保存
        </el-button>
        <el-button @click="enableModify">修改</el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {getDetails, uploadAvatar} from "@/api/userDetailsApi";

export default {
  data() {
    return {
      disableForm: true,
      detailForm: {
        avatar: null,
        userName: null,
        registerTime: null,
      },
      fileList: []
    }
  },
  mounted() {
    getDetails().then(res => {
      this.detailForm = res.data
    })
  },
  methods: {
    enableModify() {
      this.disableForm = false
    },
    saveModify() {
      this.disableForm = true
    },
    async customUpload(data) {
      const file = data.file
      const isImage = file.type.startsWith('image/')
      if (!isImage) {
        this.$message.warning('只能上传图片文件！')
        return
      }

      // 上传头像
      const formData = new FormData()
      formData.append('file', file)
      await uploadAvatar(formData).then(res => {
        this.detailForm.avatar = res.data
      })
    }
  }
}
</script>

<style scoped>
.avatar-wrapper {
  position: relative;
  width: 80px;
  height: 80px;
  cursor: pointer;
}

.avatar-overlay {
  position: absolute;
  bottom: 0;
  width: 100%;
  height: 40%;
  background: rgba(0, 0, 0, 0.5);
  color: #fff;
  font-size: 12px;
  line-height: 32px;
  text-align: center;
  opacity: 0;
  transition: opacity 0.3s;
  border-bottom-left-radius: 50%;
  border-bottom-right-radius: 50%;
}

.avatar-wrapper:hover .avatar-overlay {
  opacity: 1;
}
</style>
