<template>
  <div>
    <el-container class="login-container">
      <el-aside class="login-aside">
        <img
            class="aside-bg"
            src="../../assets/mountain.jpg"
        />
      </el-aside>

      <el-main class="login-main">
        <div class="center-container">
          <h2 style="text-align: center; color: lightskyblue">
            Welcome to Daily Word!
          </h2>
          <br/>

          <el-form
              ref="loginForm"
              :rules="rules"
              :model="loginForm"
              label-width="80px"
          >
            <el-form-item label="账&nbsp;&nbsp;&nbsp;号:" prop="username">
              <el-input
                  v-model="loginForm.username"
                  placeholder="请输入账号"
              />
            </el-form-item>
            <el-form-item label="密&nbsp;&nbsp;&nbsp;码:" prop="password">
              <el-input
                  type="password"
                  v-model="loginForm.password"
                  placeholder="请输入密码"
              />
            </el-form-item>
            <el-form-item>
              <el-button
                  type="primary"
                  style="width: 100%"
                  @click="doLogin"
              >登录
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-row type="flex" justify="space-between" style="width: 100%">
                <el-col :span="8">
                  <el-button
                      type="text"
                      @click="clickOption('register')"
                      style="float: left; z-index: 0"
                  >注册
                  </el-button>
                </el-col>
                <el-col :span="8" style="display: flex; align-items: center;">
                  <el-checkbox
                      v-model="radioValue"
                      @click="clickOption('compact')"
                  >用户协议
                  </el-checkbox>
                </el-col>
                <el-col :span="8">
                  <el-button
                      type="text"
                      @click="clickOption('forgot')"
                      style="float: right; z-index: 0"
                  >忘记密码
                  </el-button>
                </el-col>
              </el-row>
            </el-form-item>
          </el-form>
        </div>
      </el-main>

      <!-- 注册弹窗 -->
      <RegisterModal ref="registerModal"/>
      <CompactModal ref="compactModal"/>
    </el-container>
  </div>
</template>

<script>
import RegisterModal from './components/register.vue';
import CompactModal from './components/compact.vue';
import {login} from "@/api/authUser";
import {Encrypt} from '@/util/AES.js';
import {setToken} from "@/util/AuthUtil";

export default {
  components: {
    RegisterModal,
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
          this.$message.error('请填写登录信息')
          return;
        }

        if (!this.radioValue) {
          this.$message.warning('请勾选同意用户协议')
          return
        }
        // 表单验证通过，执行提交逻辑
        const user = {
          username: this.loginForm.username,
          password: Encrypt(this.loginForm.password),
        }
        login(user).then(res => {
          const _data = res.data
          if (_data != null && _data.userId !== null) {
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
          this.$refs.registerModal.show('register');
          break
        case 'compact':
          this.radioValue = this.radioValue === true;
          if (!this.radioValue) {
            this.$refs.compactModal.show();
          }
          break
        case 'forgot':
          this.$refs.registerModal.show('forgot');
          break
      }
    }
  }
}
</script>

<style scoped>
.login-container {
  height: 100vh;
}

.login-aside {
  width: 66.666%; /* 占屏幕2/3 */
  position: relative;
  overflow: hidden;
}

.aside-bg {
  width: 100%;
  height: 100%;
  object-fit: cover; /* 图片填充整个容器，保持纵横比不变 */
  object-position: center; /* 图片在容器中居中显示 */
}

.login-main {
  width: 33.333%; /* 占屏幕1/3 */
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%; /* 让 el-main 高度充满父容器，这样 Flex 布局才会生效 */
}

.center-container {
  max-width: 400px; /* 可根据实际情况调整容器最大宽度 */
  width: 100%;
}
</style>
