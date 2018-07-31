const getters = {
  sidebar: state => state.app.sidebar,
  language: state => state.app.language,
  visitedViews: state => state.tagsView.visitedViews,
  cachedViews: state => state.tagsView.cachedViews,
  token: state => state.user.token,
  userId: state => state.user.userId,
  avatar: state => state.user.avatar,
  name: state => state.user.name,
  isManagers: state => state.user.isManagers,
  storename: state => state.user.storename,
  storeId: state => state.user.storeId,
  introduction: state => state.user.introduction,
  status: state => state.user.status,
  roles: state => state.user.roles,
  setting: state => state.user.setting,
  dateTimes : state => state.user.dateTimes,
  storeOptions: state => state.user.storeOptions,
  permission_routers: state => state.permission.routers,
  addRouters: state => state.permission.addRouters
}
export default getters
