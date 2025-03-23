<template>
  <el-dialog
      v-model="visible"
      title="匹配对局"
      width="40%"
      @close="closeDialog"
  >
    <div
        v-loading="loading.active"
        :element-loading-text="loading.text"
    >
      <el-row
          v-if="reqParams.roomSize !== 0"
          style="height: 100%; padding: 20px 25px"
      >
        <el-col :span="24">
          <span style="margin-right: 6px">匹配方式: </span>
          <el-select
              v-model="reqParams.mode"
              placeholder="选择匹配方式"
              style="width: 240px"
              @change="changeMode"
          >
            <el-option
                v-for="item in modeOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-col>
      </el-row>

      <el-row
          v-if="showRoomInput"
          style="height: 100%; padding: 20px 25px"
      >
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
    </div>
  </el-dialog>
</template>

<script>
import AnswerView from "../answer/index.vue";
import {CATALOG_ARRAY, SIZE_ARRAY} from "@/views/competition/const";
import {getTaskContent} from "@/api/wordApi";

export default {
  data() {
    return {
      visible: false,
      loading: {
        active: false,
        text: '匹配对局中...',
        intervalId: null,
      },
      showRoomInput: false,
      catalogues: CATALOG_ARRAY,
      batchOptions: SIZE_ARRAY,
      roomNumValues: ['', '', '', '', '', ''],
      modeOptions: [
        {
          label: '随机匹配',
          value: 1
        },
        {
          label: '自定义房间',
          value: 2
        },
      ],
      reqParams: {
        mode: 1,
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
      this.showRoomInput = this.reqParams.mode === 2 && this.reqParams.roomSize !== 0
    },
    closeDialog() {
      this.visible = false
      this.reqParams.mode = 1
      this.reqParams.roomSize = null
      this.reqParams.roomNumber = null
      this.roomNumValues = ['', '', '', '', '', '']

      this.clearIntervalLoop();
    },
    changeMode(data) {
      this.showRoomInput = data === 2 && this.reqParams.roomSize !== 0
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
    },
    startTask() {
      const _load = this.loading
      let count = 1
      _load.active = true
      _load.intervalId = setInterval(() => {
        // TODO 模拟耗时

        if (count++ > 0) {
          this.clearIntervalLoop()

          getTaskContent(this.reqParams).then(res => {
            this.reqParams.roomNumber = null

            // 新版不支持 params 方式，通过 state 实现
            this.$router.push({
              name: 'RoomRank',
              state: {
                planData: res.data
              }
            })
          })
        }
      }, 1000);
    },
    clearIntervalLoop() {
      const _load = this.loading
      if (_load.intervalId !== null) {
        clearInterval(this.loading.intervalId)
      }
      _load.active = false
      _load.intervalId = null
    }
  }
}
</script>

<style scoped>
</style>