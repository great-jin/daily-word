<template>
  <div>
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
import {getDetails} from "@/api/userDetailsApi";

export default {
  data() {
    return {
      disableForm: true,
      detailForm: {
        userName: null,
        registerTime: null,
      }
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
    }
  }
}
</script>

<style>
</style>