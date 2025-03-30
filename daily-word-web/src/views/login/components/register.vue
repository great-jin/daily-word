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
          <el-form-item label="用户名:" prop="username">
            <el-input
                v-model="registerForm.username"
                placeholder="请输入用户名"
            />
          </el-form-item>
          <el-form-item label="邮&nbsp;&nbsp;&nbsp;箱:" prop="mail">
            <el-row style="width: 100%">
              <el-col :span="18">
                <el-input
                    v-model="registerForm.mail"
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
                placeholder="请输入验证码"
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
        mail: null,
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
        mail: [
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
      this.$refs.regForm.validateField('mail', (valid) => {
        if (valid) {
          // 按钮倒计时
          this.startCountDown()
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
      this.$refs.regForm.clearValidate()
    },
    submit() {
      // 邀请码校验
      if (this.active === 0) {
        const inviteCode = this.registerForm.inviteCode
        if (inviteCode === null || inviteCode === '') {
          this.$message.error('邀请码必填')
          return
        } else {
          if (inviteCode !== 'invincible') {
            this.$message.warning('邀请码无效')
            return
          }
        }

        // TODO 2025/3/30 校验邀请码
        this.active++
        return
      }

      // 表单校验
      if (this.active === 1) {
        this.$refs.regForm.validate(valid => {
          if (valid) {
            // TODO 2025/3/30 执行注册

            this.active++
          } else {
            this.$message.error('请填写信息后重试')
          }
        })
      }

      if (this.active === 2) {
        this.cancel()
      }
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  margin-top: 14px;
}
</style>
