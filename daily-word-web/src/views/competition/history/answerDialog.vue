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
        <el-row :style="{marginTop: '80px'}" v-if="answerData.length > 0">
          <el-col :span="24">
            <div v-for="item in answerData[wordIndex].translation" style="text-align: center">
              <strong>
                <span style="width: 100%; font-size: 16px">{{ item }}</span>
              </strong>
            </div>
          </el-col>
        </el-row>

        <!-- 单词内容 -->
        <el-row :style="{marginTop: '40px'}">
          <el-col :span="24">
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

        <el-row :style="{marginTop: 'auto'}">
          <el-col :span="24">
            <el-row>
              <el-col :span="24" style="text-align: center;">
                <span>{{ wordIndex + 1 }} / {{ answerData.length }}</span>
              </el-col>
            </el-row>

            <el-row :style="{marginTop: '30px'}">
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
            <el-col :span="11" v-for="(item, index) in subjectItems" :key="index">
              <el-checkbox
                  v-model="answeredItems[index]"
                  @change="changCurrentWord(index)"
                  size="large"
              >
                {{ item }}
              </el-checkbox>
            </el-col>
          </el-row>
        </div>
      </el-col>
    </el-row>
  </el-dialog>
</template>

<script>
import {getTaskAnswer} from "@/api/wordApi";
import {speakEn} from "@/util/SpeakUtil";

export default {
  name: 'AnswerDialog',
  data() {
    return {
      visible: false,
      answerData: [],
      wordIndex: 0,
      wordValues: [],
      subjectItems: [],
      answeredItems: []
    }
  },
  methods: {
    show(matchId) {
      this.visible = true

      this.fillData(matchId)
    },
    async fillData(matchId) {
      await getTaskAnswer(matchId).then(res => {
        if (res !== undefined && res !== null) {
          this.answerData = res.data
        }
      })

      for (let i = 1; i <= this.answerData.length; i++) {
        this.subjectItems.push(`第 ${i} 题`)
        this.answeredItems[i - 1] = true
      }
      this.wordIndex = 0
      this.setActiveWord()
    },
    clearData() {
      this.wordIndex = 0
      this.answerData = []
      this.subjectItems = []
    },
    read() {
      speakEn(this.answerData[this.wordIndex].value)
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
        if (this.wordIndex >= this.answerData.length - 1) {
          return
        }
        this.wordIndex++
      }
      this.setActiveWord()
    },
    // 切换当前单词
    changCurrentWord(index) {
      this.wordIndex = index
      this.answeredItems[index] = true
      this.setActiveWord()
    },
    setActiveWord() {
      const item = this.answerData[this.wordIndex]
      this.wordValues = item.value.split('').map(char => ({value: char}));
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
  margin-right: 10px;
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