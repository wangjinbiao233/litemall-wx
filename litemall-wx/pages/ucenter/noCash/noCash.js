const app = getApp()

var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');

Page({

  /**
   * 页面的初始数据
   */
  data: {
    total: 0,
    page: 1,
    userId: 0,
    orderList: '',
    isSearch: 0
  },
  toSearch: function (e) {
    this.setData({
      isSearch: 1
    })
  },
  search: function (e) {
    if (e.detail.value.searchInfo == '') {
      wx.showToast({
        title: '请输入订单号'
      })
    } else {
      var that = this;
      var userId = wx.getStorageSync('userId');
      var data = {
        'userId': userId,
        'orderId': e.detail.value.searchInfo
      }
      util.request(api.searchNoCash, data,'POST').then(function (res) {
        console.log(res)
        if (res.errno === 0) {
          that.setData({
            orderList: res.data.items
          });
        }
      })
    }
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    wx.showLoading({
      title: '加载中...',
    })
    var that = this;
    var userId = wx.getStorageSync('userId');
    that.setData({
      'userId': userId
    })
    var data = {
      'userId': userId,
      'page': 1,
      'size': 10
    };

    util.request(api.noCash, data).then(function (res) {
      if (res.errno === 0) {
        console.log(res)
        that.setData({
          'orderList': res.data.items,
          'total': res.data.total
        }, function () {
          wx.hideLoading();
        });
      }
    })
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
    console.log('下拉下拉')
    var that = this;
    this.setData({
      'page': 1
    })
    var data = {
      'userId': this.data.userId,
      'page': 1,
      'size': 10
    };

    util.request(api.noCash, data).then(function (res) {
      if (res.errno === 0) {
        console.log(res)
        that.setData({
          'orderList': res.data.items,
          'total': res.data.total
        }, function () {
          wx.hideLoading();
        });
      }
    })

    wx.stopPullDownRefresh();  
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
    var that = this;
    var page = this.data.page;
    this.setData({
      'page': page + 1
    })
    if (page * 10 < this.data.total) {
      getDataInfo(this, this.data.userId, this.data.page);
    }

    wx.stopPullDownRefresh();  
  }
})

function getDataInfo(that, userId, page) {
  var data = {
    'userId': userId,
    'page': page,
    'size': 10
  };

  util.request(api.noCash, data).then(function (res) {
    if (res.errno === 0) {
      console.log(res)
      that.setData({
        'orderList': that.data.orderList.concat(res.data.items),
        'total': res.data.total
      }, function () {
        wx.hideLoading();
      });
    }
  })
}
