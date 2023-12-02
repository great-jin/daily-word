const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  devServer: {
    // 跨越配置
    open: true,
    // 前端地址端口
    host: 'localhost',
    port: 8080,
    https: false,
    proxy: {
      '/daily-word': {
        // 后端地址端口
        target: 'http://localhost:9090/',
        ws: true,
        changOrigin: true,
        pathRewrite: {
          '^/daily-word': '/dailyWord'
        }
      }
    },

    client: {
      overlay: {
        warnings: false,
        errors: false,
      },

      // or
      overlay: false,
    }
  }
})
