<template>
  <el-dialog
      v-model="visible"
      title="答题"
      width="56%"
      @close="clearData"
  >
    <el-row style="height: 100%; padding: 20px 25px">
      <el-col :span="18">
        <el-row :style="{marginTop: '20px'}">
          <el-col :span="24">
            <div
                v-if="planData.length > 0"
                style="text-align: center"
            >
            <span
                v-for="item in planData[currentDataIndex].translation"
                style="width: 100%"
            >
              {{ item }}
            </span>
            </div>
          </el-col>
        </el-row>

        <el-row :style="{marginTop: '20px'}">
          <el-col :span="24">
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
          </el-col>
        </el-row>

        <el-row :style="{marginTop: '30px'}">
          <el-col :span="24">
           <span>{{ currentDataIndex + 1}} / {{planData.length}}</span>
          </el-col>
        </el-row>

        <el-row :style="{marginTop: '20px'}">
          <el-col :span="24">
            <el-button @click="clickOption('hit')">提示</el-button>
            <el-button @click="clickOption('read')">朗读</el-button>
            <el-button @click="clickOption('clear')">清空</el-button>
          </el-col>
        </el-row>

        <el-row :style="{marginTop: '20px'}">
          <el-col :span="24">
            <el-button type="primary" @click="choose('back')">上一题</el-button>
            <el-button type="primary" @click="choose('next')">下一题</el-button>
            <el-button key="submit" type="primary" @click="finish">提交</el-button>
          </el-col>
        </el-row>
      </el-col>

      <el-col :span="6" style="background-color: #e6dfdf">
        <div class="word-container">
          <el-checkbox
              v-for="(item, index) in checkboxItems"
              :key="index"
              v-model="checkedItems[index]"
              @change="changCurrentWord(index)"
              size="large"
              style="display: block"
          >
            {{ item }}
          </el-checkbox>
        </div>
      </el-col>
    </el-row>
  </el-dialog>
</template>

<script>
import {speakEn} from "@/util/SpeakUtil";

export default {
  data() {
    return {
      visible: false,
      inputValues: [], // 用于存储输入框的数据
      textLength: 0,
      checkboxItems: [],
      checkedItems: [],
      planData: [],
      currentDataIndex: 0
    }
  },
  methods: {
    show(data) {
      this.visible = true

      this.clearData()
      this.planData = data
      for (let i = 1; i <= this.planData.length; i++) {
        this.checkboxItems.push(`第 ${i} 题`)
      }
      this.currentDataIndex = 0
      this.setSingleWord(this.planData[0].value)
    },
    clearData() {
      this.currentDataIndex = 0
      this.checkboxItems = []
      this.checkedItems = []
      this.planData = []
    },
    finish() {
      this.visible = false
      this.$message.success('关闭页面')
    },
    choose(type) {
      const index = this.currentDataIndex
      if (type === 'back') {
        if (index <= 0) {
          if (this.planData.length === 0) {
            this.$message.warning('请先开始')
          } else {
            this.$message.warning('已经到第一题了')
          }
          return
        }
        this.currentDataIndex--
      } else {
        if (index >= this.planData.length - 1) {
          this.$message.warning('已经到最后一题了')
          return
        }
        this.currentDataIndex++
      }
      this.setSingleWord(this.planData[this.currentDataIndex].value)
    },
    // 切换当前单词
    changCurrentWord(index) {
      this.currentDataIndex = index
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
            this.choose('next')
          } else {
            this.$message.error('拼接不正确')
          }
          break
        case 'read':
          // TODO 次数限制
          speakEn(this.planData[this.currentDataIndex].value)
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
      }
    },
    setSingleWord(word) {
      this.textLength = word.length
      this.inputValues = Array.from({length: this.textLength}, () => ({value: ''}));
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
  width: 34px;
  margin-right: 10px;
  text-align: center;
  border-bottom: 1px solid #409EFF;
}

.word-container {
  padding: 20px 10px;
  max-height: 200px;
  scroll-behavior: smooth;
  overflow-y: scroll;
}

.el-checkbox:last-of-type{
}
</style>