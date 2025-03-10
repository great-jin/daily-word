<template>
  <div>
    <el-container>
      <el-header>
        <el-row style="height: 100%; width: 100%">
          <!-- Logo -->
          <el-col :span="4" @click="handleSelect('dictionary')">
            <h1 style="color: white">Daily Word</h1>
          </el-col>

          <el-col :span="20">
            <el-menu
                mode="horizontal"
                @select="handleSelect"
                :default-active="activeTab"
                background-color="#545c64"
                text-color="#fff"
                active-text-color="#ffd04b"
                style="height: 100%; width: 100%;"
            >
              <el-menu-item index="dictionary" class="head-banner">字典查询</el-menu-item>
              <el-menu-item index="competition" class="head-banner">知识竞赛</el-menu-item>
              <el-sub-menu index="personal" class="right-align-submenu">
                <template #title>
                  <div style="font-weight: bold; font-size: 16px;">
                    <el-icon size="22">
                      <Setting/>
                    </el-icon>
                    设置
                  </div>
                </template>
                <el-menu-item index="personal">
                  <el-icon>
                    <User/>
                  </el-icon>
                  个人中心
                </el-menu-item>
                <el-menu-item index="quit" style="text-align:center;">
                  <el-icon>
                    <SwitchButton/>
                  </el-icon>
                  退 出
                </el-menu-item>
              </el-sub-menu>
            </el-menu>
          </el-col>
        </el-row>
      </el-header>

      <el-main>
        <!-- isRouterAlive: 用于控制局部刷新 -->
        <router-view v-if="isRouterAlive"/>
      </el-main>

      <el-footer class="footer-copyright">
        <div>
          <h2 style="color: white; font-weight: normal; padding: 0; margin: 0;">
            Copyright 2024 - 2025 布袋青年 ©All Rights Reserved
          </h2>
          <br/>
          <h2 style="color: white; font-weight: normal; padding: 0; margin: 0;">
            Feel free to contact us, Email: ibudai56@163.com
          </h2>
        </div>
      </el-footer>
    </el-container>
  </div>
</template>

<script>
import {clearToken} from "@/util/AuthUtil";

export default {
  provide() {
    return {
      reload: this.reload
    }
  },
  data() {
    return {
      isRouterAlive: true,
      activeTab: 'dictionary'
    }
  },
  mounted() {
    this.handleSelect('dictionary')
  },
  methods: {
    reload() {
      this.isRouterAlive = false
      this.$nextTick(function () {
        this.isRouterAlive = true
      })
    },
    handleSelect(tab) {
      switch (tab) {
        case 'dictionary':
          this.$router.push("/dictionary");
          break
        case 'competition':
          this.$router.push("/competition");
          break
        case 'personal':
          this.$router.push("/personal");
          break
        case 'quit':
          clearToken()
          this.$router.push("/dictionary");
          break
      }
    }
  }
}
</script>

<style>
.el-header, .el-footer {
  width: 100%;
  height: 80px;
  padding: 0;
  display: flex;
  background-color: #72C1F2;
  color: #333;
  text-align: center;
}

.head-banner {
  font-weight: bold;
  font-size: 16px;
  height: calc(100vh / 4);
  padding: 0 50px;
}

.el-main {
  background-color: #E9EEF3;
  color: #333;
  text-align: center;
  height: calc(100vh - 160px);
}

.footer-copyright {
  font-size: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.el-menu--collapse .el-menu .el-submenu, .el-menu--popup {
  min-width: 100px !important;
}

.right-align-submenu .el-sub-menu__wrap {
  left: auto !important;
  right: 0 !important; /* 将子菜单定位到最右边 */
}
</style>