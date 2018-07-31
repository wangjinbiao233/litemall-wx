import { loginByUsername, logout, getUserInfo, getUserMenu } from '@/api/login'
import { getToken, setToken, removeToken } from '@/utils/auth'

const user = {
  state: {
    user: '',
    status: '',
    code: '',
    token: getToken(),
    userId: '',
    name: '',
    avatar: '',
    introduction: '',
    roles: [],
    isManagers: '',
    storename: '',
    storeId: '',
    setting: {
      articlePlatform: []
    },
    dateTimes: '',
    storeOptions: []
  },

  mutations: {
    SET_USERID: (state, userId)=>{
      state.userId = userId
    },
    SET_CODE: (state, code) => {
      state.code = code
    },
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_INTRODUCTION: (state, introduction) => {
      state.introduction = introduction
    },
    SET_SETTING: (state, setting) => {
      state.setting = setting
    },
    SET_STATUS: (state, status) => {
      state.status = status
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
    SET_ISMANAGERS: (state , isManagers) =>{
      state.isManagers = isManagers
    },
    SET_STORENAME: (state , storename) =>{
      state.storename = storename
    },
    SET_STOREID:(state , storeId)=>{
      state.storeId = storeId
    },
    SET_DATETIMES (state, date) {
      // 变更值
      state.dateTimes = date
    },
    SET_STOREOPTIONS: (state, storeOptions) => {
      state.storeOptions = storeOptions
    }
  },

  actions: {
    // 用户名登录
    LoginByUsername({ commit }, userInfo) {
      const username = userInfo.username.trim()
      return new Promise((resolve, reject) => {
        loginByUsername(username, userInfo.password,userInfo.browserEquipment).then(response => {
          const token = response.data.data.token
          commit('SET_TOKEN', token)
          commit('SET_USERID', response.data.data.userId)
          setToken(token)
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },
    // oAutho 验证登陆
    LoginByOAuth({ commit }, token) {
      return new Promise((resolve, reject) => {
        commit('SET_TOKEN', token)
        setToken(token)
        resolve()
      })
    },

    // 获取用户权限
    GetUserMenus({ commit }) {
      return new Promise((resolve, reject) => {
        getUserMenu().then(response => {
          const data2 = response.data.data;
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 获取用户信息
    GetUserInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        getUserInfo(state.token).then(response => {
          console.log('get user info')
          const data = response.data.data
          commit('SET_ROLES', data.roles)
          if (data.roles.indexOf('门店管理员') > -1) {
            commit('SET_ISMANAGERS', 'yes')
          } else if (data.roles.indexOf('美疗师') > -1) {
            commit('SET_ISMANAGERS', 'no')
          }
          commit('SET_NAME', data.name)
          commit('SET_AVATAR', data.avatar)
          commit('SET_INTRODUCTION', data.introduction)
          if(data.store) {
            commit('SET_STORENAME', data.store.storeName)
            commit('SET_STOREID',data.store.id)
          }

          if(data.userStoreList) {
            commit('SET_STOREOPTIONS',data.userStoreList)
          }

          console.log('end get user info')
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 第三方验证登录
    // LoginByThirdparty({ commit, state }, code) {
    //   return new Promise((resolve, reject) => {
    //     commit('SET_CODE', code)
    //     loginByThirdparty(state.status, state.email, state.code).then(response => {
    //       commit('SET_TOKEN', response.data.token)
    //       setToken(response.data.token)
    //       resolve()
    //     }).catch(error => {
    //       reject(error)
    //     })
    //   })
    // },

    // 登出
    LogOut({ commit, state }) {
      return new Promise((resolve, reject) => {
        logout(state.token).then(() => {
          commit('SET_TOKEN', '')
          commit('SET_ROLES', [])
          removeToken()
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 前端 登出
    FedLogOut({ commit }) {
      return new Promise(resolve => {
        commit('SET_TOKEN', '')
        removeToken()
        resolve()
      })
    },

    // 动态修改权限
    ChangeRoles({ commit }, role) {
      return new Promise(resolve => {
        commit('SET_TOKEN', role)
        setToken(role)
        getUserInfo(role).then(response => {
          const data = response.data.data
          commit('SET_ROLES', data.roles)
          commit('SET_NAME', data.name)
          commit('SET_AVATAR', data.avatar)
          commit('SET_INTRODUCTION', data.introduction)
          resolve()
        })
      })
    },

    setDateTimes({ commit }, date) {
      commit('SET_DATETIMES', date)
    },
    changeStore({ commit }, storeid) {
      commit('SET_STOREID', storeid)
    }

  }
}

export default user
