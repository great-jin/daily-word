const host = process.env.VUE_APP_API_HOST
const port = process.env.VUE_APP_API_WEBSOCKET_PORT
const servletPrefix = process.env.VUE_APP_API_SERVLET_PREFIX

export function getWsUrl() {
    return `ws://${host}:${port}${servletPrefix}`
}
