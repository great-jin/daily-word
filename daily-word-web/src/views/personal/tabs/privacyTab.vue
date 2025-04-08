<template>
  <div>
    <el-row style="padding: 10px 100px 10px 200px">
      <el-col :span="18">
        <el-form
            ref="privacyForm"
            :model="privacyForm"
            label-width="100px"
            :disabled="disableForm"
        >
          <el-form-item label="邮&nbsp;&nbsp;&nbsp;箱:" prop="email">
            <el-row style="width: 100%">
              <el-col :span="18">
                <el-input
                    v-model="privacyForm.email"
                    disabled
                />
              </el-col>
              <el-col :span="6" style="display: flex; align-items: center; padding-left: 10px">
                <el-button
                    style="width: 100%"
                    :disabled="countDown > 0"
                    @click="sendMail"
                >{{ countDown > 0 ? `${countDown} s` : '发送验证码' }}
                </el-button>
              </el-col>
            </el-row>
          </el-form-item>
          <el-form-item label="验证码:" prop="captcha">
            <el-input
                v-model="privacyForm.captcha"
                placeholder="请输入邮箱验证码"
            />
          </el-form-item>
          <el-form-item label="密&nbsp;&nbsp;&nbsp;码:" prop="password">
            <el-input
                type="password"
                v-model="privacyForm.password"
                placeholder="请输入密码"
            />
          </el-form-item>
          <el-form-item label="确认密码:" prop="passwordCheck">
            <el-input
                type="password"
                v-model="privacyForm.passwordCheck"
                placeholder="请再次输入密码"
            />
          </el-form-item>
        </el-form>
      </el-col>

      <el-col :span="6">
        <el-button
            @click="enableModify"
        >修改</el-button>
        <el-button
            type="primary"
            @click="saveModify"
            :disabled="disableForm"
        >保存
        </el-button>
        <el-button
            type="danger"
            @click="enableModify"
        >注销</el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {getDetails} from "@/api/userDetailsApi";
import {isEmail} from "@/util/commonUtil";

export default {
  data() {
    return {
      disableForm: true,
      countDown: 0,
      timer: null,
      privacyForm: {
        email: null,
        captcha: null,
        inviteCode: null,
        password: null,
        passwordCheck: null,
      }
    }
  },
  mounted() {
    getDetails().then(res => {
      this.privacyForm = res.data
    })
  },
  beforeDestroy() {
    if (this.timer) {
      clearInterval(this.timer);
    }
  },
  methods: {
    enableModify() {
      this.disableForm = false
    },
    saveModify() {
      this.disableForm = true
    },
    sendMail() {
      this.$refs.privacyForm.validateField('email', (valid) => {
        if (valid) {
          if (!isEmail(this.privacyForm.email)) {
            this.$message.warning('邮箱格式非法，请检查后重试！')
            return
          }

          // 按钮倒计时
          this.$message.success('验证码已发送，请检查收件箱')
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
    }
  }
}
</script>

<style>
</style>