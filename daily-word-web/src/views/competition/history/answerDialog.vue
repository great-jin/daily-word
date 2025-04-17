<template>
  <el-dialog
      v-model="visible"
      width="64%"
      title="对局答案"
      @close="clearData"
  >
    <el-row>
      <el-col :span="17" class="container">
        <!-- 单词翻译 -->
        <el-row
            v-if="correctData.length > 0"
            :style="{marginTop: '90px'}"
        >
          <el-col :span="24">
            <div v-for="item in correctData[wordIndex].translation" style="text-align: center">
              <strong>
                <span style="width: 100%; font-size: 16px">{{ item }}</span>
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
                  v-for="(item, index) in wordValues"
                  :key="index"
                  v-model="wordValues[index].value"
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
                  v-for="(item, index) in inputValues"
                  :key="index"
                  v-model="inputValues[index].value"
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
                <span>{{ wordIndex + 1 }} / {{ correctData.length }}</span>
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
      <el-col :span="7">
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
import {getTaskAnswer} from "@/api/taskWordApi";
import {speakEn} from "@/util/SpeakUtil";

export default {
  name: 'AnswerDialog',
  data() {
    return {
      visible: false,
      correctData: [],
      inputData: [],
      wordIndex: 0,
      wordValues: [],
      inputValues: [],
      subjectItems: [],
      answeredItems: []
    }
  },
  methods: {
    async show(matchId) {
      await getTaskAnswer(matchId).then(res => {
        if (res === undefined || res === null || res.code !== 200) {
          this.clearData()
          return
        }

        this.visible = true
        this.correctData = res.data.answers
        this.inputData = res.data.submits
      })

      for (let i = 1; i <= this.correctData.length; i++) {
        this.subjectItems.push(`第 ${i} 题`)
        this.answeredItems[i - 1] = this.correctData[i - 1].correct === true
      }

      this.wordIndex = 0
      this.setActiveWord()
    },
    clearData() {
      this.visible = false

      this.wordIndex = 0
      this.correctData = []
      this.subjectItems = []
    },
    read() {
      speakEn(this.correctData[this.wordIndex].value)
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
        if (this.wordIndex >= this.correctData.length - 1) {
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
      const item = this.correctData[this.wordIndex]
      this.wordValues = item.value.split('').map(char => ({value: char}));

      // 用户输入内容
      let inputWord = ''
      const inputItem = this.inputData.find(it => it.offset === item.offset)
      if (inputItem !== undefined && inputItem !== null) {
        inputWord = inputItem.answer
      } else {
        for (let i = 0; i < item.value.length; i++) {
          inputWord += ' '
        }
      }
      this.inputValues = inputWord.split('').map(char => ({value: char}));
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