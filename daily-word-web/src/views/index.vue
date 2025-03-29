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
                :ellipsis="false"
                @select="handleSelect"
                :default-active="activeMenu"
                background-color="#545c64"
                text-color="#fff"
                active-text-color="#ffd04b"
                class="home-menu"
            >
              <el-menu-item index="dictionary" class="head-banner" >字典查询</el-menu-item>
              <el-menu-item index="competition" class="head-banner">知识竞赛</el-menu-item>
              <el-sub-menu index="personal" class="setting-menu">
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
                <el-menu-item index="friend">
                  <el-icon>
                    <ChatDotRound/>
                  </el-icon>
                  我的好友
                </el-menu-item>
                <el-menu-item index="history">
                  <el-icon>
                    <Files/>
                  </el-icon>
                  匹配记录
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

          <!-- 我的好友组件 -->
          <FriendDrawer ref="friendDrawer"/>
          <!-- 匹配记录组件 -->
          <HistoryDrawer ref="historyDrawer"/>
        </el-row>
      </el-header>

      <el-main>
        <!-- isRouterAlive: 用于控制局部刷新 -->
        <router-view v-if="isRouterAlive"/>
      </el-main>

      <el-footer class="footer-copyright">
        <div>
          <h2 style="color: white; font-weight: normal; padding: 0; margin: 0;">
            Feel free to contact us, Email: ibudai56@163.com
          </h2>
          <br/>
          <h2 style="color: white; font-weight: normal; padding: 0; margin: 0;">
            Copyright 2024 - {{ new Date().getFullYear() }} 布袋青年 ©All Rights Reserved
          </h2>
        </div>
      </el-footer>
    </el-container>
  </div>
</template>

<script>
import {clearToken} from "@/util/AuthUtil";
import FriendDrawer from "@/views/friend/index.vue";
import HistoryDrawer from "@/views/history/index.vue";

export default {
  components: {
    FriendDrawer,
    HistoryDrawer
  },
  provide() {
    return {
      reload: this.reload
    }
  },
  data() {
    return {
      isRouterAlive: true,
      activeMenu: 'dictionary'
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
          this.activeMenu = 'dictionary';
          this.$router.push('/dictionary');
          break
        case 'competition':
          this.$router.push('/competition');
          break
        case 'personal':
          this.$router.push('/personal');
          break
        case 'friend':
          this.$refs.friendDrawer.show();
          break
        case 'history':
          this.$refs.historyDrawer.show();
          break
        case 'quit':
          clearToken()
          this.$router.push('/logout');
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

.home-menu {
  width: 100%;
  height: 100%;
}

.el-menu--collapse .el-menu .el-submenu, .el-menu--popup {
  min-width: 120px !important;
}

.el-menu--horizontal > .el-menu-item:nth-child(2) {
  margin-right: auto;
}
</style>