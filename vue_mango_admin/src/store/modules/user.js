import {
  login,
  logout,
  getInfo
} from '@/api/user'
import {
  getToken,
  setToken,
  removeToken
} from '@/utils/auth'
import {
  resetRouter
} from '@/router'

const user = {
  state: {
    token: getToken(),
    name: '',
    avatar: '',
    roles: [],
    // 第一次加载菜单时用到
    loadMenus: false
  },
  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_LOAD_MENUS: (state, loadMenus) => {
      state.loadMenus = loadMenus
    }
  },
  actions: {
    // 登录
    Login({ commit }, userInfo) {
      const {
        username,
        password
      } = userInfo
      return new Promise((resolve, reject) => {
        login({
          username: username.trim(),
          password: password.trim()
        }).then(response => {
          const data = response.data
          // 向store中设置token
          commit('SET_TOKEN', data.token)
          // 向cookie中设置token
          setToken(data.token)
          // setUserInfo(res.user, commit)
          // 第一次加载菜单时用到， 具体见 src 目录下的 permission.js
          commit('SET_LOAD_MENUS', true)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 获取用户信息
    GetInfo({ commit }) {
      return new Promise((resolve, reject) => {
        getInfo().then(response => {
          const data = response.data

        if (data && data.roles && data.roles.length > 0) { // 验证返回的roles是否是一个非空数组
          commit('SET_ROLES', data.roles)
        } else {
          return reject('登录已过期，请重新登录!')
        }

        commit('SET_NAME', data.username)
        commit('SET_AVATAR', data.avatar)
        resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },
    // 登出
    Logout({ commit }) {
      return new Promise((resolve, reject) => {
        logout().then(() => {
          removeToken() // must remove  token  first
          resetRouter()
          commit('SET_ROLES', [])
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    updateLoadMenus({ commit }) {
      return new Promise((resolve, reject) => {
        commit('SET_LOAD_MENUS', false)
      })
    }
  }

}
export const logOut = (commit) => {
  commit('SET_TOKEN', '')
  commit('SET_ROLES', [])
  removeToken()
}
// export const setUserInfo = (res, commit) => {
//   // 如果没有任何权限，则赋予一个默认的权限，避免请求死循环
//   if (res.roles.length === 0) {
//     commit('SET_ROLES', ['ROLE_SYSTEM_DEFAULT'])
//   } else {
//     commit('SET_ROLES', res.roles)
//   }
//   commit('SET_NAME', data.username)
//   commit('SET_AVATAR', data.avatar)
// }
export default user
