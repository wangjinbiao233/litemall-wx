// 以下是业务服务器API地址
// 本机开发时使用
// var WxApiRoot = 'http://127.0.0.1:8082/wx/';
// 局域网测试使用
// var WxApiRoot = 'http://192.168.0.27:8082/wx/';

//production must be in SSL
//var WxApiRoot = 'https://mall-wx.dgtis.com/wx/';
//production must be in SSL philab
var WxApiRoot = 'https://mall-wx.philab.net/wx/';

// var WxApiRoot = 'http://192.168.0.23:8082/wx/';

// 以下是图片存储服务器API地址
var StorageApi = 'https://mall-wx.philab.net/wx/storage/create';

//通誉
//var picUrl = 'http://mall.dgtis.com/images/';

//梵郎
var picUrl = 'http://mall.philab.net/images/';

module.exports = {
  IndexUrl: WxApiRoot + 'home/index', //首页数据接口
  CatalogList: WxApiRoot + 'catalog/index',  //分类目录全部分类数据接口
  CatalogCurrent: WxApiRoot + 'catalog/current',  //分类目录当前分类数据接口

  AuthLoginByWeixin: WxApiRoot + 'auth/login_by_weixin', //微信登录
  SetCodeByPhone: WxApiRoot + 'auth/setCodeByPhone',//获取验证码
  verifyCodeAndSave: WxApiRoot + 'auth/verifyCodeAndSave',//验证验证码并保存手机号
  getPhoneNo: WxApiRoot + 'order/weChatGetPhone',  //获取微信绑定手机号
  AuthLoginByAccount: WxApiRoot + 'auth/login', //账号登录
  AuthRegister: WxApiRoot + 'auth/register', //账号注册
  AuthReset: WxApiRoot + 'auth/reset', //账号密码重置
  AuthSubUserCount: WxApiRoot + 'auth/getSubUserCount', //获取团队一级人数
  AuthSubSubUserCount: WxApiRoot + 'auth/getSubSubUserCount', //获取团队二级人数
  AuthSubUserTotalCount: WxApiRoot + 'auth/getSubUserTotalCount', //获取团队总人数
  AuthaddDistribution: WxApiRoot + 'auth/addDistribution',//添加分销商权限


  ProfitSubUser: WxApiRoot + 'profit/selectSubUserInfoByUserId',//获取我的下级
  ProfitSubSubUser: WxApiRoot + 'profit/selectSubSubUserInfoByUserId',//获取我的下下级
  withdrawDeposit: WxApiRoot + 'profit/getMoneyByUserId',// 提现接口

  GoodsCount: WxApiRoot + 'goods/count',  //统计商品总数
  GoodsList: WxApiRoot + 'goods/list',  //获得商品列表
  GoodsCategory: WxApiRoot + 'goods/category',  //获得分类数据
  GoodsDetail: WxApiRoot + 'goods/detail',  //获得商品的详情
  GoodsNew: WxApiRoot + 'goods/new',  //新品
  GoodsHot: WxApiRoot + 'goods/hot',  //热门
  GoodsRelated: WxApiRoot + 'goods/related',  //商品详情页的关联商品（大家都在看）

  BrandList: WxApiRoot + 'brand/list',  //品牌列表
  BrandDetail: WxApiRoot + 'brand/detail',  //品牌详情

  CartList: WxApiRoot + 'cart/index', //获取购物车的数据
  CartAdd: WxApiRoot + 'cart/add', // 添加商品到购物车
  CartFastAdd: WxApiRoot + 'cart/fastadd', // 立即购买商品
  CartUpdate: WxApiRoot + 'cart/update', // 更新购物车的商品
  CartDelete: WxApiRoot + 'cart/delete', // 删除购物车的商品
  CartChecked: WxApiRoot + 'cart/checked', // 选择或取消选择商品
  CartGoodsCount: WxApiRoot + 'cart/goodscount', // 获取购物车商品件数
  CartCheckout: WxApiRoot + 'cart/checkout', // 下单前信息确认

  PayPrepayId: WxApiRoot + 'pay/prepay', //获取微信统一下单prepay_id

  PayRefund: WxApiRoot + 'pay/refund', //微信退款
  CollectList: WxApiRoot + 'collect/list',  //收藏列表
  CollectAddOrDelete: WxApiRoot + 'collect/addordelete',  //添加或取消收藏

  CommentList: WxApiRoot + 'comment/list',  //评论列表
  CommentListdata: WxApiRoot + 'comment/listdata',  //评论列表包含子评论数据--知识评论
  CommentCount: WxApiRoot + 'comment/count',  //评论总数
  CommentPost: WxApiRoot + 'comment/post',   //发表评论

  TopicList: WxApiRoot + 'topic/list',  //专题列表
  TopicDetail: WxApiRoot + 'topic/detail',  //专题详情
  TopicRelated: WxApiRoot + 'topic/related',  //相关专题
  kCategoryInfoList: WxApiRoot + 'knowledge/knowledgeCategory',//知识分类
  KnowledgeList: WxApiRoot + 'knowledge/knlist',//知识管理
  KnowledgePointliketag: WxApiRoot + 'knowledge/likecounttag',//知识文章点赞
  KnowledgeDetail: WxApiRoot + 'knowledge/detail',//知识详情
  KnowledgeComment: WxApiRoot + 'knowledge/detailcomment',//知识评论信息
  Knowledgeliketag: WxApiRoot + 'knowledge/liketag',//知识评论点赞

  SearchIndex: WxApiRoot + 'search/index',  //搜索关键字
  SearchResult: WxApiRoot + 'search/result',  //搜索结果
  SearchHelper: WxApiRoot + 'search/helper',  //搜索帮助
  SearchClearHistory: WxApiRoot + 'search/clearhistory',  //搜索历史清楚

  AddressList: WxApiRoot + 'address/list',  //收货地址列表
  AddressDetail: WxApiRoot + 'address/detail',  //收货地址详情
  AddressSave: WxApiRoot + 'address/save',  //保存收货地址
  AddressDelete: WxApiRoot + 'address/delete',  //保存收货地址

  RegionList: WxApiRoot + 'region/list',  //获取区域列表

  OrderSubmit: WxApiRoot + 'order/submit', // 提交订单
  OrderList: WxApiRoot + 'order/list',  //订单列表
  OrderDetail: WxApiRoot + 'order/detail',  //订单详情
  OrderCancel: WxApiRoot + 'order/cancel',  //取消订单
  OrderRefund: WxApiRoot + 'order/refund',  //取消订单并退款
  OrderDelete: WxApiRoot + 'order/delete',  //删除订单
  OrderConfirm: WxApiRoot + 'order/confirm',  //确认收货
  OrderComment: WxApiRoot + 'order/comment',  // 代评价商品信息
  OrderQuery: WxApiRoot + 'order/orderQuery',  // 查询订单支付情况


  FootprintList: WxApiRoot + 'footprint/list',  //足迹列表
  FootprintDelete: WxApiRoot + 'footprint/delete',  //删除足迹

  StorageUpload: StorageApi,  //图片上传

  Store: WxApiRoot + 'store/list',  //门店列表

  cashDetail: WxApiRoot + 'profit/selectExtractMoneyByUserId',  //提现明细列表
  noCash: WxApiRoot + 'profit/selectSubAllProfitListByPId', //不可提现列表
  searchNoCash: WxApiRoot + 'profit/searchSubAllProfitDetailsByPId', //查询不可提现列表
  cash: WxApiRoot + 'profit/selectEarningsMoneyListByUserId',  //可提现列表
  noCashNum: WxApiRoot + 'profit/selectSubAllProfitByPId',  //不可提现金额
  cashNum: WxApiRoot + 'profit/selectExtractProfitByUserId',  //可提现金额
  cashDetailNum: WxApiRoot + 'profit/selectSumProfitMoney',  //提现金额汇总
  SubUserOrderGoodsInfo: WxApiRoot + 'profit/selectSubUserOrderGoodsInfoByUserId',//一级分销详情
  SubSubUserOrderGoodsInfo: WxApiRoot + 'profit/selectSubSubUserOrderGoodsByUserId',//二级分销详情

  allCoupon: WxApiRoot + 'discount/selectByKeyAndNameGroypBy',  //查询优惠券
  myCoupon: WxApiRoot + 'discount/selectMyCoupot',  //查询我的优惠券
  myUseCoupon: WxApiRoot + 'discount/selectMyUseCoupot',  //查询我的可用优惠券  
  receiveCoupon: WxApiRoot + 'discount/getCoupon',  //领取优惠券
  ReserveList: WxApiRoot + 'reserve/reserveList',  //预约列表
  getVerificationCode: WxApiRoot + 'order/getVerificationCode',  //获取短信验证码
  verifyCode: WxApiRoot + 'order/verifyCode',  //验证短信验证码
  moneyPay: WxApiRoot + 'order/moneyPay',  //余额支付
  getUserInfo: WxApiRoot + 'order/getUserInfo',

  Issue: WxApiRoot + 'issue/helpIssue',  //帮助中心


  StoreService: WxApiRoot + 'reserve/initTimeData',//查询对应预约时间的预约情况

  StoerServirceInsert: WxApiRoot + 'reserve/reserveDetail', //插入预约数据
  ReserveCancel: WxApiRoot + 'reserve/cancel', //插入预约数据
  FacialEvaluate: WxApiRoot + 'facialevaluate/getInfo', //人脸评测
  FacialEvaluateData: WxApiRoot + 'facialevaluate/getEvaluateData', //用id拿人脸评测数据
  FacialEvaluatelistData: WxApiRoot + 'facialevaluate/faceListData',//人脸历史数据
  FacialFirstfaceCheck: WxApiRoot + 'facialevaluate/firstfaceCheck',//是否第一次检测过
  ServiceStore: WxApiRoot + 'store/getAllStore',
  GoodsStore: WxApiRoot + 'store/getGoodsStorelist',

  selectRechargeRecord: WxApiRoot + 'recharge/selectRechargeRecord', //获取充值金额和充值记录

  saveDistributionApply: WxApiRoot + 'profit/saveDistributionApply', //分销申请
  selectDistributionTypeList: WxApiRoot + 'profit/selectDistributionTypeList', //分销类型列表
  selectApplyByUserId: WxApiRoot + 'profit/selectApplyByUserId', //查询用户的分销申请
  picUrl: picUrl,
  getDistributionTag: WxApiRoot +'auth/getUserLabel'//获取分销标签
};