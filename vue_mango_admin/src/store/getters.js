const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  name: state => state.user.name,
  roles: state =>state.user.roles,
  user: state => state.user.user,
  permission_routers: state =>state.permission.routes,
  loadMenus: state => state.user.loadMenus,
  addRouters: state => state.permission.addRouters,
  sidebarRouters: state => state.permission.sidebarRouters,
  imagesUploadApi: state => state.api.imagesUploadApi,
  blogImagesUploadApi: state => state.api.blogImagesUploadApi,
  monitorWebsocketApi: state => state.api.monitorWebsocketApi,
  druidApi: state => state.api.druidApi,
  baseApi: state => state.api.baseApi,
  uploadAvatarApi: state => state.api.uploadAvatarApi,
  menuButtons: state => state.user.menuButtons
}
export default getters
