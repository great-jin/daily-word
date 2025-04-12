<template>
  <div class="container">
    <div class="search-container">
      <div class="search-logo">
        <img
            src="@/assets/word.png"
            style="max-width: 250px; max-height: 250px;margin-left: -10%"
        >
      </div>

      <el-input
          v-model="searchText"
          placeholder="请输入搜索内容"
          class="search-input"
          @keyup.enter="translateWord"
      />

      <el-button
          @click="randomizePosition"
          :style="buttonStyle"
      >{{ randomCount === 0 ? `翻  译` : buttonInfo }}
      </el-button>
    </div>

    <div style="text-align: center;">
      <p v-for="it in describeInfo" style="font-weight: bold">
        {{ it }}
      </p>
    </div>
  </div>
</template>

<script>
import {translate} from "@/api/taskWordApi";

export default {
  data() {
    return {
      randomCount: 0,
      buttonInfo: '翻译',
      searchText: '',
      describeInfo: [],
      buttonStyle: {
        position: 'absolute',
        top: '40%', // 初始位置
        left: '46%', // 初始位置
        borderRadius: '95px',
        width: '140px',
        height: '45px',
        border: 'none',
        backgroundColor: '#187CF8',
        color: 'white',
        fontSize: '18px',
        fontWeight: 'bold',
      }
    }
  },
  methods: {
    translateWord() {
      if (this.searchText === '') {
        this.$message.warning('请先输入单词')
        return
      }

      this.clearCount()
      translate(this.searchText).then(res => {
        if (res.data.existed) {
          const describeList = res.data.describeList
          this.describeInfo = []
          describeList.forEach(it => {
            this.describeInfo.push(it.type + ' ' + it.describe)
          })
        } else {
          this.describeInfo = []
          this.$message.warning('未检索到单词，请检查是否拼写错误.')
        }
      })
    },
    randomizePosition() {
      if (this.searchText === '') {
        this.$message.warning('请先输入单词')
        return
      }
      const randomInt = Math.floor(Math.random() * 101)
      if (this.randomCount === 0 && randomInt !== 56) {
        this.translateWord()
        return
      }

      if (this.randomCount >= 3) {
        this.clearCount()
        this.translateWord()
        return;
      }
      this.buttonInfo = '我在这'
      this.randomCount++
      this.buttonStyle = {
        ...this.buttonStyle,
        top:  Math.floor(Math.random() * (window.innerHeight - 65)) + 'px',
        left: Math.floor(Math.random() * (window.innerWidth - 65)) + 'px'
      }
    },
    clearCount() {
      this.buttonStyle = {
        ...this.buttonStyle,
        top: '40%', // 初始位置
        left: '46%', // 初始位置
      }
      this.randomCount = 0
    }
  }
}
</script>

<style scoped>
.container {
  height: 100%;
  border-radius: 10px;
  background-color: white;
}

.search-container {
  display: flex;
  flex-direction: column; /* 列表布局 */
  justify-content: center;
  align-items: center;
  height: 35vh; /* 让容器占据整个视窗高度 */
}

.search-logo {
  width: 200px;
  height: 100px;
  margin-bottom: 25px;
}

.search-input {
  width: 40%;
  border-radius: 15px 50px 30px 5px;
  height: 14%;
  font-size: 20px;
}

:deep(.el-input__inner) {
  padding-left: 15px;
}

:deep(.el-input__wrapper) {
  background-color: cornsilk;
  border-radius: 95px;
  border: 0;
  box-shadow: 0 0 0 0px;
}

.search-button {
  float: left;
  margin-left: 0%;
  border-radius: 95px;
  width: 8%;
  height: 14%;
  z-index: 9;
  border: none;
  background-color: #72C1F2;
  color: white;
  font-size: 18px;
  font-weight: bold;
  margin-top: 2%;
}
</style>
