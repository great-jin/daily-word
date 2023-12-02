<template>
  <el-dialog
      v-model="dialogVisible"
      width="30%"
      :title="operate === 'register' ? '注册账号' : '忘记密码'"
      style="--el-dialog-padding-primary: 10px;"
  >
    <!-- 注册表单 -->
    <el-form
        ref="regForm"
        :rules="rules"
        :model="registerForm"
        label-width="100px"
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
            >{{ countDown > 0 ? `${countDown} s` : '发送' }}</el-button>
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

    <div slot="footer" class="dialog-footer">
      <div class="button-container">
        <el-button @click="clickOption('cancel')">取 消</el-button>
        <el-button type="primary" @click="clickOption('ok')">确 定</el-button>
      </div>
    </div>
  </el-dialog>
</template>

<script>
export default {
  data() {
    return {
      operate: '',
      dialogVisible: false,
      countDown: 0,
      timer: null,
      registerForm: {
        username: '',
        mail: '',
        captcha: '',
        password: '',
        passwordCheck: '',
      },
      rules: {
        username: [
          { required: true, message: '用户名不能为空', trigger: 'blur' },
        ],
        mail: [
          { required: true, message: '邮箱不能为空', trigger: 'blur' },
        ],
        captcha: [
          { required: true, message: '验证码不能为空', trigger: 'blur' },
        ],
        password: [
          { required: true, message: '密码不能为空', trigger: 'blur' },
        ],
        passwordCheck: [
          { required: true, message: '密码不能为空', trigger: 'blur' },
        ],
      }
    }
  },
  beforeDestroy() {
    // 组件销毁时清除计时器
    if (this.timer) {
      clearInterval(this.timer);
    }
  },
  methods: {
    show(operate) {
      this.operate = operate
      this.dialogVisible = true
    },
    sendMail() {
      this.$refs.regForm.validateField('mail', (valid) => {
        if (valid) {
          // 验证未通过
          this.$message.success('发送邮件')

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
    clickOption(type) {
      if (type === 'ok') {
        this.$refs.regForm.validate(valid => {
          if (valid) {
            // 表单验证通过，执行提交逻辑
            // 处理注册逻辑
            this.$message.success(this.operate)
            console.log(this.operate, this.registerForm);
          } else {
            // 表单验证失败
            this.$message.error('表单验证失败')
          }
        })
      } else {
        this.dialogVisible = false
        this.registerForm = {}
        this.$refs.regForm.clearValidate()
      }
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  display: flex;
  justify-content: flex-end;
}

.button-container {
  display: flex;
  gap: 8px; /* 按钮之间的间隔 */
}
</style>
