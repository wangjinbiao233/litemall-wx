var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');

Page({
  data: {
    orderId: 0,
    reserveId:0,
    showType:0,
    flag:0,
    orderInfo: {},
    orderGoods: [],
    litemallReserve:[],
    handleOption: {}
  },
  onLoad: function (options) {
    // 页面初始化 options为页面跳转所带来的参数
    this.setData({
      orderId: options.id,
      reserveId: options.reserveId,
      showType: options.showType,
      flag: options.flag,
    });
    this.getOrderDetail();
  },
  getOrderDetail: function () {
    let that = this;
    util.request(api.OrderDetail, {
      orderId: that.data.orderId,
      reserveId: that.data.reserveId,
      showType: that.data.showType,
      flag: that.data.flag,
      userId:wx.getStorageSync('userId')
    },'POST').then(function (res) {
      if (res.errno === 0) {
        console.log(res.data);       
        that.setData({
          orderInfo: res.data.orderInfo,        
          orderGoods: res.data.orderGoods,
          litemallReserve: res.data.litemallReserve,
          handleOption: res.data.orderInfo.handleOption
        });
      }
    });
  },
  // “去付款”按钮点击效果
  payOrder: function () {
    let that = this;
    util.request(api.PayPrepayId, {
      orderId: that.data.orderId, userId: wx.getStorageSync('userId')
    }, 'POST').then(function (res) {
      if (res.errno === 0) {
        const payParam = res.data;
        wx.requestPayment({
          'timeStamp': payParam.timeStamp,
          'nonceStr': payParam.nonceStr,
          'package': payParam.package,
          'signType': payParam.signType,
          'paySign': payParam.paySign,
          'success': function (res) {
            console.log(res)
          },
          'fail': function (res) {
            console.log(res)
          }
        });
      }
    });

  },
  // “取消订单”点击效果
  cancelOrder: function () {
    let that = this;
    let orderInfo = that.data.orderInfo;

    wx.showModal({
      title: '',
      content: '确定要取消此订单？',
      success: function (res) {
        if (res.confirm) {
          util.request(api.OrderCancel, {
            orderId: orderInfo.id, userId: wx.getStorageSync('userId')
          }, 'POST').then(function (res) {
            if (res.errno === 0) {
              wx.showToast({
                title: '取消订单成功'
              });            
              util.redirect('/pages/ucenter/order/index?sort=1&flag=0');
            }
            else {
              util.showErrorToast(res.errmsg);
            }
          });
        }
      }
    });
  },
  
  // “取消订单并退款”点击效果
  refundOrder: function () {
    let that = this;
    let orderInfo = that.data.orderInfo;
    var payId = orderInfo.payId;
    
    wx.showModal({
      title: '',
      content: '确定要取消此订单？',
      success: function (res) {
        if (res.confirm) {        
          if (payId != null) {
            //微信支付
              util.request(api.PayRefund, {
                orderId: orderInfo.id, userId: wx.getStorageSync('userId')
              }, 'POST').then((res) => {
                console.log(res)
                if (res.return_code === 'SUCCESS') {
                  util.request(api.OrderRefund, {
                    orderId: orderInfo.id, userId: wx.getStorageSync('userId')
                  }, 'POST').then(function (res) {
                    if (res.errno === 0) {
                      wx.showToast({
                        title: '取消订单成功'
                      });
                      util.redirect('/pages/ucenter/order/index?sort=1&flag=0');
                    }
                    else {
                      wx.showToast({
                        title: res.errmsg,
                        icon: 'none',
                        duration: 2000//持续的时间
                      })
                    }
                  });
                } else {
                  wx.showToast({
                    title: res.return_msg,
                    icon: 'none',
                    duration: 2000//持续的时间
                  })
                }
              });
          } else {
            //余额支付
            util.request(api.OrderRefund, {
              orderId: orderInfo.id, userId: wx.getStorageSync('userId')
            }, 'POST').then(function (res) {
              if (res.errno === 0) {
                wx.showToast({
                  title: '取消订单成功'
                });
                util.redirect('/pages/ucenter/order/index?sort=1&flag=0');
              }
              else {
                wx.showToast({
                  title: res.errmsg,
                  icon: 'none',
                  duration: 2000//持续的时间
                })
              }
            });
          }
        }
      }
    });
  },  
  // “删除”点击效果
  deleteOrder: function () {
    let that = this;
    let orderInfo = that.data.orderInfo;

    wx.showModal({
      title: '',
      content: '确定要删除此订单？',
      success: function (res) {
        if (res.confirm) {
          util.request(api.OrderDelete, {
            orderId: orderInfo.id, userId: wx.getStorageSync('userId')
          }, 'POST').then(function (res) {
            if (res.errno === 0) {
              wx.showToast({
                title: '删除订单成功'
              });
              util.redirect('/pages/ucenter/order/index?sort=1&flag=0');
            }
            else {
              util.showErrorToast(res.errmsg);
            }
          });
        }
      }
    });
  },  
  // “确认收货”点击效果
  confirmOrder: function () {
    let that = this;
    let orderInfo = that.data.orderInfo;

    wx.showModal({
      title: '',
      content: '确认收货？',
      success: function (res) {
        if (res.confirm) {
          util.request(api.OrderConfirm, {
            orderId: orderInfo.id, userId: wx.getStorageSync('userId')
          }, 'POST').then(function (res) {
            if (res.errno === 0) {
              wx.showToast({
                title: '确认收货成功！'
              });
              util.redirect('/pages/ucenter/order/index?sort=1&flag=0');
            }
            else {
              util.showErrorToast(res.errmsg);
            }
          });
        }
      }
    });
  },
  onReady: function () {
    // 页面渲染完成
  },
  onShow: function () {
    // 页面显示
  },
  onHide: function () {
    // 页面隐藏
  },
  onUnload: function () {
    // 页面关闭
  }
})