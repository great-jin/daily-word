<template>
  <div>
    <el-container>
      <el-header>
        <el-row>
          <el-col :span="4">
            <h1 class="head-logo" @click="backHome()">Daily Word</h1>
          </el-col>
          <el-col :span="5">
            <el-button class="head-button">今日计划</el-button>
          </el-col>
          <el-col :span="5">
            <el-button class="head-button">好友竞赛</el-button>
          </el-col>
          <el-col :span="5">
            <el-button class="head-button">积分中心</el-button>
          </el-col>
          <el-col :span="5">
            <el-button class="head-button">个人中心</el-button>
          </el-col>
        </el-row>
      </el-header>

      <el-main>Main</el-main>

      <el-footer>Footer</el-footer>
    </el-container>
  </div>
  </template>

<script setup>
import router from "@/router/index.js";

function backHome() {
  router.push("/");
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

.el-main {
  background-color: #E9EEF3;
  color: #333;
  text-align: center;
  height: calc(100vh - 160px);
}

.head-logo {
  float: left;
  color: white;
}

.head-button {
  width:  calc(100vh / 3);
  height: 80px;
  background-color: mediumpurple;
  color: white;
  font-weight: bold;
}
</style>