<template>
  <div class="container">
    <el-row style="height: 100%; padding: 20px 25px">
      <el-col :span="18">
        <el-row :style="{marginTop: '20px'}">
          <el-col :span="15">
            <el-button type="primary" @click="getPlanData">开始</el-button>
            <el-button type="primary" @click="choose('back')">上一题</el-button>
            <el-button type="primary" @click="choose('next')">下一题</el-button>
            <el-button type="primary" @click="clickOption('refresh')">刷新</el-button>
          </el-col>
        </el-row>

        <el-row :style="{marginTop: '20px'}">
          <div
              v-if="planData.length > 0"
              style="margin-right: 20px;text-align: center"
          >
            <span
                v-for="item in planData[currentDataIndex].translation"
                style="width: 100%"
            >
              {{ item }}
            </span>
          </div>
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
          <el-button type="primary" @click="clickOption('hit')">提示</el-button>
          <el-button type="primary" @click="clickOption('clear')">清空</el-button>
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
import {getTaskContent} from "@/api/wordApi";
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
      planData: [],
      currentDataIndex: 0,
    }
  },
  methods: {
    async getPlanData() {
      this.clearData()
      const params = {
        size: 5,
        offset: 10
      }
      await getTaskContent(params).then(res => {
        this.planData = res.data
        for (let i = 1; i <= this.planData.length; i++) {
          this.checkboxItems.push(`第 ${i} 题`)
        }
      })
      console.log('aaa', this.planData)
      this.currentDataIndex = 0
      this.setSingleWord(this.planData[0].value)
    },
    choose(type) {
      const index = this.currentDataIndex
      if (type === 'back') {
        if (index <= 0) {
          return
        }
        this.currentDataIndex--
      } else {
        if (index >= this.planData.length - 1) {
          return
        }
        this.currentDataIndex++
      }
      this.setSingleWord(this.planData[this.currentDataIndex].value)
    },
    clickOption(type) {
      switch (type) {
        case 'enter':
          const fillLen = this.inputValues.filter(it => it.value !== '').length;
          if (fillLen < this.textLength) {
            return
          }

          // 读取输入
          let _data = '';
          this.inputValues.forEach(it => {
            _data += it.value
          })
          if (_data === this.planData[this.currentDataIndex].value) {
            speakEn(_data)
            this.$message.success(_data)
          } else {
            console.log('_data', _data)
            console.log('_data_value', this.planData[this.currentDataIndex].value)
            this.$message.error('拼接不正确')
          }
          break
        case 'hit':
          if (this.planData.length > 0) {
            this.inputValues = Array.from({length: this.textLength}, () => ({value: ''}));
            const index = this.currentDataIndex
            const words = this.planData[index].value.split('')
            if (words.length > 2) {
              this.inputValues[0].value = words[0]
              this.inputValues[1].value = words[1]
            }
          } else {
            this.$message.info('请先选择开始')
          }
          break
        case 'clear':
          this.inputValues = Array.from({length: this.textLength}, () => ({value: ''}));
          break
        case 'refresh':
          this.reload()
          break
      }
    },
    setSingleWord(word) {
      this.textLength = word.length
      this.inputValues = Array.from({length: this.textLength}, () => ({value: ''}));
    },
    clearData() {
      this.checkboxItems = []
      this.planData = []
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