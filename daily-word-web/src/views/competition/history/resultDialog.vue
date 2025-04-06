<template>
  <el-dialog
      v-model="visible"
      width="60%"
      title="对局结果"
      @close="clearData"
      :show-close="false"
  >
    <el-row>
      <el-col :span="18" class="container">
        <el-row :style="{marginTop: '50px'}">
          <el-col :span="24">
            <el-button type="primary" @click="changeCurrentIndex('back')">上一题</el-button>
            <el-button type="primary" @click="changeCurrentIndex('next')">下一题</el-button>
            <el-button type="primary" @click="submit">提&nbsp;&nbsp;交</el-button>
          </el-col>
        </el-row>

        <el-row :style="{marginTop: '100px'}">
          <el-col :span="24">
            <div
                v-if="planData.length > 0"
                v-for="item in planData[currentIndex].translation"
                style="text-align: center"
            >
              <strong>
              <span style="width: 100%">
                {{ item }}
              </span>
              </strong>
            </div>
          </el-col>
        </el-row>

        <el-row :style="{marginTop: '20px'}">
          <el-col :span="24">
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

        <el-row :style="{marginTop: '30px'}">
          <el-col :span="24">
            <span>{{ currentIndex + 1 }} / {{ planData.length }}</span>
          </el-col>
        </el-row>
      </el-col>

      <el-col :span="6">
        <div class="word-container">
          <el-checkbox
              v-for="(item, index) in subjectItems"
              :key="index"
              @change="changCurrentWord(index)"
              size="large"
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
  name: 'TaskAnswer',
  data() {
    return {
      visible: false,
      planData: [],
      currentIndex: 0,
      // 输入与长度
      inputValues: [],
      textLength: 0,
      // 题目
      subjectItems: [],
      // 回答记录
      submitRecords: []
    }
  },
  methods: {
    show(data) {
      this.visible = true

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
    submit() {

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
    },
    // 切换当前单词
    changCurrentWord(index) {
      // 未回答过移除选中效果
      const item = this.submitRecords.find(it => it.position === index)
      this.answeredItems[index] = item !== undefined && item !== null

      // 回填
      this.currentIndex = index
      this.setAndRefillData(index)
    },
    clickOption(type) {
      switch (type) {
        case 'read':
          speakEn(this.planData[this.currentIndex].value)
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
  padding: 0 100px;
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
  margin-left: 20px;
  padding: 20px;
  overflow-y: scroll;
  scroll-behavior: smooth;
  background: #fce6dd;
  border-radius: 20px;
}
</style>