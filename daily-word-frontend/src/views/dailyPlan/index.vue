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

    <el-row :style="{marginTop: '20px'}">
      <el-input
          ref="inputRefs"
          v-for="(input, index) in inputs"
          :key="index"
          v-model="inputs[index].value"
          :maxlength="1"
          placeholder=""
          @input="handleInput(index)"
          @keydown="handleKeyDown(index)"
          class="input-word"
      />
      <el-button @click="clickOption('set')">赋值</el-button>
      <el-button @click="clickOption('clear')">清空</el-button>
      <el-button @click="clickOption('ok')">确认</el-button>
    </el-row>
  </div>
</template>

<script>
import {speakEn, speakZh} from "@/util/speakUtil";

export default {
  data() {
    return {
      text: '',
      inputs: [], // 用于存储输入框的数据
      textLength: 0
    }
  },
  methods: {
    clickOption(type) {
      switch (type) {
        case 'ok':
          let _data = '';
          this.inputs.forEach(it => {
            _data += it.value
          })
          speakEn(_data)
          this.$message.success(_data)
          break
        case 'set':
          this.textLength = this.text.length
          this.inputs = Array.from({length: this.textLength}, () => ({value: ''}));
          break
        case 'clear':
          this.inputs = []
          break
      }
    },
    handleInput(index) {
      // 处理输入事件，例如可以在这里进行一些验证
      // 比如确保输入的是字母，可以使用正则表达式进行验证
      // 这里只是一个简单的示例，你可以根据需求进行扩展

      // 转为大写字母
      // this.inputs[index].value = this.inputs[index].value.toUpperCase();

      // 只允许输入字母
      if (!/^[a-z]$/.test(this.inputs[index].value)) {
        this.inputs[index].value = '';
      }

      // 将光标自动定位到下一个输入框
      if (index < this.textLength - 1 && this.inputs[index].value !== '') {
        this.$refs.inputRefs[index + 1].focus();
      }
    },
    handleKeyDown(index) {
      // 监听删除键
      if (event.key === 'Backspace' && index > 0 && this.inputs[index].value === '') {
        // 阻止默认删除行为
        event.preventDefault();
        // 将光标自动定位到前一个输入框
        this.$refs.inputRefs[index - 1].focus();
      }
    },
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

.input-word {
  width: 30px;
  margin-right: 10px;
  text-align: center;
  border-bottom: 1px solid #409EFF;
}
</style>