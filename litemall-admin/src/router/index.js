import Vue from 'vue'
import Router from 'vue-router'
const _import = require('./_import_' + process.env.NODE_ENV)
// in development-env not use lazy-loading, because lazy-loading too many pages will cause webpack hot update too slow. so only in production use lazy-loading;
// detail: https://panjiachen.github.io/vue-element-admin-site/#/lazy-loading

Vue.use(Router)

/* Layout */
import Layout from '../views/layout/Layout'
import padLayout from '../views/layout/padLayout'

/** note: submenu only apppear when children.length>=1
 *   detail see  https://panjiachen.github.io/vue-element-admin-site/#/router-and-nav?id=sidebar
 **/

/**
 * hidden: true                   if `hidden:true` will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu, whatever its child routes length
 *                                if not set alwaysShow, only more than one route under the children
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noredirect           if `redirect:noredirect` will no redirct in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']     will control the page roles (you can set multiple roles)
    title: 'title'               the name show in submenu and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar,
    noCache: true                if fasle ,the page will no be cached(default is false)
  }
 **/
export const constantRouterMap = [
  { path: '/login', component: _import('login/index'), hidden: true },
  { path: '/authredirect', component: _import('login/authredirect'), hidden: true },
  { path: '/oauthredirect', component: _import('login/oauthredirect'), hidden: true },
  { path: '/404', component: _import('error/404'), hidden: true },
  { path: '/401', component: _import('error/401'), hidden: true },
  { path: '*', component: _import('error/404'), hidden: true },
  { path: '/401', component: _import('error/401'), hidden: true }
]
export const initRouterMap = [
  {
    path: '',
    component: Layout,
    redirect: 'dashboard',
    children: [{
      path: 'dashboard',
      component: _import('dashboard/index'),
      name: 'dashboard',
      meta: { title: '首页', icon: 'dashboard', noCache: true }
    }]
  }
]
export const padRouterMap = [
  {
    path: '',
    component: padLayout,
    redirect: 'pad',
    children: [
      {
        path: 'pad',
        component: _import('pad/index'),
        name: 'pad',
        meta: { title: '首页', icon: 'dashboard', noCache: true }
      }, {
        path: 'doctor',
        component: _import('pad/doctor'),
        name: 'doctor',
        meta: { icon: 'chart', title: '医生', noCache: true }
      }, {
        path: 'cosmetologist',
        component: _import('pad/cosmetologist'),
        name: 'cosmetologist',
        meta: { icon: 'chart', title: '美容师', noCache: true }
      }
    ]
  }
]

export default new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})

export const asyncRouterMap = [
  {
    path: '/user',
    component: Layout,
    redirect: 'noredirect',
    name: 'userManage',
    meta: {
      title: '客户管理',
      icon: 'chart'
    },
    children: [
      { path: 'user', component: _import('user/user'), name: 'user', meta: { title: '会员管理', noCache: true }},
      { path: 'userExport', component: _import('user/userExport'), name: 'userExport', meta: { title: '会员信息导出', noCache: true }},
      { path: 'userDetail', component: _import('user/userDetail'), name: 'userDetail', meta: { title: '会员详情', noCache: false, hideTag: true }}
      // { path: 'address', component: _import('user/address'), name: 'address', meta: { title: '收货地址', noCache: true, hideTag: true }},
      // { path: 'express', component: _import('user/express'), name: 'express', meta: { title: '快递公司', noCache: true, hideTag: true }},
      // { path: 'collect', component: _import('user/collect'), name: 'collect', meta: { title: '会员收藏', noCache: true, hideTag: true }},
      // { path: 'footprint', component: _import('user/footprint'), name: 'footprint', meta: { title: '会员足迹', noCache: true, hideTag: true }},
      // { path: 'history', component: _import('user/history'), name: 'history', meta: { title: '搜索历史', noCache: true, hideTag: true }},
      // { path: 'cart', component: _import('user/cart'), name: 'cart', meta: { title: '购物车', noCache: true, hideTag: true }},
      // { path: 'coupon', component: _import('user/coupon'), name: 'coupon', meta: { title: '优惠券', noCache: true, hideTag: true }}
    ]
  },
  {
    path: '/store',
    component: Layout,
    redirect: 'noredirect',
    name: 'storeManage',
    meta: {
      title: '门店管理',
      icon: 'chart'
    },
    children: [
      { path: 'store', component: _import('store/store'), name: 'store', meta: { icon: 'chart', title: '门店管理', noCache: true }}
    ]
  },
  {
    path: '/goods',
    component: Layout,
    redirect: 'noredirect',
    name: 'goodsManage',
    meta: {
      title: '商品管理',
      icon: 'chart'
    },
    children: [
      { path: 'category', component: _import('mall/category'), name: 'category', meta: { title: '商品类目', noCache: true }},
      { path: 'goods', component: _import('goods/goods'), name: 'goods', meta: { title: '商品管理', noCache: true }},
      { path: 'goodsCreate', component: _import('goods/goodsCreate'), name: 'goodsCreate', meta: { title: '商品添加', noCache: true, hideTag: true }},
      { path: 'goodsDetail', component: _import('goods/goodsDetail'), name: 'goodsDetail', meta: { title: '商品详情', noCache: false, hideTag: true }},
      { path: 'brand', component: _import('mall/brand'), name: 'brand', meta: { title: '品牌制造商', noCache: true }},
      { path: 'attribute', component: _import('goods/attribute'), name: 'attribute', meta: { title: '商品参数', noCache: true, hideTag: true }},
      { path: 'specification', component: _import('goods/specification'), name: 'specification', meta: { title: '商品规格', noCache: true, hideTag: true }},
      { path: 'product', component: _import('goods/product'), name: 'product', meta: { title: '货品管理', noCache: true, hideTag: true }},
      { path: 'comment', component: _import('goods/comment'), name: 'comment', meta: { title: '用户评论', noCache: true, hideTag: true }}

    ]
  },
  {
    path: '/mall',
    component: Layout,
    redirect: 'noredirect',
    name: 'mallManage',
    meta: {
      title: '营销管理',
      icon: 'chart'
    },
    children: [
      { path: 'order', component: _import('mall/order'), name: 'order', meta: { title: '订单管理', noCache: true }},
      { path: 'reserve', component: _import('mall/reserve'), name: 'reserve', meta: { title: '预约管理', noCache: true }},
      { path: 'service', component: _import('mall/service'), name: 'service', meta: { title: '治疗管理', noCache: true }},
      { path: 'keyword', component: _import('mall/keyword'), name: 'keyword', meta: { title: '关键词', noCache: true, hideTag: true }},
      { path: 'coupon', component: _import('user/coupon'), name: 'coupon', meta: { title: '优惠券管理', noCache: true }},
      { path: 'stock', component: _import('mall/stock'), name: 'stock', meta: { title: '库存管理', noCache: true }},
      { path: 'express', component: _import('user/express'), name: 'express', meta: { title: '快递公司管理', noCache: true }}
    ]
  },
  {
    path: '/distribution',
    component: Layout,
    redirect: 'noredirect',
    name: 'distributionManage',
    meta: {
      title: '分销管理',
      icon: 'chart'
    },
    children: [
      { path: 'distribution', component: _import('distribution/distribution'), name: 'distribution', meta: { icon: 'chart', title: '分销管理', noCache: true }}
    ]
  },
  {
    path: '/promotion',
    component: Layout,
    redirect: 'noredirect',
    name: 'promotionManage',
    meta: {
      title: '知识管理',
      icon: 'chart'
    },
    children: [
      { path: 'ad', component: _import('promotion/ad'), name: 'ad', meta: { title: '广告列表', noCache: true }},
      { path: 'topic', component: _import('promotion/topic'), name: 'topic', meta: { title: '专题管理', noCache: true, hideTag: true }},
      { path: 'kCategory', component: _import('promotion/kCategory'), name: 'kCategory', meta: { title: '知识类别', noCache: true }},
      { path: 'knowledge', component: _import('promotion/knowledge'), name: 'knowledge', meta: { title: '知识管理', noCache: true }},
      { path: 'analyse', component: _import('promotion/analyse'), name: 'analyse', meta: { title: '用户10D数据管理', noCache: true }},
      { path: 'analyseDetail', component: _import('promotion/analyseDetail'), name: 'analyseDetail', meta: { title: '用户10D数据管理详情', noCache: true, hideTag: true }}

    ]
  },
  {
    path: '/report',
    component: Layout,
    redirect: 'noredirect',
    name: 'reportmanage',
    meta: {
      title: '报表管理',
      icon: 'chart'
    },
    children: [
      { path: 'saleorder', component: _import('report/saleOrder'), name: 'saleOrder', meta: { title: '销售订单统计', noCache: true }},
      { path: 'saleexecute', component: _import('report/saleExecute'), name: 'saleExecute', meta: { title: '销售执行统计', noCache: true }},
      // { path: 'customeraccountbalance', component: _import('report/customerAccountBalance'), name: 'customerAccountBalance', meta: { title: '用户账户余额', noCache: true }},
      // { path: 'customeraccountcheck', component: _import('report/customerAccountCheck'), name: 'timesale', meta: { title: '用户对账明细', noCache: true }}

    ]
  },
  {
    path: '/sys',
    component: Layout,
    redirect: 'noredirect',
    name: 'sysManage',
    meta: {
      title: '系统管理',
      icon: 'chart'
    },
    children: [
      { path: 'admin', component: _import('sys/admin'), name: 'admin', meta: { title: '用户管理', noCache: true }},
      { path: 'role', component: _import('sys/role'), name: 'role', meta: { title: '角色管理', noCache: true }},
      { path: 'os', component: _import('sys/os'), name: 'os', meta: { title: '对象存储', noCache: true, hideTag: true }},
      { path: 'region', component: _import('mall/region'), name: 'region', meta: { title: '行政区域管理', noCache: true }},
      { path: 'issue', component: _import('mall/issue'), name: 'issue', meta: { title: '通用问题管理', noCache: true }},
      { path: 'dictionarys', component: _import('sys/dictionarys'), name: 'dictionarys', meta: { title: '字典管理', noCache: true }},
      { path: 'dictionarysCreate', component: _import('sys/dictionarysCreate'), name: 'dictionarysCreate', meta: { title: '字典添加', noCache: true, hideTag: true }}
    ]
  }, {
    path: '/pad',
    component: padLayout,
    redirect: 'redirect',
    hidden: true, // 不在侧边栏线上  alwaysShow: true表示一直显示根路由（只要一个子元素）
    name: 'pad',
    meta: {
      title: 'pad',
      icon: 'chart'
    },
    children: [
      { path: 'pad', component: _import('pad/doctor'), name: 'doctor', meta: { title: '医生', noCache: true }}
    ]
  },

  { path: '*', redirect: '/404', hidden: true }
]
