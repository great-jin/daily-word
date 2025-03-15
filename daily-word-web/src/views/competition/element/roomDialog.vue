<template>
  <el-dialog
      v-model="visible"
      title="创建房间"
      width="40%"
      @close="closeDialog"
  >
    <el-row style="height: 100%; padding: 20px 25px">
      <el-col :span="24">
        <span style="margin-right: 6px">房间号: </span>
        <el-input
            ref="inputRefs"
            v-for="(value, index) in roomNumValues"
            :key="index"
            v-model="roomNumValues[index]"
            maxlength="1"
            class="input-box"
            @input="handleInput(index)"
            @keydown="handleKeyDown(index)"
            style="width: 34px; margin-right: 10px"
        />
      </el-col>
    </el-row>

    <el-row style="height: 100%; padding: 20px 25px">
      <el-col :span="24">
        <span style="padding-right: 6px">词典: </span>
        <el-select
            v-model="reqParams.catalogue"
            placeholder="Select dictionary"
            style="width: 120px; padding-right: 20px"
        >
          <el-option
              v-for="item in catalogues"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>

        <span style="padding-right: 6px">数量: </span>
        <el-select
            v-model="reqParams.size"
            placeholder="Select batch size"
            style="width: 120px; padding-right: 20px"
        >
          <el-option
              v-for="item in batchOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>

      </el-col>
    </el-row>

    <el-button type="primary" @click="startTask">开始匹配</el-button>

    <AnswerDialog ref="answerDialog"/>
  </el-dialog>
</template>

<script>
import AnswerDialog from "./answerDialog.vue";
import {CATALOG_ARRAY, SIZE_ARRAY} from "@/views/competition/const";
import {getTaskContent} from "@/api/wordApi";

export default {
  components: {
    AnswerDialog
  },
  data() {
    return {
      visible: false,
      catalogues: CATALOG_ARRAY,
      batchOptions: SIZE_ARRAY,
      roomNumValues: ['', '', '', '', '', ''],
      reqParams: {
        roomSize: null,
        roomNumber: null,
        catalogue: CATALOG_ARRAY[0].value,
        size: SIZE_ARRAY[0].value,
        offset: 10
      },
      taskWords: []
    }
  },
  methods: {
    show(data) {
      this.visible = true
      this.reqParams.roomSize = data
    },
    closeDialog() {
      this.visible = false
      this.roomNumValues = ['', '', '', '', '', '']
    },
    startTask() {
      getTaskContent(this.reqParams).then(res => {
        this.reqParams.roomNumber = null
        this.$refs.answerDialog.show(res.data)
      })
    },
    handleInput(index) {
      if (!/^\d$/.test(this.roomNumValues[index])) {
        this.roomNumValues[index] = ''
        return
      }

      const val = this.roomNumValues[index]
      if (index < 5 && val !== '') {
        this.$refs.inputRefs[index + 1].focus()
      }
    },
    handleKeyDown(index) {
      const val = this.roomNumValues[index]
      // 监听删除键
      if (event.key === 'Backspace' && index > 0 && val === '') {
        // 阻止默认删除行为
        event.preventDefault();
        // 将光标自动定位到前一个输入框
        this.$refs.inputRefs[index - 1].focus()
      }
    }
  }
}
</script>

<style scoped>
</style>