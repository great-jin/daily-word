<template>
  <div class="container">
    <el-row style="height: 100%; padding: 20px 25px">
      <el-col :span="18">
        <el-row>
          <el-col :span="15">
            <el-input
                v-model="text"
                @keyup.enter="clickOption('set')"
            />
          </el-col>
          <el-col :span="9">
            <el-button @click="speak">朗读</el-button>
            <el-button @click="clickOption('refresh')">刷新</el-button>
          </el-col>
        </el-row>

        <el-row :style="{marginTop: '20px'}">
          <el-input
              ref="inputRefs"
              v-for="(input, index) in inputValues"
              :key="index"
              v-model="inputValues[index].value"
              :maxlength="1"
              @input="handleInput(index)"
              @keydown="handleKeyDown(index)"
              @keyup.enter="clickOption('enter')"
              class="input-word"
          />
          <el-button @click="clickOption('clear')">清空</el-button>
          <el-button @click="clickOption('ok')">确认</el-button>
        </el-row>
      </el-col>

      <el-col :span="6" style="background-color: #FB9891">
        <div style="padding: 20px 25px">
          <el-checkbox
              v-for="(item, index) in checkboxItems"
              :key="index"
              v-model="checkedItems[index]"
              size="large"
          >
            {{ item }}
          </el-checkbox>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {speakEn} from "@/util/SpeakUtil";

export default {
  inject: ['reload'],
  data() {
    return {
      text: '',
      inputValues: [], // 用于存储输入框的数据
      textLength: 0,
      checkboxItems: [],
      checkedItems: [],
    }
  },
  methods: {
    clickOption(type) {
      switch (type) {
        case 'enter':
          const fillLen = this.inputValues.filter(it => it.value !== '').length;
          if (fillLen < this.textLength) {
            return
          }
          this.clickOption('ok')
          break
        case 'ok':
          let _data = '';
          this.inputValues.forEach(it => {
            _data += it.value
          })
          speakEn(_data)
          this.$message.success(_data)
          break
        case 'set':
          this.textLength = this.text.length
          this.inputValues = Array.from({length: this.textLength}, () => ({value: ''}));
          for (let i = 1; i <= this.textLength; i++) {
            this.checkboxItems.push(i)
          }
          break
        case 'clear':
          this.inputValues = []
          break
        case 'refresh':
          this.reload()
          break
      }
    },
    handleInput(index) {
      // 处理输入事件，例如可以在这里进行一些验证
      // 比如确保输入的是字母，可以使用正则表达式进行验证
      // 这里只是一个简单的示例，你可以根据需求进行扩展
      // 只允许输入字母
      if (!/^[a-z]$/.test(this.inputValues[index].value)) {
        this.inputValues[index].value = '';
      }

      // 将光标自动定位到下一个输入框
      if (index < this.textLength - 1 && this.inputValues[index].value !== '') {
        this.$refs.inputRefs[index + 1].focus();
      }
    },
    handleKeyDown(index) {
      // 监听删除键
      if (event.key === 'Backspace' && index > 0 && this.inputValues[index].value === '') {
        // 阻止默认删除行为
        event.preventDefault();
        // 将光标自动定位到前一个输入框
        this.$refs.inputRefs[index - 1].focus();
      }
    },
    speak() {
      speakEn(this.text, 'US')
    }
  }
}
</script>

<style>
.container {
  height: 100%;
  background-color: white;
}

.input-word {
  width: 30px;
  margin-right: 10px;
  text-align: center;
  border-bottom: 1px solid #409EFF;
}
</style>