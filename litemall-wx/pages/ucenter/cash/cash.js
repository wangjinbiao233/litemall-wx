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
    cashList: []
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    var userId = wx.getStorageSync('userId');
    that.setData({
      'userId': userId
    })
    getDataInfo(this, userId, 1);
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
    getDataInfo(this, this.data.userId, 1);
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
      var data = {
        'userId': this.data.userId,
        'page': this.data.page,
        'size': 10
      };

      util.request(api.cash, data).then(function (res) {
        if (res.errno === 0) {
          console.log(res)
          that.setData({
            'cashList': that.data.cashList.concat(res.data.items),
            'total': res.data.total
          })
        }
      })
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

  util.request(api.cash, data).then(function (res) {
    if (res.errno === 0) {
      console.log(res)
      that.setData({
        'cashList': res.data.items,
        'total': res.data.total
      })
    }
  })
}