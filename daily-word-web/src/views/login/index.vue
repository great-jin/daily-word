<template>
  <div class="login-container">
    <el-row style="margin: 0 auto">
      <el-col :span="24">
        <el-form
            ref="loginForm"
            :rules="rules"
            :model="loginForm"
            class="login-form"
        >
          <h2 class="form-logo">
            Daily Word
          </h2>

          <el-form-item prop="username">
            <el-input
                v-model="loginForm.username"
                placeholder="请输入用户名"
            >
              <template #prepend>
                <el-icon>
                  <User/>
                </el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input
                type="password"
                v-model="loginForm.password"
                placeholder="请输入密码"
            >
              <template #prepend>
                <el-icon>
                  <Lock/>
                </el-icon>
              </template>
            </el-input>
          </el-form-item>

          <el-form-item>
            <el-button
                type="warning"
                style="width: 100%; margin-top: 6px;"
                @click="doLogin"
            >登录
            </el-button>
          </el-form-item>

          <el-form-item>
            <el-row style="width: 100%">
              <el-col :span="8">
                <el-button
                    type="warning"
                    @click="clickOption('forgot')"
                    style="float: left; z-index: 0; line-height: 26px"
                    link
                >忘记密码
                </el-button>
              </el-col>
              <el-col :span="8" style="text-align: center;">
                <el-checkbox
                    v-model="radioValue"
                    @click="clickOption('compact')"
                >用户协议
                </el-checkbox>
              </el-col>
              <el-col :span="8">
                <el-button
                    type="warning"
                    @click="clickOption('register')"
                    style="float: right; z-index: 0; line-height: 26px"
                    link
                >账号注册
                </el-button>
              </el-col>
            </el-row>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>

    <!-- 注册弹窗 -->
    <RegisterModal ref="registerModal"/>
    <CompactModal ref="compactModal"/>
    <ForgotModal ref="forgotModal"/>
  </div>
</template>

<script>
import RegisterModal from './components/register.vue';
import ForgotModal from './components/forgot.vue';
import CompactModal from './components/compact.vue';
import {login} from "@/api/authUserApi";
import {Encrypt, SHA256} from '@/util/EncryptUtil.js';
import {setToken} from "@/util/AuthUtil";

export default {
  components: {
    RegisterModal,
    ForgotModal,
    CompactModal
  },
  data() {
    return {
      radioValue: false,
      loginForm: {
        username: '',
        password: ''
      },
      captchaUrl: '',
      rules: {
        username: [
          {required: true, message: '用户名不能为空', trigger: 'blur'},
        ],
        password: [
          {required: true, message: '密码不能为空', trigger: 'blur'},
        ]
      }
    }
  },
  methods: {
    doLogin() {
      this.$refs.loginForm.validate(valid => {
        if (!valid) {
          // 表单验证失败
          this.$message.warning('请填写登录信息')
          return;
        }

        if (!this.radioValue) {
          this.$message.warning('请勾选同意用户协议')
          return
        }
        // 表单验证通过，执行提交逻辑
        const user = {
          username: this.loginForm.username,
          password: SHA256(Encrypt(this.loginForm.password)),
        }
        login(user).then(res => {
          const _data = res.data
          if (_data != null && _data.user !== null) {
            this.$notify({
              type: 'success',
              title: '登录成功',
              message: 'Welcome to Daily Word'
            });

            // 保存认证登录信息
            setToken(_data)
            this.$router.push('/')
          } else {
            this.$notify.error({
              title: '登录失败',
              message: res.data.msg
            });
          }
        })
      })
    },
    clickOption(value) {
      switch (value) {
        case 'register':
          this.$refs.registerModal.show();
          break
        case 'compact':
          this.radioValue = this.radioValue === true;
          if (!this.radioValue) {
            // 未展示过则弹窗
            this.$refs.compactModal.show();
          }
          break
        case 'forgot':
          this.$refs.forgotModal.show();
          break
      }
    }
  }
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  background: url("@/assets/mountain.jpg") no-repeat;
}

.login-form {
  width: 20%;
  min-width: 320px;
  margin: 14% auto;
  padding: 10px 20px 1px 20px;
  background: white;
  border-radius: 25px;
}

.form-logo{
  width: 100%;
  margin-bottom: 40px;
  display: inline-block;
  text-align: center;
  color: lightskyblue;
}
</style>
