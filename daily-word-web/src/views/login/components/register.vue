<template>
  <el-dialog
      v-model="dialogVisible"
      width="36%"
      title="账号注册"
      :show-close="false"
      align-center
  >
    <el-steps
        :active="active"
        finish-status="success"
        style="margin: 20px"
        align-center
    >
      <el-step title="邀请码"/>
      <el-step title="基本信息"/>
      <el-step title="注册提交"/>
    </el-steps>

    <!-- 注册步骤 -->
    <el-card style="margin-top: 20px; border-radius: 8px">
      <div v-if="active === 0">
        <el-row>
          <el-input
              v-model="registerForm.inviteCode"
              placeholder="请输入邀请码"
          />
        </el-row>
      </div>

      <div v-if="active === 1">
        <el-form
            ref="regForm"
            :rules="rules"
            :model="registerForm"
            label-width="100px"
            style="margin-top: 20px"
        >
          <el-form-item label="邮&nbsp;&nbsp;&nbsp;箱:" prop="email">
            <el-row style="width: 100%">
              <el-col :span="18">
                <el-input
                    v-model="registerForm.email"
                    placeholder="请输入邮箱"
                />
              </el-col>
              <el-col :span="6" style="display: flex; align-items: center; padding-left: 10px">
                <el-button
                    style="width: 100%"
                    :disabled="countDown > 0"
                    @click="sendMail"
                >{{ countDown > 0 ? `${countDown} s` : '发送' }}
                </el-button>
              </el-col>
            </el-row>
          </el-form-item>
          <el-form-item label="验证码:" prop="captcha">
            <el-input
                v-model="registerForm.captcha"
                placeholder="请输入邮箱验证码"
            />
          </el-form-item>
          <el-form-item label="用户名:" prop="username">
            <el-input
                v-model="registerForm.username"
                placeholder="请输入用户名"
            />
          </el-form-item>
          <el-form-item label="密&nbsp;&nbsp;&nbsp;码:" prop="password">
            <el-input
                type="password"
                v-model="registerForm.password"
                placeholder="请输入密码"
            />
          </el-form-item>
          <el-form-item label="确认密码:" prop="passwordCheck">
            <el-input
                type="password"
                v-model="registerForm.passwordCheck"
                placeholder="请再次输入密码"
            />
          </el-form-item>
        </el-form>
      </div>

      <div v-if="active === 2">
        <el-result
            :icon="registerResult.icon"
            :title="registerResult.title"
        >
          <template #extra>
            <el-button type="primary" @click="cancel">前往登录</el-button>
          </template>
        </el-result>
      </div>
    </el-card>

    <div
        v-if="active !== 2"
        slot="footer"
        class="dialog-footer"
    >
      <el-row style="text-align: center">
        <el-col :span="24">
          <el-button @click="cancel">取 消</el-button>
          <el-button type="primary" @click="submit">下一步</el-button>
        </el-col>
      </el-row>
    </div>
  </el-dialog>
</template>

<script>
import {Encrypt} from "@/util/EncryptUtil";
import {register, sendMail, validateCode} from "@/api/authUserApi";
import {isEmail, isNameValid, isPwdLegal} from "@/util/RegexUtil";

export default {
  data() {
    return {
      operate: '',
      dialogVisible: false,
      active: 0,
      countDown: 0,
      timer: null,
      registerForm: {
        username: null,
        email: null,
        captcha: null,
        inviteCode: null,
        password: null,
        passwordCheck: null,
      },
      registerResult: {
        icon: 'success',
        title: '注册成功'
      },
      rules: {
        username: [
          {required: true, message: '用户名不能为空', trigger: 'blur'},
        ],
        email: [
          {required: true, message: '邮箱不能为空', trigger: 'blur'},
        ],
        captcha: [
          {required: true, message: '验证码不能为空', trigger: 'blur'},
        ],
        inviteCode: [
          {required: true, message: '邀请码不能为空', trigger: 'blur'},
        ],
        password: [
          {required: true, message: '密码不能为空', trigger: 'blur'},
        ],
        passwordCheck: [
          {required: true, message: '密码不能为空', trigger: 'blur'},
        ],
      }
    }
  },
  beforeDestroy() {
    this.resetTimer()
  },
  methods: {
    show() {
      this.dialogVisible = true
    },
    sendMail() {
      this.$refs.regForm.validateField('email', (valid) => {
        if (valid) {
          if (!isEmail(this.registerForm.email)) {
            this.$message.warning('邮箱格式非法，请检查后重试！')
            return
          }

          // 按钮倒计时
          this.startCountDown()
          const params = {
            type: 1,
            email: this.registerForm.email
          }
          sendMail(params).then(res => {
            if (res.code === 200 && res.data) {
              this.$message.success('验证码已发送，请检查收件箱')
            } else {
              // 发送失败重置计时器
              this.resetTimer()
            }
          })
        }
      });
    },
    startCountDown() {
      // 如果倒计时已经开始，则直接返回
      if (this.countDown > 0) {
        return;
      }
      this.countDown = 60;

      // 启动计时器，每秒减少一秒
      this.timer = setInterval(() => {
        if (this.countDown > 0) {
          this.countDown -= 1;
        } else {
          // 倒计时结束时清除计时器
          this.resetTimer()
        }
      }, 1000);
    },
    cancel() {
      this.dialogVisible = false
      this.active = 0
      this.registerForm = {}
      if (this.$refs.regForm !== undefined) {
        this.$refs.regForm.clearValidate()
      }
    },
    async submit() {
      // 邀请码校验
      if (this.active === 0) {
        await this.handlerInviteCode()
        return
      }

      // 表单校验
      if (this.active === 1) {
        await this.handlerLogin()
      }

      if (this.active === 2) {
        this.cancel()
      }
    },
    async handlerInviteCode() {
      const inviteCode = this.registerForm.inviteCode
      if (inviteCode === null || inviteCode === '') {
        this.$message.warning('邀请码必填')
        return
      }
      let isCodeValid = true
      await validateCode(inviteCode).then(res => {
        if (res.data == null || !res.data) {
          isCodeValid = false
        }
      })
      if (!isCodeValid) {
        this.$message.warning('邀请码无效')
        return
      }

      // 邀请码验证成功
      this.active++
    },
    async handlerLogin() {
      this.$refs.regForm.validate(async valid => {
        if (!valid) {
          this.$message.warning('请填写信息后重试！')
          return
        }
        // 校验非法则退出
        if (!this.formatValidate()) {
          return
        }

        // 用户注册
        let registerSuccess = false
        this.registerForm.passwordCheck = null
        this.registerForm.password = Encrypt(this.registerForm.password)
        await register(this.registerForm).then(res => {
          if (res.code === 200 && res.data) {
            registerSuccess = true
          }
        })
        if (!registerSuccess) {
          return
        }

        // 注册成功
        this.active++
      })
    },
    formatValidate() {
      const _params = this.registerForm

      // 邮箱格式校验
      if (!isEmail(_params.email)) {
        this.$message.warning('邮箱格式非法，请检查后重试！')
        return false
      }

      // 用户名格式校验
      if (!isNameValid(_params.username)) {
        this.$message.warning('用户名仅支持字母与数字！')
        return false
      }

      // 密码合规校验
      const message = isPwdLegal(_params.password, _params.passwordCheck)
      if (message !== null && message !== '') {
        this.$message.warning(message)
        return false
      }
      return true
    },
    resetTimer() {
      this.countDown = 0

      if (this.timer !== null) {
        clearInterval(this.timer)
      }
      this.timer = null
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  margin-top: 14px;
}
</style>
