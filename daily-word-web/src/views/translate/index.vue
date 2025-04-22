<template>
  <el-row :gutter="20">
    <!-- 原文输入 -->
    <el-col :span="12">
      <el-card>
        <template #header>
          <div class="card-head">原文</div>
        </template>

        <el-row style="margin-bottom: 20px">
          <el-col :span="24">
            <el-button
                type="primary"
                @click="translate"
                style="float: left"
            >翻 译</el-button>
            <el-button
                @click="clear"
                style="float: left"
            >清空</el-button>

            <el-button
                type="primary"
                @click="read"
                style="float: right"
            >朗读</el-button>
            <el-select
                v-model="targetLang"
                placeholder="选择语种"
                style="width: 120px; float: right"
            >
              <el-option label="英文" value="en"/>
              <el-option label="中文" value="zh"/>
              <el-option label="日语" value="ja"/>
              <el-option label="韩语" value="ko"/>
            </el-select>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-input
                type="textarea"
                v-model="originalText"
                :rows="15"
                resize="none"
                placeholder="请输入需要翻译的文本"
            />
          </el-col>
        </el-row>
      </el-card>
    </el-col>

    <!-- 翻译结果 -->
    <el-col :span="12">
      <el-card>
        <template #header>
            <span class="card-head">翻译结果</span>
        </template>
        <el-input
            type="textarea"
            :value="translatedText"
            :rows="15"
            resize="none"
            placeholder="翻译内容将显示在此处"
            readonly
        />
      </el-card>
    </el-col>
  </el-row>
</template>

<script>
import {speakEn} from "@/util/SpeakUtil";

export default {
  data() {
    return {
      originalText: '',
      translatedText: '',
      targetLang: 'en'
    }
  },
  methods: {
    translate() {
      if (this.originalText === '') {
        this.translatedText = ''
        this.$message.warning('请输入需翻译内容')
        return
      }

      this.translatedText = `[${this.targetLang}] ${this.originalText}`
    },
    clear() {
      this.originalText = ''
      this.translatedText = ''
    },
    read() {
      if (this.originalText === '') {
        this.$message.warning('请输入需翻译内容')
        return
      }

      speakEn(this.originalText)
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
