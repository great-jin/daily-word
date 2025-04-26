<template>
  <el-row :gutter="20" style="height: 100%">
    <!-- 原文输入 -->
    <el-col :span="12" style="height: 100%">
      <el-card style="height: 100%">
        <template #header>
          <div class="card-head">原文</div>
        </template>

        <el-row style="margin-bottom: 20px">
          <el-col :span="24">
            <el-button
                @click="clear"
                style="float: left"
            >清空
            </el-button>

            <el-button
                type="primary"
                @click="translate"
                style="float: right;"
            >翻 译
            </el-button>
            <el-select
                v-model="reqInfo.model"
                placeholder="选择模型"
                @change="changeMode"
                style="width: 120px; float: right"
            >
              <el-option label="OPUS" value="OPUS"/>
              <el-option label="NLLB" value="NLLB"/>
            </el-select>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-input
                type="textarea"
                v-model="reqInfo.text"
                :rows="15"
                resize="none"
                placeholder="请输入需要翻译的文本"
                style="font-size: 18px"
            />
          </el-col>
        </el-row>
      </el-card>
    </el-col>

    <!-- 翻译结果 -->
    <el-col :span="12" style="height: 100%">
      <el-card style="height: 100%">
        <template #header>
          <span class="card-head">翻译结果</span>
        </template>

        <el-row style="margin-bottom: 20px">
          <el-col :span="24">
            <el-select
                v-model="reqInfo.targetType"
                placeholder="选择语种"
                @change="changeLanguage"
                style="width: 120px; float: left"
            >
              <el-option label="英文" value="en"/>
              <el-option label="中文" value="zh"/>
              <el-option label="法语" value="fr"/>
              <el-option label="德语" value="de"/>
              <el-option label="日语" value="ja"/>
              <el-option label="韩语" value="ko"/>
            </el-select>

            <el-button
                type="primary"
                @click="read"
                style="float: right"
            >朗读
            </el-button>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-input
                type="textarea"
                :value="resultText"
                :rows="15"
                resize="none"
                placeholder="翻译内容将显示在此处"
                style="font-size: 18px"
                readonly
            />
          </el-col>
        </el-row>
      </el-card>
    </el-col>
  </el-row>
</template>

<script>
import {speakEn} from "@/util/SpeakUtil";
import {callTranslate} from "@/api/engineApi";

export default {
  data() {
    return {
      reqInfo: {
        text: '',
        targetType: '',
        model: ''
      },
      resultText: ''
    }
  },
  methods: {
    async translate() {
      this.resultText = ''
      if (this.reqInfo.text === '') {
        this.$message.warning('请输入需翻译内容')
        return
      }
      if (this.reqInfo.text.length > 250) {
        this.$message.warning('翻译内容不可超过 250 字符')
        return
      }
      if (this.reqInfo.model === '') {
        this.$message.warning('请选择 AI 模型')
        return
      }
      if (this.reqInfo.targetType === '') {
        this.$message.warning('请输入目标语言')
        return
      }

      await callTranslate(this.reqInfo).then(res => {
        if (res.code !== 200) {
          return
        }
        if (res.data === null) {
          this.$message.error('系统繁忙')
          return
        }

        this.resultText = res.data
      })
    },
    changeMode(data) {
      if (this.reqInfo.text === '') {
        return
      }

      this.reqInfo.model = data
      this.translate()
    },
    changeLanguage(data) {
      if (this.reqInfo.text === '') {
        return
      }

      this.reqInfo.targetType = data
      this.translate()
    },
    clear() {
      this.reqInfo.text = ''
      this.resultText = ''
    },
    read() {
      if (this.resultText === '') {
        this.$message.warning('请先翻译内容')
        return
      }

      speakEn(this.resultText)
    }
  }
}
</script>

<style scoped>
.card-head {
  font-size: 18px;
  font-weight: bold;
}
</style>
