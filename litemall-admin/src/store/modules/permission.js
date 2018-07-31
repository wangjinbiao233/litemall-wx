import { asyncRouterMap, constantRouterMap, padRouterMap, initRouterMap } from '@/router'
import { browserEquipmentCheck } from '@/utils/commonu'

/**
 * 通过meta.role判断是否与当前用户权限匹配
 * @param roles
 * @param route
 */
function hasPermission(roles, route) {
  if (route.meta) {
    return roles.indexOf(route.name) >= 0
  } else {
    return true
  }
}

/**
 * 递归过滤异步路由表，返回符合用户角色权限的路由表
 * @param asyncRouterMap
 * @param roles
 */
function filterAsyncRouter(asyncRouterMap, roles) {
  const accessedRouters = []
  const list = asyncRouterMap.slice(0)
  list.forEach(route => {
    var obj = {}
    obj.name = route.name
    obj.path = route.path
    obj.meta = route.meta
    obj.component = route.component
    obj.hidden = route.hidden
    var children = route.children
    if (children && children.length) {
      children = filterAsyncRouter(children, roles)
      obj.children = children
    }
    if(children && children.length) {
      accessedRouters.push(obj)
    } else if(hasPermission(roles, obj)) {
      accessedRouters.push(obj)
    }
  });
  // const accessedRouters = list.filter(route => {
  //   if (route.children && route.children.length) {
  //     route.children = filterAsyncRouter(route.children, roles)
  //   }
  //   if(route.children && route.children.length) {
  //     return true
  //   } else if(hasPermission(roles, route)) {
  //     return true
  //   }
  //   // if (hasPermission(roles, route)) {
  //   //   if (route.children && route.children.length) {
  //   //     route.children = filterAsyncRouter(route.children, roles)
  //   //   }
  //   //   return true
  //   // }
  //   return false
  // })
  return accessedRouters
}

const permission = {
  state: {
    routers: [],
    addRouters: []
  },
  mutations: {
    SET_ROUTERS: (state, routers) => {
      state.addRouters = initRouterMap
      state.addRouters = state.addRouters.concat(routers)
      state.routers = constantRouterMap.concat(routers)
      
    },
    SET_PAD_ROUTERS: (state, routers) => {
      state.addRouters = routers
      state.routers = constantRouterMap.concat(routers)
      state.addRouters = state.addRouters.concat(padRouterMap)
    },
  },
  actions: {
    GenerateRoutes({ commit }, data) {
      return new Promise(resolve => {
        const { roles } = data
        let accessedRouters
        // if (roles.indexOf('admin') >= 0) {
        //   accessedRouters = asyncRouterMap
        // } else {
        //   accessedRouters = filterAsyncRouter(asyncRouterMap, roles)
        // }
        accessedRouters = filterAsyncRouter(asyncRouterMap, roles, )
        
        var bec= browserEquipmentCheck()//判断访问设备是pc端浏览器还是移动设备iPad
        if(bec=='pc'){//pc端浏览器
          commit('SET_ROUTERS', accessedRouters)
        }else{//iPad端
          if (roles.indexOf('pad') > -1) {
            commit('SET_PAD_ROUTERS', padRouterMap)
          }
        }
        //if (roles.indexOf('pad') > -1) {
        //  commit('SET_PAD_ROUTERS', padRouterMap)
        //} else {
        //  commit('SET_ROUTERS', accessedRouters)
        //}
        resolve()
      })
    }    

  }

}

export default permission
