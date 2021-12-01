import {
  asyncRoutes,
  constantRoutes
} from '@/router'


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

const state = {
  routes: [],
  addRoutes: []
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes

    state.routes = constantRoutes.concat(routes)
    console.log('state.routes', state.routes)
  }
}

const actions = {
  generateRoutes({ commit }, data) {
    return new Promise(resolve => {
      const { roles } = data;
      let accessedRoutes
      if (roles.includes('admin')) {
        accessedRoutes = asyncRoutes || []
      } else {
        accessedRoutes = filterAsyncRoutes(asyncRoutes, roles)
      }
      // const accessedRoutes = asyncRoutes.filter(v => {
      //   if (roles.includes('admin')) return true;
      //   if (hasPermission(roles, v)) {
      //     if (v.children && v.children.length > 0) {
      //       v.children = v.children.filter(child => {
      //         if (hasPermission(roles, child)) {
      //           return child
      //         }
      //         return false;
      //       });
      //       return v
      //     } else {
      //       return v
      //     }
      //   }
      //   return false;
      // });
      commit('SET_ROUTES', accessedRoutes);
      resolve(accessedRoutes);
    })
  }
  // generateRoutes: async function ({
  //   commit
  // }, roles) {
  //   // return new Promise(resolve => {
  //   // 从后台请求所有的路由信息
  //   let res = await getMenu()
  //   let dbAsyncRoutes = res.data.menu
  //   // 替换组件名称，删除children
  //   let myAsyncRoutes = dbAsyncRoutes.filter(curr => {
  //     if (curr.children == null || curr.children.length == 0) {
  //       delete curr.children
  //     }
  //     return replaceComponent(curr)
  //   })
  //   // 可以访问的路由表
  //   let accessedRoutes
  //   if (roles.includes('admin')) {
  //     //路由是否有admin,有直接全部显示
  //     accessedRoutes = myAsyncRoutes || []
  //   } else {
  //     //accessedRoutes这个就是当前角色可见的动态路由
  //     accessedRoutes = filterAsyncRoutes(myAsyncRoutes, roles)
  //   }
  //   console.log("accessedRoutes", accessedRoutes)
  //   commit('SET_ROUTES', accessedRoutes)
  //   // resolve(accessedRoutes)
  //   return accessedRoutes
  //   // })
  // }

}

// 替换route对象中的component
function replaceComponent(comp) {
  if (comp.component && typeof (comp.component) == 'string') {
    comp.component = componentMap[comp.component];
  }
  if (comp.children && comp.children.length > 0) {
    for (let i = 0; i < comp.children.length; i++) {
      comp.children[i] = replaceComponent(comp.children[i])
    }
  }
  // if (comp.component && comp.component == 'Layout') {
  //     comp.component = Layout;
  //   } else {
  //     comp.component = _import(comp.component)
  //   }
  //   if (comp.children && comp.children.length) {
  //       for (let i = 0; i < comp.children.length; i++) {
  //         comp.children[i] = replaceComponent(comp.children[i])
  //       }
  //     }
  return comp
}
export default {
  namespaced: true,
  state,
  mutations,
  actions
}
