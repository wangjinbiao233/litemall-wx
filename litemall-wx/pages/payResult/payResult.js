var util = require('../../utils/util.js');
var api = require('../../config/api.js');
const pay = require('../../services/pay.js');

var app = getApp();
Page({
  data: {
    status: '',
    orderId: 0
  },
  onLoad: function (options) {

    // 页面初始化 options为页面跳转所带来的参数
    this.setData({
      orderId: options.orderId,
      status: options.status === '1' ? true : false
    });
  
    //主动查询支付结果， 防止回调失败无法更新订单状态
    util.request(api.OrderQuery, { orderId: options.orderId, userId: wx.getStorageSync('userId') }, 'POST').then(res       => {
      if (res.errno === 0) {
        if (res.data.tradeStatus == 'SUCCESS'){
          this.setData({
            status: true
          });
        } else {
          this.setData({
            status: false
          });
        }
      } 
    });
  },
  onReady: function () {

  },
  onShow: function () {
    // 页面显示

  },
  onHide: function () {
    // 页面隐藏

  },
  onUnload: function () {
    // 页面关闭

  },
  payOrder() {
    pay.payOrder(this.data.orderId).then(res => {
      this.setData({
        status: true
      });
    }).catch(res => {
      util.showErrorToast('支付失败');
    });
  }
})