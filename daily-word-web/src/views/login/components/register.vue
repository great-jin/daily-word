<template>
  <el-dialog
      v-model="dialogVisible"
      width="30%"
      title="账号注册"
      :show-close="false"
      center
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
import {isEmail, isNameValid, isPwdValid} from "@/util/RegexUtil";

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
    if (this.timer) {
      clearInterval(this.timer);
    }
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
            if (res.code !== 200) {
              this.$message.error('发送失败，请稍后重试！')
              return
            }

            switch (res.data) {
              case 1:
                this.$message.success('验证码已发送，请检查收件箱')
                break
              case 2:
                this.$message.error('发送失败，请稍后重试！')
                break
              case 3:
                this.$message.error('邮箱格式非法！')
                break
              case 4:
                this.$message.error('邮箱已被注册！')
                break
              case 7:
                this.$message.error('验证码无效！')
                break
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
          clearInterval(this.timer);
          this.timer = null;
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
          switch (res.data) {
            case 1:
              registerSuccess = true
              break
            case 3:
              this.$message.error('用户名已存在！')
              break
            default:
              this.$message.error('注册失败，请稍后重新！')
              break
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

      // 密码格式校验
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
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  margin-top: 14px;
}
</style>
