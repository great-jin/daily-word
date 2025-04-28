<template>
  <el-dialog
      v-model="visible"
      width="64%"
      title="对局答案"
      @close="clearData"
  >
    <el-row>
      <el-col :span="16" class="container">
        <!-- 单词翻译 -->
        <el-row
            v-if="taskData.answers.length > 0"
            :style="{marginTop: '90px'}"
        >
          <el-col :span="24">
            <div v-for="item in taskData.answers[wordIndex].translation" style="text-align: center">
              <strong>
                <span class="translate-content">{{ item }}</span>
              </strong>
            </div>
          </el-col>
        </el-row>

        <!-- 单词内容 -->
        <div :style="{marginTop: '40px'}">
          <el-row>
            <el-col :span="24">
              正确答案: &nbsp;&nbsp;
              <el-input
                  ref="inputRefs"
                  v-for="(item, index) in correctWord"
                  :key="index"
                  v-model="correctWord[index].value"
                  :maxlength="1"
                  class="input-word"
                  disabled
              />
            </el-col>
          </el-row>

          <el-row style="margin-top: 20px">
            <el-col :span="24">
              您的作答: &nbsp;&nbsp;
              <el-input
                  ref="inputRefs"
                  v-for="(item, index) in submitWord"
                  :key="index"
                  v-model="submitWord[index].value"
                  :maxlength="1"
                  class="input-word"
                  disabled
              />
            </el-col>
          </el-row>
        </div>

        <el-row :style="{marginTop: 'auto'}">
          <el-col :span="24">
            <el-row>
              <el-col :span="24" style="text-align: center;">
                <span>{{ wordIndex + 1 }} / {{ taskData.answers.length }}</span>
              </el-col>
            </el-row>

            <el-row :style="{marginTop: '40px'}">
              <el-col :span="24">
                <el-button type="primary" @click="changeIndex('back')">上一题</el-button>
                <el-button @click="read">朗&nbsp;&nbsp;读</el-button>
                <el-button type="primary" @click="changeIndex('next')">下一题</el-button>
              </el-col>
            </el-row>
          </el-col>
        </el-row>
      </el-col>

      <!-- 题数展示 -->
      <el-col :span="8">
        <div class="word-container">
          <el-row :gutter="20">
            <el-col
                :span="11"
                v-for="(item, index) in subjectItems"
                :key="index"
            >
              <el-checkbox
                  v-model="answeredItems[index]"
                  @change="changCurrentWord(index)"
                  size="large"
              >
                <template #default>
                  {{ item }}
                  <span style="margin-left: 8px;">
                    {{ answeredItems[index] ? '✔' : '✘' }}
                  </span>
                </template>
              </el-checkbox>
            </el-col>
          </el-row>
        </div>
      </el-col>
    </el-row>
  </el-dialog>
</template>

<script>
import {speakEn} from "@/util/SpeakUtil";

export default {
  name: 'AnswerDialog',
  data() {
    return {
      visible: false,
      taskData: {
        // 正确答案
        answers: [],
        // 提交答案
        submits: [],
      },
      // 单词
      wordIndex: 0,
      correctWord: [],
      submitWord: [],
      // 题目统计
      subjectItems: [],
      answeredItems: []
    }
  },
  methods: {
    show(data) {
      this.visible = true

      this.taskData = data
      for (let i = 1; i <= this.taskData.answers.length; i++) {
        this.subjectItems.push(`第 ${i} 题`)
        // 是否答对
        this.answeredItems[i - 1] = this.taskData.answers[i - 1].correct === true
      }

      // 默认选中第一项
      this.wordIndex = 0
      this.setActiveWord()
    },
    clearData() {
      this.visible = false

      this.wordIndex = 0
      this.taskData.answers = []
      this.subjectItems = []
    },
    read() {
      speakEn(this.taskData.answers[this.wordIndex].value)
    },
    changeIndex(type) {
      if (type === 'back') {
        // 上一题
        if (this.wordIndex <= 0) {
          this.$message.warning('已经到第一题了')
          return
        }
        this.wordIndex--
      } else {
        // 下一题
        if (this.wordIndex >= this.taskData.answers.length - 1) {
          return
        }
        this.wordIndex++
      }
      this.setActiveWord()
    },
    // 切换当前单词
    changCurrentWord(index) {
      this.answeredItems[index] = !this.answeredItems[index]
      this.wordIndex = index
      this.setActiveWord()
    },
    setActiveWord() {
      const item = this.taskData.answers[this.wordIndex]
      this.correctWord = item.value.split('').map(char => ({value: char}))

      // 用户输入内容
      let inputWord = ''
      const inputItem = this.taskData.submits.find(it => it.offset === item.offset)
      if (inputItem !== undefined && inputItem !== null && inputItem !== '') {
        inputWord = inputItem.answer

        // 输入长度补齐到原单词长度
        const wordMinus = item.value.length - inputWord.length
        if (wordMinus > 0) {
          for (let i = 0; i < wordMinus; i++) {
            inputWord += ' '
          }
        }
      } else {
        for (let i = 0; i < item.value.length; i++) {
          inputWord += ' '
        }
      }
      this.submitWord = inputWord.split('').map(char => ({value: char}));
    }
  }
}
</script>

<style scoped>
.container {
  padding: 0 100px 20px 100px;
  background: #daedf9;
  border-radius: 20px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.translate-content {
  width: 100%;
  margin-top:5px;
  display: inline-block;
  font-size: 16px;
}

.input-word {
  width: 34px;
  margin-right: 4px;
  text-align: center;
}

.word-container {
  max-height: 440px;
  margin-left: 20px;
  padding: 20px;
  overflow-y: scroll;
  scroll-behavior: smooth;
  background: #fce6dd;
  border-radius: 20px;
}
</style>