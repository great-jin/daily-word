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
              @change="changeMode('mode', $event)"
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

      <el-row style="height: 100%; padding: 20px 0 40px 0">
        <el-col :span="24">
          <span>词典: </span>
          <el-select
              v-model="reqParams.catalogue"
              placeholder="请选中词典"
              @change="changeMode('dict', $event)"
              style="width: 120px; padding-right: 14px"
          >
            <el-option
                v-for="item in catalogues"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>

          <span>数量: </span>
          <el-select
              v-model="reqParams.size"
              placeholder="请选中单词数量"
              @change="changeMode('count', $event)"
              style="width: 120px; padding-right: 14px"
          >
            <el-option
                v-for="item in batchOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>

          <span v-if="reqParams.roomSize !== 0">积分奖励: </span>
          <el-input
              v-model="rankScore"
              style="width: 60px"
              v-if="reqParams.roomSize !== 0"
              disabled
          />
        </el-col>
      </el-row>

      <el-button type="primary" @click="startTask">开始匹配</el-button>
    </div>
  </el-dialog>
</template>

<script>
import {CATALOG_ARRAY} from "@/dict/catalogDict";
import {MODE_OPTIONS} from "@/dict/rankModeDict";
import {SIZE_OPTIONS} from "@/dict/rankTypeDict";
import {getUid} from "@/util/AuthUtil";
import {getWsUrl} from "@/util/SocketUtils";
import {Encrypt} from "@/util/EncryptUtil";
import {startTask} from "@/api/taskWordApi";
import {validate} from "@/api/matchApi";

export default {
  data() {
    return {
      visible: false,
      loading: {
        active: false,
        text: '匹配对局中...',
        intervalId: null,
      },
      isSingle: false,
      showRoomInput: false,
      modeOptions: MODE_OPTIONS,
      batchOptions: SIZE_OPTIONS,
      catalogues: CATALOG_ARRAY,
      // 房间参数
      roomNumValues: ['', '', '', '', '', ''],
      reqParams: {
        mode: 'RANDOM',
        roomSize: null,
        roomNumber: null,
        catalogue: CATALOG_ARRAY[0].value,
        size: SIZE_OPTIONS[0].value,
        offset: 10
      },
      // 对局积分
      rankScore: 10,
      taskWords: []
    }
  },
  methods: {
    show(data) {
      this.visible = true
      this.reqParams.roomSize = data
      this.isSingle = this.reqParams.roomSize === 0
      this.showRoomInput = this.reqParams.mode === 'CUSTOM' && !this.isSingle
    },
    closeDialog() {
      this.visible = false
      this.reqParams.mode = 'RANDOM'
      this.reqParams.roomSize = null
      this.reqParams.roomNumber = null
      this.roomNumValues = ['', '', '', '', '', '']

      this.clearIntervalLoop();
    },
    changeMode(type, data) {
      let s1, s2
      switch (type) {
        case 'mode':
          this.showRoomInput = data === 'CUSTOM' && this.reqParams.roomSize !== 0
          break
        case 'dict':
          s1 = this.catalogues.find(it => it.value === data).score
          s2 = this.batchOptions.find(it => it.value === this.reqParams.size).score
          this.rankScore = s1 + s2
          break
        case 'count':
          s1 = this.catalogues.find(it => it.value === this.reqParams.catalogue).score
          s2 = this.batchOptions.find(it => it.value === data).score
          this.rankScore = s1 + s2
          break
      }
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
    async startTask() {
      let isValid = false
      await validate(this.reqParams.catalogue).then(res => {
        if (res.data !== null && res.data) {
          // 校验是否可开启新对局
          isValid = true
        }
      })
      if (!isValid) {
        this.$message.warning('尚有多局未结束挑战，请稍后再试')
        return;
      }

      if (this.isSingle) {
        // 单人不进行匹配
        const _params = {
          catalogue: this.reqParams.catalogue,
          size: this.reqParams.size
        }
        startTask(_params).then(res => {
          if (res !== null && res.code === 200) {
            this.toAnswerPage(res.data)
          }
        })
        return
      }

      // 加载提示
      const _load = this.loading
      _load.active = true

      // 匹配连接
      const url = getWsUrl()
      const userId = getUid(false)
      const socket = new WebSocket(`${url}/rank?key=${userId}`);
      socket.addEventListener('open', () => {
        // 发起匹配
        socket.send(Encrypt(JSON.stringify(this.reqParams)))

        // 五分钟未匹配自动关闭
        let count = 1
        _load.intervalId = setInterval(() => {
          if (count++ > 300) {
            socket.close()
          }
        }, 1000);
      })

      socket.addEventListener('message', (event) => {
        const res = JSON.parse(event.data)
        if (res !== null && res.code === 302) {
          // 匹配成功
          this.toAnswerPage(res.data)
        }
      })

      socket.addEventListener('close', (event) => {
        this.clearIntervalLoop()
        console.log('WebSocket 连接已关闭', event.data);
      })

      socket.addEventListener('error', (event) => {
        this.clearIntervalLoop()
        console.error('WebSocket 错误', event);
        this.$message.info('匹配失败，请重试')
      })
    },
    clearIntervalLoop() {
      const _load = this.loading
      if (_load.intervalId !== null) {
        clearInterval(this.loading.intervalId)
      }
      _load.active = false
      _load.intervalId = null
    },
    toAnswerPage(data) {
      data.rankScore = this.rankScore
      this.$router.push({
        name: 'Answer',
        state: {
          taskData: data
        }
      })
    }
  }
}
</script>

<style scoped>
</style>