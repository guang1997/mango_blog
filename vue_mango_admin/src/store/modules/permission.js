import {
  constantRoutes
} from '@/router'
import Layout from '@/layout/index'
import ParentView from '@/components/ParentView'

/**
 * Use meta.role to determine if the current user has permission
 * @param roles
 * @param route
 */
function hasPermission(roles, route) {
  if (route.meta && route.meta.roles) {
    return roles.some(role => route.meta.roles.includes(role))
  } else {
    return true
  }
}

/**
 * Filter asynchronous routing tables by recursion
 * @param routes asyncRoutes
 * @param roles
 */
export function filterAsyncRoutes(routes, roles) {
  const res = []
  routes.forEach(route => {
    const tmp = {
      ...route
    }
    if (hasPermission(roles, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, roles)
      }
      res.push(tmp)
    }
  })

  return res
}
const permission = {
  state : {
      routers: constantRoutes,
      addRouters: [],
      sidebarRouters: []
  },
  moutations: {
    SET_ROUTERS: (state, routers) => {
      state.addRouters = routers
      state.routers = constantRoutes.concat(routers)
    },
    SET_SIDEBAR_ROUTERS: (state, routers) => {
      state.sidebarRouters = constantRoutes.concat(routers)
    }
  },
  actions: {
    GenerateRoutes({ commit }, asyncRouter) {
      commit('SET_ROUTERS', asyncRouter)
    },
    SetSidebarRouters({ commit }, sidebarRouter) {
      commit('SET_SIDEBAR_ROUTERS', sidebarRouter)
    }
  }
}
export const filterAsyncRouter = (routers, lastRouter = false, type = false) => {
  // 将后台传过来的路由字符串转换为路由组件
  return routers.filter(router => {
    if (type && router.children) {
      router.children = filterChildren(router.children)
    }
    if (router.component) {
      if (router.component === 'Layout') { // Layout组件特殊处理
        router.component = Layout
      } else if (router.component === 'ParentView') {
        router.component = ParentView
      } else {
        const component = router.component
        router.component = loadView(component)
      }
    }
    if (router.children != null && router.children && router.children.length) {
      router.children = filterAsyncRouter(router.children, router, type)
    } else {
      delete router['children']
      delete router['redirect']
    }
    return true;
  })
}

function filterChildren(childrenMap, lastRouter = false) {
  var children = []
  childrenMap.forEach((el, index) => {
    if (el.children && el.children.length) {
      if (el.component === 'ParentView') {
        el.children.forEach(c => {
          c.path = el.path + '/' + c.path
          if (c.children && c.children.length) {
            children = children.concat(filterChildren(c.children, c))
            return
          }
          children.push(c)
        })
        return
      }
    }
    if (lastRouter) {
      el.path = lastRouter.path + '/' + el.path
    }
    children = children.concat(el)
  })
  return children
}

export const loadView = (view) => {
  return (resolve) => require([`@/views/${view}`], resolve)
}

// const actions = {
//   generateRoutes({ commit }, data) {
//     return new Promise(resolve => {
//       const { roles } = data;
//       let accessedRoutes
//       if (roles.includes('admin')) {
//         accessedRoutes = asyncRoutes || []
//       } else {
//         accessedRoutes = filterAsyncRoutes(asyncRoutes, roles)
//       }
//       // const accessedRoutes = asyncRoutes.filter(v => {
//       //   if (roles.includes('admin')) return true;
//       //   if (hasPermission(roles, v)) {
//       //     if (v.children && v.children.length > 0) {
//       //       v.children = v.children.filter(child => {
//       //         if (hasPermission(roles, child)) {
//       //           return child
//       //         }
//       //         return false;
//       //       });
//       //       return v
//       //     } else {
//       //       return v
//       //     }
//       //   }
//       //   return false;
//       // });
//       commit('SET_ROUTES', accessedRoutes);
//       resolve(accessedRoutes);
//     })
//   }
//   // generateRoutes: async function ({
//   //   commit
//   // }, roles) {
//   //   // return new Promise(resolve => {
//   //   // 从后台请求所有的路由信息
//   //   let res = await getMenu()
//   //   let dbAsyncRoutes = res.data.menu
//   //   // 替换组件名称，删除children
//   //   let myAsyncRoutes = dbAsyncRoutes.filter(curr => {
//   //     if (curr.children == null || curr.children.length == 0) {
//   //       delete curr.children
//   //     }
//   //     return replaceComponent(curr)
//   //   })
//   //   // 可以访问的路由表
//   //   let accessedRoutes
//   //   if (roles.includes('admin')) {
//   //     //路由是否有admin,有直接全部显示
//   //     accessedRoutes = myAsyncRoutes || []
//   //   } else {
//   //     //accessedRoutes这个就是当前角色可见的动态路由
//   //     accessedRoutes = filterAsyncRoutes(myAsyncRoutes, roles)
//   //   }
//   //   console.log("accessedRoutes", accessedRoutes)
//   //   commit('SET_ROUTES', accessedRoutes)
//   //   // resolve(accessedRoutes)
//   //   return accessedRoutes
//   //   // })
//   // }

// }

// 替换route对象中的component
// function replaceComponent(comp) {
//   if (comp.component && typeof (comp.component) == 'string') {
//     comp.component = componentMap[comp.component];
//   }
//   if (comp.children && comp.children.length > 0) {
//     for (let i = 0; i < comp.children.length; i++) {
//       comp.children[i] = replaceComponent(comp.children[i])
//     }
//   }
//   // if (comp.component && comp.component == 'Layout') {
//   //     comp.component = Layout;
//   //   } else {
//   //     comp.component = _import(comp.component)
//   //   }
//   //   if (comp.children && comp.children.length) {
//   //       for (let i = 0; i < comp.children.length; i++) {
//   //         comp.children[i] = replaceComponent(comp.children[i])
//   //       }
//   //     }
//   return comp
// }
export default permission
