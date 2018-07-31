var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');
var app = getApp();
Page({
  data: {
    myCouponList: '',
    userId: wx.getStorageSync('userId')
  },
  search: function (e) {
    var searchInfo = e.detail.value.searchInfo;
    if (searchInfo == '') {
      wx.showToast({
        icon: 'loading',
        title: '请输入优惠券名称'
      })
    } else {
      var that = this;
      var data = {
        userId: this.data.userId,
        page: 1,
        size: 10,
        discountName: searchInfo
      }
      util.request(api.myCoupon, data).then(function (res) {
        if (res.errno === 0) {
          console.log(res)
          that.setData({
            myCouponList: res.data.items
          });
        }
      })
    }
  },
  onLoad: function (options) {
    var that = this; 
    var data = {
      userId: this.data.userId,
      goodsTotalPrice: options.goodsTotalPrice,
      page: 1,
      size: 10
    }
    util.request(api.myUseCoupon, data).then(function (res) {
      if (res.errno === 0) {
        console.log(res)
        that.setData({
          myCouponList: res.data.items
        })
      }
    })
  },

  selectCoupon(event) {
    try {
      wx.setStorageSync('couponId', event.currentTarget.dataset.couponId);
    } catch (e) {
    }
    wx.navigateBack();
  },

  onReady: function () {

  },
  onShow: function () {

  },
  onHide: function () {
    // 页面隐藏

  },
  onUnload: function () {
    // 页面关闭
  }
})