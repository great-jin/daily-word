const { defineConfig } = require('@vue/cli-service')

const frontendHost = '119.3.153.62'
const frontendPort = 8080

const backendHost = '119.3.153.62'
const backendPort = 9090

module.exports = defineConfig({
  devServer: {
    // 跨越配置
    open: true,
    // 前端地址端口
    host: frontendHost,
    port: frontendPort,
    https: false,
    proxy: {
      '/daily-word': {
        // 后端地址端口
        target: `http://${backendHost}:${backendPort}/`,
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
