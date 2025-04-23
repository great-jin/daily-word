<template>
  <div style="height: 100%;">
    <el-row style="height: 100%;">
      <el-col :span="18" class="container">
        <el-row :style="{marginTop: '120px'}">
          <el-col :span="24">
            <div
                v-if="planData.length > 0"
                v-for="item in planData[currentIndex].translation"
                style="text-align: center"
            >
              <strong>
                <span class="translate-content">{{ item }}</span>
              </strong>
            </div>
          </el-col>
        </el-row>

        <el-row>
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

        <el-row>
          <el-col :span="24">
            <!-- 题数下标 -->
            <el-row>
              <el-col :span="24">
                <span>{{ currentIndex + 1 }} / {{ planData.length }}</span>
              </el-col>
            </el-row>

            <!-- 计时器 -->
            <el-row :style="{margin: '30px 0'}">
              <el-col :span="24">
                <span>
                  <strong>作答耗时:</strong>
                  {{ formattedTime }}
                </span>
              </el-col>
            </el-row>

            <el-row :style="{margin: '100px 0 40px 0'}">
              <el-col :span="24">
                <!-- 题目切换 -->
                <el-row :style="{marginTop: '60px'}">
                  <el-col :span="24">
                    <el-button type="primary" @click="changeCurrentIndex('back')">上一题</el-button>
                    <el-button type="primary" @click="changeCurrentIndex('next')">下一题</el-button>
                    <el-popconfirm
                        title="请确认结束对局并提交！"
                        confirm-button-text="是"
                        cancel-button-text="否"
                        placement="top"
                        width="200"
                        @confirm="submit"
                    >
                      <template #reference>
                        <el-button key="submit" type="primary">提&nbsp;&nbsp;交</el-button>
                      </template>
                    </el-popconfirm>
                    <el-button type="warning" @click="clickOption('clear')">清空</el-button>
                  </el-col>
                </el-row>
              </el-col>
            </el-row>
          </el-col>
        </el-row>
      </el-col>

      <el-col :span="6">
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

    <ResultDialog ref="resultDialog"/>
  </div>
</template>

<script>
import {speakEn} from "@/util/SpeakUtil";
import ResultDialog from "./resultDialog.vue"
import {submitTask} from "@/api/matchApi";

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
      // 对局信息
      rankInfo: {
        matchId: '',
        score: 0
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
    const taskData = history.state.taskData
    this.rankInfo.matchId = taskData.matchId
    this.rankInfo.score = taskData.rankScore
    this.show(taskData.taskWords)

    // 监听页面刷新或关闭事件
    window.addEventListener("beforeunload", this.handleForceRefresh);
  },
  beforeDestroy() {
    // 清除计时器
    const _this = this.clock
    clearInterval(_this.timer);

    // 清除监听事件
    window.removeEventListener("beforeunload", this.handleForceRefresh);
  },
  methods: {
    show(data) {
      this.clearData()
      this.planData = data
      for (let i = 1; i <= this.planData.length; i++) {
        this.subjectItems.push(`第 ${i} 题`)
      }
      this.currentIndex = 0
      this.textLength = this.planData[this.currentIndex].wordLength
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
    handleForceRefresh(event) {
      event.returnValue = '对局尚未提交，您确定退出吗？';
      // TODO 监控取消

    },
    submit() {
      this.visible = false
      this.finishTask()
    },
    finishTask() {
      const _params = {
        matchId: this.rankInfo.matchId,
        costTime: this.clock.time,
        score: this.rankInfo.score,
        contentList: this.submitRecords
      }
      submitTask(_params).then(res => {
        if (res.code === 200) {
          this.$refs.resultDialog.show()
        }
      })
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
        // 记录当前输入
        this.storeCurrentInput()

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

      // 存储输入内容
      this.storeCurrentInput()

      // 下一题
      this.answeredItems[this.currentIndex] = true
      this.changeCurrentIndex('next')
    },
    storeCurrentInput() {
      // 记录答题内容以回填
      let _data = ''
      this.inputValues.forEach(it => {
        _data += it.value
      })

      const _index = this.currentIndex
      const item = {
        position: _index,
        offset: this.planData[_index].offset,
        answer: _data
      }
      this.submitRecords = this.submitRecords.filter(it => it.position !== _index)
      this.submitRecords.push(item)
    },
    // 回填已输入内容
    setAndRefillData(index) {
      this.textLength = this.planData[this.currentIndex].wordLength

      let word;
      let item = this.submitRecords.find(it => it.position === index)
      if (item !== undefined && item !== null) {
        // 作答过读取内容回填
        word = item.answer
        const inputLength = word.length
        this.inputValues = Array.from({length: this.textLength}, (_, i) =>  ({
          value: i < inputLength ? word[i] : ''
        }))
      } else {
        // 未作答过
        this.inputValues = Array.from({length: this.textLength}, () => ({value: ''}));
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
  padding: 0 100px;
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
  margin-right: 10px;
  text-align: center;
  border-bottom: 1px solid #409EFF;
}

.word-container {
  height: calc(100vh - 240px);
  margin-left: 20px;
  padding: 20px;
  overflow-y: scroll;
  scroll-behavior: smooth;
  background: #fce6dd;
  border-radius: 20px;
}
</style>