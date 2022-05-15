import store from '@/store'

export default {
  inserted(el, binding) {
    const { value } = binding;
    const roles = value.roles;
    const menuButtons = value.menuButtons;

    const currentRoles = store.getters && store.getters.roles
    const currentmenuButtons = store.getters && store.getters.menuButtons
    
    if (value) {
      if (roles.length > 0) {
        // 先判断是否有角色权限
        const hasPermission = currentRoles.some(role => {
          return roles.includes(role)
        })
        // 如果有角色权限再判断是否有单个按钮的权限
        if (hasPermission ) {
          if (menuButtons.length > 0) {
            const hasButtonPermission = currentmenuButtons.some(menuButton => {
              return menuButtons.includes(menuButton)
            })
            if (!hasButtonPermission) {
              el.parentNode && el.parentNode.removeChild(el)
            }
          }
        } else {
          el.parentNode && el.parentNode.removeChild(el)
        }
      }
    } else {
      throw new Error(`使用方式有误`)
    }
  }
}
