<template xmlns="http://www.w3.org/1999/html">
  <div class="container">
    <el-row :gutter="20" style="width: 100%; padding: 20px 25px">
      <el-col :span="6">
        <el-card class="box-card" style="float: left">
          <template #header>
            <div class="card-header">
              <span>今日计划</span>
            </div>
          </template>
          <div v-for="o in 2" :key="o" class="text item">{{ 'List item ' + o }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card" style="float: left">
          <template #header>
            <div class="card-header">
              <span>复习计划</span>
            </div>
          </template>
          <div v-for="o in 2" :key="o" class="text item">{{ 'List item ' + o }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card" style="float: left">
          <template #header>
            <div class="card-header">
              <span>历史单词</span>
            </div>
          </template>
          <div v-for="o in 2" :key="o" class="text item">{{ 'List item ' + o }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card" style="float: left">
          <template #header>
            <div class="card-header">
              <span>我的积分</span>
            </div>
          </template>
          <div v-for="o in 2" :key="o" class="text item">{{ 'List item ' + o }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row style="padding: 20px 25px">
      <el-col :span="24">
        <el-row :style="{marginTop: '20px'}">
          <el-col :span="24">
            词典: &nbsp;
            <el-select
                v-model="wordRequest.catalogue"
                placeholder="选择字典"
                style="margin-right: 25px"
            >
              <el-option label="CET4" value="CET4"/>
              <el-option label="CET6" value="CET6"/>
              <el-option label="CET6" value="GRE"/>
              <el-option label="考研词典" value="Graduate"/>
              <el-option label="牛津词典" value="Oxford"/>
            </el-select>
            数量: &nbsp;
            <el-select
                v-model="wordRequest.batchSize"
                placeholder="选择数量"
                style="margin-right: 25px"
            >
              <el-option
                  v-for="it in [10, 20, 30, 50]"
                  :label="`${it}个/组`"
                  :value="it"
              />
            </el-select>
            偏移量: &nbsp;
            <el-input-number
                v-model="wordRequest.offset"
                :min="1"
                style="margin-right: 25px"
            />
            <el-button type="primary" @click="getPlanData">开始</el-button>
            <el-button type="primary" @click="clickOption('refresh')">刷新</el-button>
          </el-col>
        </el-row>

        <el-row :style="{marginTop: '40px', minHeight: '30%'}" v-if="this.planData.length > 0">
          <div style="width: 100%; text-align: center;">
            <p
                v-for="item in planData[currentDataIndex].translation"
                style="width: 100%; font-weight: bold"
            >
              {{ item }}
            </p>
          </div>
        </el-row>

        <el-row :style="{marginTop: '20px'}" v-if="this.planData.length > 0">
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
                :border="false"
                class="input-word"
            />

            <p style="text-align: center; color: grey">
              <el-icon @click="choose('back')" style="vertical-align: middle;">
                <CaretLeft/>
              </el-icon>
              第 {{ this.currentDataIndex + 1 }} / {{ this.planData.length }} 题
              <el-icon @click="choose('next')" style="vertical-align: middle;">
                <CaretRight/>
              </el-icon>
            </p>
          </el-col>
        </el-row>

        <el-row :style="{marginTop: '20px'}" v-if="this.planData.length > 0">
          <el-col :span="24">
            <el-button type="primary" @click="clickOption('hit')">提示</el-button>
            <el-button type="primary" @click="clickOption('speak')">朗读</el-button>
            <el-button type="primary" @click="clickOption('answer')">答案</el-button>
            <el-button type="primary" @click="clickOption('clear')">清空</el-button>
          </el-col>
        </el-row>


        <el-row :gutter=20 :style="{marginTop: '20px'}" v-if="this.planData.length > 0">
          <el-col :span="12">
            <el-button
                type="primary"
                style="float: right"
                @click="choose('back')"
            >
              <el-icon><ArrowLeftBold /></el-icon>
              &nbsp;上一题
            </el-button>
          </el-col>
          <el-col :span="12">
            <el-button
                type="primary"
                style="float: left"
                @click="choose('next')"
            >
              下一题 &nbsp;
              <el-icon><ArrowRightBold /></el-icon>
            </el-button>
          </el-col>
        </el-row>
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
      wordRequest: {
        catalogue: 'CET4',
        batchSize: 10,
        offset: 0,
      },
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
      await getTaskContent(this.wordRequest).then(res => {
        this.planData = res.data
        for (let i = 1; i <= this.planData.length; i++) {
          this.checkboxItems.push(`第 ${i} 题`)
        }
      })
      this.currentDataIndex = 0
      this.setSingleWord(this.planData[0].value)
    },
    choose(type) {
      const index = this.currentDataIndex
      switch (type) {
        case 'back':
          if (index <= 0) {
            this.$message.info('已经是第一题了哦')
            return
          }
          this.currentDataIndex--
          this.setSingleWord(this.planData[this.currentDataIndex].value)
          break
        case 'next':
          if (index >= this.planData.length - 1) {
            this.$message.info('已经是最后一题了哦')
            return
          }
          this.currentDataIndex++
          this.setSingleWord(this.planData[this.currentDataIndex].value)
          break
      }
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
          let word = this.planData[this.currentDataIndex].value
          word = word.replace(' ', '')
          if (_data === word) {
            if (this.currentDataIndex >= this.planData.length - 1) {
              this.$message.success('恭喜你已完成本次测试')
              this.reload()
              return
            } else {
              this.$message.success('回答正常')
              this.choose('next')
              this.$refs.inputRefs[0].focus();
            }
          } else {
            this.$message.error('输入不正确请检查')
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
        case 'speak':
          speakEn(this.planData[this.currentDataIndex].value)
          break
        case 'answer':
          this.$message.success(this.planData[this.currentDataIndex].value)
          break
        case 'clear':
          this.inputValues = Array.from({length: this.textLength}, () => ({value: ''}));
          this.$refs.inputRefs[0].focus();
          break
        case 'refresh':
          this.reload()
          break
      }
    },
    setSingleWord(word) {
      word = word.replace(' ', '')
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

<style scoped>
.container {
  height: 100%;
  background-color: white;
}

.input-word {
  width: 35px;
}

.box-card {
  width: 100%;
}
</style>