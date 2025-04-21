<template>
  <el-dialog
      v-model="dialogVisible"
      width="30%"
      title="忘记密码"
      style="--el-dialog-padding-primary: 10px 40px 10px 10px;"
      center
  >
    <el-form
        ref="pwdForm"
        :rules="rules"
        :model="pwdForm"
        label-width="100px"
        style="margin-top: 20px"
    >
      <el-form-item label="账号邮箱:" prop="email">
        <el-row style="width: 100%">
          <el-col :span="18">
            <el-input
                v-model="pwdForm.email"
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
      <el-form-item label="验&nbsp;&nbsp;证&nbsp;&nbsp;码:" prop="captcha">
        <el-input
            v-model="pwdForm.captcha"
            placeholder="请输入验证码"
        />
      </el-form-item>
      <el-form-item label="新&nbsp;&nbsp;密&nbsp;&nbsp;码:" prop="password">
        <el-input
            type="password"
            v-model="pwdForm.password"
            placeholder="请输入新密码"
        />
      </el-form-item>
      <el-form-item label="确认密码:" prop="passwordCheck">
        <el-input
            type="password"
            v-model="pwdForm.passwordCheck"
            placeholder="请再次输入密码"
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="cancel">取 消</el-button>
      <el-button type="primary" @click="submit">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import {forgot, sendMail} from "@/api/authUserApi";
import {isEmail, isPwdLegal} from "@/util/RegexUtil";
import {Encrypt} from "@/util/EncryptUtil";

export default {
  data() {
    return {
      dialogVisible: false,
      countDown: 0,
      timer: null,
      pwdForm: {
        email: '',
        captcha: '',
        password: '',
        passwordCheck: '',
      },
      rules: {
        email: [
          {required: true, message: '邮箱不能为空', trigger: 'blur'},
        ],
        captcha: [
          {required: true, message: '验证码不能为空', trigger: 'blur'},
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
    // 组件销毁时清除计时器
    this.resetTimer()
  },
  methods: {
    show(operate) {
      this.operate = operate
      this.dialogVisible = true
    },
    sendMail() {
      const _email = this.pwdForm.email
      if (_email === null || _email === '') {
        this.$message.warning('请先输入邮箱！')
        return
      }
      if (!isEmail(_email)) {
        this.$message.warning('邮箱格式非法！')
        return
      }

      // 开始倒计时
      this.startCountDown()
      const params = {
        type: 2,
        email: _email
      }
      sendMail(params).then(res => {
        if (res.code === 200 && res.data) {
          this.$message.success('验证码已发送，请检查收件箱')
        } else {
          // 发送失败重置计时器
          this.resetTimer()
        }
      })
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
    submit() {
      this.$refs.pwdForm.validate(valid => {
        if (valid) {
          const _formData = this.pwdForm

          // 密码合规校验
          const message = isPwdLegal(_formData.password, _formData.passwordCheck)
          if (message !== null && message !== '') {
            this.$message.warning(message)
            return
          }

          // 忘记密码
          _formData.passwordCheck = null
          _formData.password = Encrypt(_formData.password)
          forgot(_formData).then(res => {
            if (res.code === 200 && res.data) {
              this.$message.success('密码重置成功！')
              this.cancel()
            }
          })
        } else {
          this.$message.error('请填写信息后重试')
        }
      })
    },
    cancel() {
      this.dialogVisible = false
      this.pwdForm = {}
      this.$refs.pwdForm.clearValidate()
      this.resetTimer()
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
  display: flex;
  justify-content: flex-end;
}
</style>
