var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');

var app = getApp();

Page({
  data: {
    myCouponList: '',
    userId: wx.getStorageSync('userId'),
    currentIndex: 1
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
      page: 1,
      size: 10
    }
    util.request(api.myCoupon, data).then(function (res) {
      if (res.errno === 0) {
        console.log(res)
        that.setData({
          myCouponList: res.data.items
        })
      }
    })
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
  },
  //跳商城
  toGoods() {
    wx.switchTab({
        url: "/pages/category/category"
    });
  },
  onPullDownRefresh: function () {
    var that = this;
    var data = {
      userId: this.data.userId,
      page: 1,
      size: 10
    }
    util.request(api.myCoupon, data).then(function (res) {
      if (res.errno === 0) {
        console.log(res)
        that.setData({
          myCouponList: res.data.items,
          currentIndex: 1
        })
      }
    })
    wx.stopPullDownRefresh();  
  },
  onReachBottom: function () {
    var that = this;
    var currentIndex = this.data.currentIndex += 1;
    var data = {
      userId: this.data.userId,
      page: currentIndex,
      size: 10
    }
    util.request(api.myCoupon, data).then(function (res) {
      if (res.errno === 0) {
        console.log(res)
        if (currentIndex > res.data.pages) {
          wx.showToast({
            title: '没有更多了',
          });
        } else {
          that.setData({
            myCouponList: that.data.myCouponList.concat(res.data.items),
            currentIndex: currentIndex
          })
        }
      }
    })
    wx.stopPullDownRefresh();  
  }
})