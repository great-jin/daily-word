<template>
  <div class="container">
    <el-row>
      <el-col :span="15">
        <el-input v-model="text"></el-input>
      </el-col>
      <el-col :span="9">
        <el-button @click="speak('1')">朗读1</el-button>
        <el-button @click="speak('2')">朗读2</el-button>
        <el-button @click="speak('3')">朗读3</el-button>
        <el-button @click="speak('4')">朗读4</el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {speakEn, speakZh} from "@/util/speakUtil";

export default {
  data() {
    return {
      text: ''
    }
  },
  methods: {
    speak(type) {
      switch (type) {
        case '1':
          speakEn(this.text, 'US')
          break
        case '2':
          speakEn(this.text, 'UK')
          break
        case '3':
          speakZh(this.text, 'HK')
          break
        case '4':
          speakZh(this.text, 'ML')
          break
      }
    }
  }
}
</script>

<style>
.container {
  height: 100%;
  padding: 20px 25px;
  background-color: white;
}
</style>