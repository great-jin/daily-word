const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  devServer: {
    // 跨越配置
    open: true,
    // frontend server host and port
    host: process.env.VUE_APP_API_HOST,
    port: process.env.VUE_APP_API_PORT,
    https: false,
    proxy: {
      [process.env.VUE_APP_API_BASE_PREFIX]: {
        // backend server url
        target: process.env.VUE_APP_API_BASE_URL,
        ws: true,
        changOrigin: true,
        pathRewrite: {
          // Rewrite path prefix to backend "servlet:context-path"
          [`^${process.env.VUE_APP_API_BASE_PREFIX}`]: `${process.env.VUE_APP_API_SERVLET_PREFIX}`
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
