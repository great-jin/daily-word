<template>
  <div>
    <el-container>
      <el-header>
        <el-row style="height: 100%; width: 100%">
          <el-col :span="4" @click="backHome()">
            <h1 style="color: white">Daily Word</h1>
          </el-col>
          <el-col :span="20">
            <el-menu
                default-active="1"
                mode="horizontal"
                @select="handleSelect"
                background-color="#545c64"
                text-color="#fff"
                active-text-color="#ffd04b"
                style="height: 100%; width: 100%;"
            >
              <el-menu-item index="dictionary" class="head-banner">字典查询</el-menu-item>
              <el-menu-item index="dailyPlan" class="head-banner">今日计划</el-menu-item>
              <el-menu-item index="competition" class="head-banner">好友竞赛</el-menu-item>
              <el-menu-item index="scoreCenter" class="head-banner">积分中心</el-menu-item>
              <el-sub-menu index="personal" style="margin-left: auto;">
                <template #title>
                  <div style="font-weight: bold; font-size: 16px;">
                    个人中心
                  </div>
                </template>
                <el-menu-item index="quit" style="text-align:center;">
                  &nbsp;&nbsp;&nbsp;退 出
                </el-menu-item>
              </el-sub-menu>
            </el-menu>
          </el-col>
        </el-row>
      </el-header>

      <el-main>
        <router-view/>
      </el-main>

      <el-footer class="footer-copyright">
        <div>
          <h2 style="color: white; font-weight: normal; padding: 0; margin: 0;">
            Copyright 2021 - 2023 烽火戏诸诸诸侯 ©All Rights Reserved
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

<script setup="context">
import {onMounted} from "vue";
import router from "@/router/index.js";
import {clearToken} from "@/util/authUtil";

onMounted(() => {
  handleSelect('dictionary')
});

function backHome() {
  router.push("/");
}

function handleSelect(index) {
  switch (index) {
    case 'dictionary':
      router.push("/dictionary");
      break
    case 'dailyPlan':
      router.push("/dailyPlan");
      break
    case 'competition':
      router.push("/competition");
      break
    case 'scoreCenter':
      router.push("/scoreCenter");
      break
    case 'personal':
      router.push("/personalCenter");
      break
    case 'quit':
      clearToken()
      backHome()
      break
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
</style>