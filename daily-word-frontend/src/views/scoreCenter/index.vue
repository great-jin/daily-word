<template>
  <div style="background-color: white; height: 100%">
    积分中心
  </div>
</template>

<script>
export default {
  inject: ['reload'],
  data() {
    return {
      text: '',
    }
  },
  created() {
    const url = 'ws://localhost:9090/dailyWord/websocket?userId=1'
    const socket = new WebSocket(url);

    // 添加WebSocket事件监听器
    socket.addEventListener('open', (event) => {
      console.log('WebSocket 连接已打开', event);
    })

    socket.addEventListener('message', (event) => {
      const data = event.data
      console.log('接受到后端信息: ', data)
      if (data !== null) {
        this.$message.success('接受到后端消息: ' + data)
      }
    })

    socket.addEventListener('close', (event) => {
      console.log('WebSocket 连接已关闭', event.data);
    })

    socket.addEventListener('error', (event) => {
      console.error('WebSocket 错误', event);
    })
  }
}

</script>

<style scoped>
</style>