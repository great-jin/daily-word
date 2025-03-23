<template>
  <div style="height: 100%;">
    <el-row style="height: 100%;">
      <el-col :span="18" class="container">
        <el-row :style="{marginTop: '20px'}">
          <el-col :span="24">
            <div
                v-if="planData.length > 0"
                style="text-align: center"
            >
            <span
                v-for="item in planData[currentIndex].translation"
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
                @keyup.enter="handleAnswer()"
                class="input-word"
            />
          </el-col>
        </el-row>

        <el-row :style="{marginTop: '30px'}">
          <el-col :span="24">
            <span>{{ currentIndex + 1 }} / {{ planData.length }}</span>
          </el-col>
        </el-row>

        <el-row :style="{marginTop: '30px'}">
          <el-col :span="24">
            <span>
              <strong>作答耗时:</strong>
              {{ formattedTime }}
            </span>
          </el-col>
        </el-row>

        <el-row :style="{marginTop: '60px'}">
          <el-col :span="24">
            <el-popconfirm
                title="提示将降低答题得分"
                confirm-button-text="是"
                cancel-button-text="否"
                placement="top"
                width="200"
                @confirm="clickOption('hit')"
            >
              <template #reference>
                <el-button>提示</el-button>
              </template>
            </el-popconfirm>
            <el-popconfirm
                title="朗读将降低答题得分"
                confirm-button-text="是"
                cancel-button-text="否"
                placement="top"
                width="200"
                @confirm="clickOption('read')"
            >
              <template #reference>
                <el-button>朗读</el-button>
              </template>
            </el-popconfirm>
            <el-button @click="clickOption('clear')">清空</el-button>
          </el-col>
        </el-row>

        <el-row :style="{marginTop: '20px'}">
          <el-col :span="24">
            <el-button type="primary" @click="changeCurrentIndex('back')">上一题</el-button>
            <el-button type="primary" @click="changeCurrentIndex('next')">下一题</el-button>
            <el-button key="submit" type="danger" @click="quit">放弃</el-button>
            <el-button key="submit" type="primary" @click="submit">提交</el-button>
          </el-col>
        </el-row>
      </el-col>

      <el-col :span="6" style="background-color: #f5f1f1">
        <div class="word-container">
          <el-checkbox
              v-for="(item, index) in subjectItems"
              :key="index"
              v-model="answeredItems[index]"
              @change="changCurrentWord(index)"
              size="large"
              style="display: block; height: 100%"
          >
            {{ item }}
          </el-checkbox>
        </div>
      </el-col>
    </el-row>

    <ResultDialog ref="resultDialog"/>
  </div>
</template>

<script>
import {speakEn} from "@/util/SpeakUtil";
import ResultDialog from "./resultDialog.vue"

export default {
  name: 'AnswerView',
  components: {
    ResultDialog
  },
  data() {
    return {
      clock: {
        time: 0,
        isRunning: false,
        timer: null
      },
      planData: [],
      currentIndex: 0,
      // 输入与长度
      inputValues: [],
      textLength: 0,
      // 题目
      subjectItems: [],
      answeredItems: [],
      // 提示统计
      helpCount: {
        hit: 0,
        read: 0
      },
      // 回答记录
      submitRecords: []
    }
  },
  computed: {
    // 格式化时间：HH:MM:SS
    formattedTime() {
      const _this = this.clock
      const hours = String(Math.floor(_this.time / 3600)).padStart(2, "0");
      const minutes = String(Math.floor((_this.time % 3600) / 60)).padStart(2, "0");
      const seconds = String(_this.time % 60).padStart(2, "0");
      return `${hours}:${minutes}:${seconds}`;
    }
  },
  mounted() {
    const params = this.$route.params.planData
    console.log('222', params)
  },
  beforeDestroy() {
    const _this = this.clock
    clearInterval(_this.timer);
  },
  methods: {
    show(data) {
      this.clearData()
      this.planData = data
      for (let i = 1; i <= this.planData.length; i++) {
        this.subjectItems.push(`第 ${i} 题`)
      }
      this.currentIndex = 0
      this.textLength = this.planData[this.currentIndex].value.length
      this.inputValues = Array.from({length: this.textLength}, () => ({value: ''}));
      this.startTimer()
    },
    clearData() {
      this.currentIndex = 0
      this.planData = []
      this.subjectItems = []
      this.answeredItems = []
      this.submitRecords = []
    },
    quit() {
      // TODO 2025/3/23 得分计算

      this.visible = false
      this.$refs.resultDialog.show(this.clock)
      this.resetTimer()
    },
    submit() {
      const leftCount = this.planData.length - this.submitRecords.length
      if (leftCount > 0) {
        this.$message.warning(`剩余 ${leftCount} 道题未完成`)
        return
      }

      this.visible = false
      this.$refs.resultDialog.show(this.clock)
      this.resetTimer()
    },
    startTimer() {
      const _this = this.clock
      if (!_this.isRunning) {
        _this.isRunning = true;
        _this.timer = setInterval(() => {
          _this.time++;
        }, 1000);
      }
    },
    resetTimer() {
      const _this = this.clock
      _this.isRunning = false;
      clearInterval(_this.timer);
      _this.time = 0;
    },
    changeCurrentIndex(type) {
      if (type === 'back') {
        // 上一题
        if (this.currentIndex <= 0) {
          this.$message.warning('已经到第一题了')
          return
        }
        this.currentIndex--
      } else {
        // 下一题
        if (this.currentIndex >= this.planData.length - 1) {
          return
        }
        this.currentIndex++
      }

      this.setAndRefillData(this.currentIndex)
    },
    handleInput(index) {
      // 只允许输入字母
      if (!/^[a-z]$/.test(this.inputValues[index].value)) {
        this.inputValues[index].value = ''
        return
      }

      // 将光标自动定位到下一个输入框
      if (index < this.textLength - 1 && this.inputValues[index].value !== '') {
        this.$refs.inputRefs[index + 1].focus()
      }
      if (this.inputValues.length === this.textLength) {
        // 到达最后一位自动提交
        this.handleAnswer()
      }
    },
    handleKeyDown(index) {
      // 监听删除键
      if (event.key === 'Backspace' && index > 0 && this.inputValues[index].value === '') {
        // 阻止默认删除行为
        event.preventDefault();
        // 将光标自动定位到前一个输入框
        this.$refs.inputRefs[index - 1].focus()
      }
    },
    // 提交答案
    handleAnswer() {
      const fillLen = this.inputValues.filter(it => it.value !== '').length;
      if (fillLen < this.textLength) {
        return
      }

      // 读取输入
      let _data = ''
      this.inputValues.forEach(it => {
        _data += it.value
      })
      // 记录答题内容以回填
      const _index = this.currentIndex
      const item = {
        position: _index,
        correct: _data === this.planData[_index].value,
        answer: _data
      }
      this.submitRecords = this.submitRecords.filter(it => it.position !== _index)
      this.submitRecords.push(item)
      // 下一题
      this.answeredItems[_index] = true
      this.changeCurrentIndex('next')
    },
    // 回填已输入内容
    setAndRefillData(index) {
      let word;
      let item = this.submitRecords.find(it => it.position === index)
      if (item !== undefined && item !== null) {
        // 作答过读取内容回填
        word = item.answer
        this.textLength = word.length
        this.inputValues = Array.from({length: word.length}, (_, i) => ({value: word[i]}))
      } else {
        // 未作答过
        word = this.planData[this.currentIndex].value
        this.textLength = word.length
        this.inputValues = Array.from({length: this.textLength}, () => ({value: ''}));
      }
    },
    // 切换当前单词
    changCurrentWord(index) {
      this.currentIndex = index
      // 回填
      this.setAndRefillData(index)
    },
    clickOption(type) {
      switch (type) {
        case 'read':
          this.helpCount.read++;
          speakEn(this.planData[this.currentIndex].value)
          break
        case 'hit':
          if (this.planData.length > 0) {
            this.inputValues = Array.from({length: this.textLength}, () => ({value: ''}));
            const index = this.currentIndex
            const words = this.planData[index].value.split('')
            if (words.length > 2) {
              this.inputValues[0].value = words[0]
              this.inputValues[1].value = words[1]

              // 次数统计
              this.helpCount.hit++;
            }
          } else {
            this.$message.info('请先选择开始')
          }
          break
        case 'clear':
          this.inputValues = Array.from({length: this.textLength}, () => ({value: ''}));
          break
      }
    }
  }
}
</script>

<style scoped>
.container {
  height: 100%;
  background: #daedf9;
  border-radius: 20px;
}

.input-word {
  width: 34px;
  margin-right: 10px;
  text-align: center;
  border-bottom: 1px solid #409EFF;
}

.word-container {
  padding: 20px 10px;
  max-height: 290px;
  scroll-behavior: smooth;
  overflow-y: scroll;
}

.el-checkbox:last-of-type {
}
</style>