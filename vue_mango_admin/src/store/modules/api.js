// 适配 Nginx 反向代理
const baseUrl = process.env.VUE_APP_BASE_API === '/' ? '' : process.env.VUE_APP_BASE_API
const baseWebSocketUrl = process.env.VUE_APP_WEBSOCKET_URL
const api = {
  state: {
    // monitor websocket
    monitorWebsocketApi: baseWebSocketUrl + '/admin/websocket',
    // 图片上传
    imagesUploadApi: baseUrl + '/admin/oss/upload',
    // 博客中的图片上传
    blogImagesUploadApi: baseUrl + '/admin/oss/qiNiuUpload',
    // 个人中心页面上传头像
    uploadAvatarApi: baseUrl + '/admin/oss/uploadAvatar',
    // druid
    druidApi: baseUrl + '/druid',
    // baseUrl，
    baseApi: baseUrl
  }
}

export default api
