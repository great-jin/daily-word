<template>
  <div>
    <div>
      <el-row>
        <el-col :span="24">
          <el-form
              ref="passwordForm"
              :rules="rules"
              label-width="100px"
              :model="passwordForm"
              :disabled="disableForm"
              style="max-width: 400px; margin: 20px auto"
          >
            <el-form-item label="原始密码:" prop="originPwd">
              <el-input
                  type="password"
                  v-model="passwordForm.originPwd"
                  placeholder="请输入原始密码"
              />
            </el-form-item>
            <el-form-item label="新&nbsp;&nbsp;密&nbsp;&nbsp;码:" prop="password">
              <el-input
                  type="password"
                  v-model="passwordForm.password"
                  placeholder="请输入新密码"
              />
            </el-form-item>
            <el-form-item label="确认密码:" prop="passwordCheck">
              <el-input
                  type="password"
                  v-model="passwordForm.passwordCheck"
                  placeholder="请再次输入密码"
              />
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>

      <el-row style="margin-top: 20px">
        <el-col :span="24">
          <el-popconfirm
              width="200"
              title="您确认注销账号吗？"
              placement="top"
              cancel-button-text="否"
              confirm-button-text="是"
              @confirm="removeAccount"
          >
            <template #reference>
              <el-button
                  type="danger"
                  style="margin-right: 20px"
                  :disabled="disableForm"
              >注销账号
              </el-button>
            </template>
          </el-popconfirm>
          <el-button
              @click="enableModify"
              :disabled="!disableForm"
              style="margin-right: 20px"
          >修改信息
          </el-button>
          <el-button
              type="primary"
              @click="saveModify"
              :disabled="disableForm"
          >保存信息
          </el-button>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import {changePassword, getDetails} from "@/api/userDetailsApi";
import {destroyAccount} from "@/api/authUserApi";
import {clearToken} from "@/util/AuthUtil";
import {Encrypt, SHA256} from "@/util/EncryptUtil";
import {isPwdValid} from "@/util/RegexUtil";

export default {
  data() {
    return {
      disableForm: true,
      passwordForm: {
        originPwd: null,
        password: null,
        passwordCheck: null,
      },
      rules: {
        originPwd: [
          {required: true, message: '密码不能为空', trigger: 'blur'},
        ],
        password: [
          {required: true, message: '密码不能为空', trigger: 'blur'},
        ],
        passwordCheck: [
          {required: true, message: '密码不能为空', trigger: 'blur'},
        ]
      }
    }
  },
  mounted() {
    getDetails().then(res => {
      this.passwordForm = res.data
    })
  },
  methods: {
    enableModify() {
      this.disableForm = false
    },
    saveModify() {
      this.$refs.passwordForm.validate(async valid => {
        if (!valid) {
          this.$message.warning('请填写信息后重试')
          return
        }
        if (!this.passwordValidate()) {
          return
        }

        const params = {
          originPwd: SHA256(Encrypt(this.passwordForm.originPwd)),
          password: Encrypt(this.passwordForm.password)
        }
        await changePassword(params).then(res => {
          switch (res.data) {
            case 1:
              this.$message.error('旧密码错误')
              break
            case 2:
              this.$message.error('新旧密码不可一致')
              break
            case 3:
              this.$message.success('修改成功')
              this.disableForm = true
              this.$refs.passwordForm.clearValidate()
              this.clearLogin()
              break
            case 4:
              this.$message.error('修改失败，请稍后重试')
              break
          }
        })
      })
    },
    passwordValidate() {
      const _params = this.passwordForm

      const pwd = _params.password
      if (pwd !== _params.passwordCheck) {
        this.$message.warning('两次密码不一致，请检查后重试！')
        return false
      }
      if (pwd.length < 6 || pwd.length > 50) {
        this.$message.warning('密码长度需大于 6 位且小于 50 位!')
        return false
      }
      if (!isPwdValid(pwd)) {
        this.$message.warning('密码只允许数字与字母，特殊符号仅支持 (. ! #) 三者')
        return false
      }
      return true
    },
    async removeAccount() {
      await destroyAccount().then(res => {
        if (res.code === 200 && res.data) {
          this.$message.success('您已注销')
          this.disableForm = true

          this.clearLogin()
        }
      })
    },
    clearLogin() {
      clearToken()
      this.$router.push('/dictionary');
    }
  }
}
</script>

<style>
</style>