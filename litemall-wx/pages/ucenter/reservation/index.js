const util = require('../../../utils/util.js');
const api = require('../../../config/api.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    reserveList: {}
  },
  show: function (e) {
    var that = this;
    console.log(this.data.reserveList)
    console.log(e);
    var index = e.currentTarget.dataset.index;
    for (var i = 0; i < that.data.reserveList.length; i++) {
      if (index == i) {
        if (that.data.reserveList[i].isShow == true) {
          var list = that.data.reserveList;
          list[i].isShow = false;
          that.setData({
            reserveList: list
          })
        } else {
          var list = that.data.reserveList;
          list[i].isShow = true;
          that.setData({
            reserveList: list
          })
        }
      }
    }
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let that = this;
    var data = {
      userId: wx.getStorageSync('userId')
    }
    util.request(api.ReserveList, data, 'POST').then(function (res) {
      if (res.errno === 0) {
        console.log(res);
        var list = res.data.data;
        for (var i = 0; i < res.data.data.length; i++) {
          list[i].isShow = true;
        }
        that.setData({
          reserveList: list
        });
      }
    });
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

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})