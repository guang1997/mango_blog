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
  sidebarRouters: state => state.permission.sidebarRouters
}
export default getters
