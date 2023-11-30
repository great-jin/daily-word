<template>
  <div>
    <h1>每日计划</h1>

    <el-button @click="clickOption('test')">Test</el-button>
  </div>
</template>

<script setup>
import {testApi} from "@/api/testApi";

function clickOption(type) {
  switch (type) {
    case 'test':
      testApi().then((res) => {
        console.log(res)
      })
      break
  }
}
</script>

<style>
</style>